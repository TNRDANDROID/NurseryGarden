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
import com.nic.nurserygarden.activity.CameraScreen;
import com.nic.nurserygarden.activity.FullImageActivity;
import com.nic.nurserygarden.activity.GrowthTrackingActivity.GrowthTracking;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.databinding.NurseryBatchItemViewBinding;
import com.nic.nurserygarden.databinding.TrackingGrowthItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TrackingGrowthAdapter extends RecyclerView.Adapter<TrackingGrowthAdapter.MyViewHolder> {

    private List<NurserySurvey> growthList;
    private Context context;
    private LayoutInflater layoutInflater;
    private com.nic.nurserygarden.dataBase.dbData dbData;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public TrackingGrowthAdapter(List<NurserySurvey> growthList, Context context, com.nic.nurserygarden.dataBase.dbData dbData) {
        this.growthList = growthList;
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
    public TrackingGrowthAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TrackingGrowthItemViewBinding trackingGrowthItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.tracking_growth_item_view, parent, false);
        return new MyViewHolder(trackingGrowthItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackingGrowthAdapter.MyViewHolder holder, int position) {

        holder.trackingGrowthItemViewBinding.createdDate.setText("Date "+ growthList.get(position).getCreated_date());



        if(growthList.get(position).getServer_flag().equals("0")){
            ArrayList<NurserySurvey> getSpeciesCount = new ArrayList<>();
            //ArrayList<NurserySurvey> getImageCount = new ArrayList<>();
            getSpeciesCount.addAll(dbData.get_batch_growth_species_details(String.valueOf(growthList.get(position).getBatch_id()),"","0","",growthList.get(position).getCreated_date()));
            //getImageCount.addAll(dbData.get_batch_growth_tracking_photos_details(String.valueOf(growthList.get(position).getBatch_id()),growthList.get(position).getCreated_date()));
            //if(getSpeciesCount.size()>0||getImageCount.size()>0) {
            if(getSpeciesCount.size()>0) {
                holder.trackingGrowthItemViewBinding.upload.setVisibility(View.VISIBLE);
            }
            else {
                holder.trackingGrowthItemViewBinding.upload.setVisibility(View.GONE);
            }
            holder.trackingGrowthItemViewBinding.delete.setVisibility(View.VISIBLE);

        }
        else {
            ArrayList<NurserySurvey> getSpeciesCount = new ArrayList<>();
            //ArrayList<NurserySurvey> getImageCount = new ArrayList<>();
            getSpeciesCount = dbData.get_batch_growth_species_details(String.valueOf(growthList.get(position).getBatch_id()),"","0","",growthList.get(position).getCreated_date());
            //getImageCount.addAll(dbData.get_batch_growth_tracking_photos_details(String.valueOf(growthList.get(position).getBatch_id()),growthList.get(position).getCreated_date()));

            //if(getSpeciesCount.size()>0||getImageCount.size()>0) {
            if(getSpeciesCount.size()>0) {
                holder.trackingGrowthItemViewBinding.upload.setVisibility(View.VISIBLE);
            }
            else {
                holder.trackingGrowthItemViewBinding.upload.setVisibility(View.GONE);

            }
            holder.trackingGrowthItemViewBinding.delete.setVisibility(View.GONE);
        }
        holder.trackingGrowthItemViewBinding.saplingDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GrowthTracking)context).clickSaplingBtn(growthList.get(position).getBatch_growth_tracking_primary_id());
            }
        });

        holder.trackingGrowthItemViewBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_delete_alert(position,"save");
            }
        });
        holder.trackingGrowthItemViewBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_delete_alert(position,"delete");
            }
        });

        holder.trackingGrowthItemViewBinding.batchImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoAddSpeciesClass = new Intent(context, FullImageActivity.class);
                gotoAddSpeciesClass.putExtra("ImageType","Growth");
                gotoAddSpeciesClass.putExtra("On_Off_Type","Online");
                gotoAddSpeciesClass.putExtra("batch_id",growthList.get(position).getBatch_id());
                gotoAddSpeciesClass.putExtra("growth_tracking_id",growthList.get(position).getGrowth_tracking_id());

                context.startActivity(gotoAddSpeciesClass);
            }
        });
