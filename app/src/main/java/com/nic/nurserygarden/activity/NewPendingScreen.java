package com.nic.nurserygarden.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.nic.nurserygarden.adapter.NewPendingAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityNewPendingScreenBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class NewPendingScreen extends AppCompatActivity implements Api.ServerResponseListener {
    public ActivityNewPendingScreenBinding pendingScreenBinding;
    private RecyclerView recyclerView;
    private NewPendingAdapter pendingAdapter;
    private PrefManager prefManager;
    public com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private Activity context;

    String shg_code="";
    String shg_member_code="";
    String batch_id="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pendingScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_pending_screen);
        pendingScreenBinding.setActivity(this);
        context = this;
        prefManager = new PrefManager(this);
        Utils.setLocale(prefManager.getKEY_Language(),this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView = pendingScreenBinding.pendingList;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        new fetchPendingtask().execute();
    }



    public class fetchPendingtask extends AsyncTask<Void, Void,
            ArrayList<NurserySurvey>> {
        @Override
        protected ArrayList<NurserySurvey> doInBackground(Void... params) {
            dbData.open();
            ArrayList<NurserySurvey> nurseryDeadList = new ArrayList<>();
            nurseryDeadList = dbData.get_dead_sapling_batch_details();
            Log.d("COUNT", String.valueOf(nurseryDeadList.size()));
            return nurseryDeadList;
        }

        @Override
        protected void onPostExecute(ArrayList<NurserySurvey> nurseryDeadList) {
            super.onPostExecute(nurseryDeadList);
            if(nurseryDeadList.size()>0) {
                recyclerView.setVisibility(View.VISIBLE);
                pendingAdapter = new NewPendingAdapter(NewPendingScreen.this, nurseryDeadList, dbData);
                recyclerView.setAdapter(pendingAdapter);
            }
            else {
                recyclerView.setVisibility(View.GONE);
            }
        }
    }



    public JSONObject uploadDeadOrder(JSONObject deadOrderJson,String batch_id_) {
        batch_id = batch_id_;
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), deadOrderJson.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("uploadDead", Api.Method.POST, UrlGenerator.getNurseryGardenService(), dataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("uploadDead", "" + dataSet);
        return dataSet;
    }
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("uploadDead".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Uploaded");
                    //int sdsm = db.delete(DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE, null,null);
                    int sdsm = db.delete(DBHelper.DEAD_SAPLING_DETAILS_NEW_SAVE, "batch_id = ? ", new String[]{batch_id});
                    new fetchPendingtask().execute();
                    pendingAdapter.notifyDataSetChanged();
                }
                else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")){
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();

                }
                Log.d("saved_response", "" + responseDecryptedBlockKey);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void homePage() {
        Intent intent = new Intent(this, MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Home", "Home");
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
