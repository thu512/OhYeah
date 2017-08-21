package com.changjoo.ohyeah;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Changjoo on 2017-08-18.
 */

public class NestAddDialog extends Dialog {

    EditText money;
    TextView current_money;
    String cm;


    Button btn_left; //취소
    Button btn_right; //확인



    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_nest_add_dialog);

        money = (EditText) findViewById(R.id.money);
        current_money= (TextView) findViewById(R.id.current_money);
        btn_left = (Button) findViewById(R.id.btn_left);
        btn_right = (Button) findViewById(R.id.btn_right);

        current_money.setText(cm);

        money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                int nest;
                int money;

                if(s.equals("")){
                    nest = 0;
                }else{
                    nest = Integer.parseInt(s);
                }

                money = Integer.parseInt(cm);
                current_money.setText(""+(money-nest));
            }
        });

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            btn_left.setOnClickListener(mLeftClickListener);
            btn_right.setOnClickListener(mRightClickListener);
        }

    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public NestAddDialog(Context context,
                         String current_money,
                         View.OnClickListener leftListener,
                         View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.cm = current_money;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }

}
