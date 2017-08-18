package com.changjoo.ohyeah.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.LogoutDialog;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.StartActivity;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetActivity extends Activity {
    LinearLayout setMonthTime;
    LinearLayout setDayTime;
    RelativeLayout changepwd;
    AudioManager aManager;
    Switch soundSwt;
    Switch viveSwt;
    TextView monthTime1;
    TextView monthTime2;
    TextView monthTime3;
    TextView dayTime1;
    TextView dayTime2;
    TextView dayTime3;
    TextView appVersion;
    TextView versionState;
    Button logout;
    Button signout;
    LogoutDialog logoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        aManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        changepwd = (RelativeLayout)findViewById(R.id.changepwd);
        setMonthTime = (LinearLayout)findViewById(R.id.setMonthTime);
        setDayTime = (LinearLayout)findViewById(R.id.setDayTime);
        monthTime1=(TextView)findViewById(R.id.monthTime1);
        monthTime2=(TextView)findViewById(R.id.monthTime2);
        monthTime3=(TextView)findViewById(R.id.monthTime3);
        dayTime1=(TextView)findViewById(R.id.dayTime1);
        dayTime2=(TextView)findViewById(R.id.dayTime2);
        dayTime3=(TextView)findViewById(R.id.dayTime3);
        appVersion=(TextView)findViewById(R.id.appVersion);
        versionState=(TextView)findViewById(R.id.versionState);
        soundSwt =(Switch)findViewById(R.id.soundSwt);
        viveSwt  =(Switch)findViewById(R.id.viveSwt);
        logout = (Button)findViewById(R.id.logout);
        signout = (Button)findViewById(R.id.signout);

        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this, ChangePwdActivity.class);
                startActivity(intent);
            }
        });


        setMonthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(SetActivity.this, 3,listener1, 00, 00, false);
                dialog.show();
            }
        });
        setDayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(SetActivity.this, 3,listener2, 00, 00, false);
                dialog.show();
            }
        });

        soundSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    viveSwt.setChecked(false);
                }else{
                    aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    viveSwt.setChecked(true);
                }
            }
        });

        viveSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    soundSwt.setChecked(false);
                }else{
                    aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    soundSwt.setChecked(true);
                }
            }
        });
        //버전체크
        final PackageInfo pakageInfo;
        try {
            pakageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            appVersion.setText(pakageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog=new LogoutDialog(SetActivity.this, "로그아웃 하시겠습니까?",
                        new View.OnClickListener() { //취소
                            @Override
                            public void onClick(View view) {
                                logoutDialog.dismiss();
                            }
                        },
                        new View.OnClickListener() { //로그아웃
                            @Override
                            public void onClick(View view) {
                                Call<Res> res = SNet.getInstance().getMemberFactoryIm().logout();
                                res.enqueue(new Callback<Res>() {
                                    @Override
                                    public void onResponse(Call<Res> call, Response<Res> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() != null) {
                                                if(response.body().getResult()==1){
                                                    U.getInstance().log("로그아웃 성공");
                                                    U.getInstance().logout(SetActivity.this);
                                                    Intent intent = new Intent(SetActivity.this,StartActivity.class);
                                                    startActivity(intent);
                                                    finish();
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
                                    }

                                    @Override
                                    public void onFailure(Call<Res> call, Throwable t) {

                                    }
                                });
                            }
                        });

                logoutDialog.show();
            }
        });


    }


    //한달예산설정 알림 시간
    private TimePickerDialog.OnTimeSetListener listener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {// 설정버튼 눌렀을 때
            String s;
            if(hourOfDay<12){
                s = "오전";
                monthTime1.setText(s);
                monthTime2.setText(hourOfDay<10? "0"+Integer.toString(hourOfDay):Integer.toString(hourOfDay));
                monthTime3.setText(minute<10? "0"+Integer.toString(minute):Integer.toString(minute));
            }
            else{
                s = "오후";
                monthTime1.setText(s);
                monthTime2.setText(hourOfDay-12 <10? "0"+Integer.toString(hourOfDay-12):Integer.toString(hourOfDay-12));
                monthTime3.setText(minute<10? "0"+Integer.toString(minute):Integer.toString(minute));
            }

        }
    };

    //매일예산 알림 시간
    private TimePickerDialog.OnTimeSetListener listener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {// 설정버튼 눌렀을 때
            String s;
            if(hourOfDay<12){
                s = "오전";
                dayTime1.setText(s);
                dayTime2.setText(hourOfDay<10? "0"+Integer.toString(hourOfDay):Integer.toString(hourOfDay));
                dayTime3.setText(minute<10? "0"+Integer.toString(minute):Integer.toString(minute));
            }
            else{
                s = "오후";
                dayTime1.setText(s);
                dayTime2.setText(hourOfDay-12 <10? "0"+Integer.toString(hourOfDay-12):Integer.toString(hourOfDay-12));
                dayTime3.setText(minute<10? "0"+Integer.toString(minute):Integer.toString(minute));
            }
        }
    };

}
