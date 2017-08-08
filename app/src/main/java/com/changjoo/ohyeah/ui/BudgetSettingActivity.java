package com.changjoo.ohyeah.ui;

import android.os.Bundle;
import android.support.v4.widget.Space;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.utill.BackPressEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class BudgetSettingActivity extends Activity implements View.OnClickListener {
    BackPressEditText editText;
    ViewFlipper flipper;
    LinearLayout select_key;
    Space spacer;
    Space spacer1;
    Space spacer2;
    Button num;
    Button cal;

    Button btn0;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn_ac;
    Button btn_del;
    Button btn_sum;
    Button btn_sub;
    Button btn_divide;
    Button btn_mul;
    Button btn_result;
    Button btn_down;
    private ArrayList<String> operatorList;
    private boolean isPreOperator;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_setting);

        spacer = (Space)findViewById(R.id.spacer);
        spacer1 = (Space)findViewById(R.id.spacer1);
        spacer2 = (Space)findViewById(R.id.spacer2);

        num=(Button) findViewById(R.id.num);
        cal=(Button) findViewById(R.id.cal);

        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btn_ac = (Button) findViewById(R.id.btn_ac);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_sum = (Button) findViewById(R.id.btn_sum);
        btn_sub = (Button) findViewById(R.id.btn_sub);
        btn_divide = (Button) findViewById(R.id.btn_divide);
        btn_mul = (Button) findViewById(R.id.btn_mul);
        btn_result = (Button) findViewById(R.id.btn_result);
        btn_down = (Button) findViewById(R.id.btn_down);
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn_ac.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_sum.setOnClickListener(this);
        btn_sub.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_mul.setOnClickListener(this);
        btn_result.setOnClickListener(this);
        btn_down.setOnClickListener(this);

        operatorList = new ArrayList<String>();
        isPreOperator = false;

        select_key = (LinearLayout)findViewById(R.id.select_key);
        flipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        flipper.setVisibility(View.VISIBLE);  // ViewFlipper 가 Activity 첫 실행시 보이지 않게 설정
        flipper.setInAnimation(appearSecurityKeyboardAnimation()); // 나타날때 애니메이션
        flipper.setOutAnimation(disappearSecurityKeyboardAnimation()); // 사라질때 애니메이션

        //기본키보드 조작을위한 세팅
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        select_key.setVisibility(View.INVISIBLE);

        // 계산기 키보드의 오프 이미지를 표시하기 위해 임의의 empty view를 삽입
        TextView emptyView = new TextView(this);
        flipper.addView(emptyView, 0);
        flipper.setDisplayedChild(0);

        editText = (BackPressEditText)findViewById(R.id.editText);
        editText.setOnBackPressListener(onBackPressListener);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spacer.setVisibility(View.GONE);
                spacer1.setVisibility(View.VISIBLE);
                spacer2.setVisibility(View.VISIBLE);

                select_key.setVisibility(View.VISIBLE);
            }
        });


        final Map<String, String> calc = new HashMap<String, String>();
        calc.put("reset", "N");
        calc.put("number1", "");
        calc.put("operation", "");
        calc.put("number2", "");

    }

    public void number_key(View view){
        num.setBackgroundResource(R.mipmap.basic_on);
        cal.setBackgroundResource(R.mipmap.group_off);

        flipper.setDisplayedChild(0);
        imm.showSoftInput(editText, 0);

    }

    public void cal_key(View view){
        num.setBackgroundResource(R.mipmap.basic_off);
        cal.setBackgroundResource(R.mipmap.group_on);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
        flipper.setDisplayedChild(1);
    }

    public void click(View view){
        flipper.setDisplayedChild(0);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
        select_key.setVisibility(View.INVISIBLE);
        spacer.setVisibility(View.VISIBLE);
        spacer1.setVisibility(View.GONE);
        spacer2.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        if( v.equals( btn1 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("1");
            }else {
                editText.setText( editText.getText()+"1");
            }
        }else if( v.equals( btn2 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("2");
            }else {
                editText.setText( editText.getText()+"2");
            }
        }else if( v.equals( btn3 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("3");
            }else {
                editText.setText( editText.getText()+"3");
            }
        }else if( v.equals( btn4 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("4");
            }else {
                editText.setText( editText.getText()+"4");
            }
        }else if( v.equals( btn5 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("5");
            }else {
                editText.setText( editText.getText()+"5");
            }
        }else if( v.equals( btn6 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("6");
            }else {
                editText.setText( editText.getText()+"6");
            }
        }else if( v.equals( btn7 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("7");
            }else {
                editText.setText( editText.getText()+"7");
            }
        }else if( v.equals( btn8 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("8");
            }else {
                editText.setText( editText.getText()+"8");
            }
        }else if( v.equals( btn9 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("9");
            }else {
                editText.setText( editText.getText()+"9");
            }
        }else if( v.equals( btn0 )){
            if( editText.getText().length() == 1 && "0".equals(editText.getText())){
                editText.setText("0");
            }else {
                editText.setText(editText.getText() + "0");
            }
        }else if( v.equals( btn_ac )){
            isPreOperator = false;
            editText.setText("");
            operatorList.clear();
        }else if( v.equals( btn_del )){
            if( editText.getText().length() != 0 ) {
                String str = editText.getText().subSequence( editText.getText().length()-1, editText.getText().length() ).toString();
                if( "+".equals(str) || "-".equals(str) || "*".equals(str) || "/".equals(str)){
                    operatorList.remove(operatorList.size());
                }
                editText.setText( editText.getText().subSequence( 0 , editText.getText().length()-1));
            }
        }else if( v.equals( btn_divide )){
            if( isPreOperator == true ) {
                return;
            }
            editText.setText( editText.getText()+"/");
            isPreOperator = true;
            operatorList.add("/");
        }else if( v.equals( btn_sum )){
            if( isPreOperator == true ) {
                return;
            }
            isPreOperator = true;
            editText.setText( editText.getText()+"+");
            operatorList.add("+");
        }else if( v.equals( btn_sub )){
            if( isPreOperator == true ) {
                return;
            }
            isPreOperator = true;
            editText.setText( editText.getText()+"-");
            operatorList.add("-");
        }else if( v.equals( btn_mul )){
            if( isPreOperator == true ) {
                return;
            }
            isPreOperator = true;
            editText.setText( editText.getText()+"X");
            operatorList.add("*");
        }else if(v.equals(btn_down)){
            flipper.setDisplayedChild(0);
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
            select_key.setVisibility(View.INVISIBLE);
            spacer.setVisibility(View.VISIBLE);
            spacer1.setVisibility(View.GONE);
            spacer2.setVisibility(View.GONE);
            return;
        } else if( v.equals( btn_result )){
            editText.setText( calc(editText.getText().toString()) );
        }
    }
    private String calc(String exp) {
        ArrayList<Integer> numberList = new ArrayList<Integer>();
        StringTokenizer st = new StringTokenizer(exp,"X/+-");


        while( st.hasMoreTokens() ) {
            int number = Integer.parseInt(st.nextToken());
            numberList.add( number );
            Log.d("aaa", String.valueOf(number) );
        }

        int result = numberList.get(0);
        Log.d("aaa", String.valueOf(result) );
        Log.d("aaa", String.valueOf(operatorList.size()) );

        for( int i = 0 ; i < operatorList.size() ; i++ ) {
            String operator = operatorList.get(i);

            if( "*".equals(operator)){
                result = ( result * numberList.get(i+1));
            }else if( "/".equals(operator)){
                result = ( result / numberList.get(i+1));
            }else if( "+".equals(operator)){
                result = ( result + numberList.get(i+1));
            }else if( "-".equals(operator)){
                result = ( result - numberList.get(i+1));
            }
            Log.d("aaa", String.valueOf(numberList.get(1)) );
        }
        operatorList.clear();
        numberList.clear();
        isPreOperator=false;
        if(result<0){
            result=0;
        }
        return String.valueOf(result);

    }




