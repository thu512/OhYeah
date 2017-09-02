package com.changjoo.ohyeah.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.dialog.FindPwdDialog;
import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPwdActivity extends Activity {

    Button submit;
    EditText email;
    Button email_delete;
    ImageButton back;
    FindPwdDialog findPwdDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        submit = (Button)findViewById(R.id.submit);
        email_delete = (Button)findViewById(R.id.email_delete);
        email = (EditText)findViewById(R.id.email);
        back = (ImageButton)findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#d8d8d8"));
        }
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
        }

        //글자입력시  지우기 버튼 생성
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
                    email_delete.setVisibility(View.VISIBLE);
                }else{
                    email_delete.setVisibility(View.INVISIBLE);
                }
            }
        });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(email.getText())){
                    email.setError("이메일을 입력해주세요.");
                    return;
                }
                if(!checkEmail(email.getText().toString())){
                    email.setError("올바른 이메일 형식을 입력해주세요.");
                    return;
                }
                find(email.getText().toString());
            }
        });

    }

    //x버튼 클릭시 입력한데이터 삭제
    public void textDelete(View view){
        if(view.equals(email_delete)){
            email.setText("");
        }
    }


    public void find(String email){
        showPd();
        Req_email req_email= new Req_email(email);
        Call<Res> res = SNet.getInstance().getAllFactoryIm().findPwd(req_email);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        U.getInstance().log("비번찾기: "+response.body().toString());
                        if(response.body().getResult() == 1){
                            findPwdDialog= new FindPwdDialog(FindPwdActivity.this, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    findPwdDialog.dismiss();
                                    finish();
                                }
                            });
                            findPwdDialog.show();

                        }else{
                            Toast.makeText(FindPwdActivity.this,"오류가 발생하였습니다.",Toast.LENGTH_SHORT).show();
                            U.getInstance().log("서버오류 비번찾기");
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
                Toast.makeText(FindPwdActivity.this,"올바른 메일주소를 입력하세요.",Toast.LENGTH_SHORT).show();
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
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
