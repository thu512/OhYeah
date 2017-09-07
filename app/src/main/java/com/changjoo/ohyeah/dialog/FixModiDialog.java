package com.changjoo.ohyeah.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.utill.U;

/**
 * Created by Changjoo on 2017-08-18.
 */

public class FixModiDialog extends Dialog {
    private TextView msg_content;
    private TextView msg_money;
    private TextView fix_name;


    private Button btn_left; //취소
    private Button btn_right; //확인
    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;
    private String msg_content_rr = "";
    int msg_money_rr;
    private String fix_name_rr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_fix_modi_dialog);

        msg_content = (TextView)findViewById(R.id.msg_content);
        msg_money = (TextView)findViewById(R.id.msg_money);
        fix_name = (TextView)findViewById(R.id.fix_name);

        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);

        msg_content.setText(msg_content_rr);
        msg_money.setText(U.getInstance().toNumFormat(Integer.toString(msg_money_rr)));
        fix_name.setText(fix_name_rr);

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            btn_left.setOnClickListener(mLeftClickListener);
            btn_right.setOnClickListener(mRightClickListener);
        }

    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public FixModiDialog(Context context, String msg_content, int msg_money, String fix_name,
                         View.OnClickListener leftListener,
                         View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
        this.msg_content_rr = msg_content;
        this.msg_money_rr = msg_money;
        this.fix_name_rr = fix_name;
    }




}
