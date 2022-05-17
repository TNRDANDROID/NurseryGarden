package com.nic.nurserygarden.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.DeadSaplingActivty.DeadSaplingEntry;
import com.nic.nurserygarden.activity.GrowthTrackingActivity.SaplingEntryView;
import com.nic.nurserygarden.activity.SellAndBuyActivity.OrderItemActivity;
import com.nic.nurserygarden.databinding.FilterItemNameBinding;
import com.nic.nurserygarden.databinding.SpeciesListItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.List;

public class FilterItemsAdapter extends RecyclerView.Adapter<FilterItemsAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NurserySurvey> capacityList;
    Context context;
    int pos=-1;
    String type;

    public FilterItemsAdapter(List<NurserySurvey> capacityList, Context context,String type) {
        this.capacityList = capacityList;
        this.context = context;
        this.type=type;
    }

    @NonNull
    @Override
    public FilterItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        FilterItemNameBinding filterItemNameBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.filter_item_name, viewGroup, false);
        return new MyViewHolder(filterItemNameBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterItemsAdapter.MyViewHolder holder, final int position) {

        if(type.equals("Batch_Number")){
            holder.filterItemNameBinding.filterItemName.setText("Batch "+capacityList.get(position).getBatch_number());
        }
        else if(type.equals("Species_Name")){
            holder.filterItemNameBinding.filterItemName.setText(""+capacityList.get(position).getSpecies_name_en());
        }
        else if(type.equals("Days")){
            holder.filterItemNameBinding.filterItemName.setText(""+capacityList.get(position).getAge_in_days());
        }
        else {
            holder.filterItemNameBinding.filterItemName.setText(""+capacityList.get(position).getHeight_in_cm());
        }



        if(position==pos){
            holder.filterItemNameBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        }
        else {
            holder.filterItemNameBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));

        }

        holder.filterItemNameBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos=position;
                notifyDataSetChanged();
                if(type.equals("Batch_Number")){
                    ((OrderItemActivity)context).getBathNumberClickedItem(position);
                }
                else if(type.equals("Species_Name")){
                    ((OrderItemActivity)context).getSpeciesClickedItem(position);
                }
                else if(type.equals("Days")){
                    ((OrderItemActivity)context).getAgesClickedItem(position);
                }
                else {
                    ((OrderItemActivity)context).getHeightClickedItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return capacityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private FilterItemNameBinding filterItemNameBinding;

        public MyViewHolder(FilterItemNameBinding Binding) {
            super(Binding.getRoot());
            filterItemNameBinding = Binding;
        }
    }
}

