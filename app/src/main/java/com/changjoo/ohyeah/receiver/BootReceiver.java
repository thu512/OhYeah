package com.changjoo.ohyeah.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.changjoo.ohyeah.service.AlarmProcessingMonthService;
import com.changjoo.ohyeah.service.AlarmProcessingService;

/**
 * Created by Changjoo on 2017-08-31.
 */

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

//        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE); //매달
//
//        Intent myIntent = new Intent(context, NotiMonthReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
//
//        long period = 1000 * 5;
//        long after = 1000 * 5;
//        long t = SystemClock.elapsedRealtime();
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, t + after, period, pendingIntent);


        context.startService(new Intent(context, AlarmProcessingService.class));
        context.startService(new Intent(context, AlarmProcessingMonthService.class));


    }
}