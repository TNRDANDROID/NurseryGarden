package com.nic.nurserygarden.activity.BatchActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.CameraScreen;
import com.nic.nurserygarden.activity.LandActivity.AddViewLand;
import com.nic.nurserygarden.adapter.CommonAdapter;
import com.nic.nurserygarden.adapter.NurseryLandAdapter;
import com.nic.nurserygarden.adapter.NurserySpeciesAdapter;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityAddViewBatchDetailsBinding;
import com.nic.nurserygarden.databinding.ActivityAddViewBatchSpeciesDetailsBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.utils.Utils;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AddViewBatchSpeciesDetails extends AppCompatActivity {
    private ActivityAddViewBatchSpeciesDetailsBinding batchSpeciesDetailsBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);

    int species_type_id;
    int batch_id;
    int batch_primary_id;
    int batch_species_id;
    String species_type_name_ta;
    String species_type_name_en;
    ArrayList<NurserySurvey> speciesTypeList;

    NurserySpeciesAdapter nurserySpeciesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        batchSpeciesDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_view_batch_species_details);
        batchSpeciesDetailsBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initialiseRecyclerView();
        new fetchNurserySpeciesDetails().execute();

        batchSpeciesDetailsBinding.addSpeciesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSpeciesView();
            }
        });
    }

    private void initialiseRecyclerView() {
        loadSpeciesTypeList();
        batch_id = getIntent().getIntExtra("batch_id",0);
        batch_primary_id = getIntent().getIntExtra("batch_primary_id",0);
        batch_species_id = getIntent().getIntExtra("batch_species_id",0);
        batchSpeciesDetailsBinding.speciesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }
    public void loadSpeciesTypeList(){
        speciesTypeList = new ArrayList<>();
        dbData.open();
        NurserySurvey landTypeList_item = new NurserySurvey();
        landTypeList_item.setSpecies_type_id(0);
        landTypeList_item.setSpecies_name_ta("Select Species Type");
        landTypeList_item.setSpecies_name_en("Select Species Type");
        speciesTypeList.add(landTypeList_item);
        speciesTypeList.addAll(dbData.get_nursery_species_type());
        Log.d("Size",""+speciesTypeList.size());

    }
    public void addSpeciesView(){
        try {
            dbData.open();
            final Dialog dialog = new Dialog(this,R.style.AppTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_edit_species_dialog);
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
            RelativeLayout save_btn_layout = dialog.findViewById(R.id.save_btn_layout);

            TextView tittle_text = dialog.findViewById(R.id.tittle_text);
            Spinner species_type_spinner = dialog.findViewById(R.id.species_type_spinner);
            EditText species_count = dialog.findViewById(R.id.species_count);
            tittle_text.setText("Add/Edit Species Details");
            ArrayList<NurserySurvey> speciesList = new ArrayList<>();
            speciesList = dbData.get_nursery_batch_species_details(String.valueOf(batch_primary_id),"All","");
            if(speciesList.size()>0){
                for (int i=0;i<speciesList.size();i++){
                    for(int j=0 ;j<speciesTypeList.size();j++){
                        if(speciesList.get(i).getSpecies_type_id()==speciesTypeList.get(j).getSpecies_type_id()){
                            speciesTypeList.remove(j);
                        }
                    }
                }
            }
            species_type_spinner.setAdapter(new CommonAdapter(AddViewBatchSpeciesDetails.this,speciesTypeList,"speciesTypeList"));
            species_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position>0){
                        species_type_id = speciesTypeList.get(species_type_spinner.getSelectedItemPosition()).getSpecies_type_id();
                        species_type_name_ta = speciesTypeList.get(species_type_spinner.getSelectedItemPosition()).getSpecies_name_ta();
                        species_type_name_en = speciesTypeList.get(species_type_spinner.getSelectedItemPosition()).getSpecies_name_en();
                    }
                    else {
                        species_type_id =0;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            save_btn_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(species_type_id!=0){
                        if(!species_count.getText().toString().equals("")){
                            try {
                                ContentValues contentValues = new ContentValues();

                                contentValues.put("batch_primary_id",batch_primary_id);
                                contentValues.put("batch_id",batch_id);
                                contentValues.put("batch_species_id",0);
                                contentValues.put("species_type_id",species_type_id);
                                contentValues.put("server_flag","0");
                                contentValues.put("species_type_name_ta",species_type_name_ta);
                                contentValues.put("species_type_name_en",species_type_name_en);
                                contentValues.put("no_of_count",species_count.getText().toString());

                                long inserted_id = db.insert(DBHelper.BATCH_SPECIES_DETAILS,null,contentValues);
                                if(inserted_id>0){
                                    Toasty.success(AddViewBatchSpeciesDetails.this,getResources().getString(R.string.inserted_success),Toasty.LENGTH_SHORT);
                                    new fetchNurserySpeciesDetails().execute();
                                    dialog.dismiss();
                                }
                                else {
                                    Toasty.error(AddViewBatchSpeciesDetails.this,"Something Wrong",Toasty.LENGTH_SHORT);
                                    dialog.dismiss();
                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else {
                            Utils.showAlert(AddViewBatchSpeciesDetails.this,"Please Enter Species Count");
                        }
                    }
                    else {
                        Utils.showAlert(AddViewBatchSpeciesDetails.this,"Please Choose Species Type");
                    }

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public class fetchNurserySpeciesDetails extends AsyncTask<Void, Void, ArrayList<NurserySurvey>> {
        @Override
        protected ArrayList<NurserySurvey> doInBackground(Void... params) {
            dbData.open();
            ArrayList<NurserySurvey> nurserySpeciesDetails = new ArrayList<>();
            nurserySpeciesDetails = dbData.get_nursery_batch_species_details(String.valueOf(batch_primary_id),"All","");
            Log.d("nursery_species_details", String.valueOf(nurserySpeciesDetails.size()));
            return nurserySpeciesDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<NurserySurvey> nurserySpeciesDetails) {
            super.onPostExecute(nurserySpeciesDetails);
            if(nurserySpeciesDetails.size()>0){
                nurserySpeciesAdapter = new NurserySpeciesAdapter(nurserySpeciesDetails,AddViewBatchSpeciesDetails.this,dbData);
                batchSpeciesDetailsBinding.speciesRecycler.setAdapter(nurserySpeciesAdapter);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new fetchNurserySpeciesDetails().execute();
    }
}
