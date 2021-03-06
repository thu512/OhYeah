package com.gogolab.ohyeah.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gogolab.ohyeah.Activity;
import com.gogolab.ohyeah.R;
import com.gogolab.ohyeah.dialog.LogoutDialog;
import com.gogolab.ohyeah.dialog.SignoutCheckDialog;
import com.gogolab.ohyeah.dialog.SignoutDialog;
import com.gogolab.ohyeah.model.Req_email;
import com.gogolab.ohyeah.model.Res;
import com.gogolab.ohyeah.net.SNet;
import com.gogolab.ohyeah.utill.U;
import com.nhn.android.naverlogin.OAuthLogin;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetActivity extends Activity {

    RelativeLayout changepwd;
    RelativeLayout send_email;
    AudioManager aManager;
    Switch soundSwt;
    Switch viveSwt;

    TextView appVersion;
    TextView versionState;
    Button logout;
    Button signout;
    ImageButton back;
    LogoutDialog logoutDialog;
    SignoutDialog signoutDialog;
    SignoutCheckDialog signoutCheckDialog;

    NotificationManager mangager;


    @Override
    protected void onResume() {
        super.onResume();

        if (U.getInstance().getBoolean(this,"sound") && !U.getInstance().getBoolean(this,"vive")) { //사운드모드일 경우
            soundSwt.setChecked(true);
            viveSwt.setChecked(false);
        } else if (U.getInstance().getBoolean(this,"vive") && !U.getInstance().getBoolean(this,"sound")) {//벨 모드일 경우(값2)
            viveSwt.setChecked(true);
            soundSwt.setChecked(false);
        } else if(!U.getInstance().getBoolean(this,"sound") && !U.getInstance().getBoolean(this,"vive")){
            viveSwt.setChecked(false);
            soundSwt.setChecked(false);
        }else{
            viveSwt.setChecked(true);
            soundSwt.setChecked(true);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        mangager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        aManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        send_email = (RelativeLayout) findViewById(R.id.send_email);
        changepwd = (RelativeLayout) findViewById(R.id.changepwd);

        appVersion = (TextView) findViewById(R.id.appVersion);
        versionState = (TextView) findViewById(R.id.versionState);
        soundSwt = (Switch) findViewById(R.id.soundSwt);
        viveSwt = (Switch) findViewById(R.id.viveSwt);
        logout = (Button) findViewById(R.id.logout);
        signout = (Button) findViewById(R.id.signout);
        back = (ImageButton) findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (OAuthLogin.getInstance().getRefreshToken(SetActivity.this) != null) {
                    Toast.makeText(SetActivity.this, "네이버 아이디 로그인 사용자는 비밀번호를 변경하실 수 없습니다.", Toast.LENGTH_LONG).show();
                    return;
                } else {
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


        soundSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    U.getInstance().setBoolean(SetActivity.this,"sound",true);  //벨\
                } else {
                    U.getInstance().setBoolean(SetActivity.this,"sound",false);
                }
            }
        });

        viveSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    U.getInstance().setBoolean(SetActivity.this,"vive",true);
                } else {
                    U.getInstance().setBoolean(SetActivity.this,"vive",false);
                }
            }
        });
        //버전체크
        final PackageInfo pakageInfo;
        try {
            pakageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion.setText(pakageInfo.versionName);
            if (!U.getInstance().getString(this,"VERSION").equals(pakageInfo.versionName)) {
                versionState.setText("최신 버전이 아닙니다.");
                versionState.setTextColor(Color.parseColor("#c33b4d"));
            } else {
                versionState.setText("최신 버전입니다.");
                versionState.setTextColor(Color.parseColor("#3b4aaa"));
            }
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
                                                if (response.body().getResult() == 1) {
                                                    U.getInstance().log("회원탈퇴 성공");

                                                    //네아로 연동 해제
                                                    if (OAuthLogin.getInstance().getRefreshToken(SetActivity.this) != null) {
                                                        OAuthLogin.getInstance().logoutAndDeleteToken(SetActivity.this);
                                                    }

                                                    U.getInstance().logout(SetActivity.this);


                                                    //앞에 열려있던 액티비티 모두 종료
                                                    Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    } else {
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    }
                                                    startActivity(intent);
                                                    finish();
                                                } else {
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
                logoutDialog = new LogoutDialog(SetActivity.this, "로그아웃 하시겠습니까?",
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
                                                if (response.body().getResult() == 1) {
                                                    U.getInstance().log("로그아웃 성공");
                                                    U.getInstance().logout(SetActivity.this);

                                                    //앞에 열려있던 액티비티 모두 종료
                                                    Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    } else {
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    }
                                                    startActivity(intent);
                                                    finish();
                                                } else {
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

}
