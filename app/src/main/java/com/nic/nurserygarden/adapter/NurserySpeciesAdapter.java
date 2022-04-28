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
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.databinding.NurserySpeciesItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.List;

public class NurserySpeciesAdapter extends RecyclerView.Adapter<NurserySpeciesAdapter.MyViewHolder> {

    private List<NurserySurvey> speciesList;
    private Context context;
    private LayoutInflater layoutInflater;
    private com.nic.nurserygarden.dataBase.dbData dbData;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public NurserySpeciesAdapter(List<NurserySurvey> speciesList, Context context, com.nic.nurserygarden.dataBase.dbData dbData) {
        this.speciesList = speciesList;
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
    public NurserySpeciesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NurserySpeciesItemViewBinding nurserySpeciesItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.nursery_species_item_view, parent, false);
        return new MyViewHolder(nurserySpeciesItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NurserySpeciesAdapter.MyViewHolder holder, int position) {

        holder.nurserySpeciesItemViewBinding.speciesTypeId.setText("Species Type Id   : "+speciesList.get(position).getSpecies_type_id());
        holder.nurserySpeciesItemViewBinding.speciesTypeName.setText("Species Type Name : "+speciesList.get(position).getSpecies_name_en());
        holder.nurserySpeciesItemViewBinding.speciesCount.setText("Species Count     :"+speciesList.get(position).getNo_of_count());

        if(speciesList.get(position).getServer_flag().equals("0")){
            holder.nurserySpeciesItemViewBinding.delete.setVisibility(View.VISIBLE);
        }
        else {
            holder.nurserySpeciesItemViewBinding.delete.setVisibility(View.GONE);
        }

        holder.nurserySpeciesItemViewBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_and_delete_alert(position,"delete");
            }
        });

    }

    @Override
    public int getItemCount() {
        return speciesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public NurserySpeciesItemViewBinding nurserySpeciesItemViewBinding;

        public MyViewHolder(NurserySpeciesItemViewBinding Binding) {
            super(Binding.getRoot());
            nurserySpeciesItemViewBinding = Binding;
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
                        //uploadPending(position);
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



}

