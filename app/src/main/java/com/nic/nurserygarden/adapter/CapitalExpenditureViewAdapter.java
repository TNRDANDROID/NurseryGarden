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
import com.nic.nurserygarden.databinding.ExpenditureUploadItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CapitalExpenditureViewAdapter  extends RecyclerView.Adapter<CapitalExpenditureViewAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private List<NurserySurvey> capitalExpenditureList;
    JSONObject dataset = new JSONObject();
    private LayoutInflater layoutInflater;
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    public CapitalExpenditureViewAdapter(Activity context, List<NurserySurvey> capitalExpenditureList) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.capitalExpenditureList = capitalExpenditureList;

        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CapitalExpenditureViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ExpenditureUploadItemViewBinding expenditureUploadItemViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.expenditure_upload_item_view, viewGroup, false);
        return new MyViewHolder(expenditureUploadItemViewBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ExpenditureUploadItemViewBinding expenditureUploadItemViewBinding;

        public MyViewHolder(ExpenditureUploadItemViewBinding Binding) {
            super(Binding.getRoot());
            expenditureUploadItemViewBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final CapitalExpenditureViewAdapter.MyViewHolder holder, final int position) {


        holder.expenditureUploadItemViewBinding.expenditureTypeText.setText(capitalExpenditureList.get(position).getExpenditure_type());
        holder.expenditureUploadItemViewBinding.expenditureCategoryText.setText(capitalExpenditureList.get(position).getExpense_category_en());
        holder.expenditureUploadItemViewBinding.expenditureUnitText.setText(capitalExpenditureList.get(position).getExpenditure_unit_en());
        holder.expenditureUploadItemViewBinding.expenditureFoundSrcText.setText(capitalExpenditureList.get(position).getExpenditure_fund_src_en());
        holder.expenditureUploadItemViewBinding.upload.setVisibility(View.GONE);
        holder.expenditureUploadItemViewBinding.delete.setVisibility(View.GONE);

      /*  holder.expenditureUploadItemViewBinding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadPending(position);
                save_and_delete_alert(position,"save");
            }
        });

        holder.expenditureUploadItemViewBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deletePending(position);
                save_and_delete_alert(position,"delete");
            }
        });
*/


    }


    public void deletePending(int position) {
        String expenditure_primary_id = String.valueOf(capitalExpenditureList.get(position).getExpenditure_primary_id());
        //int sdsm = db.delete(DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE, null, null);
        int sdsm = db.delete(DBHelper.NURSERY_EXPENDITURE_SAVE, "expenditure_primary_id = ? ", new String[]{expenditure_primary_id});
        capitalExpenditureList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, capitalExpenditureList.size());
        Log.d("sdsm", String.valueOf(sdsm));
    }


    public void uploadPending(int position) {
        JSONObject data_set = new JSONObject();
        String expenditure_primary_id = String.valueOf(capitalExpenditureList.get(position).getExpenditure_primary_id());
        String fin_year = String.valueOf(capitalExpenditureList.get(position).getFin_year());
        String entry_date = String.valueOf(capitalExpenditureList.get(position).getEntry_date());
        String expense_category_id = String.valueOf(capitalExpenditureList.get(position).getExpense_category_id());
        String expenditure_unit_id = String.valueOf(capitalExpenditureList.get(position).getExpenditure_unit_id());
        String expenditure_fund_src_id = String.valueOf(capitalExpenditureList.get(position).getExpenditure_fund_src_id());
        String quantity = String.valueOf(capitalExpenditureList.get(position).getQuantity());
        String amount = String.valueOf(capitalExpenditureList.get(position).getAmount());
        String expense_category_others = String.valueOf(capitalExpenditureList.get(position).getExpense_category_others());
        String expenditure_unit_others = String.valueOf(capitalExpenditureList.get(position).getExpenditure_unit_others());
        String expenditure_fund_src_others = String.valueOf(capitalExpenditureList.get(position).getExpenditure_fund_src_others());

        String expenditure_type_id = String.valueOf(capitalExpenditureList.get(position).getExpenditure_type_id());
        try {
            if(expenditure_type_id.equals("1")){
                data_set.put("service_id","capital_expenditure_save");
            }
            else {
                data_set.put("service_id","recurring_expenditure_save");
            }

            data_set.put("fin_year",fin_year);
            data_set.put("entry_date",entry_date);
            data_set.put("expense_category_id",expense_category_id);
            data_set.put("expenditure_unit_id",expenditure_unit_id);
            data_set.put("expenditure_fund_src_id",expenditure_fund_src_id);
            data_set.put("quantity",quantity);
            data_set.put("amount",amount);
            data_set.put("expense_category_others",expense_category_others);
            data_set.put("expenditure_unit_others",expenditure_unit_others);
            data_set.put("expenditure_fund_src_others",expenditure_fund_src_others);
            Log.d("normal_json",""+data_set);


        }
        catch (JSONException e){
            e.printStackTrace();
        }
        if (Utils.isOnline()) {
            ((NewPendingScreen)context).uploadExpenditure(data_set,expenditure_primary_id);
        } else {
            Utils.showAlert(context, "Turn On Mobile Data To Upload");
        }

    }

    @Override
    public int getItemCount() {
        return capitalExpenditureList.size();
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
