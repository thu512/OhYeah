package com.changjoo.ohyeah.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.Req_Budget;
import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Changjoo on 2017-08-18.
 */

public class NewBudgetDialog extends Dialog {

    EditText money;

    Button btn_right; //확인

    private View.OnClickListener mRightClickListener;
    String result="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_new_budget_dialog);

        getNest(U.getInstance().getEmail(getContext()));

        money = (EditText) findViewById(R.id.money);

        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().equals(result)){     // StackOverflow를 막기위해,

                    result = U.getInstance().toNumFormat(charSequence.toString());
                    money.setText(result);    // 결과 텍스트 셋팅.
                    money.setSelection(result.length());     // 커서를 제일 끝으로 보냄.
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_right = (Button) findViewById(R.id.btn_right);

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(money.getText())){
                    money.setError("예산을 입력해 주세요.");
                    return;
                }
                if(U.getInstance().removeComa(money.getText().toString()).length() > 9){
                    money.setError("10억 미만으로 입력해주세요.");
                    return;
                }
                pushServer(Integer.parseInt(U.getInstance().removeComa(money.getText().toString())));

            }
        });


    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public NewBudgetDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
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
                        money.setText(U.getInstance().toNumFormat(""+response.body().getDoc().getAsset().getFirst_month_budget()));
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


    public void pushServer(int money){
        showPd();
        String email = U.getInstance().getEmail(getContext());
        final int budg=money;
        final int day = U.getInstance().getDay(getContext());
        Req_Budget req_budget = new Req_Budget(email,budg,day);

        Call<Res> res = SNet.getInstance().getAllFactoryIm().pushBudget(req_budget);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(response.body().getDoc().getOk()==1){
                            U.getInstance().log(response.body().getDoc().toString());
                            if(response.body().getDoc().getN()==1){
                                Toast.makeText(getContext(),"예산과 월급일이 입력되었습니다.",Toast.LENGTH_SHORT).show();
                                U.getInstance().setBudget(getContext(), budg);
                                dismiss();
                            }else{
                                Toast.makeText(getContext(),"예산과 월급일이 입력실패.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
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
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }
}
