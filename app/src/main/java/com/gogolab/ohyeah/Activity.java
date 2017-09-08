package com.gogolab.ohyeah;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Changjoo on 2017-08-07.
 */

public class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    ProgressDialog pd;
    public void showPd(){
        if(pd == null){
            pd = new ProgressDialog(this);
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


}
