package com.gogolab.ohyeah.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gogolab.ohyeah.Activity;
import com.gogolab.ohyeah.R;
import com.gogolab.ohyeah.model.Req;
import com.gogolab.ohyeah.model.Req_email;
import com.gogolab.ohyeah.model.Res;
import com.gogolab.ohyeah.net.SNet;
import com.gogolab.ohyeah.utill.U;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends Activity {
    ImageView email_check_img;
    ImageView pwd_check_img;
    ImageView pwd2_check_img;

    EditText email;
    EditText pwd;
    EditText pwd2;

    TextView error_msg;
    TextView pwd_max; //최소 8자리, 최대 15자리 이하
    TextView pwd_eng; //영문, 숫자 및 기호 조합

    Button dura_button;
    Button join_cancel;
    Button join_ok;
    Button email_delete;
    Button pw_delete;
    Button pw_ck_delete;


    Boolean id_check = false;
    Boolean pw_check = false;
    Boolean pw_double = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        U.getInstance().log("파베 토큰(회원가입): " + FirebaseInstanceId.getInstance().getToken());
        email_check_img = (ImageView) findViewById(R.id.email_check_img);
        pwd_check_img = (ImageView) findViewById(R.id.pwd_check_img);
        pwd2_check_img = (ImageView) findViewById(R.id.pwd2_check_img);

        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.pwd);
        pwd2 = (EditText) findViewById(R.id.pwd2);

        error_msg = (TextView) findViewById(R.id.error_msg);
        pwd_max = (TextView) findViewById(R.id.pwd_max);
        pwd_eng = (TextView) findViewById(R.id.pwd_eng);

        dura_button = (Button) findViewById(R.id.dura_button);
        join_cancel = (Button) findViewById(R.id.join_cancel);
        join_ok = (Button) findViewById(R.id.join_ok);

        email_delete = (Button) findViewById(R.id.email_delete);
        pw_delete = (Button) findViewById(R.id.pw_delete);
        pw_ck_delete = (Button) findViewById(R.id.pw_ck_delete);

        //이메일 중복확인
        dura_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //이메일 형식 체크하기=======================================================================================================
                if(!checkEmail(email.getText().toString())){
                    email.setError("사용가능한 이메일 형식을 입력하세요.");
                    return;
                }
                //중복확인 완료시 버튼이미지 변경
                if (TextUtils.isEmpty(email.getText().toString())) {
                    email.setError("이메일을 입력하세요.");
                    return;
                }

                showPd();

                Req_email req_email = new Req_email(email.getText().toString().trim());

                Call<Res> res1 = SNet.getInstance().getAllFactoryIm().check_email(req_email);
                res1.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getResult() == 1) {
                                    //사용 가능
                                    //중복확인되면
                                    id_check = true;
                                    error_msg.setText("사용가능한 이메일 입니다.");
                                    error_msg.setTextColor(Color.parseColor("#3b4aaa"));
                                    error_msg.setVisibility(View.VISIBLE);
                                    email_check_img.setImageResource(R.mipmap.check);
                                    dura_button.setBackgroundResource(R.mipmap.button1);
                                    dura_button.setEnabled(false);
                                    email.setEnabled(false);
                                    email_delete.setEnabled(false);
                                } else {
                                    //중복
                                    error_msg.setVisibility(View.VISIBLE);
                                    error_msg.setText("중복된 아이디가 존재합니다.");
                                    error_msg.setTextColor(Color.parseColor("#c33b4d"));
                                    email_check_img.setImageResource(R.mipmap.check_2);
                                }

                                U.getInstance().log("" + response.body().toString());

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
        });

        //비밀번호 입력값변화에따른 조건체크
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
                boolean f1 = false;
                boolean f2 = false;

                if (s.length() >= 8 && s.length() <= 15) {
                    pwd_max.setTextColor(Color.parseColor("#3b4aaa"));
                    f1 = true;
                } else {
                    pwd_max.setTextColor(Color.parseColor("#c33b4d"));
                    pwd_check_img.setImageResource(R.mipmap.check_2);
                    f1 = false;
                }

                Pattern p = Pattern.compile("([a-zA-Z])");
                Matcher m = p.matcher(s);
                Pattern p1 = Pattern.compile("([0-9])");
                Matcher m1 = p1.matcher(s);
                if (m.find() && m1.find()) {
                    pwd_eng.setTextColor(Color.parseColor("#3b4aaa"));
                    f2 = true;
                } else {
                    pwd_eng.setTextColor(Color.parseColor("#c33b4d"));
                    pwd_check_img.setImageResource(R.mipmap.check_2);
                    f2 = false;
                }

                if (f1 && f2) {
                    pwd_check_img.setImageResource(R.mipmap.check);
                    //비밀번호 조건식 완료
                    pw_check = true;
                } else {
                    pwd_check_img.setImageResource(R.mipmap.check_2);
                }
            }
        });


        //비밀번호 확인 일치여부 체크
        pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (pwd.getText().toString().equals(s)) {
                    error_msg.setVisibility(View.INVISIBLE);
                    pwd2_check_img.setImageResource(R.mipmap.check);
                    //비밀번호 확인 완료
                    pw_double = true;
                } else {
                    error_msg.setText("비밀번호가 일치하지 않습니다.");
                    error_msg.setTextColor(Color.parseColor("#c33b4d"));
                    error_msg.setVisibility(View.VISIBLE);
                    pwd2_check_img.setImageResource(R.mipmap.check_2);
                }
            }
        });

        join_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        join_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!id_check) {
                    error_msg.setText("이메일 중복확인을 해주세요.");
                    error_msg.setTextColor(Color.parseColor("#c33b4d"));
                    error_msg.setVisibility(View.VISIBLE);
                    return;
                }
                if (id_check && pw_check && pw_double) {
                    showPd();
                    Req req_login = new Req();
                    final String id = email.getText().toString();
                    String pw = pwd.getText().toString();
                    req_login.setEmail(id);
                    req_login.setPwd(pw);
                    req_login.setToken(FirebaseInstanceId.getInstance().getToken());
                    U.getInstance().log("" + pw);
                    //이면 통신보냄
                    Call<Res> res1 = SNet.getInstance().getAllFactoryIm().join(req_login);
                    res1.enqueue(new Callback<Res>() {
                        @Override
                        public void onResponse(Call<Res> call, Response<Res> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    U.getInstance().log("회원가입 성공");

                                    U.getInstance().log("" + response.body().toString());

                                    U.getInstance().setEmail(JoinActivity.this, id);

                                    U.getInstance().log("" + U.getInstance().getEmail(JoinActivity.this));
                                    //응답완료되면 예산설정 페이지로
                                    Intent intent = new Intent(JoinActivity.this, BudgetSettingActivity.class);
                                    startActivity(intent);
                                    finish();
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

                } else {
                    return;
                }
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
                if (s.length() >= 1) {
                    email_delete.setVisibility(View.VISIBLE);
                } else {
                    email_delete.setVisibility(View.INVISIBLE);
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
                if (s.length() >= 1) {
                    pw_delete.setVisibility(View.VISIBLE);
                } else {
                    pw_delete.setVisibility(View.INVISIBLE);
                }
            }
        });

        pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.length() >= 1) {
                    pw_ck_delete.setVisibility(View.VISIBLE);
                } else {
                    pw_ck_delete.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    //x버튼 클릭시 입력한데이터 삭제
    public void textDelete(View view) {
        if (view.equals(email_delete)) {
            email.setText("");
        } else if (view.equals(pw_delete)) {
            pwd.setText("");
        } else if (view.equals(pw_ck_delete)) {
            pwd2.setText("");
        }
    }

    //이메일 형식 체크
    public boolean checkEmail(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isNormal = m.matches();
        return isNormal;
    }


}
