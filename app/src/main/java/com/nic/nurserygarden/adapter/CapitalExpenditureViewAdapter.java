package com.nic.nurserygarden.adapter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.databinding.ExpenditureItemsViewBinding;
import com.nic.nurserygarden.databinding.ExpenditureUploadItemViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

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
    public void onBindViewHolder(@NonNull final CapitalExpenditureViewAdapter.MyViewHolder holder, final int position) {


        holder.expenditureItemsViewBinding.expenditureTypeText.setText(capitalExpenditureList.get(position).getExpenditure_type());
        holder.expenditureItemsViewBinding.expenditureCategoryText.setText(capitalExpenditureList.get(position).getExpense_category_en());
        holder.expenditureItemsViewBinding.expenditureUnitText.setText(capitalExpenditureList.get(position).getExpenditure_unit_en());
        holder.expenditureItemsViewBinding.expenditureFoundSrcText.setText(capitalExpenditureList.get(position).getExpenditure_fund_src_en());
        holder.expenditureItemsViewBinding.quantity.setText(capitalExpenditureList.get(position).getQuantity());
        holder.expenditureItemsViewBinding.amount.setText(indianMoney(capitalExpenditureList.get(position).getAmount()));





    }




    @Override
    public int getItemCount() {
        return capitalExpenditureList.size();
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
