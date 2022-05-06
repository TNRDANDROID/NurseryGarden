package com.nic.nurserygarden.activity.DeadSaplingActivty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.GrowthTrackingActivity.SaplingEntryView;
import com.nic.nurserygarden.activity.MainPage;
import com.nic.nurserygarden.adapter.CommonAdapter;
import com.nic.nurserygarden.adapter.GrowthTrackingSpeciesSaplingAdapter;
import com.nic.nurserygarden.adapter.NurserySpeciesAdapter;
import com.nic.nurserygarden.adapter.SpeciesListAdapter;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityDeadSaplingEntryBinding;
import com.nic.nurserygarden.databinding.ActivitySaplingEntryViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.Utils;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DeadSaplingEntry extends AppCompatActivity {
    private ActivityDeadSaplingEntryBinding deadSaplingEntryBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    PrefManager prefManager;
    ArrayList<NurserySurvey> speciesTypeList;
    ArrayList<NurserySurvey> deadStageList;
    ArrayList<NurserySurvey> speciesDetailsList;
    int batch_id=0;
    int batch_primary_id=0;
    int batch_species_id=0;
    int particular_species_count=0;
    int species_type_id=0;
    int dead_stage_id=0;
    SpeciesListAdapter speciesListAdapter;
    NurserySpeciesAdapter nurserySpeciesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deadSaplingEntryBinding = DataBindingUtil.setContentView(this, R.layout.activity_dead_sapling_entry);
        deadSaplingEntryBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefManager = new PrefManager(this);
        initializeUI();

        deadSaplingEntryBinding.stagesTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    dead_stage_id = deadStageList.get(position).getDead_stage_id();
                }
                else {
                    dead_stage_id =0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        deadSaplingEntryBinding.saveBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    public void initializeUI(){
        batch_id = getIntent().getIntExtra("batch_id",0);
        batch_primary_id = getIntent().getIntExtra("batch_primary_id",0);

        deadSaplingEntryBinding.chooseSpeciesRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        deadSaplingEntryBinding.speciesDetailsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadSpeciesTypeList();
        loadDeadStageList();
    }
    public void loadSpeciesTypeList(){
        dbData.open();
        speciesTypeList = new ArrayList<>();
        speciesTypeList = dbData.get_nursery_batch_species_details(String.valueOf(batch_primary_id),"All","","");
        if(speciesTypeList.size()>0){
            speciesListAdapter = new SpeciesListAdapter(speciesTypeList, DeadSaplingEntry.this,"DeadSapling");
            deadSaplingEntryBinding.chooseSpeciesRecycler.setAdapter(speciesListAdapter);
        }
    }
    public void loadDeadStageList(){
        deadStageList = new ArrayList<>();
        dbData.open();
        NurserySurvey landTypeList_item = new NurserySurvey();
        landTypeList_item.setDead_stage_id(0);
        landTypeList_item.setDead_stage_name_en("Select Dead Stage");
        landTypeList_item.setSpecies_name_ta("Select Dead Stage");
        deadStageList.add(landTypeList_item);
        deadStageList.addAll(dbData.get_all_dead_stage());
        Log.d("Size",""+deadStageList.size());

        if(deadStageList.size()>0){
            deadSaplingEntryBinding.stagesTypeSpinner.setAdapter(new CommonAdapter(DeadSaplingEntry.this,deadStageList,"deadStageList"));
        }

    }

    public void speciesdapterItemClicked(int pos){
        species_type_id = (speciesTypeList.get(pos).getSpecies_type_id());
        speciesDetailsList = new ArrayList<>();
        speciesDetailsList = dbData.get_nursery_batch_species_details(String.valueOf(batch_primary_id),"species_type_id","", String.valueOf(species_type_id));
        if(speciesDetailsList.size()>0){
            particular_species_count = speciesDetailsList.get(0).getNo_of_count();
            batch_species_id = speciesDetailsList.get(0).getBatch_species_id();
            nurserySpeciesAdapter = new NurserySpeciesAdapter(speciesDetailsList,DeadSaplingEntry.this,dbData,"species_type_id");
            deadSaplingEntryBinding.speciesDetailsRecycler.setAdapter(nurserySpeciesAdapter);
        }
    }

    public void checkValidation(){
        String no_of_dead_sapling = deadSaplingEntryBinding.deadSaplingCount.getText().toString();
        String dead_reason = deadSaplingEntryBinding.deadReaon.getText().toString();
        if(species_type_id>0){
            if(!no_of_dead_sapling.equals("")){
                if(dead_stage_id>0){
                    if(!dead_reason.equals("")){
                        saveDataLocally(no_of_dead_sapling,dead_reason);
                    }
                    else {
                        Utils.showAlert(DeadSaplingEntry.this,"Please Enter Dead Reason");
                    }
                }
                else {
                    Utils.showAlert(DeadSaplingEntry.this,"Choose Dead Stage Type");
                }
            }
            else {
                Utils.showAlert(DeadSaplingEntry.this,"Please Enter Dead Sapling Count");
            }

        }
        else {
            Utils.showAlert(DeadSaplingEntry.this,"Choose Species Type");
        }
    }

    public void saveDataLocally(String no_of_dead_sapling,String dead_reason){
        ContentValues contentValues =new ContentValues();
        long id = 0; String whereClause = "";String[] whereArgs = null;
        try {
            contentValues.put("batch_primary_id",batch_primary_id);
            contentValues.put("batch_id",batch_id);
            contentValues.put("batch_species_id",batch_species_id);
            contentValues.put("species_type_id",species_type_id);
            contentValues.put("dead_stage",dead_stage_id);
            contentValues.put("no_of_dead_sapling",no_of_dead_sapling);
            contentValues.put("dead_reason",dead_reason);
            contentValues.put("server_flag","0");
            whereClause = "batch_primary_id = ? and batch_species_id = ? and species_type_id = ?";
            whereArgs = new String[]{String.valueOf(batch_primary_id),String.valueOf(batch_species_id),String.valueOf(species_type_id)};
            dbData.open();
            ArrayList<NurserySurvey> deadSaplingDetailsCount = dbData.get_particular_dead_sapling_details(String.valueOf(batch_id),String.valueOf(species_type_id),"0","Particular");

            if(deadSaplingDetailsCount.size() < 1) {
                id = db.insert(DBHelper.DEAD_SAPLING_DETAILS_SAVE, null, contentValues);
                if (id > 0) {
                    Toasty.success(this, "Inserted Success!", Toast.LENGTH_SHORT, true).show();
                    super.onBackPressed();
                    overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

                }
            }
            else {
                id = db.update(DBHelper.DEAD_SAPLING_DETAILS_SAVE, contentValues, whereClause, whereArgs);
                if (id > 0) {
                    Toasty.success(this, "Updated Success!", Toast.LENGTH_SHORT, true).show();
                    super.onBackPressed();
                    overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

                }
            }

        }
        catch (Exception e){

        }
    }



}
