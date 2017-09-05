package com.changjoo.ohyeah;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.ui.BudgetSettingActivity;
import com.changjoo.ohyeah.ui.LoginActivity;
import com.changjoo.ohyeah.ui.MainActivity;
import com.changjoo.ohyeah.utill.U;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends android.app.Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission_sms();

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
                    SweetAlertDialog sweetAlertDialog = U.getInstance().showPopup3(this,
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
                    sweetAlertDialog.show();
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
