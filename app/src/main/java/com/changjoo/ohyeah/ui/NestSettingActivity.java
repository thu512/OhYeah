package com.changjoo.ohyeah.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.Req_Nest;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import me.grantland.widget.AutofitTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.changjoo.ohyeah.utill.U.doDiffOfDate;

public class NestSettingActivity extends Activity {

    EditText nest_money;
    AutofitTextView day_money;
    TextView percent;
    TextView nest_error;
    ProgressBar pb1;

    Button submit;
    ImageButton back;

    Boolean flag = false; //예산보다 초과하지 않았는지
    String result="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_setting);

        back = (ImageButton) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.submit);
        nest_error = (TextView) findViewById(R.id.nest_error);
        day_money = (AutofitTextView) findViewById(R.id.day_money);
        percent = (TextView) findViewById(R.id.percent);
        nest_money = (EditText) findViewById(R.id.nest_money);
        pb1 = (ProgressBar) findViewById(R.id.pb1);

        final int month = U.getInstance().getBudget(NestSettingActivity.this);
        final int fix = U.getInstance().getFix(NestSettingActivity.this);
        final int day = U.getInstance().getDay(NestSettingActivity.this);

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
                if (money >= month - fix) {
                    nest_error.setVisibility(View.VISIBLE);
                    day_money.setText(U.getInstance().toNumFormat("" + calDayMoney(month, fix, money, day)));
                    pb1.setProgress((int) ((double) money / (double) (month - fix) * 100.0));
                    percent.setText("" + (int) ((double) money / (double) (month - fix) * 100.0));
                    flag = false;
                } else {
                    nest_error.setVisibility(View.INVISIBLE);
                    U.getInstance().log("예상 일일 예산: " + calDayMoney(month, fix, money, day));
                    day_money.setText(U.getInstance().toNumFormat("" + calDayMoney(month, fix, money, day)));
                    U.getInstance().log("" + (double) money);
                    U.getInstance().log("" + (double) month);
                    U.getInstance().log("" + ((double) money / (double) (month - fix) * 100.0));
                    pb1.setProgress((int) ((double) money / (double) (month - fix) * 100.0));
                    percent.setText("" + (int) ((double) money / (double) (month - fix) * 100.0));
                    flag = true;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPd();
                if (nest_money.getText().toString().equals("")) {
                    Intent intent = new Intent(NestSettingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (flag) {
                    Req_Nest req_nest = new Req_Nest(U.getInstance().getEmail(NestSettingActivity.this), Integer.parseInt(U.getInstance().removeComa(nest_money.getText().toString())));
                    Call<Res> res = SNet.getInstance().getAllFactoryIm().pushNest(req_nest);
                    res.enqueue(new Callback<Res>() {
                        @Override
                        public void onResponse(Call<Res> call, Response<Res> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    U.getInstance().log("비상금 서버 내용: " + response.body().toString());
                                    U.getInstance().log("비상금 설정 완료");
                                    U.getInstance().log("" + response.body().toString());


                                    //앞에 열려있던 액티비티 모두 종료
                                    Intent intent = new Intent(NestSettingActivity.this, MainActivity.class);
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    } else {
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    }
                                    startActivity(intent);
                                    finish();


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
                        }

                        @Override
                        public void onFailure(Call<Res> call, Throwable t) {
                            U.getInstance().log(t.getMessage() + "  / " + t.getLocalizedMessage());
                        }
                    });
                } else {
                    U.getInstance().log("에라에라에라");
                    return;
                }
            }
        });

    stopPd();
    }

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
}
