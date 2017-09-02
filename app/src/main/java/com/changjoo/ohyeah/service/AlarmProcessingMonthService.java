package com.changjoo.ohyeah.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.changjoo.ohyeah.receiver.NotiMonthReceiver;
import com.changjoo.ohyeah.utill.U;

import java.util.Calendar;

public class AlarmProcessingMonthService extends Service {
    AlarmManager mAM2;


    public AlarmProcessingMonthService() {
        U.getInstance().log("한달 알람 시작");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mAM2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //매달 알림
        Intent monthintent = new Intent(this, NotiMonthReceiver.class);
        PendingIntent pendingIntentmonth = PendingIntent.getBroadcast(this, 0, monthintent, 0);
        Calendar calendar2 = Calendar.getInstance();
        int current_year = calendar2.get(Calendar.YEAR);
        int current_month = calendar2.get(Calendar.MONTH);
        int current_day = calendar2.get(Calendar.DATE);
        U.getInstance().log("설정일: "+ U.getInstance().getDay(this)+"현제일 : "+ current_day);
        int set_day = U.getInstance().getDay(this);
        int set_month;
        int set_year;

        if(current_day >= set_day){
            set_month = current_month+1;
            if(set_month>12){
                set_month=1;
                set_year = current_year+1;
            }else{
                set_year = current_year;
            }
        }else{
            set_month = current_month;
            set_year = current_year;
        }


        calendar2.set(set_year,
                set_month,
                set_day,
                U.getInstance().getMonthHour(this) == 0 ? 9 : U.getInstance().getMonthHour(this),
                U.getInstance().getMonthMin(this) == 0 ? 0 : U.getInstance().getMonthMin(this), 0);

        mAM2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntentmonth);
        U.getInstance().log("매달 알람설정 시간: "+set_year+":"+set_month+":"+set_day+":"+ U.getInstance().getMonthHour(this)+":"+U.getInstance().getMonthMin(this));
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
