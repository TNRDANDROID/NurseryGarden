package com.nic.nurserygarden.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.nic.nurserygarden.R;
import com.nic.nurserygarden.databinding.ActivityChooseLanguageBinding;
import com.nic.nurserygarden.databinding.SplashScreenBinding;
import com.nic.nurserygarden.session.PrefManager;
import com.nic.nurserygarden.utils.Utils;

public class ChooseLanguageActivity extends AppCompatActivity {
    private PrefManager prefManager;
    public ActivityChooseLanguageBinding chooseLanguageBinding;
    boolean click = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseLanguageBinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_language);
        chooseLanguageBinding.setActivity(this);
        prefManager = new PrefManager(this);

        chooseLanguageBinding.tamilLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = true;
                prefManager.setKEY_Language("ta");
                chooseLanguageBinding.tamilLanguage.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                chooseLanguageBinding.englishLanguage.setCardBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        chooseLanguageBinding.englishLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click = true;
                prefManager.setKEY_Language("en");
                chooseLanguageBinding.tamilLanguage.setCardBackgroundColor(getResources().getColor(R.color.white));
                chooseLanguageBinding.englishLanguage.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        chooseLanguageBinding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click){
                    showSignInScreen();
                }
                else {
                    Utils.showAlert(ChooseLanguageActivity.this,"Please Choose Language!");
                }
            }
        });
    }

    private void showSignInScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(ChooseLanguageActivity.this, LoginScreen.class);

                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }, 0);
    }
}
