package com.nic.nurserygarden.activity.BatchActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.CameraScreen;
import com.nic.nurserygarden.adapter.NurseryBatchesAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityAddViewBatchDetailsBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AddViewBatchDetails extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {
    private ActivityAddViewBatchDetailsBinding batchDetailsBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    NurseryBatchesAdapter nurseryBatchesAdapter;
    String batch_primary_id;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        batchDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_view_batch_details);
        batchDetailsBinding.setActivity(this);

        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefManager = new PrefManager(this);
        Utils.setLocale(prefManager.getKEY_Language(),this);
        batchDetailsBinding.addBatchBtn.setOnClickListener(this::onClick);
        initialiseRecyclerView();

        if(Utils.isOnline()){
            ArrayList<NurserySurvey> getSpeciesCount = new ArrayList<>();
            getSpeciesCount = dbData.get_nursery_batch_species_details("","local","0","");
            if(getSpeciesCount.size()>0) {
                new fetchNurseryBatchDetails().execute();
            }
            else {
                get_nursery_batch_list();
            }

        }
        else {
            new fetchNurseryBatchDetails().execute();
        }

    }

    private void initialiseRecyclerView() {
        batchDetailsBinding.batchRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        new fetchNurseryBatchDetails().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_batch_btn:
                addBatchView();
                break;
        }
    }

    public void addBatchView(){

        try {
            final Dialog dialog = new Dialog(this,R.style.AppTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_batch_dialog);
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
            RelativeLayout capture_layout = dialog.findViewById(R.id.capture_layout);
            ScrollView land_layout = dialog.findViewById(R.id.land_layout);
            LinearLayout batch_layout = dialog.findViewById(R.id.batch_layout);
            TextView tittle_text = dialog.findViewById(R.id.tittle_text);
            tittle_text.setText(getResources().getString(R.string.add_batch_details));
            land_layout.setVisibility(View.GONE);
            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            capture_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent camera_screen = new Intent(AddViewBatchDetails.this, CameraScreen.class);
                    camera_screen.putExtra("activity_type","Batch");
                    startActivity(camera_screen);
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void get_nursery_batch_list() {
        try {
            new ApiService(this).makeJSONObjectRequest("nursery_batch_list", Api.Method.POST, UrlGenerator.getNurseryGardenService(), nursery_batch_listJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject nursery_batch_listJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), nursery_batch_json().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("nursery_batch", "" + dataSet);
        return dataSet;
    }

    public  JSONObject nursery_batch_json() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_details_of_nursery_seeding_batch_view);
        Log.d("nursery_batch", "" + dataSet);
        return dataSet;
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("nursery_batch_list".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_nursery_batch_details().execute(jsonObject);
                }
                else {
                    new fetchNurseryBatchDetails().execute();
                }
                Log.d("nursery_batch_list", "" + responseDecryptedBlockKey);
            }

            if ("saveBatch".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    String whereClause = "batch_primary_id = ? ";
                    String[] whereArgs = new String[]{batch_primary_id};
                    int sdsm = db.delete(DBHelper.BATCH_DETAILS, whereClause, whereArgs);
                    int sdsm1 = db.delete(DBHelper.BATCH_IMAGES_DETAILS, whereClause, whereArgs);
                    int sdsm2 = db.delete(DBHelper.BATCH_SPECIES_DETAILS, whereClause, whereArgs);

                    get_nursery_batch_list();
                    nurseryBatchesAdapter.notifyDataSetChanged();
                }
                else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")){
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                    int dsd = db.delete(DBHelper.BATCH_SPECIES_DETAILS, "batch_primary_id = ? ", new String[]{batch_primary_id});

                    get_nursery_batch_list();
                    nurseryBatchesAdapter.notifyDataSetChanged();
                }
                Log.d("saveBatch", "" + responseDecryptedBlockKey);
            }
            if ("saveDeadSapling".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    String whereClause = "batch_primary_id = ? ";
                    String[] whereArgs = new String[]{batch_primary_id};
                    int sdsm = db.delete(DBHelper.DEAD_SAPLING_DETAILS_SAVE, whereClause, whereArgs);

                    get_nursery_batch_list();
                    nurseryBatchesAdapter.notifyDataSetChanged();
                }
                else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")){
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                    int dsd = db.delete(DBHelper.DEAD_SAPLING_DETAILS_SAVE, "batch_primary_id = ? ", new String[]{batch_primary_id});

                    get_nursery_batch_list();
                    nurseryBatchesAdapter.notifyDataSetChanged();
                }
                Log.d("saveDeadSapling", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public class Insert_nursery_batch_details extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            //dbData.delete_nursery_user_details();

            db.delete(DBHelper.BATCH_DETAILS, "server_flag=?", new String[]{"1"});
            db.delete(DBHelper.BATCH_IMAGES_DETAILS, "server_flag=?", new String[]{"1"});
            db.delete(DBHelper.BATCH_SPECIES_DETAILS, "server_flag=?", new String[]{"1"});
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
                        nurserySurvey.setBatch_id(jsonArray.getJSONObject(i).getInt("batch_id"));
                        nurserySurvey.setBatch_number(jsonArray.getJSONObject(i).getInt("batch_no"));
                        nurserySurvey.setCreated_date(jsonArray.getJSONObject(i).getString("batch_date"));
                        nurserySurvey.setIs_batch_closed(jsonArray.getJSONObject(i).getString("is_batch_closed"));
                        nurserySurvey.setServer_flag("1");

                        long batch_primary_id = dbData.insert_nursery_batch_details(nurserySurvey);
                        jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("seeding_batch_species_details");
                        for(int j=0;j<jsonArray1.length();j++){
                            NurserySurvey nurserySurvey1 = new NurserySurvey();
                            nurserySurvey1.setBatch_primary_id((int) batch_primary_id);
                            nurserySurvey1.setBatch_species_id(jsonArray1.getJSONObject(j).getInt("batch_species_id"));
                            nurserySurvey1.setBatch_id(jsonArray1.getJSONObject(j).getInt("batch_id"));
                            nurserySurvey1.setSpecies_type_id(jsonArray1.getJSONObject(j).getInt("species_type_id"));
                            nurserySurvey1.setNo_of_count(jsonArray1.getJSONObject(j).getInt("number_of_balance_active_saplings"));
                            nurserySurvey1.setSpecies_name_en(jsonArray1.getJSONObject(j).getString("species_name_en"));
                            nurserySurvey1.setSpecies_name_ta(jsonArray1.getJSONObject(j).getString("species_name_ta"));
                            nurserySurvey1.setIs_harvest_closed(jsonArray1.getJSONObject(j).getString("is_harvest_closed"));
                            nurserySurvey1.setServer_flag("1");

                            dbData.insert_nursery_batch_species_details(nurserySurvey1);
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

    @SuppressLint("StaticFieldLeak")
    public class fetchNurseryBatchDetails extends AsyncTask<Void, Void, ArrayList<NurserySurvey>> {
        @Override
        protected ArrayList<NurserySurvey> doInBackground(Void... params) {
            dbData.open();
            ArrayList<NurserySurvey> nurseryBatchDetails;
            nurseryBatchDetails = dbData.get_nursery_batch_details("All","1","");
            Log.d("nursery_batch_details", String.valueOf(nurseryBatchDetails.size()));
            return nurseryBatchDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<NurserySurvey> nurseryBatchDetails) {
            super.onPostExecute(nurseryBatchDetails);
            if(nurseryBatchDetails.size()>0){
                nurseryBatchesAdapter = new NurseryBatchesAdapter(nurseryBatchDetails, AddViewBatchDetails.this,dbData);
                batchDetailsBinding.batchRecycler.setAdapter(nurseryBatchesAdapter);
            }
        }
    }

    public void UploadData(JSONObject savePMAYDataSet, String batch_primary_id_) {
        batch_primary_id=batch_primary_id_;
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), savePMAYDataSet.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveBatch", Api.Method.POST, UrlGenerator.getNurseryGardenService(), dataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("saveBatch", "" + dataSet);
    }
    public void UploadDeadSapling(JSONObject savePMAYDataSet, String batch_primary_id_) {
        batch_primary_id=batch_primary_id_;
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), savePMAYDataSet.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveDeadSapling", Api.Method.POST, UrlGenerator.getNurseryGardenService(), dataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("saveDeadSapling", "" + dataSet);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

}
