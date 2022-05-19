package com.nic.nurserygarden.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.NurserySaplingDeadItemsBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.ArrayList;
import java.util.List;

public class DeadSpeciesAdapter extends RecyclerView.Adapter<DeadSpeciesAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NurserySurvey> orderItemList;
    private List<NurserySurvey> deadStageList;
    Context context;
    String type;
    DBHelper dbHelper;
    SQLiteDatabase db;
    dbData dbData;
    public DeadSpeciesAdapter(List<NurserySurvey> orderItemList, Context context, String type,dbData dbData,List<NurserySurvey> deadStageList) {
        this.orderItemList = orderItemList;
        this.deadStageList = deadStageList;
        this.context = context;
        this.type=type;
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
    public DeadSpeciesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        NurserySaplingDeadItemsBinding nurserySaplingDeadItemsBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.nursery_sapling_dead_items, viewGroup, false);
        return new MyViewHolder(nurserySaplingDeadItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeadSpeciesAdapter.MyViewHolder holder, final int position) {
        holder.nurserySaplingDeadItemsBinding.batchNumber.setText(""+orderItemList.get(position).getBatch_number());
        holder.nurserySaplingDeadItemsBinding.speciesName.setText(orderItemList.get(position).getSpecies_name_en());
        holder.nurserySaplingDeadItemsBinding.days.setText(""+orderItemList.get(position).getAge_in_days());
        holder.nurserySaplingDeadItemsBinding.height.setText(orderItemList.get(position).getHeight_in_cm());
        holder.nurserySaplingDeadItemsBinding.availableCount.setText(""+orderItemList.get(position).getNo_of_saplings());
        holder.nurserySaplingDeadItemsBinding.deadCount.setText(orderItemList.get(position).getDead_typed_count_text());
        holder.nurserySaplingDeadItemsBinding.deadCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                orderItemList.get(position).setDead_typed_count_text(holder.nurserySaplingDeadItemsBinding.deadCount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                new Runnable(){
                    @Override
                    public void run() {
                        //notifyItemChanged(position);
                    }
                };
            }
        });


    }

    public ArrayList<NurserySurvey> getTheDeadArrayList(){
        return (ArrayList<NurserySurvey>) orderItemList;
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private NurserySaplingDeadItemsBinding nurserySaplingDeadItemsBinding;

        public MyViewHolder(NurserySaplingDeadItemsBinding Binding) {
            super(Binding.getRoot());
            nurserySaplingDeadItemsBinding = Binding;
        }
    }
}

