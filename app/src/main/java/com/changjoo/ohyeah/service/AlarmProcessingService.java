package com.changjoo.ohyeah.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.changjoo.ohyeah.receiver.NotiDayReceiver;
import com.changjoo.ohyeah.utill.U;

import java.util.Calendar;

public class AlarmProcessingService extends Service {
    private static final String TAG = "AlarmProcessingService";
    AlarmManager mAM;
    AlarmManager mAM2;


    public AlarmProcessingService() {
        U.getInstance().log("알람 시작");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mAM = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mAM2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //매일 알림
        Intent dayintent = new Intent(this, NotiDayReceiver.class);
        PendingIntent pendingIntentday = PendingIntent.getBroadcast(this, 0, dayintent, 0);


        Calendar calendar = Calendar.getInstance();

        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                U.getInstance().getDayHour(this) == 0 ? 9 : U.getInstance().getDayHour(this),
                U.getInstance().getDayMin(this) == 0 ? 0 : U.getInstance().getDayMin(this), 0);
        U.getInstance().log("알람설정 시간: "+ U.getInstance().getDayHour(this)+":"+U.getInstance().getDayMin(this));
        mAM.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pendingIntentday);


        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
