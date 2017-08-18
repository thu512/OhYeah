package com.changjoo.ohyeah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.changjoo.ohyeah.ui.BudgetSettingActivity;
import com.changjoo.ohyeah.ui.LoginActivity;
import com.changjoo.ohyeah.ui.MainActivity;
import com.changjoo.ohyeah.utill.U;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        U.getInstance().log("로그인: "+U.getInstance().getEmail(StartActivity.this));

        if(!U.getInstance().isLogin(StartActivity.this)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            if(U.getInstance().getBoolean(StartActivity.this,U.getInstance().getEmail(StartActivity.this))==true){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(this, BudgetSettingActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
