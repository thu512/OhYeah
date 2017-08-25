package com.changjoo.ohyeah.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.dialog.LogoutDialog;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.StartActivity;
import com.changjoo.ohyeah.dialog.SignoutCheckDialog;
import com.changjoo.ohyeah.dialog.SignoutDialog;
import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;
import com.nhn.android.naverlogin.OAuthLogin;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetActivity extends Activity {
    LinearLayout setMonthTime;
    LinearLayout setDayTime;
    RelativeLayout changepwd;
    RelativeLayout send_email;
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
    ImageButton back;
    LogoutDialog logoutDialog;
    SignoutDialog signoutDialog;
    SignoutCheckDialog signoutCheckDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);


        aManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        send_email = (RelativeLayout)findViewById(R.id.send_email);
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
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(OAuthLogin.getInstance().getRefreshToken(SetActivity.this) != null){
                    Toast.makeText(SetActivity.this,"네이버 아이디 로그인 사용자는 비밀번호를 변경하실 수 없습니다.",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    Intent intent = new Intent(SetActivity.this, ChangePwdActivity.class);
                    startActivity(intent);
                }

            }
        });

        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this, SendEmailActivity.class);
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

//        soundSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                    viveSwt.setChecked(false);
//                }else{
//                    aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                    viveSwt.setChecked(true);
//                }
//            }
//        });
//
//        viveSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    aManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                    soundSwt.setChecked(false);
//                }else{
//                    aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                    soundSwt.setChecked(true);
//                }
//            }
//        });
        //버전체크
        final PackageInfo pakageInfo;
        try {
            pakageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
            appVersion.setText(pakageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        //회원탈퇴
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signoutDialog = new SignoutDialog(SetActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //탈퇴버튼 -> 탈퇴확인 다이얼로그로
                        signoutDialog.dismiss();
                        signoutCheckDialog = new SignoutCheckDialog(SetActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //탈퇴
                                Req_email req_email = new Req_email(U.getInstance().getEmail(SetActivity.this));
                                showPd();
                                Call<Res> res = SNet.getInstance().getAllFactoryIm().signOut(req_email);
                                res.enqueue(new Callback<Res>() {
                                    @Override
                                    public void onResponse(Call<Res> call, Response<Res> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() != null) {
                                                if(response.body().getResult()==1){
                                                    U.getInstance().log("회원탈퇴 성공");

                                                    //네아로 연동 해제
                                                    if(OAuthLogin.getInstance().getRefreshToken(SetActivity.this) != null){
                                                        OAuthLogin.getInstance().logoutAndDeleteToken(SetActivity.this);
                                                    }

                                                    U.getInstance().logout(SetActivity.this);


                                                    //앞에 열려있던 액티비티 모두 종료
                                                    Intent intent = new Intent(SetActivity.this,StartActivity.class);
                                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    } else {
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    }
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
                                        stopPd();
                                    }

                                    @Override
                                    public void onFailure(Call<Res> call, Throwable t) {
                                        U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                                        stopPd();
                                    }
                                });
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //취소
                                signoutCheckDialog.dismiss();
                            }
                        });

                        signoutCheckDialog.show();
                    }
                },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //건의하기
                                Intent intent = new Intent(SetActivity.this, SendEmailActivity.class);
                                startActivity(intent);
                                signoutDialog.dismiss();
                            }
                        }
                );
                signoutDialog.show();
            }
        });


        //로그아웃
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
                                showPd();
                                Call<Res> res = SNet.getInstance().getAllFactoryIm().logout();
                                res.enqueue(new Callback<Res>() {
                                    @Override
                                    public void onResponse(Call<Res> call, Response<Res> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body() != null) {
                                                if(response.body().getResult()==1){
                                                    U.getInstance().log("로그아웃 성공");
                                                    U.getInstance().logout(SetActivity.this);

                                                    //앞에 열려있던 액티비티 모두 종료
                                                    Intent intent = new Intent(SetActivity.this,StartActivity.class);
                                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    } else {
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    }
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
                                        stopPd();
                                    }

                                    @Override
                                    public void onFailure(Call<Res> call, Throwable t) {
                                        stopPd();
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
