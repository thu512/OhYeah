package com.changjoo.ohyeah.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.ui.MainActivity;

/**
 * Created by Changjoo on 2017-08-31.
 */

public class NotiMonthReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent monthintent = new Intent(context, MainActivity.class);
        monthintent.putExtra("popup","budgetSet");
        monthintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        monthintent .setAction(Intent.ACTION_MAIN);
        monthintent .addCategory(Intent.CATEGORY_LAUNCHER);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(monthintent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(7, PendingIntent.FLAG_UPDATE_CURRENT);

        //비상금사용알림림
        NotificationCompat.Builder nb2 = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.push_logo)
                .setContentTitle("이번달 예산을 설정해주세요.")
                .setContentText("")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(9,nb2.build());
    }

}
