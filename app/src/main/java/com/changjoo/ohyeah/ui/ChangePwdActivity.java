package com.changjoo.ohyeah.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.changjoo.ohyeah.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePwdActivity extends AppCompatActivity {
    ImageButton back;
    EditText newPwd;
    EditText checkNewPwd;
    TextView pwd_max;
    TextView pwd_eng;
    ImageView newPwdImg;
    ImageView checkNewPwdImg;

    Boolean pw_check;
    Boolean pw_double;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        newPwdImg = (ImageView)findViewById(R.id.newPwdImg);
        checkNewPwdImg = (ImageView)findViewById(R.id.checkNewPwdImg);

        pwd_max = (TextView)findViewById(R.id.pwd_max);
        pwd_eng = (TextView)findViewById(R.id.pwd_eng);

        newPwd = (EditText)findViewById(R.id.newPwd);
        checkNewPwd =(EditText)findViewById(R.id.checkNewPwd);

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
                }else{
                    checkNewPwdImg.setImageResource(R.mipmap.check_2);
                }
            }
        });
    }
}
