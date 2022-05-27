package com.nic.nurserygarden.activity.DeadSaplingActivty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.adapter.DeadSpeciesAdapter;
import com.nic.nurserygarden.adapter.FilterItemsAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityNewDeadSaplingEntryBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class NewDeadSaplingEntry extends AppCompatActivity implements Api.ServerResponseListener{
    public ActivityNewDeadSaplingEntryBinding deadSaplingEntryBinding;
    private PrefManager prefManager;
    public com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

    int buyer_type_id=0;
    String buyer_name="";
    String buyer_mobile="";
    String buyer_address="";
    String pv_code="";

    ArrayList<NurserySurvey> deadStageList;
    DeadSpeciesAdapter deadSpeciesAdapter;
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

    EditText no_of_dead_saplings_text;
    TextView available_count_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setStatusBarGradiant(NewDeadSaplingEntry.this);
        deadSaplingEntryBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_dead_sapling_entry);
        deadSaplingEntryBinding.setActivity(this);
        prefManager = new PrefManager(this);
        Utils.setLocale(prefManager.getKEY_Language(),this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getIntentData();
        deadSaplingEntryBinding.filterIcon.setVisibility(View.GONE);
        deadSaplingEntryBinding.mainLayout.setVisibility(View.VISIBLE);
        deadSaplingEntryBinding.filterLayout.setVisibility(View.GONE);
        deadSaplingEntryBinding.filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadSaplingEntryBinding.mainLayout.setVisibility(View.GONE);
                deadSaplingEntryBinding.filterLayout.setVisibility(View.VISIBLE);
            }
        });
        deadSaplingEntryBinding.filterSubmitIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFilterOrderItemList(nurseryOrderItemList);
                deadSaplingEntryBinding.mainLayout.setVisibility(View.VISIBLE);
                deadSaplingEntryBinding.filterLayout.setVisibility(View.GONE);
            }
        });
        deadSaplingEntryBinding.closeFilterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadSaplingEntryBinding.mainLayout.setVisibility(View.VISIBLE);
                deadSaplingEntryBinding.filterLayout.setVisibility(View.GONE);
            }
        });

        deadSaplingEntryBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadSaplingLocalSave();
            }
        });
    }

    private void getIntentData(){
        deadSaplingEntryBinding.orderItemRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        deadSaplingEntryBinding.batchNameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true));
        deadSaplingEntryBinding.speciesNameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true));
        deadSaplingEntryBinding.daysNameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true));
        deadSaplingEntryBinding.heightNameRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL, true));


        batch_id = getIntent().getIntExtra("batch_id",0);

        if(Utils.isOnline()){
            sendJsonObjectForSearch();
        }
        else {
            new fetchNurserySpeciesDetails().execute();
            //Utils.showAlert(NewDeadSaplingEntry.this,getResources().getString(R.string.no_internet));
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
            batchNumberAdapter = new FilterItemsAdapter(batchNumberItemList,NewDeadSaplingEntry.this,"Batch_Number","Dead");
            deadSaplingEntryBinding.batchNameRecycler.setAdapter(batchNumberAdapter);
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
            speciesNameAdapter = new FilterItemsAdapter(speciesNameItemList,NewDeadSaplingEntry.this,"Species_Name","Dead");
            deadSaplingEntryBinding.speciesNameRecycler.setAdapter(speciesNameAdapter);
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
            daysNameAdapter = new FilterItemsAdapter(daysItemList,NewDeadSaplingEntry.this,"Days","Dead");
            deadSaplingEntryBinding.daysNameRecycler.setAdapter(daysNameAdapter);
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
            heightAdapter = new FilterItemsAdapter(heightItemList,NewDeadSaplingEntry.this,"Height","Dead");
            deadSaplingEntryBinding.heightNameRecycler.setAdapter(heightAdapter);
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
                    Toasty.error(NewDeadSaplingEntry.this,jsonObject.getString("MESSAGE"), Toast.LENGTH_SHORT);
                }
                Log.d("searchDetails", "" + responseDecryptedBlockKey);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Insert_nursery_order_items extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            //nurseryOrderItemList = new ArrayList<>();
            dbData.open();
            db.delete(DBHelper.DEAD_SAPLING_DETAILS,null,null);
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
                        nurseryOrderItem.setBatch_species_id(jsonArray.getJSONObject(i).getInt("batch_species_id"));
                        nurseryOrderItem.setGrowth_tracking_id(jsonArray.getJSONObject(i).getInt("growth_tracking_id"));
                        nurseryOrderItem.setGrowth_tracking_details_id(jsonArray.getJSONObject(i).getInt("growth_tracking_details_id"));
                        nurseryOrderItem.setBatch_number(jsonArray.getJSONObject(i).getInt("batch_no"));
                        nurseryOrderItem.setSpecies_type_id(jsonArray.getJSONObject(i).getInt("species_type_id"));
                        nurseryOrderItem.setSpecies_name_en(jsonArray.getJSONObject(i).getString("species_name_en"));
                        nurseryOrderItem.setSpecies_name_ta(jsonArray.getJSONObject(i).getString("species_name_ta"));
                        nurseryOrderItem.setHeight_in_cm(jsonArray.getJSONObject(i).getString("sapling_height_in_cm"));
                        nurseryOrderItem.setAge_in_days(jsonArray.getJSONObject(i).getInt("sapling_age_in_days"));
                        nurseryOrderItem.setNo_of_saplings(jsonArray.getJSONObject(i).getInt("no_of_saplings"));
                        nurseryOrderItem.setDead_stage_position(0);
                        nurseryOrderItem.setDead_reason_text("");
                        nurseryOrderItem.setDead_typed_count_text("");

                        //nurseryOrderItemList.add(nurseryOrderItem);
                        dbData.insert_dead_sapling_details(nurseryOrderItem);

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            return null;


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /*if(nurseryOrderItemList.size()>0){
                deadSaplingEntryBinding.submitBtn.setVisibility(View.VISIBLE);
                *//*nurseryOrderItemFilterList = new ArrayList<>();
                nurseryOrderItemFilterList.clear();
                nurseryOrderItemFilterList.addAll(nurseryOrderItemList);
                sellSpeciesAdapter = new SellSpeciesAdapter(nurseryOrderItemFilterList,NewDeadSaplingEntry.this,"");
                deadSaplingEntryBinding.orderItemRecycler.setAdapter(sellSpeciesAdapter);
                sellSpeciesAdapter.notifyDataSetChanged();
*//*
                new fetchNurserySpeciesDetails().execute();
            }
            else {
                deadSaplingEntryBinding.submitBtn.setVisibility(View.GONE);
            }*/
            new fetchNurserySpeciesDetails().execute();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {
    }

    public class fetchNurserySpeciesDetails extends AsyncTask<Void, Void, ArrayList<NurserySurvey>> {
        @Override
        protected ArrayList<NurserySurvey> doInBackground(Void... params) {
            dbData.open();
            ArrayList<NurserySurvey> deadSapligDetails = new ArrayList<>();
            deadSapligDetails = dbData.get_dead_sapling_details();
            Log.d("dead_sapling_details", String.valueOf(deadSapligDetails.size()));
            return deadSapligDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<NurserySurvey> deadSapligDetails) {
            super.onPostExecute(deadSapligDetails);
            if(deadSapligDetails.size()>0){
                nurseryOrderItemList = new ArrayList<>();
                nurseryOrderItemList.addAll(deadSapligDetails);
                setAdapterForBatchNumber(nurseryOrderItemList);
                setAdapterForSpeciesName(nurseryOrderItemList);
                setAdapterForDays(nurseryOrderItemList);
                setAdapterForHeight(nurseryOrderItemList);
                deadSaplingEntryBinding.submitBtn.setVisibility(View.VISIBLE);
                nurseryOrderItemFilterList = new ArrayList<>();
                nurseryOrderItemFilterList.clear();
                nurseryOrderItemFilterList.addAll(nurseryOrderItemList);
                /*deadSpeciesAdapter = new DeadSpeciesAdapter(nurseryOrderItemFilterList,NewDeadSaplingEntry.this,"",dbData,deadStageList);
                deadSaplingEntryBinding.orderItemRecycler.setAdapter(deadSpeciesAdapter);
                deadSpeciesAdapter.notifyDataSetChanged();*/
                getFilterOrderItemList(nurseryOrderItemList);

            }
            else {
                deadSaplingEntryBinding.submitBtn.setVisibility(View.GONE);
            }
        }
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
            deadSaplingEntryBinding.submitBtn.setVisibility(View.VISIBLE);
            deadSpeciesAdapter = new DeadSpeciesAdapter(nurseryOrderItemFilterList,NewDeadSaplingEntry.this,"",dbData,deadStageList);
            deadSaplingEntryBinding.orderItemRecycler.setAdapter(deadSpeciesAdapter);
            deadSpeciesAdapter.notifyDataSetChanged();
        }
        else {
            deadSaplingEntryBinding.submitBtn.setVisibility(View.GONE);
        }

    }

    public void deadSaplingLocalSave() {
        ArrayList<NurserySurvey> deadOrderList = new ArrayList<>();

        boolean validation_check = false;


        String no_of_dead_saplings="";
        int no_of_available_saplings=0;
        ArrayList<NurserySurvey> deadOrderArrayList = new ArrayList<>(deadSpeciesAdapter.getTheDeadArrayList());
        //int childCount = deadSaplingEntryBinding.orderItemRecycler.getChildCount();
        try {
            if (deadOrderArrayList.size() > 0) {
                for (int i = 0; i < deadOrderArrayList.size(); i++) {

                        no_of_available_saplings = (deadOrderArrayList.get(i).getNo_of_saplings());

                        no_of_dead_saplings = String.valueOf(deadOrderArrayList.get(i).getDead_typed_count_text());

                        if(!no_of_dead_saplings.equals("")){
                            if(no_of_available_saplings>=Integer.parseInt(no_of_dead_saplings)){

                                NurserySurvey dead_item = new NurserySurvey();
                                dead_item.setBatch_id(deadOrderArrayList.get(i).getBatch_id());
                                dead_item.setBatch_number(deadOrderArrayList.get(i).getBatch_number());
                                dead_item.setBatch_species_id(deadOrderArrayList.get(i).getBatch_species_id());
                                dead_item.setGrowth_tracking_id(deadOrderArrayList.get(i).getGrowth_tracking_id());
                                dead_item.setGrowth_tracking_details_id(deadOrderArrayList.get(i).getGrowth_tracking_details_id());
                                dead_item.setSpecies_type_id(deadOrderArrayList.get(i).getSpecies_type_id());
                                dead_item.setHeight_in_cm(deadOrderArrayList.get(i).getHeight_in_cm());
                                dead_item.setAge_in_days(deadOrderArrayList.get(i).getAge_in_days());
                                dead_item.setNo_of_saplings(deadOrderArrayList.get(i).getNo_of_saplings());
                                dead_item.setNo_of_dead_sapling(Integer.parseInt(no_of_dead_saplings));
                                deadOrderList.add(dead_item);
                                validation_check = true;

                            }
                            else {
                                validation_check = false;
                                //no_of_dead_saplings_text.requestFocus();
                                Utils.showAlert(NewDeadSaplingEntry.this,getResources().getString(R.string.count_mismatched));
                            }

                        }




                }
                if(validation_check){
                    if(deadOrderList.size()>0){
                        saveDeadSaplingDetailsLocally(deadOrderList);
                    }
                    else {
                        Utils.showAlert(NewDeadSaplingEntry.this,getResources().getString(R.string.enter_the_dead_saplings));
                    }

                }
                else {
                    Utils.showAlert(NewDeadSaplingEntry.this,getResources().getString(R.string.count_mismatched));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveDeadSaplingDetailsLocally(ArrayList<NurserySurvey> deadOrderList){
        dbData.open();
        long insert_id;
        int count =0;
        ArrayList<NurserySurvey> previous_DeadList = new ArrayList<>();
        previous_DeadList.addAll(dbData.get_dead_sapling_details_for_upload("Particular",String.valueOf(batch_id)));
        if(previous_DeadList.size()>0){
            long delete_id = db.delete(DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE,null,null);
        }
        try {
            for(int i=0; i<deadOrderList.size();i++){
                count = count+1;
                ContentValues values = new ContentValues();
                values.put("batch_id",deadOrderList.get(i).getBatch_id());
                values.put("batch_no",deadOrderList.get(i).getBatch_number());
                values.put("species_type_id",deadOrderList.get(i).getSpecies_type_id());
                values.put("batch_species_id",deadOrderList.get(i).getBatch_species_id());
                values.put("growth_tracking_id",deadOrderList.get(i).getGrowth_tracking_id());
                values.put("growth_tracking_details_id",deadOrderList.get(i).getGrowth_tracking_details_id());
                values.put("sapling_height_in_cm",deadOrderList.get(i).getHeight_in_cm());
                values.put("sapling_age_in_days",deadOrderList.get(i).getAge_in_days());
                values.put("no_of_dead_sapling",deadOrderList.get(i).getNo_of_dead_sapling());

                insert_id = db.insert(DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE,null,values);
                if(deadOrderList.size()==count){
                    if(insert_id>0){
                        Toasty.success(NewDeadSaplingEntry.this, getResources().getString(R.string.success),Toasty.LENGTH_SHORT);
                        onBackPress();
                    }
                    else {
                        Toasty.error(NewDeadSaplingEntry.this, getResources().getString(R.string.faild),Toasty.LENGTH_SHORT);
                    }
                }

            }


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