//
//    private void keyboardClickHandler(){
//        Button key_done = (Button)findViewById(R.id.key_done);
//        Button key_backspace = (Button)findViewById(R.id.key_backspace);
//
//        // 확인키 누름
//        key_done.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                // 현재 viewfliper가 가지고 있는 view가 키보드인지 체크
//                if(flipper.getCurrentView().getId() == R.id.firstViewFlipper){
//                    // empty view 호출, 즉 키보드view를 퇴장시킴
//                    flipper.setDisplayedChild(0);
//                }
//            }
//        });
//
//        // backspace 키 입력
//        key_backspace.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                // 현재 커서위치
//                int curIndex = editText.getSelectionStart();
//                // 현재 입력된 패스워드 길이
//                int passWordLength = passwordStr.length();
//                if((curIndex == 0) || (passWordLength == 0)){
//                    return;
//                }
//
//                // 한글자씩 지우기
//                passwordStr = passwordStr.substring(0, curIndex-1) + passwordStr.substring(curIndex, passWordLength);
//                editText.setText("");
//                for(int i=0; i<passWordLength-1; i++){
//                    editText.append("*");
//                }
//                editText.setSelection(curIndex-1);
//            }
//        });
//    }

    /***************************************************/
    /** 애니메이션 설정 **/
    /***************************************************/
    private Animation appearSecurityKeyboardAnimation() {
        Animation appear = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        appear.setDuration(400);
        appear.setInterpolator(new AccelerateInterpolator());
        return appear;
    }

    private Animation disappearSecurityKeyboardAnimation() {
        Animation disappear = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f);
        disappear.setDuration(400);
        disappear.setInterpolator(new DecelerateInterpolator());
        return disappear;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if( event.getAction() == KeyEvent.ACTION_DOWN ) {
            if( keyCode == KeyEvent.KEYCODE_BACK ) {
                select_key.setVisibility(View.INVISIBLE);
            }
        }
        if(keyCode== KeyEvent.KEYCODE_BACK){

            // 디바이스 키중 Back버튼 눌렀을때 키보드가 올라와있으면 키보드 닫기
            if(flipper.getCurrentView().getId() == R.id.firstViewFlipper){
                flipper.setDisplayedChild(0);
                spacer.setVisibility(View.VISIBLE);
                spacer1.setVisibility(View.GONE);
                spacer2.setVisibility(View.GONE);
            }
            // 키보드가 올라오지 않은 상태면 보통때의 back 버튼 수행
            else{
                super.onKeyDown(keyCode, event);
            }

        }

        return true;
    }

    private void didBackPressOnEditText()
    {
        spacer.setVisibility(View.VISIBLE);
        spacer1.setVisibility(View.GONE);
        spacer2.setVisibility(View.GONE);
        select_key.setVisibility(View.INVISIBLE);
    }


    private BackPressEditText.OnBackPressListener onBackPressListener = new BackPressEditText.OnBackPressListener()
    {
        @Override
        public void onBackPress()
        {
            didBackPressOnEditText();
        }
    };

}
