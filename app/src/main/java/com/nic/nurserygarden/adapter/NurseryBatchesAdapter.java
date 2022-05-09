package com.nic.nurserygarden.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchDetails;
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchSpeciesDetails;
import com.nic.nurserygarden.activity.DeadSaplingActivty.DeadSaplingEntry;
import com.nic.nurserygarden.activity.GrowthTrackingActivity.GrowthTracking;
import com.nic.nurserygarden.activity.LandActivity.AddViewLand;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.databinding.NurseryBatchItemViewBinding;
import com.nic.nurserygarden.databinding.NurseryLandItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NurseryBatchesAdapter extends RecyclerView.Adapter<NurseryBatchesAdapter.MyViewHolder> {

    private List<NurserySurvey> batchList;
    private Context context;
    private LayoutInflater layoutInflater;
    private com.nic.nurserygarden.dataBase.dbData dbData;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public NurseryBatchesAdapter(List<NurserySurvey> batchList, Context context, com.nic.nurserygarden.dataBase.dbData dbData) {
        this.batchList = batchList;
        this.context = context;
        this.dbData=dbData;

        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public NurseryBatchesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NurseryBatchItemViewBinding nurseryBatchItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.nursery_batch_item_view, parent, false);
        return new MyViewHolder(nurseryBatchItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseryBatchesAdapter.MyViewHolder holder, int position) {

        holder.nurseryBatchItemViewBinding.createdDate.setText("Date "+batchList.get(position).getCreated_date());
        if(batchList.get(position).getBatch_id()==0){
            holder.nurseryBatchItemViewBinding.batchId.setVisibility(View.GONE);
            holder.nurseryBatchItemViewBinding.batchNumber.setVisibility(View.GONE);
            holder.nurseryBatchItemViewBinding.batchId.setText("");
            holder.nurseryBatchItemViewBinding.batchId.setText("");
        }
        else {
            holder.nurseryBatchItemViewBinding.batchId.setVisibility(View.VISIBLE);
            holder.nurseryBatchItemViewBinding.batchNumber.setVisibility(View.VISIBLE);
            holder.nurseryBatchItemViewBinding.batchId.setText("Batch ID "+ batchList.get(position).getBatch_id());
            holder.nurseryBatchItemViewBinding.batchNumber.setText("Batch Number "+ batchList.get(position).getBatch_number());
        }

        if(batchList.get(position).getServer_flag().equals("0")){
            ArrayList<NurserySurvey> getSpeciesCount = new ArrayList<>();
            getSpeciesCount = dbData.get_nursery_batch_species_details(String.valueOf(batchList.get(position).getBatch_primary_id()),"","0","");
            if(getSpeciesCount.size()>0) {
                holder.nurseryBatchItemViewBinding.upload.setVisibility(View.VISIBLE);
            }
            else {
                holder.nurseryBatchItemViewBinding.upload.setVisibility(View.GONE);
            }
            holder.nurseryBatchItemViewBinding.delete.setVisibility(View.VISIBLE);
            holder.nurseryBatchItemViewBinding.trackGrowth.setVisibility(View.GONE);
            holder.nurseryBatchItemViewBinding.deadSapling.setVisibility(View.GONE);
            holder.nurseryBatchItemViewBinding.deadSaplingUpload.setVisibility(View.GONE);
        }
        else {
            ArrayList<NurserySurvey> getSpeciesCount = new ArrayList<>();
            getSpeciesCount = dbData.get_nursery_batch_species_details(String.valueOf(batchList.get(position).getBatch_primary_id()),"","0","");
            if(getSpeciesCount.size()>0) {
                holder.nurseryBatchItemViewBinding.upload.setVisibility(View.VISIBLE);
                holder.nurseryBatchItemViewBinding.trackGrowth.setVisibility(View.GONE);
                holder.nurseryBatchItemViewBinding.deadSapling.setVisibility(View.GONE);
                holder.nurseryBatchItemViewBinding.deadSaplingUpload.setVisibility(View.GONE);
            }
            else {
                holder.nurseryBatchItemViewBinding.upload.setVisibility(View.GONE);
                holder.nurseryBatchItemViewBinding.trackGrowth.setVisibility(View.VISIBLE);
                holder.nurseryBatchItemViewBinding.deadSapling.setVisibility(View.VISIBLE);
                ArrayList<NurserySurvey> deadSaplingCount = new ArrayList<>();
                deadSaplingCount = dbData.get_particular_dead_sapling_details(String.valueOf(batchList.get(position).getBatch_id()),"","0","");

                if(deadSaplingCount.size()>0){
                    holder.nurseryBatchItemViewBinding.deadSaplingUpload.setVisibility(View.VISIBLE);
                }
                else {
                    holder.nurseryBatchItemViewBinding.deadSaplingUpload.setVisibility(View.GONE);
                }
            }
            holder.nurseryBatchItemViewBinding.delete.setVisibility(View.GONE);
        }


        holder.nurseryBatchItemViewBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_delete_alert(position,"save");
            }
        });
        holder.nurseryBatchItemViewBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_delete_alert(position,"delete");
            }
        });
        holder.nurseryBatchItemViewBinding.addSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAddSpeciesClass = new Intent(context, AddViewBatchSpeciesDetails.class);
                gotoAddSpeciesClass.putExtra("batch_id",batchList.get(position).getBatch_id());
                gotoAddSpeciesClass.putExtra("batch_species_id",batchList.get(position).getBatch_species_id());
                gotoAddSpeciesClass.putExtra("batch_primary_id",batchList.get(position).getBatch_primary_id());
                context.startActivity(gotoAddSpeciesClass);
            }
        });
        holder.nurseryBatchItemViewBinding.trackGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoGrowthTrackingClass = new Intent(context, GrowthTracking.class);
                gotoGrowthTrackingClass.putExtra("batch_id",batchList.get(position).getBatch_id());
                gotoGrowthTrackingClass.putExtra("batch_species_id",batchList.get(position).getBatch_species_id());
                gotoGrowthTrackingClass.putExtra("batch_primary_id",batchList.get(position).getBatch_primary_id());
                context.startActivity(gotoGrowthTrackingClass);
            }
        });
        holder.nurseryBatchItemViewBinding.deadSapling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoGrowthTrackingClass = new Intent(context, DeadSaplingEntry.class);
                gotoGrowthTrackingClass.putExtra("batch_id",batchList.get(position).getBatch_id());
                gotoGrowthTrackingClass.putExtra("batch_species_id",batchList.get(position).getBatch_species_id());
                gotoGrowthTrackingClass.putExtra("batch_primary_id",batchList.get(position).getBatch_primary_id());
                context.startActivity(gotoGrowthTrackingClass);
            }
        });
        holder.nurseryBatchItemViewBinding.deadSaplingUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_delete_alert(position,"dead_sapling_save");
            }
        });
    }

    @Override
    public int getItemCount() {
        return batchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public NurseryBatchItemViewBinding nurseryBatchItemViewBinding;

        public MyViewHolder(NurseryBatchItemViewBinding Binding) {
            super(Binding.getRoot());
            nurseryBatchItemViewBinding = Binding;
        }
    }

    public void save_and_delete_alert(int position,String save_delete){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            if(save_delete.equals("save")) {
                text.setText(context.getResources().getString(R.string.do_u_want_to_upload));
            }
            else if(save_delete.equals("delete")){
                text.setText(context.getResources().getString(R.string.do_u_want_to_delete));
            }
            else if(save_delete.equals("dead_sapling_save")){
                text.setText(context.getResources().getString(R.string.do_u_want_to_upload));
            }

            Button yesButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button noButton = (Button) dialog.findViewById(R.id.btn_cancel);
            noButton.setVisibility(View.VISIBLE);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(save_delete.equals("save")) {
                        uploadPending(position);
                        dialog.dismiss();
                    }
                    else if(save_delete.equals("delete")) {
                        deletePending(position);
                        dialog.dismiss();
                    }
                    else if(save_delete.equals("dead_sapling_save")){
                        uploadDeadSaplingPending(position);
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void deletePending(int position) {
        String batch_primary_id = String.valueOf(batchList.get(position).getBatch_primary_id());
        int bpd = db.delete(DBHelper.BATCH_DETAILS, "batch_primary_id = ? ", new String[]{batch_primary_id});
        int bid = db.delete(DBHelper.BATCH_IMAGES_DETAILS, "batch_primary_id = ? ", new String[]{batch_primary_id});
        int dsd = db.delete(DBHelper.BATCH_SPECIES_DETAILS, "batch_primary_id = ? ", new String[]{batch_primary_id});
        batchList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, batchList.size());
        Log.d("batch_details", String.valueOf(bpd));
        Log.d("batch_image", String.valueOf(bid));
        Log.d("batch_species", String.valueOf(dsd));
    }


    public void uploadPending(int position) {
        JSONObject dataset = new JSONObject();
        JSONObject dataset1 = new JSONObject();
        JSONArray nursery_seeding_batch_details = new JSONArray();
        JSONArray seeding_batch_photos = new JSONArray();
        JSONArray seeding_batch_species = new JSONArray();
        String batch_primary_id = String.valueOf(batchList.get(position).getBatch_primary_id());

        ArrayList<NurserySurvey> batchDetails = new ArrayList<>();
        ArrayList<NurserySurvey> batchPhotosDetails = new ArrayList<>();
        ArrayList<NurserySurvey> batchSpeciesDetails = new ArrayList<>();
        batchDetails = dbData.get_nursery_batch_details("Particular","0",batch_primary_id);
        batchPhotosDetails = dbData.get_nursery_batch_photos_details(batch_primary_id);
        batchSpeciesDetails = dbData.get_nursery_batch_species_details(batch_primary_id,"","0","");
        try {
            for (int i=0;i<batchDetails.size();i++){
                if(batchDetails.get(i).getBatch_id()==0){
                    dataset1.put("batch_id","");
                }
                else {
                    dataset1.put("batch_id",batchDetails.get(i).getBatch_id());
                }
                for (int j =0;j<batchPhotosDetails.size();j++){
                    JSONObject photosDetails = new JSONObject();
                    photosDetails.put("lat",batchPhotosDetails.get(j).getLatitude());
                    photosDetails.put("long",batchPhotosDetails.get(j).getLongitude());
                    photosDetails.put("image",BitMapToString(batchPhotosDetails.get(j).getImage()));
                    seeding_batch_photos.put(photosDetails);
                }
                dataset1.put("seeding_batch_photos",seeding_batch_photos);
                for (int k=0;k<batchSpeciesDetails.size();k++){
                    JSONObject speciesDetails = new JSONObject();
                    if(batchSpeciesDetails.get(k).getBatch_species_id()==0){
                        speciesDetails.put("batch_species_id","");
                    }
                    else {
                        speciesDetails.put("batch_species_id",batchSpeciesDetails.get(k).getBatch_species_id());
                    }
                    speciesDetails.put("species_type_id",batchSpeciesDetails.get(k).getSpecies_type_id());
                    speciesDetails.put("number_of_seedlings_raised",batchSpeciesDetails.get(k).getNo_of_count());
                    seeding_batch_species.put(speciesDetails);
                }
                dataset1.put("seeding_batch_species",seeding_batch_species);
            }
            nursery_seeding_batch_details.put(dataset1);
            dataset.put(AppConstant.KEY_SERVICE_ID,"nursery_seeding_batch_details_save");
            dataset.put("nursery_seeding_batch_details",nursery_seeding_batch_details);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Utils.isOnline()) {
            ((AddViewBatchDetails)context).UploadData(dataset,batch_primary_id);
        } else {
            Utils.showAlert((Activity) context, "Turn On Mobile Data To Upload");
        }

    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public void uploadDeadSaplingPending(int position) {
        JSONObject dataset = new JSONObject();
        JSONObject dataset1 = new JSONObject();
        JSONArray dead_sapling_details = new JSONArray();

        String batch_primary_id = String.valueOf(batchList.get(position).getBatch_primary_id());
        String batch_id = String.valueOf(batchList.get(position).getBatch_id());
        ArrayList<NurserySurvey> deadSaplingList = new ArrayList<>();
        deadSaplingList = dbData.get_particular_dead_sapling_details(batch_id,"","0","");

        try {
            for (int i=0;i<deadSaplingList.size();i++) {
                dataset.put("batch_id", deadSaplingList.get(i).getBatch_id());
                dataset.put("batch_species_id", deadSaplingList.get(i).getBatch_species_id());
                dataset.put("species_type_id", deadSaplingList.get(i).getSpecies_type_id());
                dataset.put("no_of_dead_sapling", deadSaplingList.get(i).getNo_of_dead_sapling());
                dataset.put("dead_stage", deadSaplingList.get(i).getDead_stage_id());
                dataset.put("dead_reason", deadSaplingList.get(i).getDead_reason());
                dead_sapling_details.put(dataset);
            }
            dataset1.put(AppConstant.KEY_SERVICE_ID, "dead_sapling_details_save");
            dataset1.put("dead_sapling_details", dead_sapling_details);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Utils.isOnline()) {
            ((AddViewBatchDetails)context).UploadDeadSapling(dataset1,batch_primary_id);
        } else {
            Utils.showAlert((Activity) context, "Turn On Mobile Data To Upload");
        }

    }
}
