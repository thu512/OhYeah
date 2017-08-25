package com.changjoo.ohyeah.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.dialog.ChangePwDialog;
import com.changjoo.ohyeah.model.Req_Change_pw;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePwdActivity extends Activity {
    ImageButton back;
    Button submit;

    EditText pwd;
    EditText newPwd;
    EditText checkNewPwd;
    TextView pwd_max;
    TextView pwd_eng;
    TextView error;

    ImageView newPwdImg;
    ImageView checkNewPwdImg;

    ChangePwDialog changePwDialog;

    Boolean pw_check = false;
    Boolean pw_double =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        newPwdImg = (ImageView)findViewById(R.id.newPwdImg);
        checkNewPwdImg = (ImageView)findViewById(R.id.checkNewPwdImg);


        pwd_max = (TextView)findViewById(R.id.pwd_max);
        pwd_eng = (TextView)findViewById(R.id.pwd_eng);
        error = (TextView)findViewById(R.id.error);

        pwd = (EditText) findViewById(R.id.pwd);
        newPwd = (EditText)findViewById(R.id.newPwd);
        checkNewPwd =(EditText)findViewById(R.id.checkNewPwd);
        submit = (Button)findViewById(R.id.submit);

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        //비밀번호 입력값변화에따른 조건체크
        newPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                boolean f1=false;
                boolean f2=false;

                if (s.length() >= 8 && s.length()<=15){
                    pwd_max.setTextColor(Color.parseColor("#3b4aaa"));
                    f1=true;
                }
                else{
                    pwd_max.setTextColor(Color.parseColor("#c33b4d"));
                    newPwdImg.setImageResource(R.mipmap.check_2);
                    f1=false;
                }

                Pattern p = Pattern.compile("([a-zA-Z])");
                Matcher m = p.matcher(s);
                Pattern p1 = Pattern.compile("([0-9])");
                Matcher m1 = p1.matcher(s);
                if(m.find() && m1.find()){
                    pwd_eng.setTextColor(Color.parseColor("#3b4aaa"));
                    f2=true;
                }else{
                    pwd_eng.setTextColor(Color.parseColor("#c33b4d"));
                    newPwdImg.setImageResource(R.mipmap.check_2);
                    f2=false;
                }

                if(f1 && f2){
                    newPwdImg.setImageResource(R.mipmap.check);
                    //비밀번호 조건식 완료
                    pw_check=true;
                }else{
                    newPwdImg.setImageResource(R.mipmap.check_2);
                    pw_check=false;
                }
            }
        });


        //비밀번호 확인 일치여부 체크
        checkNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(newPwd.getText().toString().equals(s)){
                    checkNewPwdImg.setImageResource(R.mipmap.check);
                    //비밀번호 확인 완료
                    pw_double=true;
                    error.setText("");
                }else{
                    checkNewPwdImg.setImageResource(R.mipmap.check_2);
                    error.setText("비밀번호가 일치하지 않습니다.");
                    pw_double=false;
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(pwd.getText())){
                    pwd.setError("기존비밀번호를 입력하세요.");
                    return;
                }
                if(TextUtils.isEmpty(newPwd.getText())){
                    newPwd.setError("새로운 비밀번호를 입력하세요.");
                    return;
                }
                if(TextUtils.isEmpty(checkNewPwd.getText())){
                    checkNewPwd.setError("비밀번호를 확인 하세요.");
                    return;
                }

                if(pw_check && pw_double){
                    changePw(U.getInstance().getEmail(ChangePwdActivity.this), pwd.getText().toString(), newPwd.getText().toString());
                }

            }
        });


    }


    //일반 이메일 로그인
    public void changePw(String email, String past_pwd, String new_pwd) {


        Req_Change_pw req_change_pw = new Req_Change_pw(email, past_pwd, new_pwd);

        showPd();
        Call<Res> res = SNet.getInstance().getAllFactoryIm().changePw(req_change_pw);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //코드 숫자별로 상황 분할해야함
                        U.getInstance().log("비번수정 "+response.body().toString());

                        if(response.body().getResult()==1) {
                            U.getInstance().log("변경성공");

                            changePwDialog = new ChangePwDialog(ChangePwdActivity.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    changePwDialog.dismiss();
                                    finish();
                                }
                            });
                            changePwDialog.show();

                        }
                        else if(response.body().getResult()==2){
                            U.getInstance().log("기존비밀번호 불일치");
                            error.setText("기존 비밀번호가 맞지 않습니다.");
                            pwd.setText("");

                        } else if(response.body().getResult()==3){
                            U.getInstance().log("기존비번과 동일");
                            error.setText("기존 비밀번호와 일치합니다.");
                            newPwd.setText("");
                            checkNewPwd.setText("");
                        }

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




}
