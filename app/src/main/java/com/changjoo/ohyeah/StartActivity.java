package com.changjoo.ohyeah;

import android.content.Intent;
import android.os.Bundle;

import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.ui.BudgetSettingActivity;
import com.changjoo.ohyeah.ui.LoginActivity;
import com.changjoo.ohyeah.ui.MainActivity;
import com.changjoo.ohyeah.utill.U;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends android.app.Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
                        }
                    } else {
                        try {
                            U.getInstance().log("통신실패2" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Res> call, Throwable t) {
                    U.getInstance().log("통신실패3" + t.getLocalizedMessage());

                }
            });
        }
    }
}
