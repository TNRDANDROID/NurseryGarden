package com.nic.nurserygarden.activity.Expenditure;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.SellAndBuyActivity.SellSpecies;
import com.nic.nurserygarden.adapter.CommonAdapter;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityCapitalRecurreingExpenditureBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class CapitalRecurreingExpenditure extends AppCompatActivity {
    ActivityCapitalRecurreingExpenditureBinding capitalRecurreingExpenditureBinding;
    private PrefManager prefManager;

    public  DBHelper dbHelper;
    public  SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);

    ArrayList<NurserySurvey> expenditureTypeList;
    ArrayList<NurserySurvey> expenditureUnitList;
    ArrayList<NurserySurvey> expenditureFoundSrcList;
    ArrayList<NurserySurvey> fin_Year_List;
    int expenditure_type_id=0;
    int expense_category_id=0;
    int expenditure_unit_id=0;
    int expenditure_fund_src_id=0;
    private String is_others="";
    private String fin_year="";
    private String expenditure_is_unit_others="";
    private String expenditure_is_found_src_others="";
    int year=0,day=0,month=0;

    String expense_category_name="";
    String expense_unit_name="";
    String expense_found_src_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capital_recurreing_expenditure);
        Utils.setStatusBarGradiant(CapitalRecurreingExpenditure.this);
        capitalRecurreingExpenditureBinding = DataBindingUtil.setContentView(this, R.layout.activity_capital_recurreing_expenditure);
        capitalRecurreingExpenditureBinding.setActivity(this);
        prefManager = new PrefManager(this);
        Utils.setLocale(prefManager.getKEY_Language(),this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeUi();

        capitalRecurreingExpenditureBinding.expenditureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    expense_category_id = expenditureTypeList.get(position).getExpense_category_id();
                    expense_category_name = expenditureTypeList.get(position).getExpense_category_en();
                    is_others = expenditureTypeList.get(position).getIs_others();
                    if(is_others.equals("Y")){
                        capitalRecurreingExpenditureBinding.expenditureSpecifyLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        capitalRecurreingExpenditureBinding.expenditureSpecifyLayout.setVisibility(View.GONE);
                    }
                    loadExpenditureUnit();
                    loadExpenditureFoundSrc();
                    capitalRecurreingExpenditureBinding.expenditureUnitSpinner.setSelection(0);
                    capitalRecurreingExpenditureBinding.expenditureFoundSrcSpinner.setSelection(0);
                }
                else {
                    expense_category_id =0;
                    expense_category_name = "";
                    is_others = "";
                    capitalRecurreingExpenditureBinding.expenditureSpecifyLayout.setVisibility(View.GONE);
                    capitalRecurreingExpenditureBinding.expenditureUnitSpinner.setSelection(0);
                    capitalRecurreingExpenditureBinding.expenditureFoundSrcSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        capitalRecurreingExpenditureBinding.expenditureUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    expenditure_unit_id = expenditureUnitList.get(position).getExpenditure_unit_id();
                    expense_unit_name = expenditureUnitList.get(position).getExpenditure_unit_en();
                    if(expenditureUnitList.get(position).getExpenditure_unit_en().equalsIgnoreCase("Others")){
                        expenditure_is_unit_others="Y";
                        capitalRecurreingExpenditureBinding.expenditureUnitSpecifyLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        capitalRecurreingExpenditureBinding.expenditureUnitSpecifyLayout.setVisibility(View.GONE);
                    }
                }
                else {
                    expenditure_is_unit_others="";
                    expense_unit_name="";
                    expenditure_unit_id =0;
                    capitalRecurreingExpenditureBinding.expenditureUnitSpecifyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        capitalRecurreingExpenditureBinding.expenditureFoundSrcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    expenditure_fund_src_id = expenditureFoundSrcList.get(position).getExpenditure_fund_src_id();
                    expense_found_src_name = expenditureFoundSrcList.get(position).getExpenditure_fund_src_en();
                    if(expenditureFoundSrcList.get(position).getExpenditure_fund_src_en().equalsIgnoreCase("Others ")){
                        expenditure_is_found_src_others = "Y";
                        capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecifyLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        expenditure_is_found_src_others="";
                        capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecifyLayout.setVisibility(View.GONE);
                    }
                }
                else {
                    expense_found_src_name="";
                    expenditure_is_found_src_others="";
                    expenditure_fund_src_id =0;
                    capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecifyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        capitalRecurreingExpenditureBinding.finYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    fin_year = fin_Year_List.get(position).getFin_year();
                }
                else {
                    fin_year="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        capitalRecurreingExpenditureBinding.chooseDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickdate();
            }
        });
        capitalRecurreingExpenditureBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

    }

    private void initializeUi(){
        capitalRecurreingExpenditureBinding.expenditureSpecifyLayout.setVisibility(View.GONE);
        capitalRecurreingExpenditureBinding.expenditureUnitSpecifyLayout.setVisibility(View.GONE);
        capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecifyLayout.setVisibility(View.GONE);
        expenditure_type_id = getIntent().getIntExtra("expenditure_type_id",0);
        if(expenditure_type_id==1){
            capitalRecurreingExpenditureBinding.titleTv.setText(getResources().getString(R.string.capital_expenditure));
        }
        else {
            capitalRecurreingExpenditureBinding.titleTv.setText(getResources().getString(R.string.recurring_expenditure));
        }
        loadExpenditureType();
        loadExpenditureUnit();
        loadExpenditureFoundSrc();
        loadFinYearTable();
    }
    public void loadExpenditureType(){
        dbData.open();
        expenditureTypeList = new ArrayList<>();
        NurserySurvey nurserySurvey = new NurserySurvey();
        nurserySurvey.setExpenditure_type_id(0);
        nurserySurvey.setExpense_category_id(0);
        nurserySurvey.setExpense_category_en("Select Expenditure");
        nurserySurvey.setExpense_category_ta("Select Expenditure");
        nurserySurvey.setIs_others("");
        expenditureTypeList.add(nurserySurvey);
        ArrayList<NurserySurvey> sampleList = new ArrayList<>();
        sampleList.addAll(dbData.get_all_expenditure_type());
        for(int i=0;i<sampleList.size();i++){
            if(expenditure_type_id==sampleList.get(i).getExpenditure_type_id()){
                NurserySurvey nurserySurvey1 = new NurserySurvey();
                nurserySurvey1.setExpenditure_type_id(sampleList.get(i).getExpenditure_type_id());
                nurserySurvey1.setExpense_category_id(sampleList.get(i).getExpense_category_id());
                nurserySurvey1.setExpense_category_en(sampleList.get(i).getExpense_category_en());
                nurserySurvey1.setExpense_category_ta(sampleList.get(i).getExpense_category_ta());
                nurserySurvey1.setIs_others(sampleList.get(i).getIs_others());
                expenditureTypeList.add(nurserySurvey1);
            }
        }
        if(expenditureTypeList.size()>0){
            capitalRecurreingExpenditureBinding.expenditureSpinner.setAdapter(new CommonAdapter(CapitalRecurreingExpenditure.this,expenditureTypeList,"expenditureTypeList"));
        }
    }
    public void loadExpenditureUnit(){
        dbData.open();
        expenditureUnitList = new ArrayList<>();
        NurserySurvey nurserySurvey = new NurserySurvey();
        nurserySurvey.setExpenditure_unit_id(0);
        nurserySurvey.setExpense_category_id(0);
        nurserySurvey.setExpenditure_unit_en("Select Expenditure Unit");
        nurserySurvey.setExpenditure_unit_ta("Select Expenditure Unit");
        expenditureUnitList.add(nurserySurvey);
        ArrayList<NurserySurvey> sampleList = new ArrayList<>();
        sampleList.addAll(dbData.get_all_expenditure_unit());
        for(int i=0;i<sampleList.size();i++){
            if(expense_category_id==sampleList.get(i).getExpense_category_id()){
                NurserySurvey nurserySurvey1 = new NurserySurvey();
                nurserySurvey1.setExpenditure_unit_id(sampleList.get(i).getExpenditure_unit_id());
                nurserySurvey1.setExpense_category_id(sampleList.get(i).getExpense_category_id());
                nurserySurvey1.setExpenditure_unit_en(sampleList.get(i).getExpenditure_unit_en());
                nurserySurvey1.setExpenditure_unit_ta(sampleList.get(i).getExpenditure_unit_ta());
                expenditureUnitList.add(nurserySurvey1);
            }
        }
        if(expenditureUnitList.size()>0){
            capitalRecurreingExpenditureBinding.expenditureUnitSpinner.setAdapter(new CommonAdapter(CapitalRecurreingExpenditure.this,expenditureUnitList,"expenditureUnitList"));
        }
    }
    public void loadExpenditureFoundSrc(){
        dbData.open();
        expenditureFoundSrcList = new ArrayList<>();
        NurserySurvey nurserySurvey = new NurserySurvey();
        nurserySurvey.setExpenditure_fund_src_id(0);
        nurserySurvey.setExpense_category_id(0);
        nurserySurvey.setExpenditure_fund_src_en("Select Expenditure Found Src");
        nurserySurvey.setExpenditure_fund_src_ta("Select Expenditure Found Src");
        expenditureFoundSrcList.add(nurserySurvey);
        ArrayList<NurserySurvey> sampleList = new ArrayList<>();
        sampleList.addAll(dbData.get_all_expenditure_found_src());
        for(int i=0;i<sampleList.size();i++){
            if(expense_category_id==sampleList.get(i).getExpense_category_id()){
                NurserySurvey nurserySurvey1 = new NurserySurvey();
                nurserySurvey1.setExpenditure_fund_src_id(sampleList.get(i).getExpenditure_fund_src_id());
                nurserySurvey1.setExpense_category_id(sampleList.get(i).getExpense_category_id());
                nurserySurvey1.setExpenditure_fund_src_en(sampleList.get(i).getExpenditure_fund_src_en());
                nurserySurvey1.setExpenditure_fund_src_ta(sampleList.get(i).getExpenditure_fund_src_ta());
                expenditureFoundSrcList.add(nurserySurvey1);
            }
        }
        if(expenditureFoundSrcList.size()>0){
            capitalRecurreingExpenditureBinding.expenditureFoundSrcSpinner.setAdapter(new CommonAdapter(CapitalRecurreingExpenditure.this,expenditureFoundSrcList,"expenditureFoundSrcList"));
        }
    }
    public void loadFinYearTable(){
        fin_Year_List = new ArrayList<>();
        dbData.open();
        NurserySurvey fin_Year_List_item = new NurserySurvey();
        fin_Year_List_item.setFin_year(getResources().getString(R.string.select_fin_year));
        fin_Year_List.add(fin_Year_List_item);
        fin_Year_List.addAll(dbData.getAll_Master_Fin_Year());

        capitalRecurreingExpenditureBinding.finYearSpinner.setAdapter(new CommonAdapter(this,fin_Year_List,"fin_Year_List"));

    }

    public void pickdate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //dateFormate(date);
                        capitalRecurreingExpenditureBinding.chooseDate.setText(dateFormate(date,""));

                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  String dateFormate(String strDate, String type ){
        try {
            SimpleDateFormat sdfSource =null;
            if(type.equals("")) {
                // create SimpleDateFormat object with source string date format
                sdfSource = new SimpleDateFormat(
                        "dd-M-yyyy");
            }
            else {
                sdfSource = new SimpleDateFormat(
                        "yyyy-mm-dd");
            }

            // parse the string into Date object
            Date date = sdfSource.parse(strDate);

            // create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat(
                    "dd-MM-yyyy");

            // parse the date into another format
            strDate = sdfDestination.format(date);

           /* System.out
                    .println("Date is converted from yyyy-MM-dd'T'hh:mm:ss'.000Z' format to dd/MM/yyyy, ha");
            System.out.println("Converted date is : " + strDate.toLowerCase());
*/
        } catch (ParseException pe) {
            System.out.println("Parse Exception : " + pe);
        }
        return strDate;
    }

    private void checkValidation(){
        if(!fin_year.equals("")){
            if(expense_category_id!=0){
                if(is_others.equals("Y")){
                    if(!capitalRecurreingExpenditureBinding.expenditureSpecify.getText().toString().equals("")){
                        if(!capitalRecurreingExpenditureBinding.quantity.getText().toString().equals("")){
                            if(expenditure_unit_id!=0){
                                if(expenditure_is_unit_others.equals("Y")){
                                    if(!capitalRecurreingExpenditureBinding.expenditureUnitSpecify.getText().toString().equals("")){
                                        if(!capitalRecurreingExpenditureBinding.amount.getText().toString().equals("")){
                                            if(expenditure_fund_src_id!=0){
                                                if(expenditure_is_found_src_others.equals("Y")){
                                                    if(!capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecify.getText().toString().equals("")){
                                                        if(!capitalRecurreingExpenditureBinding.chooseDate.getText().toString().equals("")){

                                                            saveLocally();
                                                        }
                                                        else {
                                                            Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.choose_date));
                                                        }
                                                    }
                                                    else {
                                                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.please_specify));

                                                    }
                                                }
                                                else {
                                                    if(!capitalRecurreingExpenditureBinding.chooseDate.getText().toString().equals("")){

                                                        saveLocally();
                                                    }
                                                    else {
                                                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.choose_date));
                                                    }
                                                }
                                            }
                                            else {
                                                Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.select_expenditure_found_src));

                                            }
                                        }
                                        else {
                                            Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.enter_amount));

                                        }
                                    }
                                    else {
                                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.please_specify));
                                    }

                                }
                                else {
                                    if(!capitalRecurreingExpenditureBinding.amount.getText().toString().equals("")){
                                        if(expenditure_fund_src_id!=0){
                                            if(expenditure_is_found_src_others.equals("Y")){
                                                if(!capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecify.getText().toString().equals("")){
                                                    if(!capitalRecurreingExpenditureBinding.chooseDate.getText().toString().equals("")){
                                                        saveLocally();
                                                    }
                                                    else {
                                                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.choose_date));
                                                    }
                                                }
                                                else {
                                                    Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.please_specify));

                                                }
                                            }
                                            else {
                                                if(!capitalRecurreingExpenditureBinding.chooseDate.getText().toString().equals("")){
                                                    saveLocally();
                                                }
                                                else {
                                                    Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.choose_date));
                                                }
                                            }
                                        }
                                        else {
                                            Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.select_expenditure_found_src));

                                        }
                                    }
                                    else {
                                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.enter_amount));

                                    }
                                }
                            }
                            else {
                                Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.select_expenditure_unit));
                            }
                        }
                        else {
                            Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.quantity));
                        }
                    }
                    else {
                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.please_specify));
                    }
                }
                else {
                    if(!capitalRecurreingExpenditureBinding.quantity.getText().toString().equals("")){
                        if(expenditure_unit_id!=0){
                            if(expenditure_is_unit_others.equals("Y")){
                                if(!capitalRecurreingExpenditureBinding.expenditureUnitSpecify.getText().toString().equals("")){
                                    if(!capitalRecurreingExpenditureBinding.amount.getText().toString().equals("")){
                                        if(expenditure_fund_src_id!=0){
                                            if(expenditure_is_found_src_others.equals("Y")){
                                                if(!capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecify.getText().toString().equals("")){
                                                    if(!capitalRecurreingExpenditureBinding.chooseDate.getText().toString().equals("")){
                                                        saveLocally();
                                                    }
                                                    else {
                                                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.choose_date));
                                                    }
                                                }
                                                else {
                                                    Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.please_specify));

                                                }
                                            }
                                            else {
                                                if(!capitalRecurreingExpenditureBinding.chooseDate.getText().toString().equals("")){
                                                    saveLocally();
                                                }
                                                else {
                                                    Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.choose_date));
                                                }
                                            }
                                        }
                                        else {
                                            Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.select_expenditure_found_src));

                                        }
                                    }
                                    else {
                                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.enter_amount));

                                    }
                                }
                                else {
                                    Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.please_specify));
                                }

                            }
                            else {
                                if(!capitalRecurreingExpenditureBinding.amount.getText().toString().equals("")){
                                    if(expenditure_fund_src_id!=0){
                                        if(expenditure_is_found_src_others.equals("Y")){
                                            if(!capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecify.getText().toString().equals("")){
                                                if(!capitalRecurreingExpenditureBinding.chooseDate.getText().toString().equals("")){
                                                    saveLocally();
                                                }
                                                else {
                                                    Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.choose_date));
                                                }
                                            }
                                            else {
                                                Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.please_specify));

                                            }
                                        }
                                        else {
                                            if(!capitalRecurreingExpenditureBinding.chooseDate.getText().toString().equals("")){
                                                saveLocally();
                                            }
                                            else {
                                                Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.choose_date));
                                            }
                                        }
                                    }
                                    else {
                                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.select_expenditure_found_src));

                                    }
                                }
                                else {
                                    Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.enter_amount));

                                }
                            }
                        }
                        else {
                            Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.select_expenditure_unit));
                        }
                    }
                    else {
                        Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.quantity));
                    }
                }
            }
            else {
                Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.select_expenditure));
            }
        }
        else {
            Utils.showAlert(CapitalRecurreingExpenditure.this,getResources().getString(R.string.select_fin_year));
        }

    }

    public void saveLocally(){
        dbData.open();
        long insert_updated_id =0;
        String whereClause = "";String[] whereArgs = null;
        try {
                String entry_date = capitalRecurreingExpenditureBinding.chooseDate.getText().toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put("fin_year",fin_year);
                contentValues.put("entry_date",entry_date);
                contentValues.put("expenditure_type_id",expenditure_type_id);
                contentValues.put("expense_category_id",expense_category_id);
                contentValues.put("expenditure_unit_id",expenditure_unit_id);
                contentValues.put("expenditure_fund_src_id",expenditure_fund_src_id);
                contentValues.put("expense_category_name",expense_category_name);
                contentValues.put("expense_unit_name",expense_unit_name);
                contentValues.put("expense_found_src_name",expense_found_src_name);
                contentValues.put("expense_category_others",capitalRecurreingExpenditureBinding.expenditureSpecify.getText().toString());
                contentValues.put("expenditure_unit_others",capitalRecurreingExpenditureBinding.expenditureUnitSpecify.getText().toString());
                contentValues.put("expenditure_fund_src_others",capitalRecurreingExpenditureBinding.expenditureFoundSrcSpecify.getText().toString());
                contentValues.put("quantity",capitalRecurreingExpenditureBinding.quantity.getText().toString());
                contentValues.put("amount",capitalRecurreingExpenditureBinding.amount.getText().toString());

                whereClause = " entry_date = ? ";
                whereArgs = new String[]{entry_date};
                insert_updated_id = db.insert(DBHelper.NURSERY_EXPENDITURE_SAVE,null,contentValues);
                if (insert_updated_id>0){
                    Toasty.success(CapitalRecurreingExpenditure.this,getResources().getString(R.string.inserted_success), Toast.LENGTH_SHORT,true).show();
                    onBackPressed();
                }
               /* if(dbData.get_All_Or_Particular_Expenditure("Particular",entry_date).size() > 0){
                    insert_updated_id = db.update(DBHelper.NURSERY_EXPENDITURE_SAVE,contentValues,whereClause,whereArgs);
                    if (insert_updated_id>0){
                        Toasty.success(CapitalRecurreingExpenditure.this,getResources().getString(R.string.updated_success),Toasty.LENGTH_SHORT);
                        onBackPressed();
                    }

                }
                else {
                    insert_updated_id = db.insert(DBHelper.NURSERY_EXPENDITURE_SAVE,null,contentValues);
                    if (insert_updated_id>0){
                        Toasty.success(CapitalRecurreingExpenditure.this,getResources().getString(R.string.inserted_success),Toasty.LENGTH_SHORT);
                        onBackPressed();
                    }
                }*/
            }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
