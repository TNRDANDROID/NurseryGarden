package com.nic.nurserygarden.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.databinding.AppUpdateDialogBinding;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.support.MyCustomTextView;
import com.nic.nurserygarden.utils.Utils;


public class AppUpdateDialog extends AppCompatActivity implements View.OnClickListener {


    private MyCustomTextView btnSave;
    private AppUpdateDialogBinding appUpdateDialogBinding;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUpdateDialogBinding = DataBindingUtil.setContentView(this, R.layout.app_update_dialog);
        appUpdateDialogBinding.setActivity(this);
        prefManager = new PrefManager(this);
        Utils.setLocale(prefManager.getKEY_Language(),this);
        intializeUI();

    }

    public void intializeUI() {
        appUpdateDialogBinding.btnOk.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                showGooglePlay();
                break;
        }
    }

    public void showGooglePlay() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drdpr.tn.gov.in/")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drdpr.tn.gov.in/")));
        }
    }
}
