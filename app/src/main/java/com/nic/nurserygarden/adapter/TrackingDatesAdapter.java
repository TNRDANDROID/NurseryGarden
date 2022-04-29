package com.nic.nurserygarden.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.databinding.TrackingDatesItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;

import java.util.List;

public class TrackingDatesAdapter extends RecyclerView.Adapter<TrackingDatesAdapter.MyViewHolder> {

    private List<NurserySurvey> dateList;
    private Context context;
    private LayoutInflater layoutInflater;
    private com.nic.nurserygarden.dataBase.dbData dbData;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    public TrackingDatesAdapter(List<NurserySurvey> dateList, Context context, com.nic.nurserygarden.dataBase.dbData dbData) {
        this.dateList = dateList;
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
    public TrackingDatesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TrackingDatesItemViewBinding trackingDatesItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.tracking_dates_item_view, parent, false);
        return new MyViewHolder(trackingDatesItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackingDatesAdapter.MyViewHolder holder, int position) {

        holder.trackingDatesItemViewBinding.entryDate.setText("Date "+ dateList.get(position).getCreated_date());

    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TrackingDatesItemViewBinding trackingDatesItemViewBinding;

        public MyViewHolder(TrackingDatesItemViewBinding Binding) {
            super(Binding.getRoot());
            trackingDatesItemViewBinding = Binding;
        }
    }


}
