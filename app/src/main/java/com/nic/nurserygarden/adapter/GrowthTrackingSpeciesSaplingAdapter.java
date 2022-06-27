package com.nic.nurserygarden.adapter;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchSpeciesDetails;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.databinding.BatchGrowthTrackingSpeciesItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.List;

public class GrowthTrackingSpeciesSaplingAdapter extends RecyclerView.Adapter<GrowthTrackingSpeciesSaplingAdapter.MyViewHolder> {

    private List<NurserySurvey> speciesList;
    private Context context;
    private LayoutInflater layoutInflater;
    private com.nic.nurserygarden.dataBase.dbData dbData;
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    String type;

    public GrowthTrackingSpeciesSaplingAdapter(List<NurserySurvey> speciesList, Context context, com.nic.nurserygarden.dataBase.dbData dbData,String type) {
        this.speciesList = speciesList;
        this.context = context;
        this.dbData=dbData;
        this.type=type;

        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public GrowthTrackingSpeciesSaplingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        BatchGrowthTrackingSpeciesItemViewBinding growthTrackingSpeciesItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.batch_growth_tracking_species_item_view, parent, false);
        return new MyViewHolder(growthTrackingSpeciesItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthTrackingSpeciesSaplingAdapter.MyViewHolder holder, int position) {

        holder.batchGrowthTrackingSpeciesItemViewBinding.speciesTypeId.setText(""+speciesList.get(position).getSpecies_type_id());
        holder.batchGrowthTrackingSpeciesItemViewBinding.noOfSaplings.setText(""+speciesList.get(position).getNo_of_saplings());
        holder.batchGrowthTrackingSpeciesItemViewBinding.heightInCm.setText(""+speciesList.get(position).getHeight_in_cm());
        holder.batchGrowthTrackingSpeciesItemViewBinding.ageInDays.setText(""+speciesList.get(position).getAge_in_days());

        if(type.equals("")){
            if(speciesList.get(position).getServer_flag().equals("0")){
                holder.batchGrowthTrackingSpeciesItemViewBinding.delete.setVisibility(View.GONE);
                holder.batchGrowthTrackingSpeciesItemViewBinding.edit.setVisibility(View.GONE);
            }
            else {
                holder.batchGrowthTrackingSpeciesItemViewBinding.delete.setVisibility(View.GONE);
                holder.batchGrowthTrackingSpeciesItemViewBinding.edit.setVisibility(View.GONE);
            }

            holder.batchGrowthTrackingSpeciesItemViewBinding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save_and_delete_alert(position,"delete");
                }
            });
            holder.batchGrowthTrackingSpeciesItemViewBinding.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save_and_delete_alert(position,"edit");
                }
            });
        }
        else {
            holder.batchGrowthTrackingSpeciesItemViewBinding.delete.setVisibility(View.GONE);
            holder.batchGrowthTrackingSpeciesItemViewBinding.edit.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return speciesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public BatchGrowthTrackingSpeciesItemViewBinding batchGrowthTrackingSpeciesItemViewBinding;

        public MyViewHolder(BatchGrowthTrackingSpeciesItemViewBinding Binding) {
            super(Binding.getRoot());
            batchGrowthTrackingSpeciesItemViewBinding = Binding;
        }
    }

    public void save_and_delete_alert(int position,String save_delete){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            if(save_delete.equals("edit")) {
                text.setText(context.getResources().getString(R.string.do_u_want_to_edit));
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
                    if(save_delete.equals("edit")) {
                        editPending(position);
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
        String batch_primary_id = String.valueOf(speciesList.get(position).getBatch_primary_id());
        String species_type_id = String.valueOf(speciesList.get(position).getSpecies_type_id());
        int dsd = db.delete(DBHelper.BATCH_SPECIES_DETAILS, "batch_primary_id = ? and species_type_id = ?", new String[]{batch_primary_id,species_type_id});
        speciesList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, speciesList.size());
        Log.d("batch_species", String.valueOf(dsd));
    }

    public void editPending(int position){
        String batch_primary_id = String.valueOf(speciesList.get(position).getBatch_primary_id());
        String species_type_id = String.valueOf(speciesList.get(position).getSpecies_type_id());
        String batch_id = String.valueOf(speciesList.get(position).getBatch_id());
        String batch_species_id = String.valueOf(speciesList.get(position).getBatch_species_id());
        String server_flag = speciesList.get(position).getServer_flag();
        String species_type_name_en = speciesList.get(position).getSpecies_name_en();
        String species_type_name_ta = speciesList.get(position).getSpecies_name_ta();
        String no_of_count = String.valueOf(speciesList.get(position).getNo_of_count());

        ((AddViewBatchSpeciesDetails)context).editItemView(batch_primary_id,species_type_id,batch_id,batch_species_id,server_flag,
                species_type_name_en,species_type_name_ta,no_of_count);
    }


}

