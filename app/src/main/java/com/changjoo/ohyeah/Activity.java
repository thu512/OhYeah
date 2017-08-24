package com.changjoo.ohyeah;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Changjoo on 2017-08-07.
 */

public class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
