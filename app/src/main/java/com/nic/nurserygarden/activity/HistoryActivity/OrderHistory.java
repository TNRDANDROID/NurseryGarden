package com.nic.nurserygarden.activity.HistoryActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.Interface.DateInterface;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchDetails;
import com.nic.nurserygarden.adapter.HistoryDetailsViewAdapter;
import com.nic.nurserygarden.adapter.HistoryViewAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityOrderHistoryBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static com.nic.nurserygarden.utils.Utils.getCurrentDate;

public class OrderHistory extends AppCompatActivity implements Api.ServerResponseListener, DateInterface {
    ActivityOrderHistoryBinding orderHistoryBinding;
    PrefManager prefManager;

    String fromDate,toDate;
    ArrayList<NurserySurvey> orderBasicList;
    ArrayList<NurserySurvey> orderDetailsList;
    ArrayList<NurserySurvey> deliveryBasicList;
    ArrayList<NurserySurvey> deliveryDetailsList;
    HistoryViewAdapter historyViewAdapter;
    HistoryDetailsViewAdapter historyDetailsViewAdapter;

    String activity_type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_history);
        orderHistoryBinding.setActivity(this);
        prefManager = new PrefManager(this);
        orderHistoryBinding.dateOfSave.setText(getCurrentDate()+" to "+getCurrentDate());

        activity_type = getIntent().getStringExtra("Activity");
        if(activity_type.equals("Order")){
            orderHistoryBinding.title.setText("Order History");

        }
        else {
            orderHistoryBinding.title.setText("Delivery History");
        }
        if(Utils.isOnline()){
            get_details_of_order_history_view("Today");
        }
        else {
            onBackPressed();
        }

        orderHistoryBinding.dateOfCommencementLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showDatePickerDialog(OrderHistory.this);
            }
        });

        orderHistoryBinding.detailsRecycler.setVisibility(View.GONE);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(OrderHistory.this,DividerItemDecoration.VERTICAL);
        orderHistoryBinding.orderBasicRecycler.addItemDecoration(itemDecoration);
        orderHistoryBinding.detailsRecycler.addItemDecoration(itemDecoration);
        orderHistoryBinding.orderBasicRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orderHistoryBinding.detailsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void get_details_of_order_history_view(String type) {
        try {
            new ApiService(this).makeJSONObjectRequest("saplings_order_history", Api.Method.POST, UrlGenerator.getNurseryGardenService(), details_of_order_history_viewParams(type), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject details_of_order_history_viewParams(String type) throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), details_of_order_history_viewJsonParams(type).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("JSONRequest", "" + authKey);
        return dataSet;
    }

    private JSONObject details_of_order_history_viewJsonParams(String type) {
        JSONObject dataSet = new JSONObject();
        try {
            if(activity_type.equals("Order")){
                dataSet.put(AppConstant.KEY_SERVICE_ID, "saplings_order_history");
            }
            else {
                dataSet.put(AppConstant.KEY_SERVICE_ID, "saplings_delivery_history");
            }

            if(type.equals("Today")){
                dataSet.put("from_date",getCurrentDate());
                dataSet.put("to_date",getCurrentDate());
            }
            else {
                //dataSet.put("date",activity_view_waste_collected_details.dateOfSave.getText().toString());
                dataSet.put("from_date",fromDate);
                dataSet.put("to_date",toDate);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d("JSONRequest",dataSet.toString());
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("saplings_order_history".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    if(activity_type.equals("Order")){
                        new Insert_nursery_order_history_details().execute(jsonObject);
                    }
                    else {
                        new Insert_nursery_delivery_history_details().execute(jsonObject);
                    }
                }
                else {
                    orderHistoryBinding.orderBasicRecycler.setVisibility(View.GONE);
                    orderHistoryBinding.detailsRecycler.setVisibility(View.GONE);
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();


                }
                Log.d("saplings_order_history", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    public void getDate(String dateValue) {
        String[] separated = dateValue.split(":");
        fromDate = separated[0]; // this will contain "Fruit"
        toDate = separated[1];
        orderHistoryBinding.dateOfSave.setText(fromDate+" to "+toDate);
//        getInCompleteActivityList();
        if(Utils.isOnline()) {
            get_details_of_order_history_view("");
        }
        else {
            Utils.showAlert(OrderHistory.this,getResources().getString(R.string.no_internet));
        }
    }

    public class Insert_nursery_order_history_details extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                orderBasicList = new ArrayList<>();
                orderDetailsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setSapling_order_id(jsonArray.getJSONObject(i).getInt("sapling_order_id"));
                        nurserySurvey.setNursery_id(jsonArray.getJSONObject(i).getInt("nursery_id"));
                        nurserySurvey.setBuyer_id(jsonArray.getJSONObject(i).getInt("buyer_id"));
                        nurserySurvey.setOrder_date(jsonArray.getJSONObject(i).getString("order_date"));
                        nurserySurvey.setBuyer_type_id(jsonArray.getJSONObject(i).getInt("buyer_type_id"));
                        nurserySurvey.setMgnregs_id(jsonArray.getJSONObject(i).getString("mgnregs_id"));
                        nurserySurvey.setBuyer_name(jsonArray.getJSONObject(i).getString("buyer_name"));

                        orderBasicList.add(nurserySurvey);

                        jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("order_details");
                        for(int j=0;j<jsonArray1.length();j++){
                            NurserySurvey nurserySurvey1 = new NurserySurvey();

                            nurserySurvey1.setSapling_order_id(jsonArray.getJSONObject(i).getInt("sapling_order_id"));

                            nurserySurvey1.setSapling_order_item_id(jsonArray1.getJSONObject(j).getInt("sapling_order_item_id"));
                            nurserySurvey1.setSpecies_name_en(jsonArray1.getJSONObject(j).getString("species_name_en"));
                            nurserySurvey1.setSpecies_name_ta(jsonArray1.getJSONObject(j).getString("species_name_ta"));
                            nurserySurvey1.setNo_of_saplings(jsonArray1.getJSONObject(j).getInt("qty"));
                            nurserySurvey1.setHeight_in_cm(jsonArray1.getJSONObject(j).getString("sapling_height_in_cm"));
                            nurserySurvey1.setAge_in_days(jsonArray1.getJSONObject(j).getInt("sapling_age_in_days"));

                            orderDetailsList.add(nurserySurvey1);

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
                if(orderBasicList.size()>0){
                    orderHistoryBinding.orderBasicRecycler.setVisibility(View.VISIBLE);
                    historyViewAdapter = new HistoryViewAdapter(orderBasicList,orderDetailsList,OrderHistory.this,"","Order");
                    orderHistoryBinding.orderBasicRecycler.setAdapter(historyViewAdapter);
                }
                else {
                    orderHistoryBinding.orderBasicRecycler.setVisibility(View.GONE);
                }
        }
    }
    public class Insert_nursery_delivery_history_details extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                deliveryBasicList = new ArrayList<>();
                deliveryDetailsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurserySurvey = new NurserySurvey();
                        nurserySurvey.setSapling_order_id(jsonArray.getJSONObject(i).getInt("sapling_order_id"));
                        nurserySurvey.setNursery_id(jsonArray.getJSONObject(i).getInt("nursery_id"));
                        nurserySurvey.setBuyer_id(jsonArray.getJSONObject(i).getInt("buyer_id"));
                        nurserySurvey.setOrder_date(jsonArray.getJSONObject(i).getString("order_date"));
                        nurserySurvey.setBuyer_type_id(jsonArray.getJSONObject(i).getInt("buyer_type_id"));
                        nurserySurvey.setMgnregs_id(jsonArray.getJSONObject(i).getString("mgnregs_id"));
                        nurserySurvey.setBuyer_name(jsonArray.getJSONObject(i).getString("buyer_name"));

                        deliveryBasicList.add(nurserySurvey);

                        jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("delivery_details");
                        for(int j=0;j<jsonArray1.length();j++){
                            NurserySurvey nurserySurvey1 = new NurserySurvey();

                            nurserySurvey1.setSapling_order_id(jsonArray.getJSONObject(i).getInt("sapling_order_id"));

                            nurserySurvey1.setSapling_item_delivery_id(jsonArray1.getJSONObject(j).getInt("sapling_item_delivery_id"));
                            nurserySurvey1.setSpecies_name_en(jsonArray1.getJSONObject(j).getString("species_name_en"));
                            nurserySurvey1.setSpecies_name_ta(jsonArray1.getJSONObject(j).getString("species_name_ta"));
                            nurserySurvey1.setNo_of_saplings(jsonArray1.getJSONObject(j).getInt("delivery_qty"));
                            nurserySurvey1.setHeight_in_cm(jsonArray1.getJSONObject(j).getString("on_delivery_sapling_height_in_cm"));
                            nurserySurvey1.setAge_in_days(jsonArray1.getJSONObject(j).getInt("on_delivery_sapling_age_in_days"));

                            deliveryDetailsList.add(nurserySurvey1);

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
                if(deliveryBasicList.size()>0){
                    orderHistoryBinding.orderBasicRecycler.setVisibility(View.VISIBLE);
                    historyViewAdapter = new HistoryViewAdapter(deliveryBasicList,deliveryDetailsList,OrderHistory.this,"",activity_type);
                    orderHistoryBinding.orderBasicRecycler.setAdapter(historyViewAdapter);
                }
                else {
                    orderHistoryBinding.orderBasicRecycler.setVisibility(View.GONE);
                }
        }
    }



    public void showDetailsView(int pos){

        if(activity_type.equals("Delivery")) {
            int sapling_order_id = deliveryBasicList.get(pos).getSapling_order_id();
            ArrayList<NurserySurvey> filterList = new ArrayList<>();
            for (int i = 0; i < deliveryDetailsList.size(); i++) {
                if (sapling_order_id == deliveryDetailsList.get(i).getSapling_order_id()) {
                    NurserySurvey nurserySurvey1 = new NurserySurvey();
                    nurserySurvey1.setSapling_order_id(deliveryDetailsList.get(i).getSapling_order_id());

                    nurserySurvey1.setSapling_item_delivery_id(deliveryDetailsList.get(i).getSapling_item_delivery_id());
                    nurserySurvey1.setSpecies_name_en(deliveryDetailsList.get(i).getSpecies_name_en());
                    nurserySurvey1.setSpecies_name_ta(deliveryDetailsList.get(i).getSpecies_name_ta());
                    nurserySurvey1.setNo_of_saplings(deliveryDetailsList.get(i).getNo_of_saplings());
                    nurserySurvey1.setHeight_in_cm(deliveryDetailsList.get(i).getHeight_in_cm());
                    nurserySurvey1.setAge_in_days(deliveryDetailsList.get(i).getAge_in_days());

                    filterList.add(nurserySurvey1);

                }
            }
            if (filterList.size() > 0) {
                orderHistoryBinding.detailsRecycler.setVisibility(View.VISIBLE);
                orderHistoryBinding.orderBasicRecycler.setVisibility(View.GONE);
                historyDetailsViewAdapter = new HistoryDetailsViewAdapter(filterList, OrderHistory.this, "", activity_type);
                orderHistoryBinding.detailsRecycler.setAdapter(historyDetailsViewAdapter);
            } else {
                orderHistoryBinding.detailsRecycler.setVisibility(View.GONE);
                orderHistoryBinding.orderBasicRecycler.setVisibility(View.VISIBLE);
            }
        }
        else {
            int sapling_order_id = orderBasicList.get(pos).getSapling_order_id();
            ArrayList<NurserySurvey> filterList = new ArrayList<>();
            for (int i = 0; i < orderDetailsList.size(); i++) {
                if (sapling_order_id == orderDetailsList.get(i).getSapling_order_id()) {
                    NurserySurvey nurserySurvey1 = new NurserySurvey();
                    nurserySurvey1.setSapling_order_id(orderDetailsList.get(i).getSapling_order_id());

                    nurserySurvey1.setSapling_order_item_id(orderDetailsList.get(i).getSapling_order_item_id());
                    nurserySurvey1.setSpecies_name_en(orderDetailsList.get(i).getSpecies_name_en());
                    nurserySurvey1.setSpecies_name_ta(orderDetailsList.get(i).getSpecies_name_ta());
                    nurserySurvey1.setNo_of_saplings(orderDetailsList.get(i).getNo_of_saplings());
                    nurserySurvey1.setHeight_in_cm(orderDetailsList.get(i).getHeight_in_cm());
                    nurserySurvey1.setAge_in_days(orderDetailsList.get(i).getAge_in_days());

                    filterList.add(nurserySurvey1);

                }
            }
            if (filterList.size() > 0) {
                orderHistoryBinding.detailsRecycler.setVisibility(View.VISIBLE);
                orderHistoryBinding.orderBasicRecycler.setVisibility(View.GONE);
                historyDetailsViewAdapter = new HistoryDetailsViewAdapter(filterList, OrderHistory.this, "", activity_type);
                orderHistoryBinding.detailsRecycler.setAdapter(historyDetailsViewAdapter);
            } else {
                orderHistoryBinding.detailsRecycler.setVisibility(View.GONE);
                orderHistoryBinding.orderBasicRecycler.setVisibility(View.VISIBLE);
            }
        }
    }
    public void onBackPress() {
        if(orderHistoryBinding.detailsRecycler.getVisibility()==View.VISIBLE){
            orderHistoryBinding.detailsRecycler.setVisibility(View.GONE);
            orderHistoryBinding.orderBasicRecycler.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
            setResult(Activity.RESULT_CANCELED);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }

    }

}
