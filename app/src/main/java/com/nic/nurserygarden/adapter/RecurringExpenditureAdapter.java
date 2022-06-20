package com.nic.nurserygarden.adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.databinding.ExpenditureItemsViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class RecurringExpenditureAdapter extends RecyclerView.Adapter<RecurringExpenditureAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private List<NurserySurvey> recurringExpenditureList;
    JSONObject dataset = new JSONObject();
    private LayoutInflater layoutInflater;
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    public RecurringExpenditureAdapter(Activity context, List<NurserySurvey> recurringExpenditureList) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.recurringExpenditureList = recurringExpenditureList;

        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecurringExpenditureAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ExpenditureItemsViewBinding expenditureItemsViewBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.expenditure_items_view, viewGroup, false);
        return new MyViewHolder(expenditureItemsViewBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ExpenditureItemsViewBinding expenditureItemsViewBinding;

        public MyViewHolder(ExpenditureItemsViewBinding Binding) {
            super(Binding.getRoot());
            expenditureItemsViewBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecurringExpenditureAdapter.MyViewHolder holder, final int position) {


        holder.expenditureItemsViewBinding.expenditureTypeText.setText(recurringExpenditureList.get(position).getExpenditure_type());
        holder.expenditureItemsViewBinding.expenditureCategoryText.setText(recurringExpenditureList.get(position).getExpense_category_en());
        holder.expenditureItemsViewBinding.expenditureUnitText.setText(recurringExpenditureList.get(position).getExpenditure_unit_en());
        holder.expenditureItemsViewBinding.expenditureFoundSrcText.setText(recurringExpenditureList.get(position).getExpenditure_fund_src_en());
        holder.expenditureItemsViewBinding.quantity.setText(recurringExpenditureList.get(position).getQuantity());
        holder.expenditureItemsViewBinding.amount.setText(indianMoney(recurringExpenditureList.get(position).getAmount()));





    }




    @Override
    public int getItemCount() {
        return recurringExpenditureList.size();
    }

    public String indianMoney(String amount){
        String amount_value="";
        try{
            Locale locale = new Locale("en","IN");
            DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(locale);
            dfs.setCurrencySymbol("\u20B9");
            decimalFormat.setDecimalFormatSymbols(dfs);
            System.out.println(decimalFormat.format(Integer.parseInt(amount)));
            amount_value = decimalFormat.format(Integer.parseInt(amount));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return amount_value;
    }



}
