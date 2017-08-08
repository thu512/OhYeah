package com.changjoo.ohyeah.utill;

/**
 * Created by Changjoo on 2017-08-08.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by PrographerJ on 2015-07-17.
 * Notofont 설정
 */
public class NotoTextView extends android.support.v7.widget.AppCompatTextView {
    public NotoTextView(Context context) {
        super(context);
        setType(context);
    }

    public NotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public NotoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context);
    }


    private void setType(Context context) {
        //asset에 폰트 복사
        //NotoSnat 경령화된 폰트 위치: https://github.com/theeluwin/NotoSansKR-Hestia
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "NotoSansKR-Regular.otf"));
    }
}
