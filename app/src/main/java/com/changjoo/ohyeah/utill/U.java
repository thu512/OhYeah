package com.changjoo.ohyeah.utill;
//싱글톤 객체로서 이앱에서 객체가 1개만 생성되는 클래스
//용도: 유틸리티 용도로 개발자가 생성한 코드

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;

import com.squareup.otto.Bus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    final String TAG="FFF";
    public void log(String msg){
        Log.d(TAG, msg);
    }
    String SAVE_TAG="EMAIL";

    public void setEmail(Context context, String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(SAVE_TAG, 0).edit();
        editor.putString("email", value);
        editor.commit();
    }

    public String getEmail(Context context)
    {
        return context.getSharedPreferences(SAVE_TAG, 0).getString("email", "");
    }

    public boolean isLogin(Context context)
    {
        return !getEmail(context).equals("");
    }

    public void logout(Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(SAVE_TAG, 0).edit();
        editor.putString("email", "");
        editor.commit();
    }

    public void setBoolean(Context context, String key, boolean value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(SAVE_TAG, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public boolean getBoolean(Context context, String key)
    {
        return context.getSharedPreferences(SAVE_TAG, 0).getBoolean(key, false);
    }


    public void setBudget(Context context, int value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(SAVE_TAG, 0).edit();
        editor.putInt("budget", value);
        editor.commit();
    }

    public Integer getBudget(Context context)
    {
        return context.getSharedPreferences(SAVE_TAG, 0).getInt("budget", 0);
    }

    public void setFix(Context context, int value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(SAVE_TAG, 0).edit();
        editor.putInt("Fix", value);
        editor.commit();
    }

    public Integer getFix(Context context)
    {
        return context.getSharedPreferences(SAVE_TAG, 0).getInt("Fix", 0);
    }

    public void setDay(Context context, int value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(SAVE_TAG, 0).edit();
        editor.putInt("Day", value);
        editor.commit();
    }

    public Integer getDay(Context context)
    {
        return context.getSharedPreferences(SAVE_TAG, 0).getInt("Day", 0);
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


    //인증문자 파싱
    public String getAuthCode(String src){
        String text=src;

        //숫자만6자리 => "\\d{6}"
        Pattern pattern= Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()) {
            String code = matcher.group(0);
            return code;
        }
        return "";
    }

    //==============================================================================================
    //인증문자 배달 버스(출발: callandsmsreceiver => 도착: mainactivity)
    Bus authBus= new Bus();
    public Bus getAuthBus(){
        return authBus;
    }

}
