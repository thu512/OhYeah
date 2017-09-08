package com.gogolab.ohyeah;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.gogolab.ohyeah.model.Req_email;
import com.gogolab.ohyeah.model.Res;
import com.gogolab.ohyeah.net.SNet;
import com.gogolab.ohyeah.ui.BudgetSettingActivity;
import com.gogolab.ohyeah.ui.LoginActivity;
import com.gogolab.ohyeah.ui.MainActivity;
import com.gogolab.ohyeah.utill.U;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nhn.android.naverlogin.OAuthLoginDefine;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends android.app.Activity {

    public static String MAIN_SERVER_DOMAIN;
    public static String OAUTH_CLIENT_ID;
    public static String OAUTH_CLIENT_SECRET;
    public static String VERSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OAuthLoginDefine.DEVELOPER_VERSION = false;
        U.getInstance().log(""+U.getInstance().getBoolean(this,"first"));


        //네트워크 연결체크
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (wifi.isConnected() || mobile.isConnected()) {
            //("연결됨" , "연결이 되었습니다.);
            U.getInstance().log("네트워크 연결됨");
        } else {
            U.getInstance().showPopup3(this,
                    "알림",
                    "네트워크연결을 확인해주세요.",
                    "확인",
                    new SweetAlertDialog.OnSweetClickListener(){
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    },
                    null,
                    null
            );

        }


        final FirebaseRemoteConfig config = FirebaseRemoteConfig.getInstance();
        //2.설정
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        config.setConfigSettings(configSettings);
        //3.패치
        //디버깅 모드에서는 0, 상용에서는 3600
        long cacheExpiration = 3600; // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (config.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        config.fetch(cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //4.성공 했을 경우에만 실제 fetch진행
                if(task.isSuccessful()){
                    //실제패치
                    config.activateFetched();
                    //5.획득
                    MAIN_SERVER_DOMAIN = config.getString("MAIN_SERVER_DOMAIN");
                    OAUTH_CLIENT_ID = config.getString("OAUTH_CLIENT_ID");
                    OAUTH_CLIENT_SECRET = config.getString("OAUTH_CLIENT_SECRET");
                    VERSION = config.getString("VERSION");

                    checkPermission_sms();
                }else{

                }
            }
        });


    }


    public void checkPermission_sms(){

        int permissionCheck_SMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);


        if( permissionCheck_SMS != PackageManager.PERMISSION_GRANTED ){
            // 권한이 없을경우

            // 최초 권한 요청인지, 혹은 사용자에 의한 재요청인지 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECEIVE_SMS)) {
                // 사용자가 임의로 권한을 취소시킨 경우
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS},1000);

            } else {
                // 최초로 권한을 요청하는 경우(첫실행)
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS},1000);
            }
        }else {
            // 사용 권한이 있음을 확인한 경우
            startApp();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1000: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 동의 버튼 선택
                    startApp();
                } else {
                    // 사용자가 권한 동의를 안함
                    // 권한 동의안함 버튼 선택
                    U.getInstance().showPopup3(this,
                            "알림",
                            "권한사용을 동의해주셔야 이용이 가능합니다.",
                            "확인",
                            new SweetAlertDialog.OnSweetClickListener(){
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    finish();
                                }
                            },
                            null,
                            null
                    );
                }
                return;
            }
        }
    }
    public void startApp(){
        U.getInstance().log("파베 토큰: "+FirebaseInstanceId.getInstance().getToken());
        U.getInstance().log("로그인: "+U.getInstance().getEmail(StartActivity.this));

        if(!U.getInstance().isLogin(StartActivity.this)){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{

            Req_email req_email = new Req_email(U.getInstance().getEmail(this));
            Call<Res> res = SNet.getInstance().getAllFactoryIm().getState(req_email);
            res.enqueue(new Callback<Res>() {
                @Override
                public void onResponse(Call<Res> call, Response<Res> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {

                            U.getInstance().log("예산설정여부: " + response.body().toString());
                            if(response.body().getDoc().getMember().getSetb_yn().equals("Y")){
                                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(StartActivity.this, BudgetSettingActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            U.getInstance().log("통신실패1");
                            finish();
                        }
                    } else {
                        try {
                            U.getInstance().log("통신실패2" + response.errorBody().string());
                            finish();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Res> call, Throwable t) {
                    U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                    finish();
                }
            });
        }
    }
}
