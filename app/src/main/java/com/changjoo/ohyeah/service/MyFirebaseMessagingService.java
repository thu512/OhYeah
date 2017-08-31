package com.changjoo.ohyeah.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.ResPushModel;
import com.changjoo.ohyeah.ui.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

/**
 * 메세지 수신.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //메세지를 수신한다.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //json형태의 string획득 => 그릇 => 파싱 => 데이터획득
        Log.d("FCM", "From: " + remoteMessage.getData().get("data"));

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Gson gson = new Gson();
            ResPushModel res = gson.fromJson(remoteMessage.getData().get("data"),ResPushModel.class);

            Log.i("FCM",res.getBody()+" / "+res.getTitle()+"/"+res.getResult());
            showNotification(res);

        }
    }

    //사용자에게 푸시가 도착했음을 통보한다.(안테나영역에 알림으로 처리)
    public void showNotification(ResPushModel res){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify(0,nb.build());
    }
}

