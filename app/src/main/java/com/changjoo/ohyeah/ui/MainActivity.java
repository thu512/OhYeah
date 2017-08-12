package com.changjoo.ohyeah.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.TradeModel;
import com.changjoo.ohyeah.utill.U;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import layout.FirstFragment;
import layout.SecondFragment;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MainActivity extends Activity {
    private ViewPager vp;
    private LinearLayout ll, bg;
    private SmartTabLayout viewPagerTab;
    private LinearLayout dragView;
    private SlidingUpPanelLayout mLayout;
    private Drawable alpha;
    boolean flag = false;
    RecyclerView list;
    ImageView down_img;
    ArrayList<TradeModel> tradeModels = new ArrayList<>(); //임시 설정
    TradeAdapter tradeAdapter;
    Button all_btn;
    Button in_btn;
    Button out_btn;
    pagerAdapter pagerAdapter;
    Button set_btn;

    @Subscribe
    public void recvBus(String msg){
        Log.d("FFF",""+msg);
        Toast.makeText(this,""+msg,Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager= (NotificationManager)MainActivity.this.getSystemService(MainActivity.this.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(MainActivity.this.getApplicationContext(),MainActivity.class); //인텐트 생성.



        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);//현재 액티비티를 최상으로 올리고, 최상의 액티비티를 제외한 모든 액티비티를


        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( MainActivity.this,0, intent1, FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.mipmap.pin).setTicker("HETT").setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle("푸쉬 제목").setContentText("푸쉬내용")
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingNotificationIntent).setAutoCancel(true).setOngoing(true);


        notificationManager.notify(1, builder.build()); // Notification send

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //도착역 설정
        U.getInstance().getAuthBus().register(this);

        set_btn = (Button)findViewById(R.id.set_btn);
        all_btn = (Button)findViewById(R.id.all_btn);
        in_btn = (Button)findViewById(R.id.in_btn);
        out_btn = (Button)findViewById(R.id.out_btn);
        dragView = (LinearLayout) findViewById(R.id.dragView);
        alpha = dragView.getBackground();
        list = (RecyclerView) findViewById(R.id.list);
        down_img = (ImageView) findViewById(R.id.down_img);
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

        //금액이 마이너스인 상황 가정
        bg.setBackgroundResource(R.drawable.bg_gradation3);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    //금액이 마이너스일때 해당 백그라운드 적용
                    bg.setBackgroundResource(R.drawable.bg_transition2);
                    if (true) {

                        TransitionDrawable td = (TransitionDrawable) getResources().getDrawable(R.drawable.bg_transition2);
                        bg.setBackground(td);
                        td.startTransition(700); // duration 3 seconds
                        changeStatusBarColor("#360909");
                    }
                } else {

                    bg.setBackgroundResource(R.drawable.bg_transition1);
                    //만약 현재 백그라운드가 빨간색이면 해당 if통과
                    if (true) {

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
        mLayout.setPanelHeight(U.getInstance().getDpToPixel(this,175));//초기 패널 높이 => 픽셀 값이므로 변환 필요
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
                    down_img.setVisibility(View.VISIBLE);
                } else {
                    down_img.setVisibility(View.GONE);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        //임시 더미데이타
        tradeModels.add(new TradeModel("08:18", "주식회사 카카오", 3500));
        tradeModels.add(new TradeModel("08:19", "애버랜드", 55000));
        tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:18", "주식회사 카카오", 3500));tradeModels.add(new TradeModel("08:18", "주식회사 카카오", 3500));tradeModels.add(new TradeModel("08:18", "주식회사 카카오", 3500));tradeModels.add(new TradeModel("08:18", "주식회사 카카오", 3500));
        tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));tradeModels.add(new TradeModel("08:20", "롯데월드", 350000));


        //리싸이클러 뷰
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        tradeAdapter = new TradeAdapter();
        //데이터셋팅

        list.setAdapter(tradeAdapter);

        //전체, 입금, 출금 내역 불러오기 => 어댑터 notify
        all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_btn.setBackgroundResource(R.mipmap.all_bt_on);
                in_btn.setBackgroundResource(R.mipmap.deposit_off);
                out_btn.setBackgroundResource(R.mipmap.withdraw_off);

            }
        });

        in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_btn.setBackgroundResource(R.mipmap.all_bt_off);
                in_btn.setBackgroundResource(R.mipmap.deposit_on);
                out_btn.setBackgroundResource(R.mipmap.withdraw_off);
            }
        });

        out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_btn.setBackgroundResource(R.mipmap.all_bt_off);
                in_btn.setBackgroundResource(R.mipmap.deposit_off);
                out_btn.setBackgroundResource(R.mipmap.withdraw_on);
            }
        });


        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
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
            TradeModel tradeModel = tradeModels.get(position);
            holder.time.setText(tradeModel.getTime());
            holder.content.setText(tradeModel.getContent());
            holder.money.setText(Integer.toString(tradeModel.getMoney()));
        }

        @Override
        public int getItemCount() {
            return tradeModels == null ? 0 : tradeModels.size();
        }
    }


    //액티비티가 소멸되면 도착역도 폐기한다.
    @Override
    protected void onDestroy() {
        U.getInstance().getAuthBus().unregister(this);
        super.onDestroy();
    }


    //상태바 색상 변경
    public void changeStatusBarColor(String color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

}


