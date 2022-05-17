package com.nic.nurserygarden.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;

import com.nic.nurserygarden.databinding.NurserySaplingsOrderItemsBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.List;

public class SellSpeciesAdapter extends RecyclerView.Adapter<SellSpeciesAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NurserySurvey> orderItemList;
    Context context;
    String type;

    public SellSpeciesAdapter(List<NurserySurvey> orderItemList, Context context, String type) {
        this.orderItemList = orderItemList;
        this.context = context;
        this.type=type;
    }

    @NonNull
    @Override
    public SellSpeciesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        NurserySaplingsOrderItemsBinding nurserySaplingsOrderItemsBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.nursery_saplings_order_items, viewGroup, false);
        return new MyViewHolder(nurserySaplingsOrderItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final SellSpeciesAdapter.MyViewHolder holder, final int position) {
        holder.nurserySaplingsOrderItemsBinding.batchNumber.setText(""+orderItemList.get(position).getBatch_number());
        holder.nurserySaplingsOrderItemsBinding.speciesName.setText(orderItemList.get(position).getSpecies_name_en());
        holder.nurserySaplingsOrderItemsBinding.days.setText(""+orderItemList.get(position).getAge_in_days());
        holder.nurserySaplingsOrderItemsBinding.height.setText(orderItemList.get(position).getHeight_in_cm());
        holder.nurserySaplingsOrderItemsBinding.availableCount.setText(""+orderItemList.get(position).getNo_of_saplings());
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private NurserySaplingsOrderItemsBinding nurserySaplingsOrderItemsBinding;

        public MyViewHolder(NurserySaplingsOrderItemsBinding Binding) {
            super(Binding.getRoot());
            nurserySaplingsOrderItemsBinding = Binding;
        }
    }
}

