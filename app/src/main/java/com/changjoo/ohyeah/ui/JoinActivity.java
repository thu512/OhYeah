package com.changjoo.ohyeah.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        email_check_img=(ImageView)findViewById(R.id.email_check_img);
        pwd_check_img=(ImageView)findViewById(R.id.pwd_check_img);
        pwd2_check_img=(ImageView)findViewById(R.id.pwd2_check_img);

        email = (EditText)findViewById(R.id.email);
        pwd = (EditText)findViewById(R.id.pwd);
        pwd2 = (EditText)findViewById(R.id.pwd2);

        error_msg = (TextView)findViewById(R.id.error_msg);
        pwd_max = (TextView)findViewById(R.id.pwd_max);
        pwd_eng = (TextView)findViewById(R.id.pwd_eng);

        dura_button = (Button)findViewById(R.id.dura_button);
        join_cancel = (Button)findViewById(R.id.join_cancel);
        join_ok = (Button)findViewById(R.id.join_ok);

        //이메일 중복확인
        dura_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //중복확인 완료시 버튼이미지 변경
            }
        });

        //비밀번호 입력값변화에따른 조건체크
        pwd.addTextChangedListener(new TextWatcher() {
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
                    pwd_check_img.setImageResource(R.mipmap.check_2);
                    f1=false;
                }

                Pattern p = Pattern.compile("([a-zA-Z0-9].*[!,@,#,$,%,?,/])|([!,@,#,$,%,?,/].*[a-zA-Z0-9])");
                Matcher m = p.matcher(s);
                if(m.find()){
                    pwd_eng.setTextColor(Color.parseColor("#3b4aaa"));
                    f2=true;
                }else{
                    pwd_eng.setTextColor(Color.parseColor("#c33b4d"));
                    pwd_check_img.setImageResource(R.mipmap.check_2);
                    f2=false;
                }

                if(f1 && f2){
                    pwd_check_img.setImageResource(R.mipmap.check);
                }else{
                    pwd_check_img.setImageResource(R.mipmap.check_2);
                }
            }
        });


        //비밀번호 확인 일치여부 체크
        pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(pwd.getText().toString().equals(s)){
                    error_msg.setVisibility(View.INVISIBLE);
                    pwd2_check_img.setImageResource(R.mipmap.check);
                }else{
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
                Intent intent = new Intent(JoinActivity.this,BudgetSettingActivity.class);
                startActivity(intent);
            }
        });
    }
}
