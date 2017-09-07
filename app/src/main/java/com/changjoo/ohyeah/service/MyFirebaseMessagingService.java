package com.changjoo.ohyeah.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.ResPushModel;
import com.changjoo.ohyeah.ui.MainActivity;
import com.changjoo.ohyeah.utill.U;
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
            if(res.getResult() == 1){  //20%미만
                showNotification_20(res);
            }
            else if(res.getResult() == 2){ //초과
                showNotification_over(res);
                showNotification_nest();
            }
            else if(res.getResult() == 3) { //목표달성 푸쉬
                showNotification_pp(res);
            }
            else if(res.getResult() == 7){ //저축알림
                showNotification_save(res);
            }
            else if(res.getResult() == 8){ //고정지출 확인
                showNotification_fix(res);
            }
            else if(res.getResult() == 4){ //한달 예산 설정 알림
                showNotification_month(res);
            }
            else if(res.getResult() == 5){ //매일 예산 설정
                showNotification_day(res);
            }else if(res.getResult() == 6){
                showNotification_day_not(res);
            }else if(res.getResult() == 9){
                showNotification_day_not2(res);
            }


        }
    }

    //20%미만 푸쉬
    public void showNotification_20(ResPushModel res){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_over)
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify(1,nb.build());
    }

    //초과 푸쉬
    public void showNotification_over(ResPushModel res){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_ONE_SHOT);
        //단순 초과알림
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.icon_over)
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify(2,nb.build());
    }

    //초과 푸쉬 -> 비상금 사용
    public void showNotification_nest(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("popup","nest");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent .setAction(Intent.ACTION_MAIN);
        intent .addCategory(Intent.CATEGORY_LAUNCHER);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);


        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(2, PendingIntent.FLAG_UPDATE_CURRENT);

        //비상금사용알림림
        NotificationCompat.Builder nb2 = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.push)
                .setContentTitle("비상금 추가")
                .setContentText("비상금에서 예산을 추가하세요.")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(3,nb2.build());
    }


    //목표달성 푸쉬
    public void showNotification_pp(ResPushModel res){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("popup","purpose");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent .setAction(Intent.ACTION_MAIN);
        intent .addCategory(Intent.CATEGORY_LAUNCHER);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(3, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.push_logo)
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify(4,nb.build());
    }


    //저축알림
    public void showNotification_save(ResPushModel res){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(this,4,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.push_logo)
                .setColor(Color.parseColor("#bbc5c6"))
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify(5,nb.build());
    }


    //고정 지출 확인 알림
    public void showNotification_fix(ResPushModel res){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("popup","fix");
        intent.putExtra("msg_content",res.getFix().getMsg_content());
        intent.putExtra("msg_money",res.getFix().getMsg_money());
        intent.putExtra("msg_date",res.getFix().getMsg_date());
        intent.putExtra("msg_time",res.getFix().getMsg_time());
        intent.putExtra("fix_data",res.getFix().getFix_data());

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent .setAction(Intent.ACTION_MAIN);
        intent .addCategory(Intent.CATEGORY_LAUNCHER);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent((int)(Math.random() * 100)+10, PendingIntent.FLAG_UPDATE_CURRENT);

        U.getInstance().log("고정 지출 푸쉬 내용 : "+res.getFix());


        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.push_logo,1)
                .setColor(Color.parseColor("#bbc5c6"))
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify((int)(Math.random() * 100)+10,nb.build());
    }

    //오늘예산알림
    public void showNotification_day(ResPushModel res){
        U.getInstance().log("알람!!!!!!!");
        Intent dayintent = new Intent(getApplicationContext(), MainActivity.class);
        dayintent.addFlags(dayintent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),5,dayintent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.push_logo)
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify(6,nb.build());


    }


    //오늘예산알림 -> 한달예산 마이너스, 비상금 남았을때
    public void showNotification_day_not(ResPushModel res){
        U.getInstance().log("알람!!!!!!!");
        Intent dayintent = new Intent(getApplicationContext(), MainActivity.class);
        dayintent.addFlags(dayintent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),6,dayintent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.push_logo)
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify(7,nb.build());


    }

    //오늘예산알림 -> 한달예산 마이너스, 비상금 안 남았을때
    public void showNotification_day_not2(ResPushModel res){
        U.getInstance().log("알람!!!!!!!");
        Intent dayintent = new Intent(getApplicationContext(), MainActivity.class);
        dayintent.addFlags(dayintent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),7,dayintent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.push_logo)
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
        notificationManager.notify(8,nb.build());


    }



    //한달 새로운 예산 설정
    public void showNotification_month(ResPushModel res) {
        Intent monthintent = new Intent(getApplicationContext(), MainActivity.class);
        monthintent.putExtra("popup","budgetSet");
        monthintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        monthintent .setAction(Intent.ACTION_MAIN);
        monthintent .addCategory(Intent.CATEGORY_LAUNCHER);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(monthintent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(8, PendingIntent.FLAG_UPDATE_CURRENT);

        //비상금사용알림림
        NotificationCompat.Builder nb2 = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.push_logo)
                .setContentTitle(res.getTitle())
                .setContentText(res.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        //노티 작동
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(9,nb2.build());

    }
}

