package com.nic.nurserygarden.activity.GrowthTrackingActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchDetails;
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchSpeciesDetails;
import com.nic.nurserygarden.activity.CameraScreen;
import com.nic.nurserygarden.adapter.GrowthTrackingSpeciesSaplingAdapter;
import com.nic.nurserygarden.adapter.NurserySpeciesAdapter;
import com.nic.nurserygarden.adapter.SpeciesListAdapter;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityGrowthTrackingBinding;
import com.nic.nurserygarden.databinding.ActivitySaplingEntryViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.Utils;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SaplingEntryView extends AppCompatActivity {
    private ActivitySaplingEntryViewBinding saplingEntryViewBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    PrefManager prefManager;
    ArrayList<NurserySurvey> speciesTypeList;
    ArrayList<NurserySurvey> speciesDetailsList;
    ArrayList<NurserySurvey> growthSpeciesDetailsList;
    int batch_id;
    int batch_primary_id;
    int batch_growth_tracking_primary_id;
    int particular_species_count;
    int batch_species_id;
    String species_type_id;
    String entry_date;
    SpeciesListAdapter speciesListAdapter;
    NurserySpeciesAdapter nurserySpeciesAdapter;
    GrowthTrackingSpeciesSaplingAdapter growthTrackingSpeciesSaplingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saplingEntryViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_sapling_entry_view);
        saplingEntryViewBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefManager = new PrefManager(this);
        initializeUI();

        saplingEntryViewBinding.addSaplingDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSaplingCountLimitation()) {
                    addBatchView();
                }
                else {
                    Utils.showAlert(SaplingEntryView.this,getResources().getString(R.string.maximum_count_reached));
                }
            }
        });
    }

    public void initializeUI(){
        batch_id = getIntent().getIntExtra("batch_id",0);
        batch_primary_id = getIntent().getIntExtra("batch_primary_id",0);
        batch_growth_tracking_primary_id = getIntent().getIntExtra("batch_growth_tracking_primary_id",0);

        saplingEntryViewBinding.chooseSpeciesRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        saplingEntryViewBinding.speciesDetailsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        saplingEntryViewBinding.saplingsDetailsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        loadSpeciesTypeList();
    }

    public void loadSpeciesTypeList(){
        dbData.open();
        speciesTypeList = new ArrayList<>();
        speciesTypeList = dbData.get_nursery_batch_species_details(String.valueOf(batch_primary_id),"All","","");
        if(speciesTypeList.size()>0){
            speciesListAdapter = new SpeciesListAdapter(speciesTypeList, SaplingEntryView.this,"");
            saplingEntryViewBinding.chooseSpeciesRecycler.setAdapter(speciesListAdapter);
        }
    }


    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void speciesdapterItemClicked(int pos){
        species_type_id = String.valueOf(speciesTypeList.get(pos).getSpecies_type_id());
        speciesDetailsList = new ArrayList<>();
        speciesDetailsList = dbData.get_nursery_batch_species_details(String.valueOf(batch_primary_id),"species_type_id","",species_type_id);
        if(speciesDetailsList.size()>0){
            particular_species_count = speciesDetailsList.get(0).getNo_of_count();
            batch_species_id = speciesDetailsList.get(0).getBatch_species_id();
            nurserySpeciesAdapter = new NurserySpeciesAdapter(speciesDetailsList,SaplingEntryView.this,dbData,"species_type_id");
            saplingEntryViewBinding.speciesDetailsRecycler.setAdapter(nurserySpeciesAdapter);
            loadSaplingDetailsList();
        }
    }

    public void addBatchView(){

        try {
            final Dialog dialog = new Dialog(this,R.style.AppTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_saplings_details_dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.7f;
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();

            ImageView close_icon = dialog.findViewById(R.id.close_icon);
            EditText saplings_count = dialog.findViewById(R.id.saplings_count);
            EditText height_in_cm = dialog.findViewById(R.id.height_in_cm);
            RelativeLayout save_btn_layout = dialog.findViewById(R.id.save_btn_layout);

            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            save_btn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int total_count =0;
                    boolean sapling_height_ = false;
                    if(!saplings_count.getText().toString().equals("")){
                        total_count = getSaplingCountLimitationNumber()+Integer.parseInt(saplings_count.getText().toString());
                    }
                    if(!height_in_cm.getText().toString().equals("")){
                        sapling_height_= getSaplingHeight(height_in_cm.getText().toString());
                    }

                    if(particular_species_count>=total_count){
                        if(getSaplingCountLimitation()){
                            if(!saplings_count.getText().toString().equals("")){
                                if(!height_in_cm.getText().toString().equals("")&&(!height_in_cm.getText().toString().equals("0"))){
                                    if(!sapling_height_){
                                        saveSaplingDetails(saplings_count.getText().toString(),height_in_cm.getText().toString());
                                        dialog.dismiss();
                                    }
                                    else {
                                        Utils.showAlert(SaplingEntryView.this,getResources().getString(R.string.please_enter_valid_height));
                                    }


                                }
                                else {
                                    Utils.showAlert(SaplingEntryView.this,getResources().getString(R.string.please_enter_valid_height));
                                }
                            }
                            else {
                                Utils.showAlert(SaplingEntryView.this,getResources().getString(R.string.please_enter_sapling_count));
                            }
                        }
                        else {
                            Utils.showAlert(SaplingEntryView.this,getResources().getString(R.string.maximum_count_reached));
                        }
                    }
                    else {
                        Utils.showAlert(SaplingEntryView.this,getResources().getString(R.string.maximum_count_reached));
                    }
                }
            });

        }
        catch (Exception e) {
            Utils.showAlert(SaplingEntryView.this,getResources().getString(R.string.something_wrong));
            e.printStackTrace();
        }

    }

    public void saveSaplingDetails(String no_of_saplings,String height_in_cm){
        dbData.open();

        long id = 0;
        try {
            ContentValues values = new ContentValues();

            values.put("batch_growth_tracking_primary_id", batch_growth_tracking_primary_id);
            values.put("batch_primary_id",batch_primary_id);
            values.put("batch_id", batch_id);
            values.put("growth_tracking_details_id", 0);
            values.put("batch_species_id", batch_species_id);
            values.put("species_type_id", species_type_id);
            values.put("server_flag", "0");
            values.put("entry_date", getIntent().getStringExtra("entry_date"));
            values.put("no_of_saplings", no_of_saplings);
            values.put("height_in_cm", height_in_cm);
            values.put("age_in_days", 0);


            id = db.insert(DBHelper.BATCH_GROWTH_TRACKING_SPECIES_DETAILS, null, values);

            if (id > 0) {
                Toasty.success(this, getResources().getString(R.string.success), Toast.LENGTH_SHORT, true).show();
                loadSaplingDetailsList();

            }
            Log.d("growthInsID", String.valueOf(id));
        }
        catch (Exception e){
            Log.d("Error...>",""+e);
        }

    }

    public void loadSaplingDetailsList(){
        dbData.open();
        growthSpeciesDetailsList = new ArrayList<>();
        growthSpeciesDetailsList = dbData.get_batch_growth_species_details(String.valueOf(batch_id),"species_type_id","",species_type_id,getIntent().getStringExtra("entry_date"));
        if(growthSpeciesDetailsList.size()>0){
            saplingEntryViewBinding.saplingsDetailsLayout.setVisibility(View.VISIBLE);
            growthTrackingSpeciesSaplingAdapter = new GrowthTrackingSpeciesSaplingAdapter(growthSpeciesDetailsList, SaplingEntryView.this,dbData,"");
            saplingEntryViewBinding.saplingsDetailsRecycler.setAdapter(growthTrackingSpeciesSaplingAdapter);
        }
        else {
            saplingEntryViewBinding.saplingsDetailsLayout.setVisibility(View.GONE);
        }
    }

    public boolean getSaplingCountLimitation(){
        dbData.open();
        int saplings_count=0;
        ArrayList<NurserySurvey>growthSpeciesDetailsList = new ArrayList<>();
        growthSpeciesDetailsList = dbData.get_batch_growth_species_details(String.valueOf(batch_id),"species_type_id","",species_type_id,getIntent().getStringExtra("entry_date"));
        if(growthSpeciesDetailsList.size()>0){
            for(int i=0;i<growthSpeciesDetailsList.size();i++){
                if(growthSpeciesDetailsList.get(i).getAge_in_days()!=0 || !growthSpeciesDetailsList.get(i).getHeight_in_cm().equals("0")){
                    saplings_count = saplings_count+growthSpeciesDetailsList.get(i).getNo_of_saplings();
                }

            }
            if(particular_species_count==saplings_count){
                return false;
            }
            else if(particular_species_count>saplings_count) {
                return true;
            }
        }
        else {
            return true;
        }

        return false;
    }
    public int getSaplingCountLimitationNumber(){
        dbData.open();
        int saplings_count=0;
        ArrayList<NurserySurvey>growthSpeciesDetailsList = new ArrayList<>();
        growthSpeciesDetailsList = dbData.get_batch_growth_species_details(String.valueOf(batch_id),"species_type_id","",species_type_id,getIntent().getStringExtra("entry_date"));
        if(growthSpeciesDetailsList.size()>0) {
            for (int i = 0; i < growthSpeciesDetailsList.size(); i++) {
                if(growthSpeciesDetailsList.get(i).getAge_in_days()!=0 && !growthSpeciesDetailsList.get(i).getHeight_in_cm().equals("0")){
                    saplings_count = saplings_count+growthSpeciesDetailsList.get(i).getNo_of_saplings();
                }
                //saplings_count = saplings_count + growthSpeciesDetailsList.get(i).getNo_of_saplings();
            }
        }

        return saplings_count;
    }
    public boolean getSaplingHeight(String height_entered_text){
        dbData.open();
        String sapling_height="";
        boolean height_flag = false;
        ArrayList<NurserySurvey>growthSpeciesDetailsList = new ArrayList<>();
        growthSpeciesDetailsList = dbData.get_batch_growth_species_details(String.valueOf(batch_id),"species_type_id","",species_type_id,getIntent().getStringExtra("entry_date"));
        if(growthSpeciesDetailsList.size()>0) {
            for (int i = 0; i < growthSpeciesDetailsList.size(); i++) {
                sapling_height = growthSpeciesDetailsList.get(i).getHeight_in_cm();
                if(height_entered_text.equals(sapling_height)){
                    height_flag = true;
                    break;
                }
                else {
                    height_flag = false;
                    continue;
                }
            }
        }

        return height_flag;
    }


}
