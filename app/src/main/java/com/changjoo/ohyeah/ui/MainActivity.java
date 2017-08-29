package com.changjoo.ohyeah.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.changjoo.ohyeah.dialog.NestAddDialog;
import com.changjoo.ohyeah.model.Expense;
import com.changjoo.ohyeah.model.Req_Main_day;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

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
    public void recvBus(String msg) {
        Log.d("FFF", "" + msg);
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
//        NotificationManager notificationManager = (NotificationManager) MainActivity.this.getSystemService(MainActivity.this.NOTIFICATION_SERVICE);
//        Intent intent1 = new Intent(MainActivity.this.getApplicationContext(), MainActivity.class); //인텐트 생성.
//
//
//        Notification.Builder builder = new Notification.Builder(getApplicationContext());
//        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);//현재 액티비티를 최상으로 올리고, 최상의 액티비티를 제외한 모든 액티비티를
//
//
//        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(MainActivity.this, 0, intent1, FLAG_UPDATE_CURRENT);
//
//        builder.setSmallIcon(R.mipmap.pin).setTicker("HETT").setWhen(System.currentTimeMillis())
//                .setNumber(1).setContentTitle("푸쉬 제목").setContentText("푸쉬내용")
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingNotificationIntent).setAutoCancel(true).setOngoing(true);
//
//
//        notificationManager.notify(1, builder.build()); // Notification send

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //도착역 설정
        U.getInstance().getAuthBus().register(this);


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
        view0.setTextSize(21);
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




        ///===============================팝업 테스트==============================================
        //=========================================================================================
//        nestAddDialog = new NestAddDialog(MainActivity.this, new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                nestAddDialog.dismiss();
//            }
//        },
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        nestAddDialog.dismiss();
//                    }
//                }
//        );
//        nestAddDialog.show();
    }





    @Override
    protected void onResume() {
        super.onResume();
        expenses_total.clear();
        expenses_total_month.clear();
        expenses.clear();
        expenses_temp.clear();
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
            U.getInstance().log("뷰페이저 아이템: "+vp.getCurrentItem());

            if(vp.getCurrentItem()==1) {
                if (expense.getEx_in().equals("출금")) {
                    holder.money.setText("-" + Integer.toString(expense.getMoney()));
                } else {
                    holder.money.setText("+" + Integer.toString(expense.getMoney()));
                }
                holder.time.setText(expense.getDate()+" "+expense.getTime());
                holder.content.setText(expense.getRecord());
            }else{
                if (expense.getEx_in().equals("출금")) {
                    holder.money.setText("-" + Integer.toString(expense.getMoney()));
                } else {
                    holder.money.setText("+" + Integer.toString(expense.getMoney()));
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
        showPd();
        Req_Main_day req_main_day = new Req_Main_day(U.getInstance().getEmail(this));
        Call<Res> res = SNet.getInstance().getAllFactoryIm().readDay(req_main_day);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        U.getInstance().log("메인 하루 불러오기: " + response.body().toString());

                        budget = response.body().getDoc().getAsset().getBudget();
                        first_budget = response.body().getDoc().getAsset().getFirst_daily_budget();
                        daily_budget = response.body().getDoc().getAsset().getDaily_budget();
                        goal_item = response.body().getDoc().getGoal().getGoal_item();
                        goal_money = response.body().getDoc().getGoal().getGoal_money();

                        now_saving = response.body().getDoc().getGoal().getNow_saving();
                        ratio_saving = response.body().getDoc().getGoal().getRatio_saving();
                        U.getInstance().log(budget+"/"+daily_budget+"/"+goal_item+"/"+goal_money+"/"+now_saving+"/"+ratio_saving);

                        for(Expense expense : response.body().getDoc().getExpenditure().getExpense()){
                            expenses_total.add(expense);
                            expenses.add(expense);
                            expenses_temp.add(expense);
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
                stopPd();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });
    }

    public void readMonth() {
        showPd();
        Req_Main_day req_main_day = new Req_Main_day(U.getInstance().getEmail(this));
        Call<Res> res = SNet.getInstance().getAllFactoryIm().readMonth(req_main_day);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        U.getInstance().log("메인 한달 불러오기: " + response.body().toString());

                        budget = response.body().getDoc().getAsset().getBudget();
                        first_budget_month = response.body().getDoc().getAsset().getFirst_month_budget();

                        goal_item = response.body().getDoc().getGoal().getGoal_item();
                        goal_money = response.body().getDoc().getGoal().getGoal_money();
                        ratio_saving = response.body().getDoc().getGoal().getRatio_saving();
                        U.getInstance().log(budget+"/"+first_budget_month+"/"+goal_item+"/"+goal_money+"/"+ratio_saving);

                        for(Expense expense : response.body().getDoc().getExpenditure().getExpense()){
                            expenses_total_month.add(expense);
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


