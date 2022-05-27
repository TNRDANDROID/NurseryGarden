package com.nic.nurserygarden.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.nurserygarden.BuildConfig;
import com.nic.nurserygarden.R;
import com.nic.nurserygarden.databinding.SplashScreenBinding;
import com.nic.nurserygarden.helper.AppVersionHelper;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.Utils;


public class SplashScreen extends AppCompatActivity implements
        AppVersionHelper.myAppVersionInterface {
    private TextView textView;
    private Button button;
    private static int SPLASH_TIME_OUT = 2000;
    private PrefManager prefManager;
    public SplashScreenBinding splashScreenBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashScreenBinding = DataBindingUtil.setContentView(this, R.layout.splash_screen);
        splashScreenBinding.setActivity(this);
        prefManager = new PrefManager(this);
        //if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("production")) {
            if (Utils.isOnline()) {
                checkAppVersion();
            } else {
                showSignInScreen();

            }
        /*} else {
            showSignInScreen();
        }*/
    }


    private void showSignInScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(prefManager.getKEY_Language()!=null&&!prefManager.getKEY_Language().equals("")){
                    Intent i = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SplashScreen.this, ChooseLanguageActivity.class);
                    startActivity(i);
                }

                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkAppVersion() {
        new AppVersionHelper(this, SplashScreen.this).callAppVersionCheckApi();
    }

    @Override
    public void onAppVersionCallback(String value) {
        if (value.length() > 0 && "Update".equalsIgnoreCase(value)) {
            startActivity(new Intent(this, AppUpdateDialog.class));
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            showSignInScreen();
        }

    }

}
