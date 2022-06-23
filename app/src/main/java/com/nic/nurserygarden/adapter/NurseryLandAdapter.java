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

import com.bumptech.glide.Glide;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.FullImageActivity;
import com.nic.nurserygarden.activity.LandActivity.AddViewLand;
import com.nic.nurserygarden.activity.NewPendingScreen;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.NurseryLandItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NurseryLandAdapter extends RecyclerView.Adapter<NurseryLandAdapter.MyViewHolder> {

    private List<NurserySurvey> landList;
    private Context context;
    private LayoutInflater layoutInflater;
    private com.nic.nurserygarden.dataBase.dbData dbData;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public NurseryLandAdapter(List<NurserySurvey> landList, Context context,dbData dbData) {
        this.landList = landList;
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
    public NurseryLandAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NurseryLandItemViewBinding nurseryLandItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.nursery_land_item_view, parent, false);
        return new MyViewHolder(nurseryLandItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NurseryLandAdapter.MyViewHolder holder, int position) {
        /*Glide.with(context).load(landList.get(position).getImage())
                .into(holder.nurseryLandItemViewBinding.landImage);*/
        holder.nurseryLandItemViewBinding.landNumber.setText("Number "+landList.get(position).getLand_primary_id());
        holder.nurseryLandItemViewBinding.landAddressName.setText(landList.get(position).getLand_address());
        holder.nurseryLandItemViewBinding.landNameTa.setText(landList.get(position).getLand_type_name_ta());
        holder.nurseryLandItemViewBinding.landNameEn.setText(landList.get(position).getLand_type_name_en());
        holder.nurseryLandItemViewBinding.landImage.setImageBitmap(landList.get(position).getImage());

        if(landList.get(position).getServer_flag().equals("0")){
            holder.nurseryLandItemViewBinding.upload.setVisibility(View.VISIBLE);
        }
        else {
            holder.nurseryLandItemViewBinding.upload.setVisibility(View.GONE);
        }

        holder.nurseryLandItemViewBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_delete_alert(position,"save");
            }
        });
    }

    @Override
    public int getItemCount() {
        return landList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public NurseryLandItemViewBinding nurseryLandItemViewBinding;

        public MyViewHolder(NurseryLandItemViewBinding Binding) {
            super(Binding.getRoot());
            nurseryLandItemViewBinding = Binding;
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
        String land_primary_id = String.valueOf(landList.get(position).getLand_primary_id());
        int sdsm = db.delete(DBHelper.NURSERY_LAND_SAVE_DETAILS, "land_primary_id = ? ", new String[]{land_primary_id});
        landList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, landList.size());
        Log.d("nlsd", String.valueOf(sdsm));
    }


    public void uploadPending(int position) {
        JSONObject dataset = new JSONObject();
        JSONObject dataset1 = new JSONObject();
        JSONArray nursery_land_details = new JSONArray();
        String land_primary_id = String.valueOf(landList.get(position).getLand_primary_id());

        try {
            dataset.put(AppConstant.KEY_SERVICE_ID,"nursery_land_save");
            dataset1.put("land_type_id",landList.get(position).getLand_type_id());
            dataset1.put("address",landList.get(position).getLand_address());
            dataset1.put("land_type_others",landList.get(position).getOther_land_type());
            dataset1.put("area",landList.get(position).getArea());
            dataset1.put("survey_no",landList.get(position).getSurvey_number());
            dataset1.put("subdiv_no",landList.get(position).getSub_div_no());
            dataset1.put("lat",landList.get(position).getLatitude());
            dataset1.put("long",landList.get(position).getLongitude());
            dataset1.put("image",BitMapToString(landList.get(position).getImage()));
            nursery_land_details.put(dataset1);
            dataset.put("nursery_land_details",nursery_land_details);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Utils.isOnline()) {
            ((AddViewLand)context).UploadData(dataset,land_primary_id);
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
