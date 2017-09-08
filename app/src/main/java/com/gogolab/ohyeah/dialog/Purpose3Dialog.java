package com.gogolab.ohyeah.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gogolab.ohyeah.R;
import com.gogolab.ohyeah.model.Req_Purpose;
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

public class Purpose3Dialog extends Dialog {

    private Button btn_left; //확인
    private EditText pupose_money;
    private RadioButton home;
    private RadioButton car;
    private RadioButton airplane;
    private RadioButton gift;
    private RadioButton heart;
    private RadioButton etc;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener cancelClickListener;
    int pp_money;
    int selectedItem=1;
    String result="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_purpose_dialog3);

        pupose_money = (EditText)findViewById(R.id.pupose_money);
        btn_left = (Button) findViewById(R.id.btn_left);
        home = (RadioButton) findViewById(R.id.home); //1
        car = (RadioButton) findViewById(R.id.car); //2
        airplane = (RadioButton) findViewById(R.id.airplane); //3
        gift = (RadioButton) findViewById(R.id.gift); //4
        heart = (RadioButton) findViewById(R.id.heart); //5
        etc = (RadioButton) findViewById(R.id.etc); //6
        pupose_money.setText(U.getInstance().toNumFormat(Integer.toString(pp_money)));

        RadioButton.OnClickListener optionOnClickListener
                = new RadioButton.OnClickListener() {

            public void onClick(View v) {
                if(v.equals(home)){
                    selectedItem=1;
                    home.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(car)){
                    selectedItem=2;
                    car.setChecked(true);
                    home.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(airplane)){
                    selectedItem=3;
                    airplane.setChecked(true);
                    car.setChecked(false);
                    home.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(gift)){
                    selectedItem=4;
                    gift.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    home.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(heart)){
                    selectedItem=5;
                    heart.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    home.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(etc)){
                    selectedItem=6;
                    etc.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    home.setChecked(false);
                    return;
                }
            }
        };
        home.setOnClickListener(optionOnClickListener);
        car.setOnClickListener(optionOnClickListener);
        airplane.setOnClickListener(optionOnClickListener);
        gift.setOnClickListener(optionOnClickListener);
        heart.setOnClickListener(optionOnClickListener);
        etc.setOnClickListener(optionOnClickListener);

        pupose_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(result)){     // StackOverflow를 막기위해,
                    result = U.getInstance().toNumFormat(charSequence.toString());   // 에딧텍스트의 값을 변환하여, result에 저장.
                    pupose_money.setText(result);    // 결과 텍스트 셋팅.
                    pupose_money.setSelection(result.length());     // 커서를 제일 끝으로 보냄.
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // 클릭 이벤트 셋팅
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPurpose();
            }
        });


    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public Purpose3Dialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }


    //목표 수정 데이터 서버로 전송
    public void setPurpose(){
        if(TextUtils.isEmpty(pupose_money.getText().toString())){
            pupose_money.setText("0");
        }

        Req_Purpose req_purpose = new Req_Purpose(U.getInstance().getEmail(getContext()), Integer.parseInt(U.getInstance().removeComa(pupose_money.getText().toString())),selectedItem);

        Call<Res> res = SNet.getInstance().getAllFactoryIm().pushPurpose(req_purpose);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getDoc().getOk() == 1) {
                            U.getInstance().log("목표설정 완료" + response.body().toString());
                            Toast.makeText(getContext(), "새로운 목표가 설정 되었습니다.",Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            U.getInstance().log("서버전송 에러");
                            Toast.makeText(getContext(), "서버전송 에러.",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }

                    } else {
                        U.getInstance().log("통신실패1");
                        Toast.makeText(getContext(), "서버전송 에러.",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                } else {
                    try {
                        U.getInstance().log("통신실패2" + response.errorBody().string());
                        Toast.makeText(getContext(), "서버전송 에러.",Toast.LENGTH_SHORT).show();
                        dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                Toast.makeText(getContext(), "서버전송 에러.",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
