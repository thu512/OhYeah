package com.gogolab.ohyeah.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.gogolab.ohyeah.R;

/**
 * 비밀번호 변경 완료 팝업
 */

public class ChangePwDialog extends Dialog {


    private Button btn_left; //확인
    private View.OnClickListener mLeftClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.activity_changepw_dialog);

        btn_left = (Button) findViewById(R.id.btn_left);
        btn_left.setOnClickListener(mLeftClickListener);

    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public ChangePwDialog(Context context, View.OnClickListener leftListener) {

        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mLeftClickListener = leftListener;
    }

}
