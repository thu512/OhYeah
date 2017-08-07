package com.changjoo.ohyeah.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.NaverProfileModel;
import com.changjoo.ohyeah.model.Req_Join;
import com.changjoo.ohyeah.model.Req_login;
import com.changjoo.ohyeah.model.Res_Join;
import com.changjoo.ohyeah.model.Res_login;
import com.changjoo.ohyeah.net.Net;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private static String OAUTH_CLIENT_ID = "mMjBFyvGtnq61b5DSk4s";  // 1)에서 받아온 값들을 넣어좁니다
    private static String OAUTH_CLIENT_SECRET = "UwOSuRj5jr";
    private static String OAUTH_CLIENT_NAME = "오예";
    private static OAuthLogin mOAuthLoginModule;
    private static Context mContext;

    TextView login_error; //로그인 에러메세지 띄움
    TextView login_pro1; //이용약관
    TextView login_pro2; //개인정보 수집
    EditText email;
    EditText pwd;
    Button login_btn;
    Button join_btn;
    OAuthLoginButton mOAuthLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this
                , OAUTH_CLIENT_ID
                , OAUTH_CLIENT_SECRET
                , OAUTH_CLIENT_NAME);
        initSetting();

    }


    //네이버 로그인 버튼 및 뷰 세팅
    private void initSetting() {
        login_error = (TextView)findViewById(R.id.login_error);
        login_pro1 = (TextView)findViewById(R.id.login_pro1);
        login_pro2 = (TextView)findViewById(R.id.login_pro2);
        email     = (EditText)findViewById(R.id.email);
        pwd       = (EditText)findViewById(R.id.pwd);
        login_btn = (Button)findViewById(R.id.login_btn);
        join_btn  = (Button)findViewById(R.id.join_btn);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, JoinActivity.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    email.setError("이메일을 입력하세요.");
                    return;
                }
                if(TextUtils.isEmpty(pwd.getText().toString())){
                    pwd.setError("비밀번호를 입력하세요.");
                    return;
                }

                //로그인
                login(email.getText().toString(), pwd.getText().toString());
            }
        });

        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setBgResourceId(R.drawable.login_white2);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        mOAuthLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
            }
        });
    }


    //startOauthLoginActivity()메소드 호출시 해당 핸들러 수행
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                String tokenType = mOAuthLoginModule.getTokenType(mContext);

                Log.i("NAVER", "accessToken  " + accessToken);

                Log.i("NAVER", "refreshToken  " + refreshToken);

                Log.i("NAVER", "String.valueOf(expiresAt)  " + String.valueOf(expiresAt));

                Log.i("NAVER", "tokenType  " + tokenType);

                Log.i("NAVER", "mOAuthLoginInstance.getState(mContext).toString()  " + mOAuthLoginModule.getState(mContext).toString());

                //로그인이 성공하면  네이버에 계정값들을 가져온다.
                getProfile(accessToken);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Toast.makeText(LoginActivity.this, "로그인이 취소/실패 하였습니다.!"+"errorCode:" + errorCode +", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    //네이버아이디로 가입,로그인
    public void getProfile(String accessToken) {
        //헤더 작성
        String authorization = "Bearer " + accessToken;

        Call<NaverProfileModel> res = Net.getInstance().getMemberFactoryIm().profile(authorization);
        //응답 처리 작업
        res.enqueue(new Callback<NaverProfileModel>() {
            @Override
            public void onResponse(Call<NaverProfileModel> call, Response<NaverProfileModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String message = response.body().getMessage();
                        if (message.equals("success")) {
                            //네이버 개인정보 받아옴
                            NaverProfileModel.Response profile = response.body().getResponse();
                            final Req_Join req_join = new Req_Join();
                            req_join.setResponse(profile);
                            U.getInstance().log(profile.toString());

                            //===========서버로 전송==========================================================

                            Call<Res_Join> res1 = SNet.getInstance().getMemberFactoryIm().join(req_join);
                            res1.enqueue(new Callback<Res_Join>() {
                                @Override
                                public void onResponse(Call<Res_Join> call, Response<Res_Join> response) {
                                    if(response.isSuccessful()){
                                        if(response.body() != null){
                                            //코드 숫자별로 상황 분할해야함
                                            //이미 서버에 등록된 이메일이면 그냥 로그인, 처음 등록자면 서버에 등록후 로그인
                                            U.getInstance().log("회원가입 성공");
                                            Res_Join res_join = new Res_Join();
                                            res_join.setCode(response.body().getCode());
                                            res_join.setMsg(response.body().getMsg());
                                            res_join.setEmail(response.body().getEmail());
                                            U.getInstance().log(res_join.toString());

                                            //네아로 최초등록 후 회원정보 받아와서 서버로 전송
                                            //전송 성공시 서버에서 id만 반환해줌
                                            //id받아서 SharedPreference에 저장
                                            U.getInstance().setLogin(LoginActivity.this,response.body().getEmail());

                                        }else{
                                            U.getInstance().log("통신실패1");
                                        }
                                    }else{
                                        try {
                                            U.getInstance().log("통신실패2"+response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<Res_Join> call, Throwable t) {
                                    U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                                }
                            });
                            Toast.makeText(LoginActivity.this, "프로필을 불러오는데 성공하였습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "프로필을 불러오는데 실패하였습니다.", Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<NaverProfileModel> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
            }
        });
    }

    //일반 이메일 로그인
    public void login(String email,String pwd){

        Req_login req_login = new Req_login();
        req_login.setEmail(email);
        req_login.setPwd(pwd);

        Call<Res_login> res = SNet.getInstance().getMemberFactoryIm().login(req_login);
        res.enqueue(new Callback<Res_login>() {
            @Override
            public void onResponse(Call<Res_login> call, Response<Res_login> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        //코드 숫자별로 상황 분할해야함

                        //로그인 성공시 -> sp저장
                        U.getInstance().setLogin(LoginActivity.this,response.body().getEmail());

                    }else{
                        U.getInstance().log("통신실패1");
                    }
                }else{
                    try {
                        U.getInstance().log("통신실패2"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Res_login> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
            }
        });
    }


}
