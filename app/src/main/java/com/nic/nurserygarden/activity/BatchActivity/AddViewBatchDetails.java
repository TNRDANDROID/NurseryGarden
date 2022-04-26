package com.nic.nurserygarden.activity.BatchActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.activity.CameraScreen;
import com.nic.nurserygarden.dataBase.DBHelper;
import com.nic.nurserygarden.dataBase.dbData;
import com.nic.nurserygarden.databinding.ActivityAddViewBatchDetailsBinding;
import com.nic.nurserygarden.databinding.CameraScreenBinding;

public class AddViewBatchDetails extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddViewBatchDetailsBinding batchDetailsBinding;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private com.nic.nurserygarden.dataBase.dbData dbData = new dbData(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        batchDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_view_batch_details);
        batchDetailsBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        batchDetailsBinding.addBatchBtn.setOnClickListener(this::onClick);
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
            LinearLayout land_layout = dialog.findViewById(R.id.land_layout);
            LinearLayout batch_layout = dialog.findViewById(R.id.batch_layout);
            TextView tittle_text = dialog.findViewById(R.id.tittle_text);
            tittle_text.setText("Add Batch Details");
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
}
