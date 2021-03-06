package com.gogolab.ohyeah.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gogolab.ohyeah.Activity;
import com.gogolab.ohyeah.R;
import com.gogolab.ohyeah.StartActivity;
import com.gogolab.ohyeah.model.NaverProfileModel;
import com.gogolab.ohyeah.model.Req;
import com.gogolab.ohyeah.model.Req_email;
import com.gogolab.ohyeah.model.Req_login;
import com.gogolab.ohyeah.model.Res;
import com.gogolab.ohyeah.net.Net;
import com.gogolab.ohyeah.net.SNet;
import com.gogolab.ohyeah.terms.Terms1Activity;
import com.gogolab.ohyeah.terms.Terms2Activity;
import com.gogolab.ohyeah.utill.U;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private static String OAUTH_CLIENT_ID = StartActivity.OAUTH_CLIENT_ID;  // 1)에서 받아온 값들을 넣어좁니다
    private static String OAUTH_CLIENT_SECRET = StartActivity.OAUTH_CLIENT_SECRET;
    private static String OAUTH_CLIENT_NAME = "오예";
    private static OAuthLogin mOAuthLoginModule;
    private static Context mContext;

    TextView login_error; //로그인 에러메세지 띄움
    TextView login_pro1; //이용약관
    TextView login_pro2; //개인정보 수집
    TextView findpwd;
    EditText email;
    EditText pwd;
    Button login_btn;
    Button join_btn;
    Button id_delete;
    Button pw_delete;

    OAuthLoginButton mOAuthLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        //푸시알림 최초설정
        if(!U.getInstance().getBoolean(this,"first")){
            U.getInstance().setBoolean(this,"first",true);

            U.getInstance().setBoolean(this,"sound",true);
            U.getInstance().setBoolean(this,"vive",true);
        }
        U.getInstance().log("파베 토큰(로그인): "+FirebaseInstanceId.getInstance().getToken());
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
        id_delete = (Button)findViewById(R.id.id_delete);
        pw_delete = (Button)findViewById(R.id.pw_delete);
        findpwd = (TextView)findViewById(R.id.findpwd);
        login_error = (TextView) findViewById(R.id.login_error);
        login_pro1 = (TextView) findViewById(R.id.login_pro1);
        login_pro2 = (TextView) findViewById(R.id.login_pro2);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        login_btn = (Button) findViewById(R.id.login_btn);
        join_btn = (Button) findViewById(R.id.join_btn);


        findpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindPwdActivity.class);
                startActivity(intent);
            }
        });

        //글자입력시 지우기 버튼 생성
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(s.length()>=1){
                    id_delete.setVisibility(View.VISIBLE);
                }else{
                    id_delete.setVisibility(View.INVISIBLE);
                }
            }
        });

        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(s.length()>=1){
                    pw_delete.setVisibility(View.VISIBLE);
                }else{
                    pw_delete.setVisibility(View.INVISIBLE);
                }
            }
        });



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
                if (TextUtils.isEmpty(email.getText().toString())) {
                    email.setError("이메일을 입력하세요.");
                    return;
                }
                if (TextUtils.isEmpty(pwd.getText().toString())) {
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



            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Toast.makeText(LoginActivity.this, "로그인이 취소/실패 하였습니다.!" + "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    //네이버아이디로 가입,로그인
    public void getProfile(String accessToken) {
        //헤더 작성
        String authorization = "Bearer " + accessToken;
        showPd();
        Call<NaverProfileModel> res = Net.getInstance().getAllFactoryIm().profile(authorization);
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

                            U.getInstance().log(profile.toString());

                            //중복확인 요청 모델
                            Req_email req_email = new Req_email(profile.getEmail());
                            String pw= profile.getId();

                            checkServer(req_email,pw);

                            U.getInstance().log("네이버 개인정보 불러오기 성공");
                        } else {
                            U.getInstance().log("실패");
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

                stopPd();
            }

            @Override
            public void onFailure(Call<NaverProfileModel> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }



    //일반 이메일 로그인
    public void login(String email, String pwd) {

        Req_login req_login = new Req_login();
        req_login.setEmail(email);
        req_login.setPw(pwd);
        U.getInstance().log(req_login.toString());
        showPd();
        Call<Res> res = SNet.getInstance().getAllFactoryIm().login(req_login);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //코드 숫자별로 상황 분할해야함
                        U.getInstance().log("응답직후"+response.body().toString());
                        if(response.body().getResult()==1){
                            Toast.makeText(LoginActivity.this,"로그인되었습니다.",Toast.LENGTH_SHORT).show();
                            //로그인 성공시 -> sp저장
                            U.getInstance().setEmail(LoginActivity.this, response.body().getDoc().getMember().getEmail());
                            U.getInstance().log(""+response.body());

                            //초기 예산 설정 되있는지 여부 체크
                            if(response.body().getDoc().getMember().getSetb_yn().equals("N")){
                                U.getInstance().setBoolean(LoginActivity.this,response.body().getDoc().getMember().getEmail(),false);
                                Intent intent = new Intent(LoginActivity.this,BudgetSettingActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        }else if(response.body().getResult()==-1){
                            login_error.setVisibility(View.VISIBLE);
                            login_error.setText("아이디가 존재하지 않습니다.");

                        }else if(response.body().getResult()==-2){
                            login_error.setVisibility(View.VISIBLE);
                            login_error.setText("비밀번호가 틀렸습니다.");
                        }else{
                            Toast.makeText(LoginActivity.this,"로그인실패."+response.body().getResult(),Toast.LENGTH_SHORT).show();
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

                stopPd();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }



    //네이버 로그인 시 아이디 확인
    public void checkServer(final Req_email req_email, final String pw){
        //===========서버로 전송==========================================================
        //받아온이메일을 중복확인 -> 중복된이메일 존재 하면 sp이메일저장후 메인으로 점프
        //                    -> 중복 이메일 존재 하지않으면 회원가입도메인으로 ㄱㄱ
        showPd();
        Call<Res> res1 = SNet.getInstance().getAllFactoryIm().check_email(req_email);
        res1.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //코드 숫자별로 상황 분할해야함
                        //이미 서버에 등록된 이메일이면 그냥 로그인, 처음 등록자면 서버에 등록후 로그인
                        if(response.body().getResult()==1){
                            U.getInstance().log("네이버 - 회원가입 성공");
                            //회원가입 도메인으로 보내기

                            //회원가입 요청모델
                            Req req = new Req();
                            req.setEmail(req_email.getEmail());
                            req.setPwd(pw);
                            req.setToken(FirebaseInstanceId.getInstance().getToken());
                            signUp(req);

                        }else if(response.body().getResult()==2){
                            U.getInstance().log("네이버 - 아이디 중복");
                            login(req_email.getEmail(),pw);
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
                stopPd();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }



    //회원가입
    public void signUp(Req req_login){
        showPd();
        Call<Res> res1 = SNet.getInstance().getAllFactoryIm().join(req_login);
        res1.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(response.body().getResult()==1){
                            U.getInstance().log("회원가입 성공");

                            U.getInstance().setEmail(LoginActivity.this,response.body().getDoc().getMember().getEmail());

                            U.getInstance().log( ""+U.getInstance().getEmail(LoginActivity.this));
                            //응답완료되면 예산설정 페이지로
                            Intent intent = new Intent(LoginActivity.this,BudgetSettingActivity.class);
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
                stopPd();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }


    public void term1(View view){
        Intent intent = new Intent(LoginActivity.this, Terms1Activity.class);
        startActivity(intent);
    }

    public void term2(View view){
        Intent intent = new Intent(LoginActivity.this, Terms2Activity.class);
        startActivity(intent);
    }


    //x버튼 클릭시 입력한데이터 삭제
    public void textDelete(View view){
        if(view.equals(id_delete)){
            email.setText("");
        }else if(view.equals(pw_delete)){
            pwd.setText("");
        }
    }

    //백키에 대응하는 메소드
    @Override
    public void onBackPressed() {
        //아래코드를 막으면 현재 화면의 종료처리가 중단됨
        //super.onBackPressed();
        if(!isFirstEnd){
            //최초한번 백키를 눌렀다.
            isFirstEnd=true;
            //3초후에 초기화된다.(최초로 한번 백키를 눌렀던 상황이)
            handler.sendEmptyMessageDelayed(1,3000);
            Toast.makeText(this,"뒤로가기를 한번 더 누르시면 종료됩니다.",Toast.LENGTH_LONG).show();
        }else{
            super.onBackPressed();
        }
    }

    boolean isFirstEnd; //백키를 한번 눌렀나?

    //핸들러, 메세지를 던져서 큐에 담고 하나씩 꺼내서 처리하는 메시징 시스템
    Handler handler = new Handler(){
        //이 메소드는 큐에 메세지가 존재하면 뽑아서 호출된다.
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){ //최초로 백키를 한번 눌렀다.

            }else if(msg.what == 1){ //3초가 지났다. 다시 초기화.
                isFirstEnd=false;
            }
        }
    };
}
