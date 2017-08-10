package com.changjoo.ohyeah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.changjoo.ohyeah.ui.BudgetSettingActivity;
import com.changjoo.ohyeah.ui.LoginActivity;
import com.changjoo.ohyeah.ui.MainActivity;
import com.changjoo.ohyeah.utill.U;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

public class StartActivity extends AppCompatActivity {
    SharedPreferences auto;
    OAuthLogin oAuthLogin = OAuthLogin.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        oAuthLogin = OAuthLogin.getInstance();
        String accessToken = oAuthLogin.getAccessToken(this);
        String refreshToken = oAuthLogin.getRefreshToken(this);
        Log.i("NAVER", "접근 토큰: " + accessToken);
        Log.i("NAVER", "갱신 토큰: " + refreshToken);

        SharedPreferences pref = this.getSharedPreferences("NaverOAuthLoginPreferenceData",MODE_PRIVATE);
        String ac = pref.getString("REFRESH_TOKEN","");
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



    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = oAuthLogin.getAccessToken(StartActivity.this);
                String refreshToken = oAuthLogin.getRefreshToken(StartActivity.this);

                Log.i("NAVER", "accessToken  " + accessToken);
                Log.i("NAVER", "refreshToken  " + refreshToken);
                Log.i("NAVER", "mOAuthLoginInstance.getState(mContext).toString()  " + oAuthLogin.getState(StartActivity.this).toString());

                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
                //new RequestApiTask().execute();

            } else {
                String errorCode = oAuthLogin.getLastErrorCode(StartActivity.this).getCode();
                String errorDesc = oAuthLogin.getLastErrorDesc(StartActivity.this);

                Toast.makeText(StartActivity.this, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();

                Toast.makeText(StartActivity.this, "로그인이 취소/실패 하였습니다.!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
