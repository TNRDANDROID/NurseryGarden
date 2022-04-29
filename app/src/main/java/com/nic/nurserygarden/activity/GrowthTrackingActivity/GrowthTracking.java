package com.nic.nurserygarden.activity.GrowthTrackingActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchDetails;
import com.nic.nurserygarden.adapter.NurseryBatchesAdapter;
import com.nic.nurserygarden.adapter.TrackingDatesAdapter;
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

public class GrowthTracking extends AppCompatActivity implements Api.ServerResponseListener{

    private ActivityGrowthTrackingBinding growthTrackingBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    PrefManager prefManager;
    int batch_id;
    TrackingDatesAdapter trackingDatesAdapter;
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

    }
    private void initialiseRecyclerView() {
        growthTrackingBinding.chooseDateRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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

                    new fetchBatchGrowthTrackingDates().execute();
                }
                Log.d("batch_tracking_date", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

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
                trackingDatesAdapter = new TrackingDatesAdapter(nurseryBatchDetails, GrowthTracking.this,dbData);
                growthTrackingBinding.chooseDateRecycler.setAdapter(trackingDatesAdapter);
            }
        }
    }
}
