package com.changjoo.ohyeah.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.dialog.NestAddDialog;
import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Req_set;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.BackPressEditText;
import com.changjoo.ohyeah.utill.U;
import com.shawnlin.numberpicker.NumberPicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import me.grantland.widget.AutofitTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.changjoo.ohyeah.utill.U.doDiffOfDate;

public class ModifySettingActivity extends Activity implements View.OnClickListener {

    ImageButton back;

    BackPressEditText editText;
    BackPressEditText nest_money;
    TextView day;
    TextView percent;
    AutofitTextView money;
    ProgressBar pb;
    Button add_money;
    TextView err;

    Boolean flag = false; //예산보다 초과하지 않았는지

    Button submit1;

    //커스텀 키보드
    ViewFlipper flipper;
    LinearLayout select_key;
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
    String result ="";
    NestAddDialog nestAddDialog;

    @Override
    protected void onResume() {
        super.onResume();
        //기존데이터 셋팅
        settingData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_setting);


        View cal_keyboard = (View) getLayoutInflater().inflate(R.layout.cal_keyboard, null);
        View num_keyboard = (View) getLayoutInflater().inflate(R.layout.default_keyboard, null);

        err = (TextView)findViewById(R.id.err);

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit1 = (Button)findViewById(R.id.submit);

        add_money = (Button) findViewById(R.id.add_money);
        editText = (BackPressEditText) findViewById(R.id.remain_money);
        nest_money = (BackPressEditText) findViewById(R.id.nest_money);
        day = (TextView) findViewById(R.id.day);
        percent = (TextView) findViewById(R.id.percent);
        pb = (ProgressBar) findViewById(R.id.pb);
        money = (AutofitTextView) findViewById(R.id.money);


