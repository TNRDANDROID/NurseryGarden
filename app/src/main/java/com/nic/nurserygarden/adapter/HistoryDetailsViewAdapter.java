package com.nic.nurserygarden.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.databinding.HistoryDetailsItemViewBinding;
import com.nic.nurserygarden.databinding.OrderBasicHistoryItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.List;

public class HistoryDetailsViewAdapter extends RecyclerView.Adapter<HistoryDetailsViewAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NurserySurvey> historyItemList;
    Context context;
    int pos=-1;
    String type;
    String which_class;

    public HistoryDetailsViewAdapter(List<NurserySurvey> historyItemList, Context context, String type, String which_class) {
        this.historyItemList = historyItemList;
        this.context = context;
        this.type=type;
        this.which_class=which_class;
    }

    @NonNull
    @Override
    public HistoryDetailsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        HistoryDetailsItemViewBinding historyDetailsItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.history_details_item_view, viewGroup, false);
        return new MyViewHolder(historyDetailsItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryDetailsViewAdapter.MyViewHolder holder, final int position) {

        if(position ==0){
            holder.historyItemViewBinding.titleLayout.setVisibility(View.VISIBLE);
        }
        else {
            holder.historyItemViewBinding.titleLayout.setVisibility(View.GONE);
        }
        holder.historyItemViewBinding.speciesName.setText(historyItemList.get(position).getSpecies_name_en());
        holder.historyItemViewBinding.quantity.setText(""+historyItemList.get(position).getNo_of_saplings());
        holder.historyItemViewBinding.height.setText(""+historyItemList.get(position).getHeight_in_cm());
        holder.historyItemViewBinding.age.setText(""+historyItemList.get(position).getAge_in_days());
    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private HistoryDetailsItemViewBinding historyItemViewBinding;

        public MyViewHolder(HistoryDetailsItemViewBinding Binding) {
            super(Binding.getRoot());
            historyItemViewBinding = Binding;
        }
    }
}

