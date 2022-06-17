package com.nic.nurserygarden.activity.Expenditure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.NewPendingScreen;
import com.nic.nurserygarden.adapter.CapitalExpenditureViewAdapter;
import com.nic.nurserygarden.adapter.ExpenditureUploadAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.databinding.ActivityExpenditureViewBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExpenditureView extends AppCompatActivity implements Api.ServerResponseListener{
    ActivityExpenditureViewBinding activityExpenditureViewBinding;
    private PrefManager prefManager;
    /*final String capital_expenditure_view = "capital_expenditure_view";
    final String recurring_expenditure_view = "recurring_expenditure_view";*/
    ArrayList<NurserySurvey> capitalExpenditureList;
    ArrayList<NurserySurvey> recurringExpenditureList;
    CapitalExpenditureViewAdapter capitalExpenditureViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityExpenditureViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_expenditure_view);
        activityExpenditureViewBinding.setActivity(this);
        prefManager  = new PrefManager(this);

        activityExpenditureViewBinding.capitalRecycler.setVisibility(View.GONE);
        activityExpenditureViewBinding.recurringRecycler.setVisibility(View.GONE);
        activityExpenditureViewBinding.notFoundTv.setVisibility(View.GONE);

        activityExpenditureViewBinding.capitalRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityExpenditureViewBinding.recurringRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityExpenditureViewBinding.capitalExpenditureRl.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_side_curve_bg));
        activityExpenditureViewBinding.recurringExpenditureRl.setBackgroundDrawable(null);
        get_capital_expenditure_list();
        activityExpenditureViewBinding.capitalExpenditureRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityExpenditureViewBinding.capitalExpenditureRl.setBackgroundDrawable(getResources().getDrawable(R.drawable.left_side_curve_bg));
                activityExpenditureViewBinding.recurringExpenditureRl.setBackgroundDrawable(null);
                get_capital_expenditure_list();
            }
        });
        activityExpenditureViewBinding.recurringExpenditureRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityExpenditureViewBinding.recurringExpenditureRl.setBackgroundDrawable(getResources().getDrawable(R.drawable.right_side_curve_bg));
                activityExpenditureViewBinding.capitalExpenditureRl.setBackgroundDrawable(null);
                get_recurring_expenditure_list();
            }
        });
    }

    public void get_capital_expenditure_list() {
        try {
            new ApiService(this).makeJSONObjectRequest("capital_expenditure", Api.Method.POST, UrlGenerator.getNurseryGardenService(), capital_expenditure_encrypt_json(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void get_recurring_expenditure_list() {
        try {
            new ApiService(this).makeJSONObjectRequest("recurring_expenditure", Api.Method.POST, UrlGenerator.getNurseryGardenService(), recurring_expenditure_encrypt_json(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject capital_expenditure_encrypt_json() throws JSONException {
        String authKey = "";
        authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.capital_expenditure_view_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("expenditure_list", "" + authKey);
        return dataSet;
    }
    public JSONObject recurring_expenditure_encrypt_json() throws JSONException {
        String authKey = "";
        authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.recurring_expenditure_view_JsonParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("expenditure_list", "" + authKey);
        return dataSet;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            if ("capital_expenditure".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new fetch_capital_expenditure().execute(jsonObject);
                }
                else {
                    Utils.showAlert(ExpenditureView.this,jsonObject.getString("MESSAGE"));
                    activityExpenditureViewBinding.capitalRecycler.setVisibility(View.GONE);
                    activityExpenditureViewBinding.recurringRecycler.setVisibility(View.GONE);
                    activityExpenditureViewBinding.notFoundTv.setVisibility(View.VISIBLE);
                }
                Log.d("capital_expenditure", "" + responseDecryptedBlockKey);
            }
            if ("recurring_expenditure".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new fetch_recurring_expenditure().execute(jsonObject);
                }
                else {
                    Utils.showAlert(ExpenditureView.this,jsonObject.getString("MESSAGE"));
                    activityExpenditureViewBinding.capitalRecycler.setVisibility(View.GONE);
                    activityExpenditureViewBinding.recurringRecycler.setVisibility(View.GONE);
                    activityExpenditureViewBinding.notFoundTv.setVisibility(View.VISIBLE);
                }
                Log.d("recurring_expenditure", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
    public class fetch_capital_expenditure extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                capitalExpenditureList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setFin_year(jsonArray.getJSONObject(i).getString("fin_year"));
                        nurserySurvey.setEntry_date(jsonArray.getJSONObject(i).getString("entry_date"));
                        nurserySurvey.setExpenditure_type_id(jsonArray.getJSONObject(i).getInt("expenditure_type_id"));
                        nurserySurvey.setExpense_category_id(jsonArray.getJSONObject(i).getInt("expense_category_id"));
                        nurserySurvey.setExpenditure_unit_id(jsonArray.getJSONObject(i).getInt("expenditure_unit_id"));
                        nurserySurvey.setExpenditure_fund_src_id(jsonArray.getJSONObject(i).getInt("expenditure_fund_src_id"));
                        nurserySurvey.setQuantity(jsonArray.getJSONObject(i).getString("quantity"));
                        nurserySurvey.setAmount(jsonArray.getJSONObject(i).getString("amount"));
                        nurserySurvey.setExpenditure_type(jsonArray.getJSONObject(i).getString("expenditure_type"));
                        nurserySurvey.setExpense_category_en(jsonArray.getJSONObject(i).getString("expense_category_en"));
                        nurserySurvey.setExpense_category_ta(jsonArray.getJSONObject(i).getString("expense_category_ta"));
                        nurserySurvey.setExpenditure_unit_en(jsonArray.getJSONObject(i).getString("expenditure_unit_en"));
                        nurserySurvey.setExpenditure_unit_ta(jsonArray.getJSONObject(i).getString("expenditure_unit_ta"));
                        nurserySurvey.setExpenditure_fund_src_en(jsonArray.getJSONObject(i).getString("expenditure_fund_src_en"));
                        nurserySurvey.setExpenditure_fund_src_ta(jsonArray.getJSONObject(i).getString("expenditure_fund_src_ta"));
                        capitalExpenditureList.add(nurserySurvey);
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
            if(capitalExpenditureList.size()>0) {
                activityExpenditureViewBinding.capitalRecycler.setVisibility(View.VISIBLE);
                activityExpenditureViewBinding.recurringRecycler.setVisibility(View.GONE);
                activityExpenditureViewBinding.notFoundTv.setVisibility(View.GONE);
                capitalExpenditureViewAdapter = new CapitalExpenditureViewAdapter(ExpenditureView.this, capitalExpenditureList);
                activityExpenditureViewBinding.capitalRecycler.setAdapter(capitalExpenditureViewAdapter);
            }
            else {
                activityExpenditureViewBinding.capitalRecycler.setVisibility(View.GONE);
                activityExpenditureViewBinding.recurringRecycler.setVisibility(View.GONE);
                activityExpenditureViewBinding.notFoundTv.setVisibility(View.VISIBLE);
            }
        }
    }
    public class fetch_recurring_expenditure extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recurringExpenditureList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setFin_year(jsonArray.getJSONObject(i).getString("fin_year"));
                        nurserySurvey.setEntry_date(jsonArray.getJSONObject(i).getString("entry_date"));
                        nurserySurvey.setExpenditure_type_id(jsonArray.getJSONObject(i).getInt("expenditure_type_id"));
                        nurserySurvey.setExpense_category_id(jsonArray.getJSONObject(i).getInt("expense_category_id"));
                        nurserySurvey.setExpenditure_unit_id(jsonArray.getJSONObject(i).getInt("expenditure_unit_id"));
                        nurserySurvey.setExpenditure_fund_src_id(jsonArray.getJSONObject(i).getInt("expenditure_fund_src_id"));
                        nurserySurvey.setQuantity(jsonArray.getJSONObject(i).getString("quantity"));
                        nurserySurvey.setAmount(jsonArray.getJSONObject(i).getString("amount"));
                        nurserySurvey.setExpenditure_type(jsonArray.getJSONObject(i).getString("expenditure_type"));
                        nurserySurvey.setExpense_category_en(jsonArray.getJSONObject(i).getString("expense_category_en"));
                        nurserySurvey.setExpense_category_ta(jsonArray.getJSONObject(i).getString("expense_category_ta"));
                        nurserySurvey.setExpenditure_unit_en(jsonArray.getJSONObject(i).getString("expenditure_unit_en"));
                        nurserySurvey.setExpenditure_unit_ta(jsonArray.getJSONObject(i).getString("expenditure_unit_ta"));
                        nurserySurvey.setExpenditure_fund_src_en(jsonArray.getJSONObject(i).getString("expenditure_fund_src_en"));
                        nurserySurvey.setExpenditure_fund_src_ta(jsonArray.getJSONObject(i).getString("expenditure_fund_src_ta"));
                        recurringExpenditureList.add(nurserySurvey);
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
            if(recurringExpenditureList.size()>0) {
                activityExpenditureViewBinding.recurringRecycler.setVisibility(View.VISIBLE);
                activityExpenditureViewBinding.capitalRecycler.setVisibility(View.GONE);
                activityExpenditureViewBinding.notFoundTv.setVisibility(View.GONE);
                capitalExpenditureViewAdapter = new CapitalExpenditureViewAdapter(ExpenditureView.this, recurringExpenditureList);
                activityExpenditureViewBinding.recurringRecycler.setAdapter(capitalExpenditureViewAdapter);
            }
            else {
                activityExpenditureViewBinding.capitalRecycler.setVisibility(View.GONE);
                activityExpenditureViewBinding.recurringRecycler.setVisibility(View.GONE);
                activityExpenditureViewBinding.notFoundTv.setVisibility(View.VISIBLE);
            }
        }
    }
}
