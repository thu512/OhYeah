package com.changjoo.ohyeah.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.Req_Purpose;
import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPurposeActivity extends Activity {
    RadioButton home;
    RadioButton car;
    RadioButton airplane;
    RadioButton gift;
    RadioButton heart;
    RadioButton etc;
    Button submit;
    Button toss;
    ImageButton back;
    EditText pupose_money;
    me.grantland.widget.AutofitTextView day;
    TextView dday;

    int selectedItem=1;
    String result= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_purpose);
        toss = (Button)findViewById(R.id.toss);
        dday = (TextView)findViewById(R.id.dday);
        day = (me.grantland.widget.AutofitTextView)findViewById(R.id.day);
        pupose_money = (EditText)findViewById(R.id.pupose_money);
        home = (RadioButton)findViewById(R.id.home);
        car = (RadioButton)findViewById(R.id.car);
        airplane = (RadioButton)findViewById(R.id.airplane);
        gift = (RadioButton)findViewById(R.id.gift);
        heart = (RadioButton)findViewById(R.id.heart);
        etc = (RadioButton)findViewById(R.id.etc);
        back = (ImageButton)findViewById(R.id.back);
        submit = (Button)findViewById(R.id.submit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyPurposeActivity.this.finish();
            }
        });


        toss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                U.getInstance().showPopup3(ModifyPurposeActivity.this,
                        "알림",
                        "Toss로 연결하시겠습니까?",
                        "네",
                        new SweetAlertDialog.OnSweetClickListener(){
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                checkToss();
                            }
                        },
                        "아니요",
                        new SweetAlertDialog.OnSweetClickListener(){
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        }
                );
            }
        });




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



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(pupose_money.getText().toString())){
                    pupose_money.setText("0");
                }

                Req_Purpose req_purpose = new Req_Purpose(U.getInstance().getEmail(ModifyPurposeActivity.this), Integer.parseInt(U.getInstance().removeComa(pupose_money.getText().toString())),selectedItem);

                Call<Res> res = SNet.getInstance().getAllFactoryIm().update_PP(req_purpose);
                res.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getDoc().getOk() == 1) {
                                    U.getInstance().log("목표수정 완료" + response.body().toString());

                                } else {
                                    U.getInstance().log("고정 서버전송 에러");
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
                finish();
            }
        });


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


        Req_email req_email = new Req_email(U.getInstance().getEmail(ModifyPurposeActivity.this));
        Call<Res> res = SNet.getInstance().getAllFactoryIm().updatePP(req_email);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 1) {
                            U.getInstance().log("목표수정" + response.body().getDoc().getGoal());
                            pupose_money.setText(U.getInstance().toNumFormat(""+response.body().getDoc().getGoal().getGoal_money()));
                            day.setText(U.getInstance().toNumFormat(""+response.body().getDoc().getGoal().getNow_saving()));

                            if(response.body().getDoc().getGoal().getRecent_saving() ==0){
                                ModifyPurposeActivity.this.dday.setText("저금을 시작해주세요.");
                                ModifyPurposeActivity.this.dday.setTextSize(20);
                            }else{
                                int dday = (response.body().getDoc().getGoal().getGoal_money() - response.body().getDoc().getGoal().getNow_saving())/(int)response.body().getDoc().getGoal().getRecent_saving();

                                ModifyPurposeActivity.this.dday.setText("D-"+dday);
                            }


                            selectedItem = response.body().getDoc().getGoal().getGoal_item();
                            if(selectedItem==1){
                                selectedItem=1;
                                home.setChecked(true);
                                car.setChecked(false);
                                airplane.setChecked(false);
                                gift.setChecked(false);
                                heart.setChecked(false);
                                etc.setChecked(false);
                                return;
                            }
                            if(selectedItem==2){
                                selectedItem=2;
                                car.setChecked(true);
                                home.setChecked(false);
                                airplane.setChecked(false);
                                gift.setChecked(false);
                                heart.setChecked(false);
                                etc.setChecked(false);
                                return;
                            }
                            if(selectedItem==3){
                                selectedItem=3;
                                airplane.setChecked(true);
                                car.setChecked(false);
                                home.setChecked(false);
                                gift.setChecked(false);
                                heart.setChecked(false);
                                etc.setChecked(false);
                                return;
                            }
                            if(selectedItem==4){
                                selectedItem=4;
                                gift.setChecked(true);
                                car.setChecked(false);
                                airplane.setChecked(false);
                                home.setChecked(false);
                                heart.setChecked(false);
                                etc.setChecked(false);
                                return;
                            }
                            if(selectedItem==5){
                                selectedItem=5;
                                heart.setChecked(true);
                                car.setChecked(false);
                                airplane.setChecked(false);
                                gift.setChecked(false);
                                home.setChecked(false);
                                etc.setChecked(false);
                                return;
                            }
                            if(selectedItem==6){
                                selectedItem=6;
                                etc.setChecked(true);
                                car.setChecked(false);
                                airplane.setChecked(false);
                                gift.setChecked(false);
                                heart.setChecked(false);
                                home.setChecked(false);
                                return;
                            }






                        } else {
                            U.getInstance().log("고정 서버전송 에러");
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
    //목표완료시 toss앱으로 연결 -> 설치안되있을시 에러 팝업 띄우기
    public void checkToss(){
        PackageManager pm = this.getPackageManager();
        boolean flag = true;
// 설치된 어플리케이션 리스트 취득
        List<ApplicationInfo> packs = pm.getInstalledApplications(
                PackageManager.GET_META_DATA);

        String mAppPackge = null;
        for (ApplicationInfo app : packs) {
            mAppPackge = app.packageName;
            Log.d("FFF","패키지명: "+mAppPackge);
            if(mAppPackge.equals("viva.republica.toss")){
                Log.d("FFF","=================================확인: "+mAppPackge);
                Intent intent = this.getPackageManager().getLaunchIntentForPackage("viva.republica.toss");
                intent.setAction(Intent.ACTION_MAIN);
                startActivity(intent);
                flag=false;
            }
        }
        if(flag){
            U.getInstance().showSimplePopup(ModifyPurposeActivity.this, "연결 에러", "Toss앱을 설치해주세요.",SweetAlertDialog.ERROR_TYPE);
        }
    }
}
