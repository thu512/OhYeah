package com.gogolab.ohyeah.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gogolab.ohyeah.R;
import com.gogolab.ohyeah.model.Req_email;
import com.gogolab.ohyeah.model.Req_use_nest;
import com.gogolab.ohyeah.model.Res;
import com.gogolab.ohyeah.net.SNet;
import com.gogolab.ohyeah.utill.U;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Changjoo on 2017-08-18.
 */

public class NestAddDialog extends Dialog {

    private EditText money;
    private me.grantland.widget.AutofitTextView current_money;
    private TextView error2;
    private int cm=0;
    private Button btn_left; //취소
    private Button btn_right; //확인
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_nest_add_dialog);

        getNest(U.getInstance().getEmail(getContext()));

        money = (EditText) findViewById(R.id.money);
        current_money= (me.grantland.widget.AutofitTextView) findViewById(R.id.current_money);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);
        error2 = (TextView)findViewById(R.id.error2);



        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(result)){     // StackOverflow를 막기위해,
                    result = U.getInstance().toNumFormat(charSequence.toString());   // 에딧텍스트의 값을 변환하여, result에 저장.
                    money.setText(result);    // 결과 텍스트 셋팅.
                    money.setSelection(result.length());     // 커서를 제일 끝으로 보냄.
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                int nest;
                if(s.equals("")){
                    nest = 0;
                }else{
                    nest = Integer.parseInt(U.getInstance().removeComa(s));
                }
                current_money.setText(U.getInstance().toNumFormat(""+(cm-nest)));
                if((cm-nest)<0){
                    error2.setVisibility(View.VISIBLE);
                }else{
                    error2.setVisibility(View.INVISIBLE);
                }
            }
        });

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            btn_left.setOnClickListener(mLeftClickListener);
            btn_right.setOnClickListener(mRightClickListener);
        }

    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public NestAddDialog(Context context,
                         View.OnClickListener leftListener,
                         View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;

    }


    public void getNest(String email){

        showPd();
        Req_email req_email = new Req_email(email);
        Call<Res> res = SNet.getInstance().getAllFactoryIm().getState(req_email);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        U.getInstance().log("남은 비상금: " + response.body().toString());
                        current_money.setText(U.getInstance().toNumFormat(""+response.body().getDoc().getAsset().getSpare_money()));
                        cm = response.body().getDoc().getAsset().getSpare_money();
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


    ProgressDialog pd;
    public void showPd(){
        if(pd == null){
            pd = new ProgressDialog(getContext());
            pd.setCancelable(true);
            pd.setMessage("잠시만 기다려주세요...");
        }
        pd.show();
    }

    public void stopPd(){
        if(pd != null && pd.isShowing()){
            pd.dismiss();
        }
    }


    public void pushServer(){
        showPd();
        Req_use_nest req_use_nest= new Req_use_nest(U.getInstance().getEmail(getContext()), Integer.parseInt(U.getInstance().removeComa(money.getText().toString())));
        Call<Res> res = SNet.getInstance().getAllFactoryIm().nestAdd(req_use_nest);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        U.getInstance().log("비상금 추가: " + response.body().getMsg());
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
}
