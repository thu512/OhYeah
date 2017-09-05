package com.changjoo.ohyeah.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.dialog.FixModiDialog;
import com.changjoo.ohyeah.dialog.NestAddDialog;
import com.changjoo.ohyeah.dialog.NewBudgetDialog;
import com.changjoo.ohyeah.dialog.Purpose1Dialog;
import com.changjoo.ohyeah.dialog.Purpose2Dialog;
import com.changjoo.ohyeah.dialog.Purpose3Dialog;
import com.changjoo.ohyeah.model.Expense;
import com.changjoo.ohyeah.model.Fix;
import com.changjoo.ohyeah.model.Req_Main_day;
import com.changjoo.ohyeah.model.Req_ModiFix;
import com.changjoo.ohyeah.model.Req_token;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import layout.FirstFragment;
import layout.SecondFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    private ViewPager vp;
    private LinearLayout ll, bg;
    private SmartTabLayout viewPagerTab;
    private LinearLayout dragView;
    private SlidingUpPanelLayout mLayout;
    private Drawable alpha;
    boolean flag = false;
    RecyclerView list;


    ArrayList<Expense> expenses_total = new ArrayList<>(); //전제 거래내역 저장
    ArrayList<Expense> expenses = new ArrayList<>(); //상황에 맞게 데이터 변경

    //한달
    ArrayList<Expense> expenses_total_month = new ArrayList<>(); //전제 거래내역 저장
    ArrayList<Expense> expenses_temp = new ArrayList<>(); //상황에 맞게 데이터 변경

    TradeAdapter tradeAdapter;
    RelativeLayout all_btn;
    RelativeLayout in_btn;
    RelativeLayout out_btn;
    RelativeLayout pagedown;
    pagerAdapter pagerAdapter;
    Button pin_btn;
    Button set_btn;
    Button wallet_btn;
    Button all_bt;
    Button in_bt;
    Button out_bt;
    TextView all_txt;
    TextView in_txt;
    TextView out_txt;

    NestAddDialog nestAddDialog;
    NewBudgetDialog newBudgetDialog;
    FixModiDialog fixModiDialog;
    Purpose1Dialog purpose1Dialog;
    Purpose2Dialog purpose2Dialog;
    Purpose3Dialog purpose3Dialog;

    int first_budget_month;
    int budget;
    int first_budget;

    double daily_budget;
    int goal_item;
    int goal_money;
    int now_saving;
    double ratio_saving;



    //버스 ==================문자===================================
    @Subscribe
    public void recvBus(String msg) throws InterruptedException {
        U.getInstance().log(msg);
        Thread.sleep(2000);
//        readDay();
//        readMonth();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    //액티비티가 소멸되면 도착역도 폐기한다.
    @Override
    protected void onDestroy() {
        U.getInstance().getAuthBus().unregister(this);
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshToken();

        //도착역 설정
        U.getInstance().getAuthBus().register(this);
        String str = getIntent().getStringExtra("popup");
        //예산 초과시 푸쉬타고와서 비상금 추가 팝업띄우기
        if(str != null){
            if(str.equals("nest")) //비상금 추가
            {
                nestAddDialog = new NestAddDialog(MainActivity.this, new View.OnClickListener() {
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
                                readDay();
                                readMonth();
                                Toast.makeText(getApplicationContext(),"비상금이 추가되었습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                nestAddDialog.show();

            }else if(str.equals("budgetSet"))  //새로운 예산 설정
            {
                newBudgetDialog = new NewBudgetDialog(MainActivity.this);
                newBudgetDialog.show();
            }
            else if(str.equals("fix")) //고정지출 확인 알림
            {

                final String msg_content = getIntent().getStringExtra("msg_content");
                final int msg_money = getIntent().getIntExtra("msg_money",0);
                final String msg_date = getIntent().getStringExtra("msg_date");
                final String msg_time = getIntent().getStringExtra("msg_time");
                final String fix_data = getIntent().getStringExtra("fix_data");

                U.getInstance().log("고정지출 내역 받아옴!!"+msg_content+" / "+msg_money+" / "+msg_date+" / "+msg_time+" / "+fix_data);

                fixModiDialog = new FixModiDialog(this, msg_content, msg_money, fix_data, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { //아니오
                        Req_ModiFix req_modiFix = new Req_ModiFix(U.getInstance().getEmail(MainActivity.this), 0,new Fix(msg_content,msg_money,msg_date,msg_time,fix_data));
                        modiFix(req_modiFix);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { //네

                        Req_ModiFix req_modiFix = new Req_ModiFix(U.getInstance().getEmail(MainActivity.this), 1,new Fix(msg_content,msg_money,msg_date,msg_time,fix_data));
                        modiFix(req_modiFix);
                    }
                });

                fixModiDialog.show();
            }
            else if(str.equals("purpose")) //목표 달성
            {
                //목표달성 푸쉬에서 intent로 목표 달성액 받아와서 뿌려줌
                purpose1Dialog = new Purpose1Dialog(this, 800000000, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { //토스연결후 목표 설정 팝업 띄우기
                        purpose1Dialog.dismiss();
                        checkToss();

                    }
                }, new View.OnClickListener() { //그냥 끄고 다음 저축 확인 팝업
                    @Override
                    public void onClick(View view) {
                        purpose1Dialog.dismiss();
                        purpose2Dialog = new Purpose2Dialog(MainActivity.this, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                purpose2Dialog.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                purpose2Dialog.dismiss();
                                purpose3Dialog = new Purpose3Dialog(MainActivity.this);
                                purpose3Dialog.show();
                            }
                        });
                        purpose2Dialog.show();
                    }
                });
                purpose1Dialog.show();
            }
        }


        all_bt = (Button)findViewById(R.id.all_bt);
        in_bt = (Button)findViewById(R.id.in_bt);
        out_bt = (Button)findViewById(R.id.out_bt);
        all_txt = (TextView)findViewById(R.id.all_txt);
        in_txt = (TextView)findViewById(R.id.in_txt);
        out_txt = (TextView)findViewById(R.id.out_txt);

        pin_btn= (Button) findViewById(R.id.pin_btn);
        wallet_btn = (Button) findViewById(R.id.wallet_btn);
        set_btn = (Button) findViewById(R.id.set_btn);
        all_btn = (RelativeLayout) findViewById(R.id.all_btn);
        in_btn = (RelativeLayout) findViewById(R.id.in_btn);
        out_btn = (RelativeLayout) findViewById(R.id.out_btn);
        pagedown = (RelativeLayout) findViewById(R.id.pagedown);
        dragView = (LinearLayout) findViewById(R.id.dragView);
        alpha = dragView.getBackground();
        list = (RecyclerView) findViewById(R.id.list);

        ll = (LinearLayout) findViewById(R.id.ll);
        bg = (LinearLayout) findViewById(R.id.bg);
        vp = (ViewPager) findViewById(R.id.vp);
        pagerAdapter = new pagerAdapter(getSupportFragmentManager());
        vp.setAdapter(pagerAdapter);
        vp.setCurrentItem(0);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(vp);
        final TextView view0 = (TextView) viewPagerTab.getTabAt(0);
        final TextView view1 = (TextView) viewPagerTab.getTabAt(1);
        view0.setTypeface(Typeface.DEFAULT);
        view1.setTypeface(Typeface.DEFAULT);
        view0.setTextSize(20);
        view1.setTextSize(15);
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    view1.setTextSize(15);
                    view0.setTextSize(21);
                } else {
                    view0.setTextSize(15);
                    view1.setTextSize(21);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });






        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    all_bt.setBackgroundResource(R.mipmap.radio_on);
                    in_bt.setBackgroundResource(R.mipmap.radio_off);
                    out_bt.setBackgroundResource(R.mipmap.radio_off);
                    all_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_bold);
                    in_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);
                    out_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);
                    U.getInstance().log("호출됨!!");
                    expenses.clear();
                    expenses_temp.clear();
                    for(Expense expense:expenses_total){
                        expenses.add(expense);
                        expenses_temp.add(expense);
                    }
                    tradeAdapter.notifyDataSetChanged();

                    //금액이 마이너스일때 해당 백그라운드 적용
                    if(daily_budget<0){
                        bg.setBackgroundResource(R.drawable.bg_transition2);
                        TransitionDrawable td = (TransitionDrawable) getResources().getDrawable(R.drawable.bg_transition2);
                        bg.setBackground(td);
                        td.startTransition(700); // duration 3 seconds
                        changeStatusBarColor("#360909");
                    }

                } else {
                    all_bt.setBackgroundResource(R.mipmap.radio_on);
                    in_bt.setBackgroundResource(R.mipmap.radio_off);
                    out_bt.setBackgroundResource(R.mipmap.radio_off);
                    all_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_bold);
                    in_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);
                    out_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);
                    expenses.clear();
                    expenses_temp.clear();
                    for(Expense expense:expenses_total_month){
                        expenses.add(expense);
                        expenses_temp.add(expense);
                    }
                    tradeAdapter.notifyDataSetChanged();
                    if(daily_budget<0){
                        bg.setBackgroundResource(R.drawable.bg_transition1);
                        //만약 현재 백그라운드가 빨간색이면 해당 if통과
                        TransitionDrawable td = (TransitionDrawable) getResources().getDrawable(R.drawable.bg_transition1);
                        bg.setBackground(td);
                        td.startTransition(700); // duration 3 seconds
                        changeStatusBarColor("#3b4aaa");
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //거래내역 뷰 초기설정
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setShadowHeight(0);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mLayout.setPanelHeight(U.getInstance().getDpToPixel(this, 170));//초기 패널 높이 => 픽셀 값이므로 변환 필요

        alpha.setAlpha(0);
        dragView.setBackground(alpha);

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                alpha.setAlpha((int) (slideOffset * 255));
                dragView.setBackground(alpha);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    pagedown.setVisibility(View.VISIBLE);
                } else {
                    pagedown.setVisibility(View.GONE);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        //리싸이클러 뷰
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tradeAdapter = new TradeAdapter();
        //데이터셋팅
        list.setAdapter(tradeAdapter);

        //전체, 입금, 출금 내역 불러오기 => 어댑터 notify
        all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_bt.setBackgroundResource(R.mipmap.radio_on);
                in_bt.setBackgroundResource(R.mipmap.radio_off);
                out_bt.setBackgroundResource(R.mipmap.radio_off);
                all_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_bold);
                in_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);
                out_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);

                expenses_temp.clear();
                for(Expense expense:expenses){
                    expenses_temp.add(expense);
                }
                tradeAdapter.notifyDataSetChanged();

            }
        });

        in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                all_bt.setBackgroundResource(R.mipmap.radio_off);
                in_bt.setBackgroundResource(R.mipmap.radio_on);
                out_bt.setBackgroundResource(R.mipmap.radio_off);
                all_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);
                in_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_bold);
                out_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);

                expenses_temp.clear();
                for(Expense expense:expenses){
                    if(expense.getEx_in().equals("입금")){
                        expenses_temp.add(expense);
                    }
                }
                tradeAdapter.notifyDataSetChanged();
            }
        });

        out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                all_bt.setBackgroundResource(R.mipmap.radio_off);
                in_bt.setBackgroundResource(R.mipmap.radio_off);
                out_bt.setBackgroundResource(R.mipmap.radio_on);
                all_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);
                in_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_normal);
                out_txt.setTextAppearance(MainActivity.this, R.style.TextStyle_bold);

                expenses_temp.clear();
                for(Expense expense:expenses){
                    if(expense.getEx_in().equals("출금")){
                        expenses_temp.add(expense);
                    }
                }
                tradeAdapter.notifyDataSetChanged();

            }
        });


        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent);
            }
        });

        wallet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ModifySettingActivity.class);
                startActivity(intent);
            }
        });

        pin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ModifyFixActivity.class);
                startActivity(intent);
            }
        });
    }





    @Override
    protected void onResume() {
        super.onResume();
        expenses_total.clear();
        expenses_total_month.clear();
        expenses.clear();
        expenses_temp.clear();
        vp.setCurrentItem(0);
        //데이터 불러오기=========================================================================
        readDay();
        readMonth();
    }

    //뷰페이저 아답타
    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FirstFragment.create(first_budget,daily_budget,ratio_saving,goal_item);
                case 1:
                    return SecondFragment.create(first_budget_month, budget, ratio_saving, goal_item);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "오늘";
                case 1:
                    return "한 달";
                default:
                    return null;
            }
        }
        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

    }


    //거래내역 뷰홀더
    class TradeViewHolder extends RecyclerView.ViewHolder {
        TextView time, content, money;

        public TradeViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            content = (TextView) itemView.findViewById(R.id.content);
            money = (TextView) itemView.findViewById(R.id.money);
        }

    }

    //거래내역 adapter
    class TradeAdapter extends RecyclerView.Adapter<TradeViewHolder> {
        @Override
        public TradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(MainActivity.this).inflate(R.layout.cell_trade_layout, parent, false);
            return new TradeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TradeViewHolder holder, int position) {
            //데이터세팅!!!
            Expense expense = expenses_temp.get(position);

            if(vp.getCurrentItem()==1) {
                if (expense.getEx_in().equals("출금")) {
                    holder.money.setText("-" + U.getInstance().toNumFormat(Integer.toString(expense.getMoney())));
                } else {
                    holder.money.setText("+" + U.getInstance().toNumFormat(Integer.toString(expense.getMoney())));
                }
                holder.time.setText(expense.getDate()+" "+expense.getTime());
                holder.content.setText(expense.getRecord());
            }else{
                if (expense.getEx_in().equals("출금")) {
                    holder.money.setText("-" + U.getInstance().toNumFormat(Integer.toString(expense.getMoney())));
                } else {
                    holder.money.setText("+" + U.getInstance().toNumFormat(Integer.toString(expense.getMoney())));
                }
                holder.time.setText(expense.getTime());
                holder.content.setText(expense.getRecord());
            }

        }

        @Override
        public int getItemCount() {
            return expenses_temp == null ? 0 : expenses_temp.size();
        }


    }





    //상태바 색상 변경
    public void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public void readDay() {
        final SweetAlertDialog sweetAlertDialog = U.getInstance().showLoading(this);
        sweetAlertDialog.show();
        Req_Main_day req_main_day = new Req_Main_day(U.getInstance().getEmail(this));
        Call<Res> res = SNet.getInstance().getAllFactoryIm().readDay(req_main_day);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        U.getInstance().log("메인 하루 불러오기: " + response.body().toString());

                        budget = response.body().getDoc().getAsset().getBudget();
                        first_budget = (int)response.body().getDoc().getAsset().getFirst_daily_budget();
                        daily_budget = response.body().getDoc().getAsset().getDaily_budget();
                        goal_item = response.body().getDoc().getGoal().getGoal_item();
                        goal_money = response.body().getDoc().getGoal().getGoal_money();

                        now_saving = response.body().getDoc().getGoal().getNow_saving();
                        ratio_saving = response.body().getDoc().getGoal().getRatio_saving();
                        U.getInstance().log(budget+"/"+daily_budget+"/"+goal_item+"/"+goal_money+"/"+now_saving+"/"+ratio_saving);

                        for(Expense expense : response.body().getDoc().getExpenditure().getExpense()){
                            if(expense.getToday_yn().equals("Y")){
                                expenses_total.add(expense);
                                expenses.add(expense);
                                expenses_temp.add(expense);
                            }
                        }

                        if(daily_budget<0){
                            bg.setBackgroundResource(R.drawable.bg_gradation3);
                            changeStatusBarColor("#360909");
                        }else {
                            bg.setBackgroundResource(R.drawable.bg_gradation);
                            changeStatusBarColor("#3b4aaa");
                        }

                        pagerAdapter.notifyDataSetChanged();
                        tradeAdapter.notifyDataSetChanged();

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
                sweetAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }

    public void readMonth() {
        final SweetAlertDialog sweetAlertDialog = U.getInstance().showLoading(this);
        sweetAlertDialog.show();
        Req_Main_day req_main_day = new Req_Main_day(U.getInstance().getEmail(this));
        Call<Res> res = SNet.getInstance().getAllFactoryIm().readMonth(req_main_day);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        U.getInstance().log("메인 한달 불러오기: " + response.body().toString());

                        budget = response.body().getDoc().getAsset().getBudget();
                        first_budget_month = (int)response.body().getDoc().getAsset().getFirst_month_budget();

                        goal_item = response.body().getDoc().getGoal().getGoal_item();
                        goal_money = response.body().getDoc().getGoal().getGoal_money();
                        ratio_saving = response.body().getDoc().getGoal().getRatio_saving();
                        U.getInstance().log(budget+"/"+first_budget_month+"/"+goal_item+"/"+goal_money+"/"+ratio_saving);

                        for(Expense expense : response.body().getDoc().getExpenditure().getExpense()){
                            if(expense.getMonth_yn().equals("Y")){
                                expenses_total_month.add(expense);
                            }
                        }

                        pagerAdapter.notifyDataSetChanged();
                        tradeAdapter.notifyDataSetChanged();

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
                sweetAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }


    public void refreshToken(){
        showPd();
        Req_token req_token = new Req_token(U.getInstance().getEmail(this), FirebaseInstanceId.getInstance().getToken());

        Call<Res> res = SNet.getInstance().getAllFactoryIm().refreshToken(req_token);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(response.body().getResult()==1){
                            U.getInstance().log("토큰갱신 완료");
                        }else{
                            U.getInstance().log("토큰갱신 실패");
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


    public void modiFix(Req_ModiFix req_modiFix){
        showPd();
        Call<Res> res = SNet.getInstance().getAllFactoryIm().modiFix(req_modiFix);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if(response.body().getResult()==1){ //고정 지출 수정
                            U.getInstance().log("고정지출 수정완료");
                            Toast.makeText(MainActivity.this, "고정지출 수정완료!!",Toast.LENGTH_SHORT).show();
                            if(fixModiDialog != null){
                                fixModiDialog.dismiss();
                            }
                        }else if(response.body().getResult()==2) { //고정 x 지출됨
                            U.getInstance().log("지출내역 추가완료");
                            Toast.makeText(MainActivity.this, "지출내역 추가완료!!", Toast.LENGTH_SHORT).show();
                            if (fixModiDialog != null) {
                                fixModiDialog.dismiss();
                            }
                        }else{
                            U.getInstance().log("에러");
                            Toast.makeText(MainActivity.this, "에러", Toast.LENGTH_SHORT).show();
                            if (fixModiDialog != null) {
                                fixModiDialog.dismiss();
                            }
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

    //목표완료시 toss앱으로 연결 -> 설치안되있을시 에러 팝업 띄우기
    public void checkToss(){
        PackageManager pm = this.getPackageManager();
        boolean flag = true;
        // 설치된 어플리케이션 리스트 취득
        List<ApplicationInfo> packs = pm.getInstalledApplications(
                PackageManager.GET_META_DATA);

        String mAppPackge = null;
        for (ApplicationInfo app : packs) {
            mAppPackge = app.packageName;
            Log.d("FFF","패키지명: "+mAppPackge);
            if(mAppPackge.equals("viva.republica.toss")){
                Log.d("FFF","=================================확인: "+mAppPackge);
                Intent intent = this.getPackageManager().getLaunchIntentForPackage("viva.republica.toss");
                intent.setAction(Intent.ACTION_MAIN);
                startActivity(intent);
                flag=false;
            }
        }
        if(flag){
            U.getInstance().showSimplePopup(MainActivity.this, "연결 에러", "Toss앱을 설치해주세요.", SweetAlertDialog.ERROR_TYPE);
        }
    }

    //백키에 대응하는 메소드
    @Override
    public void onBackPressed() {
        //아래코드를 막으면 현재 화면의 종료처리가 중단됨
        //super.onBackPressed();
        if(!isFirstEnd){
            //최초한번 백키를 눌렀다.
            isFirstEnd=true;
            //3초후에 초기화된다.(최초로 한번 백키를 눌렀던 상황이)
            handler.sendEmptyMessageDelayed(1,3000);
            Toast.makeText(this,"뒤로가기를 한번 더 누르시면 종료됩니다.",Toast.LENGTH_LONG).show();
        }else{
            super.onBackPressed();
        }
    }

    boolean isFirstEnd; //백키를 한번 눌렀나?

    //핸들러, 메세지를 던져서 큐에 담고 하나씩 꺼내서 처리하는 메시징 시스템
    Handler handler = new Handler(){
        //이 메소드는 큐에 메세지가 존재하면 뽑아서 호출된다.
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){ //최초로 백키를 한번 눌렀다.

            }else if(msg.what == 1){ //3초가 지났다. 다시 초기화.
                isFirstEnd=false;
            }
        }
    };
}


