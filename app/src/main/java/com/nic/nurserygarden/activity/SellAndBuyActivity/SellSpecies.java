package com.nic.nurserygarden.activity.SellAndBuyActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.DeadSaplingActivty.DeadSaplingEntry;
import com.nic.nurserygarden.activity.LoginScreen;
import com.nic.nurserygarden.adapter.CommonAdapter;
import com.nic.nurserygarden.adapter.SellSpeciesAdapter;
import com.nic.nurserygarden.adapter.SpeciesListAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;


import com.nic.nurserygarden.databinding.ActivitySellSpeciesBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SellSpecies extends AppCompatActivity implements Api.ServerResponseListener{
    public ActivitySellSpeciesBinding activitySellSpeciesBinding;
    private PrefManager prefManager;

    ArrayList<NurserySurvey> nurseryBuyerTypeList;
    ArrayList<NurserySurvey> speciesCountList;
    ArrayList<NurserySurvey> villageList;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    int buyer_type_id=0;
    String nursery_level_id="";
    String pv_code="";
    boolean village_layout_visible=false;
    boolean mobile_number_valid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusBarGradiant(SellSpecies.this);
        activitySellSpeciesBinding = DataBindingUtil.setContentView(this, R.layout.activity_sell_species);
        activitySellSpeciesBinding.setActivity(this);


        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        nursery_level_id = getIntent().getStringExtra("nursery_level_id");
        prefManager = new PrefManager(this);

        if(Utils.isOnline()){
            get_nursery_buyer_type();
        }
        else {
            Utils.showAlert(SellSpecies.this,getResources().getString(R.string.no_internet));
        }

        activitySellSpeciesBinding.buyerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    buyer_type_id = nurseryBuyerTypeList.get(position).getBuyer_type_id();
                }
                else {
                    buyer_type_id=0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activitySellSpeciesBinding.villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    pv_code = villageList.get(position).getPvCode();
                }
                else {
                    pv_code="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activitySellSpeciesBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        if(nursery_level_id.equals("1")){
            village_layout_visible = true;
            loadVillageList();
            activitySellSpeciesBinding.villageNameLayout.setVisibility(View.VISIBLE);
        }
        else {
            village_layout_visible = false;
            activitySellSpeciesBinding.villageNameLayout.setVisibility(View.GONE);

        }

        activitySellSpeciesBinding.mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mobile_number_valid = !activitySellSpeciesBinding.mobileNumber.getText().toString().equals("");
            }
        });
    }



    public void get_nursery_buyer_type() {
        try {
            new ApiService(this).makeJSONObjectRequest("nursery_buyer_type", Api.Method.POST, UrlGenerator.getNurseryGardenService(), nursery_buyer_type_JsonParams_JsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject nursery_buyer_type_JsonParams_JsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.nursery_buyer_type_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("nursery_buyer_type", "" + authKey);
        return dataSet;
    }


    public void loadVillageList(){
        dbData.open();
        villageList = new ArrayList<>();
        NurserySurvey nurserySurvey = new NurserySurvey();
        nurserySurvey.setPvCode("0");
        nurserySurvey.setPvName("Select Village");
        villageList.add(nurserySurvey);
        villageList.addAll(dbData.getAll_Village(prefManager.getDistrictCode(),prefManager.getBlockCode()));
        if(villageList.size()>0){
            activitySellSpeciesBinding.villageSpinner.setAdapter(new CommonAdapter(SellSpecies.this,villageList,"VillageList"));
        }
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("nursery_buyer_type".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_nursery_buyer_type().execute(jsonObject);
                }
                Log.d("nursery_buyer_type", "" + responseDecryptedBlockKey);
            }
            if ("searchDetails".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                }
                else {
                    Toasty.error(SellSpecies.this,jsonObject.getString("MESSAGE"),Toast.LENGTH_SHORT);
                }
                Log.d("nursery_buyer_type", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnError(VolleyError volleyError) {

    }
    @SuppressLint("StaticFieldLeak")
    public class Insert_nursery_buyer_type extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            nurseryBuyerTypeList = new ArrayList<>();
            NurserySurvey nurserySurvey = new NurserySurvey();
            nurserySurvey.setBuyer_type_id(0);
            nurserySurvey.setBuyer_type_name_en("Select Buyer Type");
            nurserySurvey.setBuyer_type_name_ta("Select Buyer Type");
            nurseryBuyerTypeList.add(nurserySurvey);
            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey buyerType = new NurserySurvey();
                        buyerType.setBuyer_type_id(jsonArray.getJSONObject(i).getInt("buyer_type_id"));
                        buyerType.setBuyer_type_name_en(jsonArray.getJSONObject(i).getString("buyer_type_name_en"));
                        buyerType.setBuyer_type_name_ta(jsonArray.getJSONObject(i).getString("buyer_type_name_ta"));
                        nurseryBuyerTypeList.add(buyerType);
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
            if(nurseryBuyerTypeList.size()>0){
                activitySellSpeciesBinding.buyerSpinner.setAdapter(new CommonAdapter(SellSpecies.this,nurseryBuyerTypeList,"nurseryBuyerTypeList"));
            }
        }
    }



    public void validation(){
        if(village_layout_visible){
            if(!pv_code.equals("")){
                if(buyer_type_id>0){
                    if(!activitySellSpeciesBinding.buyerName.getText().toString().equals("")){
                        if(mobile_number_valid){
                            if(Utils.isValidMobile1(activitySellSpeciesBinding.mobileNumber.getText().toString())){
                                gotoOrderItemActivity();
                            }
                            else {
                                Utils.showAlert(SellSpecies.this,"Please Enter Valid Mobile Number");
                            }
                        }
                        else {
                            gotoOrderItemActivity();
                        }

                    }
                    else {
                        Utils.showAlert(SellSpecies.this,"Please Enter Name");
                    }
                }
                else {
                    Utils.showAlert(SellSpecies.this,"Please Choose Buyer Type");
                }
            }
            else {
                Utils.showAlert(SellSpecies.this,"Please Select Village");
            }

        }
        else {
            if(buyer_type_id>0){
                if(!activitySellSpeciesBinding.buyerName.getText().toString().equals("")){
                    gotoOrderItemActivity();
                }
                else {
                    Utils.showAlert(SellSpecies.this,"Please Enter Name");
                }
            }
            else {
                Utils.showAlert(SellSpecies.this,"Please Choose Buyer Type");
            }
        }

    }

    private void gotoOrderItemActivity(){
        Intent orderItemActivity = new Intent(SellSpecies.this,OrderItemActivity.class);
        orderItemActivity.putExtra("pv_code",pv_code);
        orderItemActivity.putExtra("buyer_type_id",buyer_type_id);
        orderItemActivity.putExtra("buyer_name",activitySellSpeciesBinding.buyerName.getText().toString());
        orderItemActivity.putExtra("buyer_mobile",activitySellSpeciesBinding.mobileNumber.getText().toString());
        orderItemActivity.putExtra("buyer_address",activitySellSpeciesBinding.address.getText().toString());
        startActivity(orderItemActivity);
        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
    }



    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
