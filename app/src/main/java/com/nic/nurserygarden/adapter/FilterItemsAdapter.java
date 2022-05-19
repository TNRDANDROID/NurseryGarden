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
import com.nic.nurserygarden.activity.DeadSaplingActivty.NewDeadSaplingEntry;
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
    String which_class;

    public FilterItemsAdapter(List<NurserySurvey> capacityList, Context context,String type,String which_class) {
        this.capacityList = capacityList;
        this.context = context;
        this.type=type;
        this.which_class=which_class;
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

        switch (type) {
            case "Batch_Number":
                holder.filterItemNameBinding.filterItemName.setText("Batch " + capacityList.get(position).getBatch_number());
                break;
            case "Species_Name":
                holder.filterItemNameBinding.filterItemName.setText("" + capacityList.get(position).getSpecies_name_en());
                break;
            case "Days":
                holder.filterItemNameBinding.filterItemName.setText("" + capacityList.get(position).getAge_in_days());
                break;
            default:
                holder.filterItemNameBinding.filterItemName.setText("" + capacityList.get(position).getHeight_in_cm());
                break;
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
                if(which_class.equals("Sale")){
                    switch (type) {
                        case "Batch_Number":
                            ((OrderItemActivity) context).getBathNumberClickedItem(position);
                            break;
                        case "Species_Name":
                            ((OrderItemActivity) context).getSpeciesClickedItem(position);
                            break;
                        case "Days":
                            ((OrderItemActivity) context).getAgesClickedItem(position);
                            break;
                        default:
                            ((OrderItemActivity) context).getHeightClickedItem(position);
                            break;
                    }
                }
                else {
                    switch (type) {
                        case "Batch_Number":
                            ((NewDeadSaplingEntry) context).getBathNumberClickedItem(position);
                            break;
                        case "Species_Name":
                            ((NewDeadSaplingEntry) context).getSpeciesClickedItem(position);
                            break;
                        case "Days":
                            ((NewDeadSaplingEntry) context).getAgesClickedItem(position);
                            break;
                        default:
                            ((NewDeadSaplingEntry) context).getHeightClickedItem(position);
                            break;
                    }
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

