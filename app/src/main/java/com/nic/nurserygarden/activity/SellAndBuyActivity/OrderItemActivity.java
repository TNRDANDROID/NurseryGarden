package com.nic.nurserygarden.activity.SellAndBuyActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.util.Util;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.adapter.CommonAdapter;
import com.nic.nurserygarden.adapter.FilterItemsAdapter;
import com.nic.nurserygarden.adapter.SellSpeciesAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.databinding.ActivityOrderItemBinding;
import com.nic.nurserygarden.databinding.ActivitySellSpeciesBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class OrderItemActivity extends AppCompatActivity implements Api.ServerResponseListener{
    public ActivityOrderItemBinding orderItemBinding;
    private PrefManager prefManager;

    int buyer_type_id=0;
    String buyer_name="";
    String buyer_mobile="";
    String buyer_address="";
    String pv_code="";

    SellSpeciesAdapter sellSpeciesAdapter;
    ArrayList<NurserySurvey> nurseryOrderItemList;
    ArrayList<NurserySurvey> nurseryOrderItemFilterList;
    ArrayList<NurserySurvey> syncOrderItemList;

    ArrayList<NurserySurvey> batchNumberItemList;
    ArrayList<NurserySurvey> speciesNameItemList;
    ArrayList<NurserySurvey> daysItemList;
    ArrayList<NurserySurvey> heightItemList;
    FilterItemsAdapter batchNumberAdapter;
    FilterItemsAdapter speciesNameAdapter;
    FilterItemsAdapter daysNameAdapter;
    FilterItemsAdapter heightAdapter;

    int batch_id=0;
    int species_type_id=0;
    int age = 0;
    String height = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setStatusBarGradiant(OrderItemActivity.this);
        orderItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_item);
        orderItemBinding.setActivity(this);
        prefManager = new PrefManager(this);

        getIntentData();
        orderItemBinding.mainLayout.setVisibility(View.VISIBLE);
        orderItemBinding.filterLayout.setVisibility(View.GONE);
        orderItemBinding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderItemBinding.mainLayout.setVisibility(View.GONE);
                orderItemBinding.filterLayout.setVisibility(View.VISIBLE);
            }
        });
        orderItemBinding.filterSubmitIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterOrderItemList(nurseryOrderItemList);
                orderItemBinding.mainLayout.setVisibility(View.VISIBLE);
                orderItemBinding.filterLayout.setVisibility(View.GONE);
            }
        });
        orderItemBinding.closeFilterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderItemBinding.mainLayout.setVisibility(View.VISIBLE);
                orderItemBinding.filterLayout.setVisibility(View.GONE);
            }
        });

        orderItemBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isOnline()){
                    saveAndDelivery();
                }
                else {
                    Utils.showAlert(OrderItemActivity.this,getResources().getString(R.string.no_internet));
                }

            }
        });
    }

    private void getIntentData(){
        orderItemBinding.orderItemRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        orderItemBinding.batchNameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true));
        orderItemBinding.speciesNameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true));
        orderItemBinding.daysNameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true));
        orderItemBinding.heightNameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true));

        buyer_type_id = getIntent().getIntExtra("buyer_type_id",0);
        pv_code = getIntent().getStringExtra("pv_code");
        buyer_name = getIntent().getStringExtra("buyer_name");
        buyer_mobile = getIntent().getStringExtra("buyer_mobile");
        buyer_address = getIntent().getStringExtra("buyer_address");

        if(Utils.isOnline()){
            sendJsonObjectForSearch();
        }
        else {
            Utils.showAlert(OrderItemActivity.this,getResources().getString(R.string.no_internet));
        }
    }

    private void setAdapterForBatchNumber(ArrayList<NurserySurvey> nurseryOrderItemList){
        batchNumberItemList = new ArrayList<>();
        batchNumberItemList.clear();
        batchNumberItemList.addAll(nurseryOrderItemList);
        for(int i=0;i<batchNumberItemList.size();i++){
            for(int j=i+1;j<batchNumberItemList.size();j++){
                if(batchNumberItemList.get(i).getBatch_number()==(batchNumberItemList.get(j).getBatch_number())){
                    batchNumberItemList.remove(j);
                    j--;
                }
            }
        }
        if(batchNumberItemList.size()>0){
            batchNumberAdapter = new FilterItemsAdapter(batchNumberItemList,OrderItemActivity.this,"Batch_Number");
            orderItemBinding.batchNameRecycler.setAdapter(batchNumberAdapter);
        }
    }
    private void setAdapterForSpeciesName(ArrayList<NurserySurvey> nurseryOrderItemList){
        speciesNameItemList = new ArrayList<>();
        speciesNameItemList.clear();
        speciesNameItemList.addAll(nurseryOrderItemList);
        for(int i=0;i<speciesNameItemList.size();i++){
            for(int j=i+1;j<speciesNameItemList.size();j++){
                if(speciesNameItemList.get(i).getSpecies_type_id()==(speciesNameItemList.get(j).getSpecies_type_id())){
                    speciesNameItemList.remove(j);
                    j--;
                }
            }
        }
        if(speciesNameItemList.size()>0){
            speciesNameAdapter = new FilterItemsAdapter(speciesNameItemList,OrderItemActivity.this,"Species_Name");
            orderItemBinding.speciesNameRecycler.setAdapter(speciesNameAdapter);
        }
    }
    private void setAdapterForDays(ArrayList<NurserySurvey> nurseryOrderItemList){
        daysItemList = new ArrayList<>();
        daysItemList.clear();
        daysItemList.addAll(nurseryOrderItemList);
        for(int i=0;i<daysItemList.size();i++){
            for(int j=i+1;j<daysItemList.size();j++){
                if(daysItemList.get(i).getAge_in_days()==(daysItemList.get(j).getAge_in_days())){
                    daysItemList.remove(j);
                    j--;
                }
            }
        }
        if(daysItemList.size()>0){
            daysNameAdapter = new FilterItemsAdapter(daysItemList,OrderItemActivity.this,"Days");
            orderItemBinding.daysNameRecycler.setAdapter(daysNameAdapter);
        }
    }
    private void setAdapterForHeight(ArrayList<NurserySurvey> nurseryOrderItemList){
        heightItemList = new ArrayList<>();
        heightItemList.clear();
        heightItemList.addAll(nurseryOrderItemList);
        for(int i=0;i<heightItemList.size();i++){
            for(int j=i+1;j<heightItemList.size();j++){
                if(heightItemList.get(i).getHeight_in_cm().equals(heightItemList.get(j).getHeight_in_cm())){
                    heightItemList.remove(j);
                    j--;
                }
            }
        }
        if(heightItemList.size()>0){
            heightAdapter = new FilterItemsAdapter(heightItemList,OrderItemActivity.this,"Height");
            orderItemBinding.heightNameRecycler.setAdapter(heightAdapter);
        }
    }

    private void sendJsonObjectForSearch(){
        JSONObject data_set = new JSONObject();

        try {
            data_set.put("service_id","nursery_order_items_list");
            Log.d("normal_json",""+data_set);

            String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), data_set.toString());
            JSONObject dataSet = new JSONObject();
            try {
                dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
                dataSet.put(AppConstant.DATA_CONTENT, authKey);
                new ApiService(this).makeJSONObjectRequest("searchDetails", Api.Method.POST, UrlGenerator.getNurseryGardenService(), dataSet, "not cache", this);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("searchDetails", "" + authKey);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("searchDetails".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                    new Insert_nursery_order_items().execute(jsonObject);

                }
                else {
                    Toasty.error(OrderItemActivity.this,jsonObject.getString("MESSAGE"), Toast.LENGTH_SHORT);
                }
                Log.d("searchDetails", "" + responseDecryptedBlockKey);
            }
            if ("nursery_sapling_buyer_order_save".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                    Utils.showAlert(OrderItemActivity.this,jsonObject.getString("MESSAGE"));
                    onBackPress();
                }
                else {
                    Toasty.error(OrderItemActivity.this,jsonObject.getString("MESSAGE"),Toast.LENGTH_SHORT);
                }
                Log.d("order_save", "" + responseDecryptedBlockKey);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Insert_nursery_order_items extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            nurseryOrderItemList = new ArrayList<>();
            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();

                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        NurserySurvey nurseryOrderItem = new NurserySurvey();

                        nurseryOrderItem.setNursery_id(jsonArray.getJSONObject(i).getInt("nursery_id"));
                        nurseryOrderItem.setBatch_id(jsonArray.getJSONObject(i).getInt("batch_id"));
                        nurseryOrderItem.setGrowth_tracking_id(jsonArray.getJSONObject(i).getInt("growth_tracking_id"));
                        nurseryOrderItem.setGrowth_tracking_details_id(jsonArray.getJSONObject(i).getInt("growth_tracking_details_id"));
                        nurseryOrderItem.setBatch_number(jsonArray.getJSONObject(i).getInt("batch_no"));
                        nurseryOrderItem.setSpecies_type_id(jsonArray.getJSONObject(i).getInt("species_type_id"));
                        nurseryOrderItem.setSpecies_name_en(jsonArray.getJSONObject(i).getString("species_name_en"));
                        nurseryOrderItem.setSpecies_name_ta(jsonArray.getJSONObject(i).getString("species_name_ta"));
                        nurseryOrderItem.setHeight_in_cm(jsonArray.getJSONObject(i).getString("sapling_height_in_cm"));
                        nurseryOrderItem.setAge_in_days(jsonArray.getJSONObject(i).getInt("sapling_age_in_days"));
                        nurseryOrderItem.setNo_of_saplings(jsonArray.getJSONObject(i).getInt("no_of_saplings"));

                        nurseryOrderItemList.add(nurseryOrderItem);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            setAdapterForBatchNumber(nurseryOrderItemList);
            setAdapterForSpeciesName(nurseryOrderItemList);
            setAdapterForDays(nurseryOrderItemList);
            setAdapterForHeight(nurseryOrderItemList);

            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(nurseryOrderItemList.size()>0){
                orderItemBinding.submitBtn.setVisibility(View.VISIBLE);
                nurseryOrderItemFilterList = new ArrayList<>();
                nurseryOrderItemFilterList.clear();
                nurseryOrderItemFilterList.addAll(nurseryOrderItemList);
                sellSpeciesAdapter = new SellSpeciesAdapter(nurseryOrderItemFilterList,OrderItemActivity.this,"");
                orderItemBinding.orderItemRecycler.setAdapter(sellSpeciesAdapter);
                sellSpeciesAdapter.notifyDataSetChanged();

            }
            else {
                orderItemBinding.submitBtn.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {
    }

    public void getBathNumberClickedItem(int position){
        batch_id = batchNumberItemList.get(position).getBatch_id();
    }
    public void getSpeciesClickedItem(int position){
        species_type_id = speciesNameItemList.get(position).getSpecies_type_id();
    }
    public void getAgesClickedItem(int position){
        age = daysItemList.get(position).getAge_in_days();
    }
    public void getHeightClickedItem(int position){
        height = heightItemList.get(position).getHeight_in_cm();
    }


    public void getFilterOrderItemList(ArrayList<NurserySurvey> listAllTasks){
        nurseryOrderItemFilterList.clear();
        for(NurserySurvey task_obj : listAllTasks)
        {
            String batch_id_ = String.valueOf(task_obj.getBatch_id());
            String species_type_id_ = String.valueOf(task_obj.getSpecies_type_id());
            String age_in_days_ = String.valueOf(task_obj.getAge_in_days());
            String height_in_cm = task_obj.getHeight_in_cm();

            if(batch_id_.equals(String.valueOf(batch_id)) || batch_id==0)
                if(species_type_id_.equals(String.valueOf(species_type_id)) || species_type_id==0)
                    if(age_in_days_.equals(String.valueOf(age)) || age==0)
                        if(height_in_cm.equals(height)||height.isEmpty())
                        if(batch_id!=0 || species_type_id!=0 || !height.isEmpty()){
                            nurseryOrderItemFilterList.add(task_obj);
                        }
        }

        if(nurseryOrderItemFilterList.size()>0){
            sellSpeciesAdapter = new SellSpeciesAdapter(nurseryOrderItemFilterList,OrderItemActivity.this,"");
            orderItemBinding.orderItemRecycler.setAdapter(sellSpeciesAdapter);
            sellSpeciesAdapter.notifyDataSetChanged();
        }

    }
    public void saveAndDelivery() {
        boolean validation_check = false;
        syncOrderItemList  = new ArrayList<>();
        EditText no_of_required_saplings_text;
        TextView available_count_text;

        String pvcode=pv_code;
        int buyer_type_id_=buyer_type_id;
        String buyer_name_=buyer_name;
        String buyer_mobile_no=buyer_mobile;
        String buyer_address_=buyer_address;

        String no_of_required_saplings="";
        int no_of_available_saplings=0;
        int childCount = sellSpeciesAdapter.getItemCount();
        try {
            if (childCount > 0) {
               for (int i = 0; i < nurseryOrderItemFilterList.size(); i++) {
                   RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) orderItemBinding.orderItemRecycler.findViewHolderForAdapterPosition(i);
                        if(null!=holder){
                            no_of_required_saplings_text = (EditText) holder.itemView.findViewById(R.id.quantity);
                            available_count_text = (TextView)holder.itemView.findViewById(R.id.available_count);
                            no_of_available_saplings = Integer.parseInt(available_count_text.getText().toString());
                            if(!no_of_required_saplings_text.getText().toString().equals("")){
                                no_of_required_saplings = no_of_required_saplings_text.getText().toString();
                            }
                            else {
                                no_of_required_saplings="";
                            }

                                if(!no_of_required_saplings.equals("")){
                                    if(no_of_available_saplings>=Integer.parseInt(no_of_required_saplings)){
                                        NurserySurvey sync_order_item = new NurserySurvey();
                                        sync_order_item.setPvCode(pvcode);
                                        sync_order_item.setBuyer_type_id(buyer_type_id_);
                                        sync_order_item.setBuyer_name(buyer_name_);
                                        sync_order_item.setBuyer_mobile_no(buyer_mobile_no);
                                        sync_order_item.setBuyer_address(buyer_address_);
                                        sync_order_item.setBatch_id(nurseryOrderItemFilterList.get(i).getBatch_id());
                                        sync_order_item.setGrowth_tracking_id(nurseryOrderItemFilterList.get(i).getGrowth_tracking_id());
                                        sync_order_item.setGrowth_tracking_details_id(nurseryOrderItemFilterList.get(i).getGrowth_tracking_details_id());
                                        sync_order_item.setSpecies_type_id(nurseryOrderItemFilterList.get(i).getSpecies_type_id());
                                        sync_order_item.setHeight_in_cm(nurseryOrderItemFilterList.get(i).getHeight_in_cm());
                                        sync_order_item.setAge_in_days(nurseryOrderItemFilterList.get(i).getAge_in_days());
                                        sync_order_item.setNo_of_saplings(nurseryOrderItemFilterList.get(i).getNo_of_saplings());
                                        sync_order_item.setNo_of_required_saplings(Integer.parseInt(no_of_required_saplings));

                                        syncOrderItemList.add(sync_order_item);
                                        validation_check = true;

                                    }
                                    else {
                                        validation_check = false;
                                        no_of_required_saplings_text.requestFocus();
                                        Utils.showAlert(OrderItemActivity.this,"Count MisMatched");


                                    }

                                }

                        }


                    }
               if(validation_check){
                   if(syncOrderItemList.size()>0){
                       getJsonObjectForDelivery(syncOrderItemList);
                   }
                   else {
                       Utils.showAlert(OrderItemActivity.this,"Enter the Required Saplings");
                   }

               }
               else {
                   Utils.showAlert(OrderItemActivity.this,"Count MisMatched");
               }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getJsonObjectForDelivery(ArrayList<NurserySurvey> syncOrderItemList){
        JSONObject data_set = new JSONObject();
        JSONArray nursery_sapling_order_details = new JSONArray();
        try {
            data_set.put("service_id","nursery_sapling_buyer_order_save");
            data_set.put("pvcode",pv_code);
            data_set.put("buyer_type_id",buyer_type_id);
            data_set.put("buyer_name",buyer_name);
            data_set.put("buyer_mobile_no",buyer_mobile);
            data_set.put("buyer_address",buyer_address);
            for(int i=0; i<syncOrderItemList.size();i++){
                JSONObject data_set1= new JSONObject();
                data_set1.put("batch_id",syncOrderItemList.get(i).getBatch_id());
                data_set1.put("growth_tracking_id",syncOrderItemList.get(i).getGrowth_tracking_id());
                data_set1.put("growth_tracking_details_id",syncOrderItemList.get(i).getGrowth_tracking_details_id());
                data_set1.put("species_type_id",syncOrderItemList.get(i).getSpecies_type_id());
                data_set1.put("sapling_height_in_cm",syncOrderItemList.get(i).getHeight_in_cm());
                data_set1.put("sapling_age_in_days",syncOrderItemList.get(i).getAge_in_days());
                data_set1.put("no_of_required_saplings",syncOrderItemList.get(i).getNo_of_required_saplings());
                nursery_sapling_order_details.put(data_set1);
            }
            data_set.put("nursery_sapling_order_details",nursery_sapling_order_details);
            Log.d("normal_json",""+data_set);

            String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), data_set.toString());
            JSONObject dataSet = new JSONObject();
            try {
                dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
                dataSet.put(AppConstant.DATA_CONTENT, authKey);
                new ApiService(this).makeJSONObjectRequest("nursery_sapling_buyer_order_save", Api.Method.POST, UrlGenerator.getNurseryGardenService(), dataSet, "not cache", this);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("order_save", "" + dataSet);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
