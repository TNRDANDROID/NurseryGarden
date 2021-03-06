package com.nic.nurserygarden.activity.LandActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.CameraScreen;
import com.nic.nurserygarden.adapter.CommonAdapter;
import com.nic.nurserygarden.adapter.NurseryLandAdapter;
import com.nic.nurserygarden.api.Api;
import com.nic.nurserygarden.api.ApiService;
import com.nic.nurserygarden.api.ServerResponse;
import com.nic.nurserygarden.constant.AppConstant;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityAddViewLandBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.UrlGenerator;
import com.nic.nurserygarden.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class AddViewLand extends AppCompatActivity implements Api.ServerResponseListener{
    ActivityAddViewLandBinding landBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);

    ArrayList<NurserySurvey> landTypeList;
    ArrayList<NurserySurvey> waterSourceTypeList;
    ArrayList<NurserySurvey> fencingTypeList;
    int land_type_id = 0;
    PrefManager prefManager;
    NurseryLandAdapter nurseryLandAdapter;
    String land_primary_id;
    String land_type_name_en;
    String land_type_name_ta;
    String water_source_type_id="";
    String water_source_type_name="";
    String water_source_is_others="";
    String fencing_type_id="";
    String fencing_type_name="";
    String fencing_type_is_others="";
    String land_type_is_others="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        landBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_view_land);
        landBinding.setActivity(this);
        prefManager = new PrefManager(this);
        Utils.setLocale(prefManager.getKEY_Language(),this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadLandTypeList();
        loadWaterSourceTypeList();
        loadFencingTypeList();
        landBinding.addLandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLandView();
            }
        });
        initialiseRecyclerView();
        if(Utils.isOnline()){
            get_nursery_land_list();
        }
        else {
            new fetchNurseryLandDetails().execute();
        }
    }
    private void initialiseRecyclerView() {
        landBinding.landRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


    public void addLandView(){

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
            RelativeLayout other_land_type_layout = dialog.findViewById(R.id.other_land_type_layout);
            RelativeLayout other_water_source_layout = dialog.findViewById(R.id.other_water_source_layout);
            RelativeLayout other_fencing_layout = dialog.findViewById(R.id.other_fencing_layout);
            ScrollView land_layout = dialog.findViewById(R.id.land_layout);
            LinearLayout batch_layout = dialog.findViewById(R.id.batch_layout);
            TextView tittle_text = dialog.findViewById(R.id.tittle_text);
            TextView other_land_type = dialog.findViewById(R.id.other_land_type);
            TextView other_water_source_type = dialog.findViewById(R.id.other_water_source_type);
            TextView other_fencing_type = dialog.findViewById(R.id.other_fencing_type);
            Spinner land_type_spinner = dialog.findViewById(R.id.land_type_spinner);
            Spinner water_source_spinner = dialog.findViewById(R.id.water_source_spinner);
            Spinner fencing_type_spinner = dialog.findViewById(R.id.fencing_type_spinner);
            EditText land_address = dialog.findViewById(R.id.land_address);
            EditText area = dialog.findViewById(R.id.area);
            EditText other_land_type_et = dialog.findViewById(R.id.other_land_type_et);
            EditText other_fencing_et = dialog.findViewById(R.id.other_fencing_et);
            EditText other_water_source_et = dialog.findViewById(R.id.other_water_source_et);
            EditText survey_number = dialog.findViewById(R.id.survey_number);
            EditText sub_div_no = dialog.findViewById(R.id.sub_div_no);
            tittle_text.setText(getResources().getString(R.string.add_land_details));
            batch_layout.setVisibility(View.GONE);
            other_land_type.setVisibility(View.GONE);
            other_land_type_layout.setVisibility(View.GONE);
            land_type_spinner.setAdapter(new CommonAdapter(AddViewLand.this,landTypeList,"landTypeList"));
            water_source_spinner.setAdapter(new CommonAdapter(AddViewLand.this,waterSourceTypeList,"waterSourceTypeList"));
            fencing_type_spinner.setAdapter(new CommonAdapter(AddViewLand.this,fencingTypeList,"fencingTypeList"));
            land_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position>0){
                        land_type_id = landTypeList.get(position).getLand_type_id();
                        land_type_name_ta = landTypeList.get(position).getLand_type_name_ta();
                        land_type_name_en = landTypeList.get(position).getLand_type_name_en();
                        land_type_is_others = landTypeList.get(position).getIs_others();
                        if(land_type_is_others.equals("Y")){
                            other_land_type.setVisibility(View.VISIBLE);
                            other_land_type_layout.setVisibility(View.VISIBLE);
                        }else {
                            other_land_type.setVisibility(View.GONE);
                            other_land_type_layout.setVisibility(View.GONE);
                        }
                    }
                    else {
                        land_type_id =0;
                        land_type_is_others ="";
                        land_type_name_ta ="";
                        land_type_name_en ="";
                        other_land_type.setVisibility(View.GONE);
                        other_land_type_layout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    other_land_type.setVisibility(View.GONE);
                    other_land_type_layout.setVisibility(View.GONE);
                }
            });
            water_source_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position>0){
                        water_source_type_id = waterSourceTypeList.get(position).getWater_source_type_id();
                        water_source_type_name = waterSourceTypeList.get(position).getWater_source_type_name();
                        water_source_is_others = waterSourceTypeList.get(position).getIs_others();
                        if(water_source_is_others.equals("Y")){
                            other_water_source_type.setVisibility(View.VISIBLE);
                            other_water_source_layout.setVisibility(View.VISIBLE);
                        }else {
                            other_water_source_type.setVisibility(View.GONE);
                            other_water_source_layout.setVisibility(View.GONE);
                        }
                    }
                    else {
                        water_source_type_id ="";
                        water_source_type_name ="";
                        water_source_is_others ="";
                        other_water_source_type.setVisibility(View.GONE);
                        other_water_source_layout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    other_water_source_type.setVisibility(View.GONE);
                    other_water_source_layout.setVisibility(View.GONE);
                }
            });
            fencing_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position>0){
                        fencing_type_id = fencingTypeList.get(position).getFencing_type_id();
                        fencing_type_name = fencingTypeList.get(position).getFencing_type_name();
                        fencing_type_is_others = fencingTypeList.get(position).getIs_others();
                        if(fencing_type_is_others.equals("Y")){
                            other_fencing_type.setVisibility(View.VISIBLE);
                            other_fencing_layout.setVisibility(View.VISIBLE);
                        }else {
                            other_fencing_type.setVisibility(View.GONE);
                            other_fencing_layout.setVisibility(View.GONE);
                        }
                    }
                    else {
                        fencing_type_id ="";
                        fencing_type_name ="";
                        fencing_type_is_others ="";
                        other_fencing_type.setVisibility(View.GONE);
                        other_fencing_layout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    other_fencing_type.setVisibility(View.GONE);
                    other_fencing_layout.setVisibility(View.GONE);
                }
            });
            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            capture_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(land_type_id!=0){
                        if(land_type_id == 3){
                            if(!other_land_type_et.getText().toString().equals("")){
                                if(!area.getText().toString().equals("")){
                                    if(!survey_number.getText().toString().equals("")){
                                        if(!sub_div_no.getText().toString().equals("")){
                                            if(!land_address.getText().toString().equals("")){
                                                if(!water_source_type_id.equals("")){
                                                    if(water_source_is_others.equals("Y")){
                                                        if(!other_water_source_et.getText().toString().equals("")){
                                                            if(!fencing_type_id.equals("")){
                                                                if(fencing_type_is_others.equals("Y")){
                                                                    if(!other_fencing_et.getText().toString().equals("")){
                                                                        Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                                                        camera_screen.putExtra("activity_type","Land");
                                                                        camera_screen.putExtra("land_type_id",land_type_id);
                                                                        camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                                                        camera_screen.putExtra("land_address",land_address.getText().toString());
                                                                        camera_screen.putExtra("area",area.getText().toString());
                                                                        camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                                                        camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                                                        camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                                                        camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                                                        camera_screen.putExtra("water_source_type_id",water_source_type_id);
                                                                        camera_screen.putExtra("water_source_type_name",water_source_type_name);
                                                                        camera_screen.putExtra("fencing_type_id",fencing_type_id);
                                                                        camera_screen.putExtra("fencing_type_name",fencing_type_name);
                                                                        camera_screen.putExtra("other_fencing_type_name",other_fencing_et.getText().toString());
                                                                        camera_screen.putExtra("other_water_source_type_name",other_water_source_et.getText().toString());
                                                                        startActivity(camera_screen);
                                                                        dialog.dismiss();
                                                                    }
                                                                    else {
                                                                        Utils.showAlert(AddViewLand.this,getResources().getString(R.string.other_fencing_type));

                                                                    }
                                                                }
                                                                else {
                                                                    Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                                                    camera_screen.putExtra("activity_type","Land");
                                                                    camera_screen.putExtra("land_type_id",land_type_id);
                                                                    camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                                                    camera_screen.putExtra("land_address",land_address.getText().toString());
                                                                    camera_screen.putExtra("area",area.getText().toString());
                                                                    camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                                                    camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                                                    camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                                                    camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                                                    camera_screen.putExtra("water_source_type_id",water_source_type_id);
                                                                    camera_screen.putExtra("water_source_type_name",water_source_type_name);
                                                                    camera_screen.putExtra("fencing_type_id",fencing_type_id);
                                                                    camera_screen.putExtra("fencing_type_name",fencing_type_name);
                                                                    camera_screen.putExtra("other_fencing_type_name",other_fencing_et.getText().toString());
                                                                    camera_screen.putExtra("other_water_source_type_name",other_water_source_et.getText().toString());
                                                                    startActivity(camera_screen);
                                                                    dialog.dismiss();
                                                                }
                                                            }
                                                            else {
                                                                Utils.showAlert(AddViewLand.this,getResources().getString(R.string.select_fencing_type));

                                                            }
                                                        }
                                                        else {
                                                            Utils.showAlert(AddViewLand.this,getResources().getString(R.string.other_water_source_type));
                                                        }
                                                    }
                                                    else {
                                                        if(!fencing_type_id.equals("")){
                                                            if(fencing_type_is_others.equals("Y")){
                                                                if(!other_fencing_et.getText().toString().equals("")){
                                                                    Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                                                    camera_screen.putExtra("activity_type","Land");
                                                                    camera_screen.putExtra("land_type_id",land_type_id);
                                                                    camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                                                    camera_screen.putExtra("land_address",land_address.getText().toString());
                                                                    camera_screen.putExtra("area",area.getText().toString());
                                                                    camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                                                    camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                                                    camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                                                    camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                                                    camera_screen.putExtra("water_source_type_id",water_source_type_id);
                                                                    camera_screen.putExtra("water_source_type_name",water_source_type_name);
                                                                    camera_screen.putExtra("fencing_type_id",fencing_type_id);
                                                                    camera_screen.putExtra("fencing_type_name",fencing_type_name);
                                                                    camera_screen.putExtra("other_fencing_type_name",other_fencing_et.getText().toString());
                                                                    camera_screen.putExtra("other_water_source_type_name",other_water_source_et.getText().toString());
                                                                    startActivity(camera_screen);
                                                                    dialog.dismiss();
                                                                }
                                                                else {
                                                                    Utils.showAlert(AddViewLand.this,getResources().getString(R.string.other_fencing_type));

                                                                }
                                                            }
                                                            else {
                                                                Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                                                camera_screen.putExtra("activity_type","Land");
                                                                camera_screen.putExtra("land_type_id",land_type_id);
                                                                camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                                                camera_screen.putExtra("land_address",land_address.getText().toString());
                                                                camera_screen.putExtra("area",area.getText().toString());
                                                                camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                                                camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                                                camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                                                camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                                                camera_screen.putExtra("water_source_type_id",water_source_type_id);
                                                                camera_screen.putExtra("water_source_type_name",water_source_type_name);
                                                                camera_screen.putExtra("fencing_type_id",fencing_type_id);
                                                                camera_screen.putExtra("fencing_type_name",fencing_type_name);
                                                                camera_screen.putExtra("other_fencing_type_name",other_fencing_et.getText().toString());
                                                                camera_screen.putExtra("other_water_source_type_name",other_water_source_et.getText().toString());
                                                                startActivity(camera_screen);
                                                                dialog.dismiss();
                                                            }
                                                        }
                                                        else {
                                                            Utils.showAlert(AddViewLand.this,getResources().getString(R.string.select_fencing_type));

                                                        }
                                                    }
                                                }
                                                else {
                                                    Utils.showAlert(AddViewLand.this,getResources().getString(R.string.select_water_source_type));
                                                }

                                            }
                                            else {
                                                Utils.showAlert(AddViewLand.this,getResources().getString(R.string.land_address));
                                            }
                                        }
                                        else {
                                            Utils.showAlert(AddViewLand.this,getResources().getString(R.string.enter_sub_div_no));
                                        }
                                    }
                                    else {
                                        Utils.showAlert(AddViewLand.this,getResources().getString(R.string.enter_survey_number));
                                    }
                                }
                              else {
                                    Utils.showAlert(AddViewLand.this,getResources().getString(R.string.enter_area));
                                }
                            }
                            else {
                                Utils.showAlert(AddViewLand.this,getResources().getString(R.string.other_land_type));
                            }

                        }
                        else {
                                if(!area.getText().toString().equals("")){
                                    if(!survey_number.getText().toString().equals("")){
                                        if(!sub_div_no.getText().toString().equals("")){
                                        if(!land_address.getText().toString().equals("")){
                                            if(!water_source_type_id.equals("")){
                                                if(water_source_is_others.equals("Y")){
                                                    if(!other_water_source_et.getText().toString().equals("")){
                                                        if(!fencing_type_id.equals("")){
                                                            if(fencing_type_is_others.equals("Y")){
                                                                if(!other_fencing_et.getText().toString().equals("")){
                                                                    Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                                                    camera_screen.putExtra("activity_type","Land");
                                                                    camera_screen.putExtra("land_type_id",land_type_id);
                                                                    camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                                                    camera_screen.putExtra("land_address",land_address.getText().toString());
                                                                    camera_screen.putExtra("area",area.getText().toString());
                                                                    camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                                                    camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                                                    camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                                                    camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                                                    camera_screen.putExtra("water_source_type_id",water_source_type_id);
                                                                    camera_screen.putExtra("water_source_type_name",water_source_type_name);
                                                                    camera_screen.putExtra("fencing_type_id",fencing_type_id);
                                                                    camera_screen.putExtra("fencing_type_name",fencing_type_name);
                                                                    camera_screen.putExtra("other_fencing_type_name",other_fencing_et.getText().toString());
                                                                    camera_screen.putExtra("other_water_source_type_name",other_water_source_et.getText().toString());
                                                                    startActivity(camera_screen);
                                                                    dialog.dismiss();
                                                                }
                                                                else {
                                                                    Utils.showAlert(AddViewLand.this,getResources().getString(R.string.other_fencing_type));

                                                                }
                                                            }
                                                            else {
                                                                Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                                                camera_screen.putExtra("activity_type","Land");
                                                                camera_screen.putExtra("land_type_id",land_type_id);
                                                                camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                                                camera_screen.putExtra("land_address",land_address.getText().toString());
                                                                camera_screen.putExtra("area",area.getText().toString());
                                                                camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                                                camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                                                camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                                                camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                                                camera_screen.putExtra("water_source_type_id",water_source_type_id);
                                                                camera_screen.putExtra("water_source_type_name",water_source_type_name);
                                                                camera_screen.putExtra("fencing_type_id",fencing_type_id);
                                                                camera_screen.putExtra("fencing_type_name",fencing_type_name);
                                                                camera_screen.putExtra("other_fencing_type_name",other_fencing_et.getText().toString());
                                                                camera_screen.putExtra("other_water_source_type_name",other_water_source_et.getText().toString());
                                                                startActivity(camera_screen);
                                                                dialog.dismiss();
                                                            }
                                                        }
                                                        else {
                                                            Utils.showAlert(AddViewLand.this,getResources().getString(R.string.select_fencing_type));

                                                        }
                                                    }
                                                    else {
                                                        Utils.showAlert(AddViewLand.this,getResources().getString(R.string.other_water_source_type));
                                                    }
                                                }
                                                else {
                                                    if(!fencing_type_id.equals("")){
                                                        if(fencing_type_is_others.equals("Y")){
                                                            if(!other_fencing_et.getText().toString().equals("")){
                                                                Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                                                camera_screen.putExtra("activity_type","Land");
                                                                camera_screen.putExtra("land_type_id",land_type_id);
                                                                camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                                                camera_screen.putExtra("land_address",land_address.getText().toString());
                                                                camera_screen.putExtra("area",area.getText().toString());
                                                                camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                                                camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                                                camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                                                camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                                                camera_screen.putExtra("water_source_type_id",water_source_type_id);
                                                                camera_screen.putExtra("water_source_type_name",water_source_type_name);
                                                                camera_screen.putExtra("fencing_type_id",fencing_type_id);
                                                                camera_screen.putExtra("fencing_type_name",fencing_type_name);
                                                                camera_screen.putExtra("other_fencing_type_name",other_fencing_et.getText().toString());
                                                                camera_screen.putExtra("other_water_source_type_name",other_water_source_et.getText().toString());
                                                                startActivity(camera_screen);
                                                                dialog.dismiss();
                                                            }
                                                            else {
                                                                Utils.showAlert(AddViewLand.this,getResources().getString(R.string.other_fencing_type));

                                                            }
                                                        }
                                                        else {
                                                            Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                                            camera_screen.putExtra("activity_type","Land");
                                                            camera_screen.putExtra("land_type_id",land_type_id);
                                                            camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                                            camera_screen.putExtra("land_address",land_address.getText().toString());
                                                            camera_screen.putExtra("area",area.getText().toString());
                                                            camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                                            camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                                            camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                                            camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                                            camera_screen.putExtra("water_source_type_id",water_source_type_id);
                                                            camera_screen.putExtra("water_source_type_name",water_source_type_name);
                                                            camera_screen.putExtra("fencing_type_id",fencing_type_id);
                                                            camera_screen.putExtra("fencing_type_name",fencing_type_name);
                                                            camera_screen.putExtra("other_fencing_type_name",other_fencing_et.getText().toString());
                                                            camera_screen.putExtra("other_water_source_type_name",other_water_source_et.getText().toString());
                                                            startActivity(camera_screen);
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                    else {
                                                        Utils.showAlert(AddViewLand.this,getResources().getString(R.string.select_fencing_type));

                                                    }
                                                }
                                            }
                                            else {
                                                Utils.showAlert(AddViewLand.this,getResources().getString(R.string.select_water_source_type));
                                            }
                                          /*  Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                                            camera_screen.putExtra("activity_type","Land");
                                            camera_screen.putExtra("land_type_id",land_type_id);
                                            camera_screen.putExtra("other_land_type", other_land_type_et.getText().toString());
                                            camera_screen.putExtra("land_address",land_address.getText().toString());
                                            camera_screen.putExtra("area",area.getText().toString());
                                            camera_screen.putExtra("survey_number",survey_number.getText().toString());
                                            camera_screen.putExtra("sub_div_no",sub_div_no.getText().toString());
                                            camera_screen.putExtra("land_type_name_en",land_type_name_en);
                                            camera_screen.putExtra("land_type_name_ta",land_type_name_ta);
                                            startActivity(camera_screen);
                                            dialog.dismiss();*/
                                        }
                                        else {
                                            Utils.showAlert(AddViewLand.this,getResources().getString(R.string.land_address));
                                        }
                                    }
                                        else {
                                            Utils.showAlert(AddViewLand.this,getResources().getString(R.string.enter_sub_div_no));
                                        }
                                    }
                                    else {
                                        Utils.showAlert(AddViewLand.this,getResources().getString(R.string.enter_survey_number));
                                    }
                                }
                                else {
                                    Utils.showAlert(AddViewLand.this,getResources().getString(R.string.enter_area));
                                }

                        }

                    }
                    else {
                        Utils.showAlert(AddViewLand.this,getResources().getString(R.string.select_land_type));
                    }


                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadLandTypeList(){
        landTypeList = new ArrayList<>();
        dbData.open();
        NurserySurvey landTypeList_item = new NurserySurvey();
        landTypeList_item.setLand_type_id(0);
        landTypeList_item.setLand_type_name_en(getResources().getString(R.string.select_land_type));
        landTypeList_item.setLand_type_name_ta(getResources().getString(R.string.select_land_type));
        landTypeList_item.setIs_others("");
        landTypeList.add(landTypeList_item);
        landTypeList.addAll(dbData.get_nursery_land_type());
        Log.d("Size",""+landTypeList.size());

    }
    public void loadWaterSourceTypeList(){
        waterSourceTypeList = new ArrayList<>();
        dbData.open();
        NurserySurvey water_source_item = new NurserySurvey();
        water_source_item.setWater_source_type_id("0");
        water_source_item.setIs_others("0");
        water_source_item.setWater_source_type_name(getResources().getString(R.string.select_water_source_type));
        waterSourceTypeList.add(water_source_item);
        waterSourceTypeList.addAll(dbData.get_nursery_water_source_type());
        Log.d("Size",""+waterSourceTypeList.size());

    }
    public void loadFencingTypeList(){
        fencingTypeList = new ArrayList<>();
        dbData.open();
        NurserySurvey water_source_item = new NurserySurvey();
        water_source_item.setFencing_type_id("0");
        water_source_item.setIs_others("0");
        water_source_item.setFencing_type_name(getResources().getString(R.string.select_fencing_type));
        fencingTypeList.add(water_source_item);
        fencingTypeList.addAll(dbData.get_nursery_fencing_type());
        Log.d("Size",""+fencingTypeList.size());

    }

    public void get_nursery_land_list() {
        try {
            new ApiService(this).makeJSONObjectRequest("nursery_land_list", Api.Method.POST, UrlGenerator.getNurseryGardenService(), nursery_land_listJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject nursery_land_listJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), nursery_land_json().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("nursery_land", "" + dataSet);
        return dataSet;
    }

    public  JSONObject nursery_land_json() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_details_of_nursery_land_view);
        Log.d("nursery_land", "" + dataSet);
        return dataSet;
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("nursery_land_list".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_nursery_land_details().execute(jsonObject);
                }
                else {
                    new fetchNurseryLandDetails().execute();
                }
                Log.d("nursery_land_list", "" + responseDecryptedBlockKey);
            }

            if ("saveLandImages".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Uploaded");
                    String whereClause = "land_primary_id = ? ";
                    String[] whereArgs = new String[]{land_primary_id};
                    int sdsm = db.delete(DBHelper.NURSERY_LAND_SAVE_DETAILS, whereClause, whereArgs);

                    get_nursery_land_list();
                    nurseryLandAdapter.notifyDataSetChanged();
                }
                else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")){
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                }
                Log.d("saved_response", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public class Insert_nursery_land_details extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            //dbData.delete_nursery_user_details();

            db.delete(DBHelper.NURSERY_LAND_SAVE_DETAILS, "server_flag=?", new String[]{"1"});
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
                        nurserySurvey.setNursery_land_id(jsonArray.getJSONObject(i).getInt("nursery_land_id"));
                        nurserySurvey.setLand_type_id(jsonArray.getJSONObject(i).getInt("land_type_id"));
                        nurserySurvey.setLand_type_name_en(jsonArray.getJSONObject(i).getString("land_type_name_en"));
                        nurserySurvey.setLand_type_name_ta(jsonArray.getJSONObject(i).getString("land_type_name_ta"));
                        byte[] decodedString = Base64.decode(jsonArray.getJSONObject(i).getString("image"), Base64.DEFAULT);
                        nurserySurvey.setImageInByte(decodedString);
                        nurserySurvey.setLatitude(jsonArray.getJSONObject(i).getString("lat"));
                        nurserySurvey.setLongitude(jsonArray.getJSONObject(i).getString("long"));
                        nurserySurvey.setServer_flag("1");
                        nurserySurvey.setLand_address(jsonArray.getJSONObject(i).getString("address"));
                        nurserySurvey.setOther_land_type(jsonArray.getJSONObject(i).getString("land_type_others"));
                        nurserySurvey.setArea(jsonArray.getJSONObject(i).getString("area"));
                        nurserySurvey.setSurvey_number(jsonArray.getJSONObject(i).getString("survey_no"));
                        nurserySurvey.setSub_div_no(jsonArray.getJSONObject(i).getString("subdiv_no"));

                        nurserySurvey.setWater_source_type_id(jsonArray.getJSONObject(i).getString("water_source_type_id"));
                        nurserySurvey.setOther_water_source_type_name(jsonArray.getJSONObject(i).getString("water_source_type_others"));
                        nurserySurvey.setFencing_type_id(jsonArray.getJSONObject(i).getString("fencing_type_id"));
                        nurserySurvey.setOther_fencing_type_name(jsonArray.getJSONObject(i).getString("fencing_type_others"));
                        nurserySurvey.setFencing_type_name(jsonArray.getJSONObject(i).getString("fencing_type_name"));
                        nurserySurvey.setWater_source_type_name(jsonArray.getJSONObject(i).getString("water_source_type_name"));
                        dbData.insert_nursery_land_details(nurserySurvey);
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
            new fetchNurseryLandDetails().execute();
        }
    }

    public class fetchNurseryLandDetails extends AsyncTask<Void, Void,ArrayList<NurserySurvey>> {
        @Override
        protected ArrayList<NurserySurvey> doInBackground(Void... params) {
            dbData.open();
            ArrayList<NurserySurvey> nurseryLandDetails = new ArrayList<>();
            nurseryLandDetails = dbData.get_nursery_land_details("All","1","");
            Log.d("nursery_land_details", String.valueOf(nurseryLandDetails.size()));
            return nurseryLandDetails;
        }

        @Override
        protected void onPostExecute(ArrayList<NurserySurvey> nurseryLandDetails) {
            super.onPostExecute(nurseryLandDetails);
            if(nurseryLandDetails.size()>0){
                nurseryLandAdapter = new NurseryLandAdapter(nurseryLandDetails,AddViewLand.this,dbData);
                landBinding.landRecycler.setAdapter(nurseryLandAdapter);
            }
        }
    }

    public JSONObject UploadData(JSONObject savePMAYDataSet, String land_primary_id_) {
        land_primary_id=land_primary_id_;
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), savePMAYDataSet.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveLandImages", Api.Method.POST, UrlGenerator.getNurseryGardenService(), dataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("saveLandDataSet", "" + savePMAYDataSet);
        Log.d("saveLandImages", "" + dataSet);
        return dataSet;
    }
    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new fetchNurseryLandDetails().execute();
    }
}
