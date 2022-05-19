package com.nic.nurserygarden.activity.GrowthTrackingActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.CameraScreen;
import com.nic.nurserygarden.adapter.TrackingDatesAdapter;
import com.nic.nurserygarden.adapter.TrackingGrowthAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityGrowthTrackingBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class GrowthTracking extends AppCompatActivity implements Api.ServerResponseListener{

    private ActivityGrowthTrackingBinding growthTrackingBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    PrefManager prefManager;
    int batch_id;
    int batch_primary_id;
    String batch_growth_tracking_primary_id;
    String choose_date="";
    TrackingDatesAdapter trackingDatesAdapter;
    TrackingGrowthAdapter trackingGrowthAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        growthTrackingBinding = DataBindingUtil.setContentView(this, R.layout.activity_growth_tracking);
        growthTrackingBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefManager = new PrefManager(this);
        batch_id = getIntent().getIntExtra("batch_id",0);
        batch_primary_id = getIntent().getIntExtra("batch_primary_id",0);
        growthTrackingBinding.takePhotoBtn.setVisibility(View.GONE);
        growthTrackingBinding.scrollView.setVisibility(View.GONE);
        initialiseRecyclerView();
        if(Utils.isOnline()){
            ArrayList<NurserySurvey> growthTrackingDateList = dbData.get_batch_growth_tracking_dates(String.valueOf(batch_id));
            if(growthTrackingDateList.size()> 0){
                new fetchBatchGrowthTrackingDates().execute();
            }
            else {
                get_batch_tracking_date_list();
            }
        }
        else {
            Utils.showAlert(GrowthTracking.this,getResources().getString(R.string.no_internet));
        }

        growthTrackingBinding.takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_screen = new Intent(GrowthTracking.this, CameraScreen.class);
                camera_screen.putExtra("activity_type","GrowthTracking");
                camera_screen.putExtra("batch_id",batch_id);
                camera_screen.putExtra("batch_primary_id",batch_primary_id);
                camera_screen.putExtra("entry_date",choose_date);
                startActivity(camera_screen);
            }
        });
    }
    private void initialiseRecyclerView() {
        growthTrackingBinding.chooseDateRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        growthTrackingBinding.growthDetailsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    public void get_batch_tracking_date_list() {
        try {
            new ApiService(this).makeJSONObjectRequest("batch_tracking_date_list", Api.Method.POST, UrlGenerator.getNurseryGardenService(), batch_tracking_date_listJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject batch_tracking_date_listJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), batch_tracking_date_json().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("batch_tracking_date", "" + dataSet);
        return dataSet;
    }

    public  JSONObject batch_tracking_date_json() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_tracking_date_list);
        dataSet.put("batch_id",batch_id);
        Log.d("batch_tracking_date", "" + dataSet);
        return dataSet;
    }


    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("batch_tracking_date_list".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_batch_tracking_date_list().execute(jsonObject);
                }
                else {
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                    new fetchBatchGrowthTrackingDates().execute();
                }
                Log.d("batch_tracking_date", "" + responseDecryptedBlockKey);
            }
            if ("saveTrack".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    String whereClause = "batch_id = ?";
                    String[] whereArgs = new String[]{String.valueOf(batch_id)};
                    int sdsm = db.delete(DBHelper.BATCH_GROWTH_TRACKING_DETAILS, whereClause, whereArgs);
                    int sdsm1 = db.delete(DBHelper.BATCH_GROWTH_TRACKING_PHOTOS_DETAILS, whereClause, whereArgs);
                    int sdsm2 = db.delete(DBHelper.BATCH_GROWTH_TRACKING_SPECIES_DETAILS, whereClause, whereArgs);

                    //get_nursery_batch_list();
                    //nurseryBatchesAdapter.notifyDataSetChanged();
                }
                else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")){
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                    int dsd = db.delete(DBHelper.BATCH_GROWTH_TRACKING_SPECIES_DETAILS, "batch_id = ?", new String[]{String.valueOf(batch_id)});
                    int dsd1 = db.delete(DBHelper.BATCH_GROWTH_TRACKING_PHOTOS_DETAILS, "batch_id = ?", new String[]{String.valueOf(batch_id)});

                    //get_nursery_batch_list();
                    //nurseryBatchesAdapter.notifyDataSetChanged();
                }
                Log.d("saveTrack", "" + responseDecryptedBlockKey);
            }
            if ("nursery_batch_growth_track_details_list".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_nursery_batch_growth_details().execute(jsonObject);
                }
                else {
                    new fetchNurseryBatchDetails().execute();
                }
                Log.d("nursery_batch_list", "" + responseDecryptedBlockKey);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @SuppressLint("StaticFieldLeak")
    public class Insert_batch_tracking_date_list extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            db.delete(DBHelper.BATCH_GROWTH_TRACKING_DATES, "batch_id = ?", new String[]{String.valueOf(batch_id)});
            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setBatch_id(batch_id);
                        nurserySurvey.setCreated_date(jsonArray.getJSONObject(i).getString("tracking_date"));

                        dbData.insert_nursery_batch_growth_tracking_dates(nurserySurvey);


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
            new fetchBatchGrowthTrackingDates().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class fetchBatchGrowthTrackingDates extends AsyncTask<Void, Void, ArrayList<NurserySurvey>> {
        @Override
        protected ArrayList<NurserySurvey> doInBackground(Void... params) {
            dbData.open();
            ArrayList<NurserySurvey> nurseryBatchDetails = new ArrayList<>();
            nurseryBatchDetails = dbData.get_batch_growth_tracking_dates(String.valueOf(batch_id));
            Log.d("batch_date_details", String.valueOf(nurseryBatchDetails.size()));
            return nurseryBatchDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<NurserySurvey> nurseryBatchDetails) {
            super.onPostExecute(nurseryBatchDetails);
            if(nurseryBatchDetails.size()>0){
                growthTrackingBinding.scrollView.setVisibility(View.VISIBLE);
                growthTrackingBinding.chooseDateRecycler.setVisibility(View.VISIBLE);
                trackingDatesAdapter = new TrackingDatesAdapter(nurseryBatchDetails, GrowthTracking.this,dbData);
                growthTrackingBinding.chooseDateRecycler.setAdapter(trackingDatesAdapter);
            }
            else {
                growthTrackingBinding.scrollView.setVisibility(View.GONE);
                growthTrackingBinding.chooseDateRecycler.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class fetchNurseryBatchDetails extends AsyncTask<Void, Void, ArrayList<NurserySurvey>> {
        @Override
        protected ArrayList<NurserySurvey> doInBackground(Void... params) {
            dbData.open();
            ArrayList<NurserySurvey> BatchTrackingDetails = new ArrayList<>();
            BatchTrackingDetails = dbData.get_nursery_batch_growth_tacking_details(String.valueOf(batch_id),choose_date,"");
            Log.d("tracking_details", String.valueOf(BatchTrackingDetails.size()));
            return BatchTrackingDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<NurserySurvey> BatchTrackingDetails) {
            super.onPostExecute(BatchTrackingDetails);
            if(BatchTrackingDetails.size()>0){
                growthTrackingBinding.growthDetailsRecycler.setVisibility(View.VISIBLE);
                growthTrackingBinding.takePhotoBtn.setVisibility(View.GONE);
                trackingGrowthAdapter = new TrackingGrowthAdapter(BatchTrackingDetails, GrowthTracking.this,dbData);
                growthTrackingBinding.growthDetailsRecycler.setAdapter(trackingGrowthAdapter);
            }
            else {
                growthTrackingBinding.growthDetailsRecycler.setVisibility(View.GONE);
                growthTrackingBinding.takePhotoBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    public void chooseDateAction(String choose_date_){
        choose_date=choose_date_;
        dbData.open();
        ArrayList<NurserySurvey> growthTrackingDetailsList = new ArrayList<>();
        ArrayList<NurserySurvey> get_batch_growth_species_details = new ArrayList<>();
        growthTrackingDetailsList = dbData.get_nursery_batch_growth_tacking_details(String.valueOf(batch_id),choose_date,"");
        get_batch_growth_species_details = dbData.get_batch_growth_species_details(String.valueOf(batch_id),"","0","");
        if(get_batch_growth_species_details.size()>0){
                new fetchNurseryBatchDetails().execute();
        }
        else {
            if(Utils.isOnline()){
                get_nursery_batch_growth_track_details_list();
            }
            else {
                new fetchNurseryBatchDetails().execute();
            }
        }

    }

    public void clickSaplingBtn(int batch_growth_tracking_primary_id){
        Intent gotoSaplingEntry = new Intent(GrowthTracking.this,SaplingEntryView.class);
        gotoSaplingEntry.putExtra("batch_primary_id",batch_primary_id);
        gotoSaplingEntry.putExtra("batch_id",batch_id);
        gotoSaplingEntry.putExtra("batch_growth_tracking_primary_id",batch_growth_tracking_primary_id);
        gotoSaplingEntry.putExtra("entry_date",choose_date);
        startActivity(gotoSaplingEntry);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }

    public void UploadData(JSONObject savePMAYDataSet, String batch_primary_id_, String batch_id_) {
        batch_growth_tracking_primary_id = batch_primary_id_;
        batch_id = Integer.parseInt(batch_id_);
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), savePMAYDataSet.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveTrack", Api.Method.POST, UrlGenerator.getNurseryGardenService(), dataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("saveTrack", "" + dataSet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbData.open();
        ArrayList<NurserySurvey> growthTrackingDetailsList = new ArrayList<>();
        ArrayList<NurserySurvey> get_batch_growth_species_details = new ArrayList<>();
        growthTrackingDetailsList = dbData.get_nursery_batch_growth_tacking_details(String.valueOf(batch_id),choose_date,"");
        get_batch_growth_species_details = dbData.get_batch_growth_species_details(String.valueOf(batch_id),"","0","");
        if(get_batch_growth_species_details.size()>0){
            new fetchNurseryBatchDetails().execute();
        }
        else {
            if(Utils.isOnline()){
                get_nursery_batch_growth_track_details_list();
            }
            else {
                new fetchNurseryBatchDetails().execute();
            }
        }

    }

    public void get_nursery_batch_growth_track_details_list() {
        try {
            new ApiService(this).makeJSONObjectRequest("nursery_batch_growth_track_details_list", Api.Method.POST, UrlGenerator.getNurseryGardenService(), nursery_batch_growth_listJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject nursery_batch_growth_listJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), nursery_batch_growth_json().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("batch_growth", "" + dataSet);
        return dataSet;
    }

    public  JSONObject nursery_batch_growth_json() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_details_of_sapling_growth_tracking_view);
        dataSet.put("batch_id", batch_id);
        dataSet.put("entry_date", choose_date);
        Log.d("batch_growth", "" + dataSet);
        return dataSet;
    }

    @SuppressLint("StaticFieldLeak")
    public class Insert_nursery_batch_growth_details extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            //dbData.delete_nursery_user_details();

            db.delete(DBHelper.BATCH_GROWTH_TRACKING_DETAILS, "batch_id =? and server_flag=?", new String[]{String.valueOf(batch_id),"1"});
            db.delete(DBHelper.BATCH_GROWTH_TRACKING_PHOTOS_DETAILS, "batch_id =? and server_flag=?", new String[]{String.valueOf(batch_id),"1"});
            db.delete(DBHelper.BATCH_GROWTH_TRACKING_SPECIES_DETAILS, "batch_id =? and server_flag=?", new String[]{String.valueOf(batch_id),"1"});
            //dbData.delete_nursery_land_type() ;

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setGrowth_tracking_id(jsonArray.getJSONObject(i).getInt("growth_tracking_id"));
                        nurserySurvey.setBatch_primary_id(batch_primary_id);
                        nurserySurvey.setNursery_id(jsonArray.getJSONObject(i).getInt("nursery_id"));
                        nurserySurvey.setBatch_id(jsonArray.getJSONObject(i).getInt("batch_id"));
                        nurserySurvey.setCreated_date(jsonArray.getJSONObject(i).getString("entry_date"));
                        nurserySurvey.setServer_flag("1");

                        long batch_growth_tracking_primary_id = dbData.insert_nursery_batch_growth_details(nurserySurvey);
                        jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("sapling_growth_tracking_details");
                        for(int j=0;j<jsonArray1.length();j++){
                            NurserySurvey nurserySurvey1 = new NurserySurvey();
                            nurserySurvey1.setBatch_growth_tracking_primary_id((int) batch_growth_tracking_primary_id);
                            nurserySurvey1.setBatch_primary_id(batch_primary_id);
                            nurserySurvey1.setGrowth_tracking_details_id(jsonArray1.getJSONObject(j).getInt("growth_tracking_details_id"));
                            nurserySurvey1.setBatch_species_id(jsonArray1.getJSONObject(j).getInt("batch_species_id"));
                            nurserySurvey1.setBatch_id(jsonArray1.getJSONObject(j).getInt("batch_id"));
                            nurserySurvey1.setSpecies_type_id(jsonArray1.getJSONObject(j).getInt("species_type_id"));
                            nurserySurvey1.setNo_of_saplings(jsonArray1.getJSONObject(j).getInt("no_of_saplings"));
                            nurserySurvey1.setHeight_in_cm(jsonArray1.getJSONObject(j).getString("sapling_height_in_cm"));
                            nurserySurvey1.setAge_in_days(jsonArray1.getJSONObject(j).getInt("sapling_age_in_days"));
                            nurserySurvey1.setCreated_date(jsonArray1.getJSONObject(j).getString("entry_date"));
                            nurserySurvey1.setServer_flag("1");

                            dbData.insert_nursery_batch_growth_species_details(nurserySurvey1);
                        }

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
            new fetchNurseryBatchDetails().execute();
        }
    }
}
