package com.nic.nurserygarden.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchDetails;
import com.nic.nurserygarden.activity.DeadSaplingActivty.NewDeadSaplingEntry;
import com.nic.nurserygarden.activity.Expenditure.CapitalRecurreingExpenditure;
import com.nic.nurserygarden.activity.Expenditure.ExpenditureView;
import com.nic.nurserygarden.activity.HistoryActivity.OrderHistory;
import com.nic.nurserygarden.activity.LandActivity.AddViewLand;
import com.nic.nurserygarden.activity.SellAndBuyActivity.SellSpecies;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.MainPageBinding;
import com.nic.nurserygarden.dialog.MyDialog;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity implements Api.ServerResponseListener ,MyDialog.myOnClickListener{

    public MainPageBinding mainPageBinding;
    private PrefManager prefManager;
    public com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPageBinding = DataBindingUtil.setContentView(this, R.layout.main_page);
        mainPageBinding.setActivity(this);

        prefManager = new PrefManager(this);
        Utils.setLocale(prefManager.getKEY_Language(),this);

        mainPageBinding.tvName.setText(prefManager.getDistrictName());
        mainPageBinding.designation.setText(prefManager.getBlockName());

        if(Utils.isOnline()){
            //getNutri_garden_master_form_list();
            get_nursery_user_details();
            get_nursery_land_type();
            get_nursery_species_type();
            get_dead_stage();
            get_nursery_expenditure_type();
            get_nursery_expenditure_unit();
            get_nursery_expenditure_found_src();
            get_fin_year();
            get_nursery_water_source_type();
            get_nursery_fencing_type();
        }
        //mainPageBinding.recurringExpenditure.setEnabled(false);
        mainPageBinding.syncLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPendingScreen();
            }
        });
        mainPageBinding.addBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddBatchOrLandScreen("Batch");
            }
        });
        mainPageBinding.addLand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddBatchOrLandScreen("Land");
            }
        });
        mainPageBinding.saplingSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NurserySurvey> workCount = dbData.get_dead_sapling_details_for_upload("","");
                if(workCount.size()>0){
                    Utils.showAlert(MainPage.this,getResources().getString(R.string.please_upload_your_dead_saplings));
                }
                else {
                    gotoAddBatchOrLandScreen("Sell");
                }

            }
        });
       /* mainPageBinding.saplingDead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddBatchOrLandScreen("Dead");
            }
        });*/
        mainPageBinding.orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrderAndDeliveryHistory("Order");
            }
        });
        mainPageBinding.deliveryHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrderAndDeliveryHistory("Delivery");
            }
        });
        mainPageBinding.capitalExpenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrderAndDeliveryHistory("Capital");
            }
        });
        mainPageBinding.recurringExpenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrderAndDeliveryHistory("Recurring");
            }
        });
        mainPageBinding.expenditureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoExpenditureView();
            }
        });
        syncButtonVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncButtonVisibility();
    }

    public void getNutri_garden_master_form_list() {
        try {
            new ApiService(this).makeJSONObjectRequest("nutri_garden_master_form_list", Api.Method.POST, UrlGenerator.getNurseryGardenService(), nutri_garden_master_form_listJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_nursery_land_type() {
        try {
            new ApiService(this).makeJSONObjectRequest("nursery_land_type", Api.Method.POST, UrlGenerator.getNurseryGardenService(), nursery_land_type_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_nursery_user_details() {
        try {
            new ApiService(this).makeJSONObjectRequest("nursery_user_details", Api.Method.POST, UrlGenerator.getNurseryGardenService(), nursery_user_details_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_nursery_species_type() {
        try {
            new ApiService(this).makeJSONObjectRequest("nursery_species_type", Api.Method.POST, UrlGenerator.getNurseryGardenService(), nursery_species_type_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_dead_stage() {
        try {
            new ApiService(this).makeJSONObjectRequest("dead_stage", Api.Method.POST, UrlGenerator.getNurseryGardenService(), dead_stage_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_nursery_expenditure_type() {
        try {
            new ApiService(this).makeJSONObjectRequest("expenditure_type", Api.Method.POST, UrlGenerator.getNurseryGardenService(), expenditure_type_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_nursery_expenditure_unit() {
        try {
            new ApiService(this).makeJSONObjectRequest("expenditure_unit", Api.Method.POST, UrlGenerator.getNurseryGardenService(), expenditure_unit_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_nursery_expenditure_found_src() {
        try {
            new ApiService(this).makeJSONObjectRequest("expenditure_found_src", Api.Method.POST, UrlGenerator.getNurseryGardenService(), expenditure_found_src_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_fin_year() {
        try {
            new ApiService(this).makeJSONObjectRequest("fin_year", Api.Method.POST, UrlGenerator.getNurseryGardenService(), expenditure_fin_year_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_nursery_water_source_type() {
        try {
            new ApiService(this).makeJSONObjectRequest("water_source_type", Api.Method.POST, UrlGenerator.getNurseryGardenService(), water_source_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_nursery_fencing_type() {
        try {
            new ApiService(this).makeJSONObjectRequest("fencing_type", Api.Method.POST, UrlGenerator.getNurseryGardenService(),fencing_type_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public JSONObject nutri_garden_master_form_listJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.nutri_garden_master_form_listJsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("master_listJsonParams", "" + authKey);
        return dataSet;
    }
    public JSONObject nursery_user_details_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.nursery_user_details_JsonParams_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("nursery_user_details", "" + authKey);
        return dataSet;
    }
    public JSONObject nursery_land_type_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.nursery_land_type_JsonParams_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("nursery_land_type", "" + authKey);
        return dataSet;
    }
    public JSONObject nursery_species_type_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.nursery_species_type_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("nursery_species_type", "" + authKey);
        return dataSet;
    }
    public JSONObject dead_stage_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.dead_stage_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("dead_stage", "" + authKey);
        return dataSet;
    }
    public JSONObject expenditure_type_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.expenditure_type_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("expenditure_type", "" + authKey);
        return dataSet;
    }
    public JSONObject expenditure_unit_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.expenditure_unit_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("expenditure_unit", "" + authKey);
        return dataSet;
    }
    public JSONObject expenditure_found_src_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.expenditure_found_src_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("expenditure_found_src", "" + authKey);
        return dataSet;
    }
 public JSONObject expenditure_fin_year_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.expenditure_fin_year_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("fin_year", "" + authKey);
        return dataSet;
    }

    public JSONObject water_source_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.water_source_type_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_source_type", "" + authKey);
        return dataSet;
    }
    public JSONObject fencing_type_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.fencing_type_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_source_type", "" + authKey);
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("nutri_garden_master_form_list".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insertnutri_garden_master_form_list().execute(jsonObject);
                }
                Log.d("garden_master_list", "" + responseDecryptedBlockKey);
            }
            if ("nursery_user_details".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_nursery_user_details().execute(jsonObject);
                }
                Log.d("nursery_user_details", "" + responseDecryptedBlockKey);
            }
            if ("nursery_land_type".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_nursery_land_type().execute(jsonObject);
                }
                Log.d("nursery_land_type", "" + responseDecryptedBlockKey);
            }
            if ("nursery_species_type".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_nursery_species_type().execute(jsonObject);
                }
                Log.d("nursery_species_type", "" + responseDecryptedBlockKey);
            }
            if ("dead_stage".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_dead_stage().execute(jsonObject);
                }
                Log.d("dead_stage", "" + responseDecryptedBlockKey);
            }
            if ("expenditure_type".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_expenditure_type().execute(jsonObject);
                }
                Log.d("expenditure_type", "" + responseDecryptedBlockKey);
            }
            if ("expenditure_unit".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_expenditure_unit().execute(jsonObject);
                }
                Log.d("expenditure_unit", "" + responseDecryptedBlockKey);
            }
            if ("expenditure_found_src".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_expenditure_found_src().execute(jsonObject);
                }
                Log.d("expenditure_found_src", "" + responseDecryptedBlockKey);
            }
            if ("fin_year".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_fin_year().execute(jsonObject);
                }
                Log.d("fin_year", "" + responseDecryptedBlockKey);
            }
            if ("water_source_type".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_water_source_type().execute(jsonObject);
                }
                Log.d("water_source_type", "" + responseDecryptedBlockKey);
            }
            if ("fencing_type".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_fencing_type().execute(jsonObject);
                }
                Log.d("fencing_type", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public class Insertnutri_garden_master_form_list extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            dbData.deletefin_year();
            dbData.deletework_type();
            dbData.deleteself_help_group();
            dbData.deleteself_help_group_member();

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();
                JSONArray jsonArray2 = new JSONArray();
                JSONArray jsonArray3 = new JSONArray();
                JSONArray jsonArray4 = new JSONArray();
                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        jsonArray1= jsonArray.getJSONObject(i).getJSONArray("fin_year");
                        jsonArray2= jsonArray.getJSONObject(i).getJSONArray("work_type");
                        jsonArray3= jsonArray.getJSONObject(i).getJSONArray("self_help_group");
                        jsonArray4= jsonArray.getJSONObject(i).getJSONArray("self_help_group_member");
                        for (int j=0;j<jsonArray1.length();j++){
                            NurserySurvey nurserySurvey = new NurserySurvey();
                            nurserySurvey.setFin_year(jsonArray1.getJSONObject(j).getString("fin_year"));
                            dbData.insert_Master_Fin_Year(nurserySurvey);
                        }
                        for (int j=0;j<jsonArray2.length();j++){
                            NurserySurvey nurserySurvey = new NurserySurvey();
                            nurserySurvey.setWork_code(jsonArray2.getJSONObject(j).getInt("work_code"));
                            nurserySurvey.setWork_name(jsonArray2.getJSONObject(j).getString("work_name"));
                            dbData.insert_Master_Work_Type(nurserySurvey);
                        }
                        for (int j=0;j<jsonArray3.length();j++){
                            NurserySurvey nurserySurvey = new NurserySurvey();
                            nurserySurvey.setShg_code(jsonArray3.getJSONObject(j).getInt("shg_code"));
                            nurserySurvey.setShg_name(jsonArray3.getJSONObject(j).getString("shg_name"));
                            dbData.insert_Master_Self_Help_Group(nurserySurvey);
                        }
                        for (int j=0;j<jsonArray4.length();j++){
                            NurserySurvey nurserySurvey = new NurserySurvey();
                            nurserySurvey.setShg_code(jsonArray4.getJSONObject(j).getInt("shg_code"));
                            nurserySurvey.setShg_member_code(jsonArray4.getJSONObject(j).getInt("shg_member_code"));
                            nurserySurvey.setMember_name(jsonArray4.getJSONObject(j).getString("member_name"));
                            dbData.insert_Master_Self_Help_Group_Member(nurserySurvey);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }
    }
    public class Insert_nursery_user_details extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            dbData.delete_nursery_user_details();
            //dbData.delete_nursery_land_type() ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setNursery_id(jsonArray.getJSONObject(i).getInt("nursery_id"));
                        nurserySurvey.setNursery_level_id(jsonArray.getJSONObject(i).getInt("nursery_level_id"));
                        nurserySurvey.setNursery_name_en(jsonArray.getJSONObject(i).getString("nursery_name_en"));
                        nurserySurvey.setNursery_name_ta(jsonArray.getJSONObject(i).getString("nursery_name_ta"));
                        nurserySurvey.setNursery_address(jsonArray.getJSONObject(i).getString("nursery_address"));
                        nurserySurvey.setNursery_dname(jsonArray.getJSONObject(i).getString("dname"));
                        nurserySurvey.setNursery_bname(jsonArray.getJSONObject(i).getString("bname"));
                        nurserySurvey.setNursery_pvname(jsonArray.getJSONObject(i).getString("pvname"));
                        nurserySurvey.setPia_members_count(jsonArray.getJSONObject(i).getString("no_of_members"));
                        nurserySurvey.setPia_type_name_en(jsonArray.getJSONObject(i).getString("pia_type_name_en"));
                        nurserySurvey.setPia_type_name_ta(jsonArray.getJSONObject(i).getString("pia_type_name_ta"));
                        nurserySurvey.setPia_name_en(jsonArray.getJSONObject(i).getString("pia_name_en"));
                        nurserySurvey.setPia_name_ta(jsonArray.getJSONObject(i).getString("pia_name_ta"));
                        dbData.insert_nursery_user_details(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new fetchNurseryUserDetails().execute();
        }
    }
    public class Insert_nursery_land_type extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            //dbData.delete_nursery_user_details();
            dbData.delete_nursery_land_type() ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setLand_type_id(jsonArray.getJSONObject(i).getInt("land_type_id"));
                        nurserySurvey.setLand_type_name_en(jsonArray.getJSONObject(i).getString("land_type_name_en"));
                        nurserySurvey.setLand_type_name_ta(jsonArray.getJSONObject(i).getString("land_type_name_ta"));
                        nurserySurvey.setIs_others(jsonArray.getJSONObject(i).getString("is_others"));
                        dbData.insert_nursery_land_type(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class Insert_nursery_species_type extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            //dbData.delete_nursery_user_details();
            dbData.delete_nursery_species_type() ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setSpecies_type_id(jsonArray.getJSONObject(i).getInt("species_type_id"));
                        nurserySurvey.setSpecies_name_en(jsonArray.getJSONObject(i).getString("species_name_en"));
                        nurserySurvey.setSpecies_name_ta(jsonArray.getJSONObject(i).getString("species_name_ta"));
                        dbData.insert_nursery_species_type(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class Insert_dead_stage extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            //dbData.delete_nursery_user_details();
            dbData.delete_dead_stage() ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setDead_stage_id(jsonArray.getJSONObject(i).getInt("dead_stage_id"));
                        nurserySurvey.setDead_stage_name_en(jsonArray.getJSONObject(i).getString("dead_stage_name_en"));
                        nurserySurvey.setDead_stage_name_ta(jsonArray.getJSONObject(i).getString("dead_stage_name_ta"));
                        dbData.insert_dead_stage(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class Insert_expenditure_type extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            dbData.delete_nursery_expenditure_type() ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setExpense_category_id(jsonArray.getJSONObject(i).getInt("expense_category_id"));
                        nurserySurvey.setExpenditure_type_id(jsonArray.getJSONObject(i).getInt("expenditure_type_id"));
                        nurserySurvey.setExpense_category_en(jsonArray.getJSONObject(i).getString("expense_category_en"));
                        nurserySurvey.setExpense_category_ta(jsonArray.getJSONObject(i).getString("expense_category_ta"));
                        nurserySurvey.setIs_others(jsonArray.getJSONObject(i).getString("is_others"));
                        dbData.insert_nursery_expenditure_type(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class Insert_expenditure_unit extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            dbData.delete_nursery_expenditure_unit() ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setExpense_category_id(jsonArray.getJSONObject(i).getInt("expense_category_id"));
                        nurserySurvey.setExpenditure_unit_id(jsonArray.getJSONObject(i).getInt("expenditure_unit_id"));
                        nurserySurvey.setExpenditure_unit_en(jsonArray.getJSONObject(i).getString("expenditure_unit_en"));
                        nurserySurvey.setExpenditure_unit_ta(jsonArray.getJSONObject(i).getString("expenditure_unit_ta"));
                        dbData.insert_nursery_expenditure_unit(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class Insert_expenditure_found_src extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();

            dbData.delete_nursery_expenditure_found_src() ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setExpense_category_id(jsonArray.getJSONObject(i).getInt("expense_category_id"));
                        nurserySurvey.setExpenditure_fund_src_id(jsonArray.getJSONObject(i).getInt("expenditure_fund_src_id"));
                        nurserySurvey.setExpenditure_fund_src_en(jsonArray.getJSONObject(i).getString("expenditure_fund_src_en"));
                        nurserySurvey.setExpenditure_fund_src_ta(jsonArray.getJSONObject(i).getString("expenditure_fund_src_ta"));
                        dbData.insert_nursery_expenditure_found_src(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class Insert_fin_year extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();

            dbData.deletefin_year(); ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setFin_year(jsonArray.getJSONObject(i).getString("fin_year"));
                        dbData.insert_Master_Fin_Year(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class Insert_water_source_type extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            if (params.length > 0) {
                dbData.delete_nursery_water_source_types(); ;
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setWater_source_type_id(jsonArray.getJSONObject(i).getString("water_source_type_id"));
                        nurserySurvey.setWater_source_type_name(jsonArray.getJSONObject(i).getString("water_source_type_name"));
                        nurserySurvey.setIs_others(jsonArray.getJSONObject(i).getString("is_others"));
                        dbData.insert_nursery_water_source_type(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    public class Insert_fencing_type extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            if (params.length > 0) {
                dbData.delete_nursery_fencing_types();
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setFencing_type_id(jsonArray.getJSONObject(i).getString("fencing_type_id"));
                        nurserySurvey.setFencing_type_name(jsonArray.getJSONObject(i).getString("fencing_type_name"));
                        nurserySurvey.setIs_others(jsonArray.getJSONObject(i).getString("is_others"));
                        dbData.insert_nursery_fencing_type(nurserySurvey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    private void showHomeScreen() {
        Intent intent = new Intent(MainPage.this, NewHomePage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void syncButtonVisibility() {
        dbData.open();
        ArrayList<NurserySurvey> workCount = dbData.get_dead_sapling_details_for_upload("","");
        ArrayList<NurserySurvey> expenditureCount = dbData.get_All_Or_Particular_Expenditure("","");

        int count = workCount.size()+expenditureCount.size();
        if (workCount.size() > 0 || expenditureCount.size()>0) {
            mainPageBinding.syncLayout.setVisibility(View.VISIBLE);
            mainPageBinding.pendingCount.setText(""+count);
        }else {
            mainPageBinding.syncLayout.setVisibility(View.GONE);
            mainPageBinding.pendingCount.setText(""+count);
        }
    }

    public void openPendingScreen() {
        Intent intent = new Intent(this, NewPendingScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void logout() {
        dbData.open();
        //ArrayList<NurserySurvey> ImageCount = dbData.getAllTreeImages();
        ArrayList<NurserySurvey> deadworkCount = dbData.get_dead_sapling_details_for_upload("","");
        ArrayList<NurserySurvey> expenditureCount = dbData.get_All_Or_Particular_Expenditure("","");
        ArrayList<NurserySurvey> getSpeciesCount = dbData.get_nursery_batch_species_details("","local","0","");
        ArrayList<NurserySurvey> getGrowthSpeciesCount = dbData.get_batch_growth_species_details("","local","0","","");


        if (!Utils.isOnline()) {
            Utils.showAlert(this, getResources().getString(R.string.logging_out_while_offline_may_leads_to_loss_data));
        } else {
            if (!(deadworkCount.size() > 0) && !(expenditureCount.size()>0) &&(! (getSpeciesCount.size() > 0) && (!(getGrowthSpeciesCount.size() > 0)))) {
                closeApplication();
            } else {
                Utils.showAlert(this, getResources().getString(R.string.sync_all_the_data_before_logout));
            }
        }
    }

    public void closeApplication() {
        new MyDialog(this).exitDialog(this, getResources().getString(R.string.are_you_sure_you_want_to_logout), "Logout");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new MyDialog(this).exitDialog(this, getResources().getString(R.string.are_you_sure_you_want_to_exit), "Exit");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
            onBackPressed();
        } else {

            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    public class fetchNurseryUserDetails extends AsyncTask<Void, Void,ArrayList<NurserySurvey>> {
        @Override
        protected ArrayList<NurserySurvey> doInBackground(Void... params) {
            dbData.open();
            ArrayList<NurserySurvey> nurseryDetails = new ArrayList<>();
            nurseryDetails = dbData.get_nursery_user_details();
            Log.d("nursery_details", String.valueOf(nurseryDetails.size()));
            return nurseryDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<NurserySurvey> nurseryDetails) {
            super.onPostExecute(nurseryDetails);
            if(nurseryDetails.size()>0){

                for (int i=0;i<nurseryDetails.size();i++){
                    mainPageBinding.nurseryId.setText(""+nurseryDetails.get(i).getNursery_id());
                    mainPageBinding.nurseryLevelId.setText(""+nurseryDetails.get(i).getNursery_level_id());
                    mainPageBinding.nurseryNameEn.setText(""+nurseryDetails.get(i).getNursery_name_en());
                    mainPageBinding.nurseryNameTa.setText(""+nurseryDetails.get(i).getNursery_name_ta());
                    mainPageBinding.nurseryAddress.setText(""+nurseryDetails.get(i).getNursery_address());
                    mainPageBinding.nurseryDname.setText(""+nurseryDetails.get(i).getNursery_dname());
                    mainPageBinding.nurseryBname.setText(""+nurseryDetails.get(i).getNursery_bname());
                    mainPageBinding.nurseryPvname.setText(""+nurseryDetails.get(i).getNursery_pvname());
                    mainPageBinding.memberCount.setText(""+nurseryDetails.get(i).getPia_members_count());
                    mainPageBinding.piaType.setText(""+nurseryDetails.get(i).getPia_type_name_en());
                    mainPageBinding.piaName.setText(""+nurseryDetails.get(i).getPia_name_en());
                }

            }
            if(mainPageBinding.nurseryLevelId.getText().toString().equals("1")){
                mainPageBinding.pvNameLayout.setVisibility(View.GONE);
            }
            else {
                mainPageBinding.pvNameLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void gotoAddBatchOrLandScreen(String type) {
        Intent intent;
        if(type.equals("Batch")){
            intent = new Intent(MainPage.this, AddViewBatchDetails.class);
        }
        else if(type.equals("Land")){
            intent = new Intent(MainPage.this, AddViewLand.class);
        }
        else if(type.equals("Dead")){
            intent = new Intent(MainPage.this, NewDeadSaplingEntry.class);
            intent.putExtra("nursery_level_id",mainPageBinding.nurseryLevelId.getText().toString());
        }
        else {
            intent = new Intent(MainPage.this, SellSpecies.class);
            intent.putExtra("nursery_level_id",mainPageBinding.nurseryLevelId.getText().toString());
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private void gotoOrderAndDeliveryHistory(String type) {
        Intent intent;
        if(type.equals("Order")){
            intent = new Intent(MainPage.this, OrderHistory.class);
            intent.putExtra("Activity","Order");
        }
        else if(type.equals("Capital")) {
            intent = new Intent(MainPage.this, CapitalRecurreingExpenditure.class);
            intent.putExtra("expenditure_type_id",1);
        }
        else if(type.equals("Recurring")) {
            intent = new Intent(MainPage.this, CapitalRecurreingExpenditure.class);
            intent.putExtra("expenditure_type_id",2);
        }
        else {
            intent = new Intent(MainPage.this, OrderHistory.class);
            intent.putExtra("Activity","Delivery");
        }

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void gotoExpenditureView() {
        Intent intent;
        intent = new Intent(MainPage.this, ExpenditureView.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}
