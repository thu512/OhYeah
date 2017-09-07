package com.changjoo.ohyeah.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.utill.U;

/**
 * Created by Changjoo on 2017-08-18.
 */

public class Purpose1Dialog extends Dialog {

    private TextView money;
    private Button btn_left; //확인
    private ImageButton cancel;
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener cancelClickListener;
    private int pp_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_purpose_dialog1);

        money = (TextView)findViewById(R.id.money);
        btn_left = (Button) findViewById(R.id.btn_left);
        cancel = (ImageButton)findViewById(R.id.cancel);
        money.setText(U.getInstance().toNumFormat(Integer.toString(pp_money)));

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && cancelClickListener != null) {
            btn_left.setOnClickListener(mLeftClickListener);
            cancel.setOnClickListener(cancelClickListener);
        }

    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public Purpose1Dialog(Context context, int money,
                          View.OnClickListener leftListener,
                          View.OnClickListener cancelListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.pp_money = money;
        this.mLeftClickListener = leftListener;
        this.cancelClickListener = cancelListener;
    }

}
