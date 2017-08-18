package com.changjoo.ohyeah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.Req_Purpose;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurposeSetActivity extends Activity {
    EditText pupose_money;
    RadioButton home;
    RadioButton car;
    RadioButton airplane;
    RadioButton gift;
    RadioButton heart;
    RadioButton etc;
    Button submit;
    ImageButton back;
    int selectedItem = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purpose_set);
        pupose_money = (EditText) findViewById(R.id.pupose_money);
        home = (RadioButton) findViewById(R.id.home); //1
        car = (RadioButton) findViewById(R.id.car); //2
        airplane = (RadioButton) findViewById(R.id.airplane); //3
        gift = (RadioButton) findViewById(R.id.gift); //4
        heart = (RadioButton) findViewById(R.id.heart); //5
        etc = (RadioButton) findViewById(R.id.etc); //6
        back = (ImageButton) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.submit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PurposeSetActivity.this.finish();
            }
        });

        //서버로 목표데이터 전송
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = pupose_money.getText().toString();
                if (!money.equals("")) {
                    int m = Integer.parseInt(money);
                    Req_Purpose req_purpose = new Req_Purpose(U.getInstance().getEmail(PurposeSetActivity.this), m, selectedItem);
                    Call<Res> res = SNet.getInstance().getMemberFactoryIm().pushPurpose(req_purpose);
                    res.enqueue(new Callback<Res>() {
                        @Override
                        public void onResponse(Call<Res> call, Response<Res> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if (response.body().getDoc().getOk() == 1) {
                                        U.getInstance().log("목표설정" + response.body().toString());
                                        Toast.makeText(PurposeSetActivity.this, "목표설정완료", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PurposeSetActivity.this, NestSettingActivity.class);
                                        startActivity(intent);
                                    } else {
                                        U.getInstance().log("목표 서버전송 에러");
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
                        }

                        @Override
                        public void onFailure(Call<Res> call, Throwable t) {

                        }
                    });

                } else {
                    Intent intent = new Intent(PurposeSetActivity.this, NestSettingActivity.class);
                    startActivity(intent);
                }
            }
        });


        RadioButton.OnClickListener optionOnClickListener
                = new RadioButton.OnClickListener() {

            public void onClick(View v) {
                if (v.equals(home)) {
                    selectedItem = 1;
                    home.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if (v.equals(car)) {
                    selectedItem = 2;
                    car.setChecked(true);
                    home.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if (v.equals(airplane)) {
                    selectedItem = 3;
                    airplane.setChecked(true);
                    car.setChecked(false);
                    home.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if (v.equals(gift)) {
                    selectedItem = 4;
                    gift.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    home.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if (v.equals(heart)) {
                    selectedItem = 5;
                    heart.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    home.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if (v.equals(etc)) {
                    selectedItem = 6;
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

    }
}
