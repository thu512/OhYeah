package com.gogolab.ohyeah.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.Space;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.gogolab.ohyeah.Activity;
import com.gogolab.ohyeah.R;
import com.gogolab.ohyeah.model.Req_Budget;
import com.gogolab.ohyeah.model.Res;
import com.gogolab.ohyeah.net.SNet;
import com.gogolab.ohyeah.utill.BackPressEditText;
import com.gogolab.ohyeah.utill.U;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetSettingActivity extends Activity implements View.OnClickListener {

    BackPressEditText editText;
    EditText day;
    TextView remain_day;
    TextView error1;
    TextView error_msg;
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
    Button submit;


    Button bt0;
    Button bt1;
    Button bt2;
    Button bt3;
    Button bt4;
    Button bt5;
    Button bt6;
    Button bt7;
    Button bt8;
    Button bt9;
    Button bt_down;
    Button bt_del;

    private ArrayList<String> operatorList;
    private boolean isPreOperator;

    Boolean error_check1;
    Boolean error_check2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_setting);




        View cal_keyboard = (View) getLayoutInflater().inflate(R.layout.cal_keyboard, null);
        View num_keyboard = (View) getLayoutInflater().inflate(R.layout.default_keyboard, null);
        error_msg = (TextView)findViewById(R.id.error_msg);
        error1 = (TextView)findViewById(R.id.error1);
        day = (EditText)findViewById(R.id.day);
        spacer = (Space)findViewById(R.id.spacer);
        spacer1 = (Space)findViewById(R.id.spacer1);
        spacer2 = (Space)findViewById(R.id.spacer2);
        num=(Button) findViewById(R.id.num);
        cal=(Button) findViewById(R.id.cal);
        remain_day = (TextView)findViewById(R.id.remain_day);

        bt0 = (Button) num_keyboard.findViewById(R.id.bt0);
        bt1 = (Button) num_keyboard.findViewById(R.id.bt1);
        bt2 = (Button) num_keyboard.findViewById(R.id.bt2);
        bt3 = (Button) num_keyboard.findViewById(R.id.bt3);
        bt4 = (Button) num_keyboard.findViewById(R.id.bt4);
        bt5 = (Button) num_keyboard.findViewById(R.id.bt5);
        bt6 = (Button) num_keyboard.findViewById(R.id.bt6);
        bt7 = (Button) num_keyboard.findViewById(R.id.bt7);
        bt8 = (Button) num_keyboard.findViewById(R.id.bt8);
        bt9 = (Button) num_keyboard.findViewById(R.id.bt9);
        bt_down = (Button) num_keyboard.findViewById(R.id.bt_down);
        bt_del = (Button) num_keyboard.findViewById(R.id.bt_del);
        bt0.setOnClickListener(this);
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
        bt5.setOnClickListener(this);
        bt6.setOnClickListener(this);
        bt7.setOnClickListener(this);
        bt8.setOnClickListener(this);
        bt9.setOnClickListener(this);
        bt_del.setOnClickListener(this);
        bt_down.setOnClickListener(this);


        btn0 = (Button) cal_keyboard.findViewById(R.id.btn0);
        btn1 = (Button) cal_keyboard.findViewById(R.id.btn1);
        btn2 = (Button) cal_keyboard.findViewById(R.id.btn2);
        btn3 = (Button) cal_keyboard.findViewById(R.id.btn3);
        btn4 = (Button) cal_keyboard.findViewById(R.id.btn4);
        btn5 = (Button) cal_keyboard.findViewById(R.id.btn5);
        btn6 = (Button) cal_keyboard.findViewById(R.id.btn6);
        btn7 = (Button) cal_keyboard.findViewById(R.id.btn7);
        btn8 = (Button) cal_keyboard.findViewById(R.id.btn8);
        btn9 = (Button) cal_keyboard.findViewById(R.id.btn9);
        btn_ac = (Button) cal_keyboard.findViewById(R.id.btn_ac);
        btn_del = (Button) cal_keyboard.findViewById(R.id.btn_del);
        btn_sum = (Button) cal_keyboard.findViewById(R.id.btn_sum);
        btn_sub = (Button) cal_keyboard.findViewById(R.id.btn_sub);
        btn_divide = (Button) cal_keyboard.findViewById(R.id.btn_divide);
        btn_mul = (Button) cal_keyboard.findViewById(R.id.btn_mul);
        btn_result = (Button) cal_keyboard.findViewById(R.id.btn_result);
        btn_down = (Button) cal_keyboard.findViewById(R.id.btn_down);
        submit = (Button)findViewById(R.id.submit);
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


        select_key.setVisibility(View.INVISIBLE);

        //월급일 설정 키보드 띄움 막기
        day.setInputType(0);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);


        //월급일 설정 넘버픽커 다이얼로그 띄움
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });

        // 계산기 키보드의 오프 이미지를 표시하기 위해 임의의 empty view를 삽입
        TextView emptyView = new TextView(this);

        flipper.addView(emptyView, 0);
        flipper.addView(cal_keyboard, 1);
        flipper.addView(num_keyboard, 2);
        flipper.setDisplayedChild(0);
        editText = (BackPressEditText)findViewById(R.id.editText);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
        editText.setSelection(editText.getText().length());
        editText.setOnBackPressListener(onBackPressListener);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Log.d("FFF","포커스받음");
                    spacer1.setVisibility(View.GONE);
                    spacer2.setVisibility(View.VISIBLE);
                    flipper.setDisplayedChild(2);
                    select_key.setVisibility(View.VISIBLE);
                }else{
                    flipper.setDisplayedChild(0);
                    select_key.setVisibility(View.INVISIBLE);
                    num.setBackgroundResource(R.mipmap.basic_on);
                    cal.setBackgroundResource(R.mipmap.group_off);
                }
            }
        });


        //예산입력 폼누르면
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s =editText.getText().toString();
                s = s.replace("+","");
                s = s.replace("-","");
                s = s.replace("X","");
                s = s.replace("/","");
                editText.setText(s);
                isPreOperator=false;
                operatorList.clear();
                spacer1.setVisibility(View.GONE);
                flipper.setDisplayedChild(2);
                spacer2.setVisibility(View.VISIBLE);
                select_key.setVisibility(View.VISIBLE);
                num.setBackgroundResource(R.mipmap.basic_on);
                cal.setBackgroundResource(R.mipmap.group_off);
            }
        });


        //키보드에서 확인키 누르면 키보드 내리기
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    flipper.setDisplayedChild(0);
                    select_key.setVisibility(View.INVISIBLE);
                    num.setBackgroundResource(R.mipmap.basic_on);
                    cal.setBackgroundResource(R.mipmap.group_off);
                    spacer.setVisibility(View.VISIBLE);
                    spacer1.setVisibility(View.VISIBLE);
                    spacer2.setVisibility(View.GONE);
                }
                return false;
            }

        });


        //예산일 입력값 체크
        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString();
                if(s.length()>0){
                    error_check1=true;
                    error1.setVisibility(View.INVISIBLE);
                }
            }
        });

        //예산 입력 값 체크
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if(s.length()>0){
                    error_check2=true;
                    error_msg.setText("");
                }
            }
        });


        final Map<String, String> calc = new HashMap<String, String>();
        calc.put("reset", "N");
        calc.put("number1", "");
        calc.put("operation", "");
        calc.put("number2", "");

        //월급일, 예산 서버로 전송 성공시 -> 고정지출페이지로
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(day.getText().toString().equals("")){
                    error1.setVisibility(View.VISIBLE);
                    error_check1=false;
                    return;
                }
                if(editText.getText().toString().equals("")){
                    error_check2=false;
                    error_msg.setText(R.string.budget_error3);
                    return;
                }
                if(day.getText().toString().equals("") && editText.getText().toString().equals("")){
                    error1.setVisibility(View.INVISIBLE);
                    error_check1=false;
                    error_check2=false;
                    error_msg.setText(R.string.budget_error2);
                    return;
                }
                if(isPreOperator){
                    error_msg.setText("계산을 완료해주세요.");
                    return;
                }
                if(U.getInstance().removeComa(editText.getText().toString()).length() > 9){
                    error_msg.setText("10억 미만으로 입력해주세요.");
                    return;
                }

                if(error_check1&&error_check2){

                    pushBudget(Integer.parseInt(U.getInstance().removeComa(editText.getText().toString())),Integer.parseInt(day.getText().toString()));
                    U.getInstance().setBoolean(BudgetSettingActivity.this,U.getInstance().getEmail(BudgetSettingActivity.this),true);
                    U.getInstance().setDay(BudgetSettingActivity.this, Integer.parseInt(day.getText().toString()));
                    Intent intent = new Intent(BudgetSettingActivity.this, FixSettingActivity.class);
                    startActivity(intent);

                }

            }
        });

    }

    //일반 키보드로 전환
    public void number_key(View view){
        num.setBackgroundResource(R.mipmap.basic_on);
        cal.setBackgroundResource(R.mipmap.group_off);
        flipper.setDisplayedChild(2);
        String s =editText.getText().toString();
        s = s.replace("+","");
        s = s.replace("-","");
        s = s.replace("X","");
        s = s.replace("/","");
        editText.setText(s);
        isPreOperator=false;
        operatorList.clear();
        editText.setSelection(editText.length());

    }


    //계산기 키보드로 전환
    public void cal_key(View view){
        num.setBackgroundResource(R.mipmap.basic_off);
        cal.setBackgroundResource(R.mipmap.group_on);
        flipper.setDisplayedChild(1);
        if(!editText.getText().toString().equals("")){
            editText.setText(U.getInstance().removeComa(editText.getText().toString()));
        }
        editText.setSelection(editText.length());
    }


    //키보드 올라와있을때 빈화면 클릭시 키보드 내리기
    public void click(View view){
        flipper.setDisplayedChild(0);
        select_key.setVisibility(View.INVISIBLE);
        spacer1.setVisibility(View.VISIBLE);
        spacer2.setVisibility(View.GONE);
        num.setBackgroundResource(R.mipmap.basic_on);
        cal.setBackgroundResource(R.mipmap.group_off);
    }


    //계산기 키보드/ 기본 키보드 버튼 이벤트
    @Override
    public void onClick(View v) {

        if( v.equals( btn1 )){
            editText.setText(editText.getText()+"1");
            editText.setSelection(editText.length());
        }else if( v.equals( btn2 )){
            editText.setText( editText.getText()+"2");
            editText.setSelection(editText.length());
        }else if( v.equals( btn3 )){
            editText.setText( editText.getText()+"3");
            editText.setSelection(editText.length());
        }else if( v.equals( btn4 )){
            editText.setText( editText.getText()+"4");
            editText.setSelection(editText.length());
        }else if( v.equals( btn5 )){
            editText.setText( editText.getText()+"5");
            editText.setSelection(editText.length());
        }else if( v.equals( btn6 )){
            editText.setText( editText.getText()+"6");
            editText.setSelection(editText.length());
        }else if( v.equals( btn7 )){
            editText.setText( editText.getText()+"7");
            editText.setSelection(editText.length());
        }else if( v.equals( btn8 )){
            editText.setText( editText.getText()+"8");
            editText.setSelection(editText.length());
        }else if( v.equals( btn9 )){
            editText.setText( editText.getText()+"9");
            editText.setSelection(editText.length());
        }else if( v.equals( btn0 )){
            editText.setText(editText.getText() + "0");
            editText.setSelection(editText.length());
        }else if( v.equals( btn_ac )){
            isPreOperator = false;
            editText.setText("");
            editText.setSelection(editText.length());
            operatorList.clear();
        }else if( v.equals( btn_del )){

            if( editText.getText().length() != 0 ) {
                String str = editText.getText().subSequence( editText.getText().length()-1, editText.getText().length() ).toString();
                if( "+".equals(str) || "-".equals(str) || "X".equals(str) || "/".equals(str)){
                    operatorList.remove(operatorList.size()-1);
                    isPreOperator=false;
                }
                editText.setText( editText.getText().subSequence( 0 , editText.getText().length()-1));
                editText.setSelection(editText.length());
            }
        }else if( v.equals( btn_divide )){
            if( isPreOperator == true ) {
                return;
            }
            if(editText.getText().length() >= 9){
                error_msg.setText("10억 미만으로 입력해주세요.");
                return;
            }
            editText.setText( editText.getText()+"/");
            editText.setSelection(editText.length());
            isPreOperator = true;
            operatorList.add("/");
        }else if( v.equals( btn_sum )){
            if( isPreOperator == true ) {
                return;
            }
            if(editText.getText().length() >= 9){
                error_msg.setText("10억 미만으로 입력해주세요.");
                return;
            }
            isPreOperator = true;
            editText.setText( editText.getText()+"+");
            editText.setSelection(editText.length());
            operatorList.add("+");
        }else if( v.equals( btn_sub )){
            if( isPreOperator == true ) {
                return;
            }
            if(editText.getText().length() >= 9){
                error_msg.setText("10억 미만으로 입력해주세요.");
                return;
            }
            isPreOperator = true;
            editText.setText( editText.getText()+"-");
            editText.setSelection(editText.length());
            operatorList.add("-");
        }else if( v.equals( btn_mul )){
            if( isPreOperator == true ) {
                return;
            }
            if(editText.getText().length() >= 9){
                error_msg.setText("10억 미만으로 입력해주세요.");
                return;
            }
            isPreOperator = true;
            editText.setText( editText.getText()+"X");
            editText.setSelection(editText.length());
            operatorList.add("X");
        }else if(v.equals(btn_down)){
            flipper.setDisplayedChild(0);
            select_key.setVisibility(View.INVISIBLE);
            spacer1.setVisibility(View.VISIBLE);
            spacer2.setVisibility(View.GONE);
            num.setBackgroundResource(R.mipmap.basic_on);
            cal.setBackgroundResource(R.mipmap.group_off);
        } else if( v.equals( btn_result )){
            if(editText.getText().toString().equals("")){
                return;
            }else{
                String str = editText.getText().subSequence( editText.getText().length()-1, editText.getText().length() ).toString();
                if( "+".equals(str) || "-".equals(str) || "X".equals(str) || "/".equals(str)){
                    return;
                }
            }
            editText.setText( calc(editText.getText().toString()) );
            editText.setSelection(editText.length());
        }else if( v.equals( bt0 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText( U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"0"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt1 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"1"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt2 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"2"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt3 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"3"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt4 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"4"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt5 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"5"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt6 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"6"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt7 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"7"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt8 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"8"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt9 )){
            if(editText.getText().length() >= 11){
                return;
            }
            editText.setText(  U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString())+"9"));
            editText.setSelection(editText.length());
        }else if( v.equals( bt_del )){
            if( editText.getText().length() != 0 ) {
                editText.setText(U.getInstance().toNumFormat(U.getInstance().removeComa(editText.getText().toString()).subSequence( 0 , U.getInstance().removeComa(editText.getText().toString()).length()-1).toString()));
                editText.setSelection(editText.length());
            }
        }else if( v.equals( bt_down )){
            flipper.setDisplayedChild(0);
            select_key.setVisibility(View.INVISIBLE);
            spacer1.setVisibility(View.VISIBLE);
            spacer2.setVisibility(View.GONE);
            num.setBackgroundResource(R.mipmap.basic_on);
            cal.setBackgroundResource(R.mipmap.group_off);
        }
    }

    //계산기 계산 함수
    private String calc(String exp) {
        ArrayList<Integer> numberList = new ArrayList<Integer>();
        StringTokenizer st = new StringTokenizer(exp,"X/+-");


        while( st.hasMoreTokens() ) {
            int number = Integer.parseInt(st.nextToken());
            numberList.add( number );
            Log.d("aaa", String.valueOf(number) );
        }

        int result = numberList.get(0);
        U.getInstance().log("계산기==== 1차항: "+String.valueOf(result));
        U.getInstance().log("계산기==== 연산자: "+ String.valueOf(operatorList.size()));

        for (int i = 0; i < operatorList.size(); i++) {
            String operator = operatorList.get(i);

            if ("X".equals(operator)) {
                result = (result * numberList.get(i + 1));
            } else if ("/".equals(operator)) {
                result = (result / numberList.get(i + 1));
            } else if ("+".equals(operator)) {
                result = (result + numberList.get(i + 1));
            } else if ("-".equals(operator)) {
                result = (result - numberList.get(i + 1));
            }
            U.getInstance().log("계산기==== 2차항: "+String.valueOf(numberList.get(1)));
        }
        operatorList.clear();
        numberList.clear();
        isPreOperator=false;
        if(result<0){
            result=0;
        }
        return String.valueOf(result);

    }

    /***************************************************/
    /** 애니메이션 설정 **/

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
    /***************************************************/


    //백 키 이벤트
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
                spacer1.setVisibility(View.VISIBLE);
                spacer2.setVisibility(View.GONE);
                num.setBackgroundResource(R.mipmap.basic_on);
                cal.setBackgroundResource(R.mipmap.group_off);
            }else if(flipper.getCurrentView().getId() == R.id.secondViewFlipper){
                flipper.setDisplayedChild(0);
                spacer1.setVisibility(View.VISIBLE);
                spacer2.setVisibility(View.GONE);
                num.setBackgroundResource(R.mipmap.basic_on);
                cal.setBackgroundResource(R.mipmap.group_off);
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

        spacer1.setVisibility(View.VISIBLE);
        spacer2.setVisibility(View.GONE);
        num.setBackgroundResource(R.mipmap.basic_on);
        cal.setBackgroundResource(R.mipmap.group_off);
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


    //서버로 예산 월급일 전송
    public void pushBudget(final int budget, int set_date){
        showPd();
        String email = U.getInstance().getEmail(BudgetSettingActivity.this);
        final int budg=budget;
        final int day = set_date;
        Req_Budget req_budget = new Req_Budget(email,budget,set_date);

        Call<Res> res = SNet.getInstance().getAllFactoryIm().pushBudget(req_budget);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(response.body().getDoc().getOk()==1){
                            U.getInstance().log(response.body().getDoc().toString());
                            if(response.body().getDoc().getN()==1){
                                Toast.makeText(BudgetSettingActivity.this,"예산과 월급일이 입력되었습니다.",Toast.LENGTH_SHORT).show();
                                U.getInstance().setBudget(BudgetSettingActivity.this, budg);
                                U.getInstance().setDay(BudgetSettingActivity.this,day);
                            }else{
                                Toast.makeText(BudgetSettingActivity.this,"예산과 월급일이 입력실패.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            U.getInstance().log("실패");
                        }

                    } else {
                        U.getInstance().log("통신실패1");
                    }
                } else {
                    try {
                        U.getInstance().log("통신실패2" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                stopPd();
            }
            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }

    //날짜선택 다이얼로그
    private void ShowDialog()
    {
        LayoutInflater dialog = LayoutInflater.from(this);
        final View dialogLayout = dialog.inflate(R.layout.activity_custom_dialog, null);
        final Dialog myDialog = new Dialog(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = myDialog.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = layoutParams.MATCH_PARENT;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);


        myDialog.setTitle("월급일 지정");
        myDialog.setContentView(dialogLayout);
        myDialog.show();





        final NumberPicker number_picker = (NumberPicker)dialogLayout.findViewById(R.id.number_picker);
        Button btn_ok = (Button)dialogLayout.findViewById(R.id.ok);
        Button btn_cancel = (Button)dialogLayout.findViewById(R.id.cencel);

        btn_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                day.setText(Integer.toString(number_picker.getValue()));

                remain_day.setText(""+calRemainDay(number_picker.getValue()));

                myDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                myDialog.cancel();
            }
        });
    }



    //남은 일 구하기
    public int calRemainDay(int set_day){
        int nYear;
        int nMonth;
        int nDay;

        int nextMonth;
        int nextYear;

        // 현재 날짜 구하기
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        nYear = calendar.get(Calendar.YEAR);
        nMonth = calendar.get(Calendar.MONTH) + 1;
        nDay = calendar.get(Calendar.DAY_OF_MONTH);

        if(set_day <= nDay ) {
            if(nMonth==12) {
                nextMonth=1;
                nextYear=nYear+1;
            }else {
                nextMonth = nMonth+1;
                nextYear=nYear;
            }
        }else {
            nextMonth = nMonth;
            nextYear=nYear;
        }

        String current= nYear+""+(nMonth<10? "0"+Integer.toString(nMonth):Integer.toString(nMonth)) +""+nDay;
        String next= nextYear+""+(nextMonth<10? "0"+Integer.toString(nextMonth):Integer.toString(nextMonth)) +""+set_day;

        U.getInstance().log(current+","+next+" 날짜 차 "+U.getInstance().doDiffOfDate(next,current));



        return (int)U.getInstance().doDiffOfDate(next,current);
    }

}
