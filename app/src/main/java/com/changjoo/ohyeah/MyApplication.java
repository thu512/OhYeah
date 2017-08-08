package com.changjoo.ohyeah;

import android.support.multidex.MultiDexApplication;

import com.tsengvn.typekit.Typekit;

/**
 * Created by Tacademy on 2017-08-07.
 */

public class MyApplication extends MultiDexApplication
{
    @Override
    public void onCreate() {
        super.onCreate();
        // 폰트 정의NotoSansCJKkr-Medium
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NotoSansKR-DemiLight-Hestia.otf"))
                .addBold(Typekit.createFromAsset(this, "NotoSansKR-Medium-Hestia.otf"))
                .addCustom1(Typekit.createFromAsset(this, "NotoSansKR-Regular-Hestia.otf"));
    }
//    .addNormal(Typekit.createFromAsset(this, "NanumGothic.ttf"))
//        .addBold(Typekit.createFromAsset(this, "NanumGothicBold.ttf"))
}
