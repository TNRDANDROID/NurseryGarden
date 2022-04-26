package com.nic.nurserygarden.activity.LandActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.BatchActivity.AddViewBatchDetails;
import com.nic.nurserygarden.activity.CameraScreen;
import com.nic.nurserygarden.adapter.CommonAdapter;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityAddViewLandBinding;
import com.nic.nurserygarden.model.NurserySurvey;
import com.nic.nurserygarden.utils.Utils;

import java.util.ArrayList;

public class AddViewLand extends AppCompatActivity {
    ActivityAddViewLandBinding landBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);

    ArrayList<NurserySurvey> landTypeList;
    int land_type_id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        landBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_view_land);
        landBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadLandTypeList();
        landBinding.addLandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLandView();
            }
        });
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
            LinearLayout land_layout = dialog.findViewById(R.id.land_layout);
            LinearLayout batch_layout = dialog.findViewById(R.id.batch_layout);
            TextView tittle_text = dialog.findViewById(R.id.tittle_text);
            Spinner land_type_spinner = dialog.findViewById(R.id.land_type_spinner);
            tittle_text.setText("Add Land Details");
            batch_layout.setVisibility(View.GONE);
            land_type_spinner.setAdapter(new CommonAdapter(AddViewLand.this,landTypeList,"landTypeList"));
            land_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position>0){
                        land_type_id = landTypeList.get(land_type_spinner.getSelectedItemPosition()).getLand_type_id();
                    }
                    else {
                        land_type_id =0;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
                        Intent camera_screen = new Intent(AddViewLand.this, CameraScreen.class);
                        camera_screen.putExtra("activity_type","Land");
                        camera_screen.putExtra("land_type_id",land_type_id);
                        startActivity(camera_screen);
                        dialog.dismiss();
                    }
                    else {
                        Utils.showAlert(AddViewLand.this,"Please Select Land Type");
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
        landTypeList_item.setLand_type_name_en("Select Land Type");
        landTypeList_item.setLand_type_name_ta("Select Land Type");
        landTypeList.add(landTypeList_item);
        landTypeList.addAll(dbData.get_nursery_land_type());
        Log.d("Size",""+landTypeList.size());

    }

}