/*
        holder.trackingGrowthItemViewBinding.takePhotoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_screen = new Intent(context, CameraScreen.class);
                camera_screen.putExtra("activity_type","GrowthTracking");
                camera_screen.putExtra("batch_id",growthList.get(position).getBatch_id());
                camera_screen.putExtra("batch_primary_id",growthList.get(position).getBatch_primary_id());
                camera_screen.putExtra("entry_date",growthList.get(position).getCreated_date());
                camera_screen.putExtra("batch_growth_tracking_primary_id",growthList.get(position).getBatch_growth_tracking_primary_id());

                context.startActivity(camera_screen);
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return growthList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TrackingGrowthItemViewBinding trackingGrowthItemViewBinding;

        public MyViewHolder(TrackingGrowthItemViewBinding Binding) {
            super(Binding.getRoot());
            trackingGrowthItemViewBinding = Binding;
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
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void deletePending(int position) {
        String batch_growth_tracking_primary_id = String.valueOf(growthList.get(position).getBatch_growth_tracking_primary_id());
        int bpd = db.delete(DBHelper.BATCH_GROWTH_TRACKING_DETAILS, "batch_growth_tracking_primary_id = ? ", new String[]{batch_growth_tracking_primary_id});
        int bid = db.delete(DBHelper.BATCH_GROWTH_TRACKING_PHOTOS_DETAILS, "batch_growth_tracking_primary_id = ? ", new String[]{batch_growth_tracking_primary_id});
        int dsd = db.delete(DBHelper.BATCH_GROWTH_TRACKING_SPECIES_DETAILS, "batch_growth_tracking_primary_id = ? ", new String[]{batch_growth_tracking_primary_id});
        growthList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, growthList.size());
        Log.d("batch_details", String.valueOf(bpd));
        Log.d("batch_image", String.valueOf(bid));
        Log.d("batch_species", String.valueOf(dsd));
    }


    public void uploadPending(int position) {
        JSONObject dataset = new JSONObject();
        JSONObject dataset1 = new JSONObject();
        JSONArray nursery_seeding_batch_sapling_growth_tracking = new JSONArray();
        JSONArray seeding_growth_tracking_photos = new JSONArray();
        JSONArray seeding_growth_tracking_details = new JSONArray();
        String batch_id = String.valueOf(growthList.get(position).getBatch_id());
        String batch_growth_tracking_primary_id = String.valueOf(growthList.get(position).getBatch_growth_tracking_primary_id());
        String entry_date = growthList.get(position).getCreated_date();

        ArrayList<NurserySurvey> batchGrowthDetails = new ArrayList<>();
        ArrayList<NurserySurvey> batchGrowthPhotosDetails = new ArrayList<>();
        ArrayList<NurserySurvey> batchSpeciesGrowthDetails = new ArrayList<>();
        batchGrowthDetails = dbData.get_nursery_batch_growth_tacking_details(batch_id,entry_date,"All");
        batchGrowthPhotosDetails = dbData.get_batch_growth_tracking_photos_details(batch_id,entry_date);
        batchSpeciesGrowthDetails = dbData.get_batch_growth_species_details(batch_id,"","0","",entry_date);
        try {
            for (int i=0;i<batchGrowthDetails.size();i++){
                if(batchGrowthDetails.get(i).getGrowth_tracking_id()==0){
                    dataset1.put("growth_tracking_id","");
                }
                else {
                    dataset1.put("growth_tracking_id",batchGrowthDetails.get(i).getGrowth_tracking_id());
                }
                dataset1.put("batch_id",batchGrowthDetails.get(i).getBatch_id());
                dataset1.put("entry_date",batchGrowthDetails.get(i).getCreated_date());
                for (int j =0;j<batchGrowthPhotosDetails.size();j++){
                    JSONObject photosDetails = new JSONObject();
                    photosDetails.put("lat",batchGrowthPhotosDetails.get(j).getLatitude());
                    photosDetails.put("long",batchGrowthPhotosDetails.get(j).getLongitude());
                    photosDetails.put("image",BitMapToString(batchGrowthPhotosDetails.get(j).getImage()));
                    seeding_growth_tracking_photos.put(photosDetails);
                }
                dataset1.put("seeding_growth_tracking_photos",seeding_growth_tracking_photos);
                for (int k=0;k<batchSpeciesGrowthDetails.size();k++){
                    JSONObject speciesDetails = new JSONObject();
                    if(batchSpeciesGrowthDetails.get(k).getBatch_species_id()==0){
                        speciesDetails.put("batch_species_id","");
                    }
                    else {
                        speciesDetails.put("batch_species_id",batchSpeciesGrowthDetails.get(k).getBatch_species_id());
                    }
                    speciesDetails.put("species_type_id",batchSpeciesGrowthDetails.get(k).getSpecies_type_id());
                    speciesDetails.put("no_of_saplings",batchSpeciesGrowthDetails.get(k).getNo_of_saplings());
                    speciesDetails.put("sapling_height_in_cm",batchSpeciesGrowthDetails.get(k).getHeight_in_cm());
                    seeding_growth_tracking_details.put(speciesDetails);
                }
                dataset1.put("seeding_growth_tracking_details",seeding_growth_tracking_details);
            }
            nursery_seeding_batch_sapling_growth_tracking.put(dataset1);
            dataset.put(AppConstant.KEY_SERVICE_ID,"sapling_growth_tracking_save");
            dataset.put("nursery_seeding_batch_sapling_growth_tracking",nursery_seeding_batch_sapling_growth_tracking);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Utils.isOnline()) {
            ((GrowthTracking)context).UploadData(dataset,batch_growth_tracking_primary_id,batch_id);
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
}
