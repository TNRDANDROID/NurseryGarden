package com.nic.nurserygarden.adapter;

import android.app.Activity;
import android.app.Dialog;
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
import com.nic.nurserygarden.activity.NewPendingScreen;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.NewPendingAdapterBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NewPendingAdapter extends RecyclerView.Adapter<NewPendingAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private List<NurserySurvey> pendingListValues;
    JSONObject dataset = new JSONObject();
    dbData dbData;
    private LayoutInflater layoutInflater;
    public  DBHelper dbHelper;
    public  SQLiteDatabase db;
    public NewPendingAdapter(Activity context, List<NurserySurvey> pendingListValues, dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);

        this.dbData=dbData;
        this.pendingListValues = pendingListValues;

        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public NewPendingAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        NewPendingAdapterBinding pendingAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.new_pending_adapter, viewGroup, false);
        return new MyViewHolder(pendingAdapterBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private NewPendingAdapterBinding pendingAdapterBinding;

        public MyViewHolder(NewPendingAdapterBinding Binding) {
            super(Binding.getRoot());
            pendingAdapterBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final NewPendingAdapter.MyViewHolder holder, final int position) {

        holder.pendingAdapterBinding.batchNumberText.setText("Batch Number "+ pendingListValues.get(position).getBatch_number());

        holder.pendingAdapterBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadPending(position);
                save_and_delete_alert(position,"save");
            }
        });

        holder.pendingAdapterBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deletePending(position);
                save_and_delete_alert(position,"delete");
            }
        });



    }


    public void deletePending(int position) {
        String batch_id = String.valueOf(pendingListValues.get(position).getBatch_id());
        //int sdsm = db.delete(DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE, null, null);
        int sdsm = db.delete(DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE, "batch_id = ? ", new String[]{batch_id});
        pendingListValues.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, pendingListValues.size());
        Log.d("sdsm", String.valueOf(sdsm));
    }


    public void uploadPending(int position) {
        JSONObject data_set = new JSONObject();
        JSONArray dead_sapling_details = new JSONArray();
        String batch_id = String.valueOf(pendingListValues.get(position).getBatch_id());
        try {
            dbData.open();
            ArrayList<NurserySurvey> deadOrderList= new ArrayList<>();
            deadOrderList.addAll(dbData.get_dead_sapling_details_for_upload("Particular",batch_id));
            data_set.put("service_id","dead_sapling_details_save");
            for(int i=0; i<deadOrderList.size();i++){
                JSONObject data_set1= new JSONObject();
                data_set1.put("batch_id",deadOrderList.get(i).getBatch_id());
                data_set1.put("batch_species_id",deadOrderList.get(i).getBatch_species_id());
                data_set1.put("growth_tracking_id",deadOrderList.get(i).getGrowth_tracking_id());
                data_set1.put("growth_tracking_details_id",deadOrderList.get(i).getGrowth_tracking_details_id());
                data_set1.put("species_type_id",deadOrderList.get(i).getSpecies_type_id());
                data_set1.put("sapling_height_in_cm",deadOrderList.get(i).getHeight_in_cm());
                data_set1.put("sapling_age_in_days",deadOrderList.get(i).getAge_in_days());
                data_set1.put("no_of_dead_sapling",deadOrderList.get(i).getNo_of_dead_sapling());
                dead_sapling_details.put(data_set1);
            }
            data_set.put("dead_sapling_details",dead_sapling_details);
            Log.d("normal_json",""+data_set);


        }
        catch (JSONException e){
            e.printStackTrace();
        }
        if (Utils.isOnline()) {
            ((NewPendingScreen)context).uploadDeadOrder(data_set,batch_id);
        } else {
            Utils.showAlert(context, "Turn On Mobile Data To Upload");
        }

    }

    @Override
    public int getItemCount() {
        return pendingListValues.size();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

}
