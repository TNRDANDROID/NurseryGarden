package com.nic.nurserygarden.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.GrowthTrackingActivity.SaplingEntryView;
import com.nic.nurserygarden.databinding.SpeciesListItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.List;

public class SpeciesListAdapter extends RecyclerView.Adapter<SpeciesListAdapter.MyViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NurserySurvey> capacityList;
    Context context;
    int pos=-1;
    String type;

    public SpeciesListAdapter(List<NurserySurvey> capacityList, Context context,String type) {
        this.capacityList = capacityList;
        this.context = context;
        this.type=type;
    }

    @NonNull
    @Override
    public SpeciesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        SpeciesListItemViewBinding speciesListItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.species_list_item_view, viewGroup, false);
        return new MyViewHolder(speciesListItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final SpeciesListAdapter.MyViewHolder holder, final int position) {

        holder.speciesListItemViewBinding.speciesName.setText(capacityList.get(position).getSpecies_name_en());


        if(position==pos){
            holder.speciesListItemViewBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        }
        else {
            holder.speciesListItemViewBinding.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.grey_2));

        }

        holder.speciesListItemViewBinding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos=position;
                notifyDataSetChanged();
                ((SaplingEntryView) context).speciesdapterItemClicked(pos);

            }
        });
    }

    @Override
    public int getItemCount() {
        return capacityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private SpeciesListItemViewBinding speciesListItemViewBinding ;

        public MyViewHolder(SpeciesListItemViewBinding Binding) {
            super(Binding.getRoot());
            speciesListItemViewBinding = Binding;
        }
    }
}

