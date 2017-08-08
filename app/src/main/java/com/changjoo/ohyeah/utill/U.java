package com.changjoo.ohyeah.utill;
//싱글톤 객체로서 이앱에서 객체가 1개만 생성되는 클래스
//용도: 유틸리티 용도로 개발자가 생성한 코드

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by Tacademy on 2017-06-29.
 */
public class U {

    private static U ourInstance = new U();

    public static U getInstance() {
        return ourInstance;
    }

    private U() {
    }
    ////////////////////////////////////////////////////////////////


    //로그 출력
    final String TAG="NAVER";
    public void log(String msg){
        Log.i(TAG, msg);
    }


    public static void setLogin(Activity ctx, String userName)
    {
        SharedPreferences.Editor editor = ctx.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString("email", userName);
        editor.commit();
    }

    public static void logout(Activity ctx)
    {
        SharedPreferences.Editor editor = ctx.getPreferences(Context.MODE_PRIVATE).edit();
        editor.putString("email", "");
        editor.commit();
    }

    public static boolean isLogin(Activity ctx)
    {
        return ctx.getPreferences(Context.MODE_PRIVATE).getString("email", "") != "";
    }


    // DP ---> PX
    public static int getDpToPixel(Context context, float DP) {
        float px = 0;
        try {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources().getDisplayMetrics());
        } catch (Exception e) {
        }
        return (int) px;
    }
}