        //월급일 설정 키보드 띄움 막기
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialog();
            }
        });

        //예산 추가
        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestAddDialog = new NestAddDialog(ModifySettingActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nestAddDialog.dismiss();
                    }
                },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                nestAddDialog.pushServer();
                                nestAddDialog.dismiss();
                                finish();
                                Toast.makeText(getApplicationContext(),"비상금이 추가되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                nestAddDialog.show();
            }
        });


        //비상금 설정에 따른 예상 일일예산 및 예산대비 퍼센트구하기
        nest_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(result)){     // StackOverflow를 막기위해,
                    result = U.getInstance().toNumFormat(charSequence.toString());
                    nest_money.setText(result);    // 결과 텍스트 셋팅.
                    nest_money.setSelection(result.length());     // 커서를 제일 끝으로 보냄.
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int money;
                if (editable.toString().equals("")) {
                    money = 0;
                } else {
                    money = Integer.parseInt(U.getInstance().removeComa(editable.toString()));
                }
                if (money >= Integer.parseInt(U.getInstance().removeComa(editText.getText().toString()))) {
                    err.setVisibility(View.VISIBLE);
                    ModifySettingActivity.this.money.setText(U.getInstance().toNumFormat("" + calDayMoney(Integer.parseInt(U.getInstance().removeComa(editText.getText().toString())), 0, money, Integer.parseInt(day.getText().toString()))));

                    pb.setProgress((int) ((double) money / (double) (Integer.parseInt(U.getInstance().removeComa(editText.getText().toString()))) * 100.0));
                    percent.setText("" + (int) ((double) money / (double) ((Integer.parseInt(U.getInstance().removeComa(editText.getText().toString())))) * 100.0));

                    flag = false;
                } else {
                    err.setVisibility(View.INVISIBLE);
                    U.getInstance().log("예상 일일 예산: " +  calDayMoney(Integer.parseInt(U.getInstance().removeComa(editText.getText().toString())), 0, money, Integer.parseInt(day.getText().toString())));
                    ModifySettingActivity.this.money.setText(U.getInstance().toNumFormat(""+calDayMoney(Integer.parseInt(U.getInstance().removeComa(editText.getText().toString())), 0, money, Integer.parseInt(day.getText().toString()))));

                    U.getInstance().log("" + (double) money);
                    U.getInstance().log("" + (double) Integer.parseInt(U.getInstance().removeComa(editText.getText().toString())));
                    U.getInstance().log("" + ((double) money / (double) (Integer.parseInt(U.getInstance().removeComa(editText.getText().toString()))) * 100.0));
                    pb.setProgress((int) ((double) money / (double) (Integer.parseInt(U.getInstance().removeComa(editText.getText().toString()))) * 100.0));
                    percent.setText("" + (int) ((double) money / (double) ((Integer.parseInt(U.getInstance().removeComa(editText.getText().toString())))) * 100.0));

                    flag = true;
                }
            }
        });


        //서버전송!
        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPreOperator){
                    editText.setError("계산을 완료해주세요.");
                    return;
                }
                if(TextUtils.isEmpty(editText.getText())){
                    editText.setError("에산을 입력해주세요.");
                    return;
                }
                if(U.getInstance().removeComa(editText.getText().toString()).length() > 9){
                    editText.setError("10억미만으로 입력해주세요.");
                    return;
                }
                if(flag){
                    U.getInstance().setDay(ModifySettingActivity.this, Integer.parseInt(day.getText().toString()));

                    sendServer(Integer.parseInt(U.getInstance().removeComa(editText.getText().toString())),Integer.parseInt(day.getText().toString()),Integer.parseInt(U.getInstance().removeComa(nest_money.getText().toString())));
                }
            }
        });


        //커스텀 키보드
        num = (Button) findViewById(R.id.num);
        cal = (Button) findViewById(R.id.cal);
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
        submit = (Button) findViewById(R.id.submit);
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
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        operatorList = new ArrayList<String>();
        isPreOperator = false;
        final Map<String, String> calc = new HashMap<String, String>();
        calc.put("reset", "N");
        calc.put("number1", "");
        calc.put("operation", "");
        calc.put("number2", "");

        select_key = (LinearLayout) findViewById(R.id.select_key);
        flipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        flipper.setVisibility(View.VISIBLE);  // ViewFlipper 가 Activity 첫 실행시 보이지 않게 설정
        flipper.setInAnimation(appearSecurityKeyboardAnimation()); // 나타날때 애니메이션
        flipper.setOutAnimation(disappearSecurityKeyboardAnimation()); // 사라질때 애니메이션

        // 계산기 키보드의 오프 이미지를 표시하기 위해 임의의 empty view를 삽입
        TextView emptyView = new TextView(this);

        flipper.addView(emptyView, 0);
        flipper.addView(cal_keyboard, 1);
        flipper.addView(num_keyboard, 2);
        flipper.setDisplayedChild(0);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.setTextIsSelectable(true);
        editText.setSelection(editText.getText().length());
        editText.setOnBackPressListener(onBackPressListener);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Log.d("FFF", "포커스받음");
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    flipper.setDisplayedChild(2);
                    select_key.setVisibility(View.VISIBLE);
                } else {
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
                flipper.setDisplayedChild(2);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                select_key.setVisibility(View.VISIBLE);

            }
        });


        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    flipper.setDisplayedChild(0);
                    select_key.setVisibility(View.INVISIBLE);
                    num.setBackgroundResource(R.mipmap.basic_on);
                    cal.setBackgroundResource(R.mipmap.group_off);
                }
                return false;
            }

        });

    }

    //기존설정값 불러와서 표시
    public void settingData() {
        showPd();
        Req_email req_email = new Req_email(U.getInstance().getEmail(this));
        final Call<Res> res = SNet.getInstance().getAllFactoryIm().getState(req_email);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, final Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        U.getInstance().log("예산수정: " + response.body().toString());
                        if (response.body().getResult() == 1) {
                            day.setText("" + response.body().getDoc().getAsset().getSet_date());
                            editText.setText(U.getInstance().toNumFormat("" + response.body().getDoc().getAsset().getBudget()));
                            nest_money.setText(U.getInstance().toNumFormat("" + response.body().getDoc().getAsset().getSpare_money()));
                            percent.setText("" + response.body().getDoc().getAsset().getRatio_spare());
                            Handler handler = new Handler() {
                            };
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    pb.setProgress(response.body().getDoc().getAsset().getRatio_spare());
                                }
                            }, 100);
                            money.setText(U.getInstance().toNumFormat("" + (int)response.body().getDoc().getAsset().getDaily_budget()));
                        } else {

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

    //월급일 선택 다이얼로그
    private void ShowDialog() {
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

        final NumberPicker number_picker = (NumberPicker) dialogLayout.findViewById(R.id.number_picker);
        Button btn_ok = (Button) dialogLayout.findViewById(R.id.ok);
        Button btn_cancel = (Button) dialogLayout.findViewById(R.id.cencel);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day.setText(Integer.toString(number_picker.getValue()));
                myDialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });
    }


    //일반 키보드로 전환
    public void number_key(View view) {
        num.setBackgroundResource(R.mipmap.basic_on);
        cal.setBackgroundResource(R.mipmap.group_off);
        flipper.setDisplayedChild(2);
        editText.setSelection(editText.length());
    }


    //계산기 키보드로 전환
    public void cal_key(View view) {
        num.setBackgroundResource(R.mipmap.basic_off);
        cal.setBackgroundResource(R.mipmap.group_on);
        flipper.setDisplayedChild(1);
        if(!editText.getText().toString().equals("")){
            editText.setText(U.getInstance().removeComa(editText.getText().toString()));
        }
        editText.setSelection(editText.length());
    }


    //키보드 올라와있을때 빈화면 클릭시 키보드 내리기
    public void click(View view) {
        flipper.setDisplayedChild(0);
        select_key.setVisibility(View.INVISIBLE);
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
                return;
            }
            isPreOperator = true;
            editText.setText( editText.getText()+"X");
            editText.setSelection(editText.length());
            operatorList.add("X");
        }else if(v.equals(btn_down)){
            flipper.setDisplayedChild(0);
            select_key.setVisibility(View.INVISIBLE);
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
            num.setBackgroundResource(R.mipmap.basic_on);
            cal.setBackgroundResource(R.mipmap.group_off);
        }
    }

    //계산기 계산 함수
    private String calc(String exp) {
        ArrayList<Integer> numberList = new ArrayList<Integer>();
        StringTokenizer st = new StringTokenizer(exp, "X/+-");


        while (st.hasMoreTokens()) {
            int number = Integer.parseInt(st.nextToken());
            numberList.add(number);
            Log.d("aaa", String.valueOf(number));
        }

        int result = numberList.get(0);
        Log.d("aaa", String.valueOf(result));
        Log.d("aaa", String.valueOf(operatorList.size()));

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
            Log.d("aaa", String.valueOf(numberList.get(1)));
        }
        operatorList.clear();
        numberList.clear();
        isPreOperator = false;
        if (result < 0) {
            result = 0;
        }
        return String.valueOf(result);

    }

    /***************************************************/
    /**
     * 애니메이션 설정
     **/

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                select_key.setVisibility(View.INVISIBLE);
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // 디바이스 키중 Back버튼 눌렀을때 키보드가 올라와있으면 키보드 닫기
            if (flipper.getCurrentView().getId() == R.id.firstViewFlipper) {
                flipper.setDisplayedChild(0);
                num.setBackgroundResource(R.mipmap.basic_on);
                cal.setBackgroundResource(R.mipmap.group_off);
            } else if (flipper.getCurrentView().getId() == R.id.secondViewFlipper) {
                flipper.setDisplayedChild(0);
                num.setBackgroundResource(R.mipmap.basic_on);
                cal.setBackgroundResource(R.mipmap.group_off);
            }
            // 키보드가 올라오지 않은 상태면 보통때의 back 버튼 수행
            else {
                super.onKeyDown(keyCode, event);
            }

        }

        return true;
    }

    private void didBackPressOnEditText() {

        num.setBackgroundResource(R.mipmap.basic_on);
        cal.setBackgroundResource(R.mipmap.group_off);
        select_key.setVisibility(View.INVISIBLE);
    }

    private BackPressEditText.OnBackPressListener onBackPressListener = new BackPressEditText.OnBackPressListener() {
        @Override
        public void onBackPress() {
            didBackPressOnEditText();
        }
    };


    //일일 예상 예산 구하기
    public static int calDayMoney(int budget, int fix, int nest, int set_day) {
        int result = 0;

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

        if (set_day <= nDay) {
            if (nMonth == 12) {
                nextMonth = 1;
                nextYear = nYear + 1;
            } else {
                nextMonth = nMonth + 1;
                nextYear = nYear;
            }
        } else {
            nextMonth = nMonth;
            nextYear = nYear;
        }

        String current = nYear + "" + (nMonth < 10 ? "0" + Integer.toString(nMonth) : Integer.toString(nMonth)) + "" + nDay;
        String next = nextYear + "" + (nextMonth < 10 ? "0" + Integer.toString(nextMonth) : Integer.toString(nextMonth)) + "" + set_day;

        U.getInstance().log(current + "," + next + " 날짜 차 " + doDiffOfDate(next, current));


        result = (budget - fix - nest) / (int) U.getInstance().doDiffOfDate(next, current);
        if (result < 0) {
            result = 0;
        }
        return result;
    }


    public void sendServer(int budget, int set_date, int nest){
        showPd();
        Req_set req_set = new Req_set(U.getInstance().getEmail(ModifySettingActivity.this),budget,set_date,nest);
        final Call<Res> res = SNet.getInstance().getAllFactoryIm().modifyBudget(req_set);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, final Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        U.getInstance().log("예산수정: " + response.body().toString());
                        if (response.body().getResult() == 1) {
                            Toast.makeText(ModifySettingActivity.this,"예산과 비상금이 수정되었습니다.",Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            U.getInstance().log("서버에러");
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
}
