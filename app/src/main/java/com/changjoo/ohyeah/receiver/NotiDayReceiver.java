package com.changjoo.ohyeah.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.Req_Main_day;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.ui.MainActivity;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Changjoo on 2017-08-31.
 */

public class NotiDayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        U.getInstance().log("알람!!!!!!!");
        Intent dayintent = new Intent(context, MainActivity.class);
        dayintent.addFlags(dayintent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context,6,dayintent,PendingIntent.FLAG_ONE_SHOT);


        Req_Main_day req_main_day = new Req_Main_day(U.getInstance().getEmail(context));
        Call<Res> res = SNet.getInstance().getAllFactoryIm().readDay(req_main_day);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.push_logo)
                                .setContentTitle("오늘 예산은 "+U.getInstance().toNumFormat(Integer.toString((int)response.body().getDoc().getAsset().getDaily_budget()))+"원 입니다.")
                                .setContentText("오늘의 예산을 확인하세요!!!")
                                .setAutoCancel(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(pendingIntent);

                        //노티 작동
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        //0:노티피케이션 고유 번호 알림을 눌러서 시작하면 해당 번호를 넣어서 알림 삭제
                        notificationManager.notify(8,nb.build());


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
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
            }
        });



    }

    public void getMoney(){


    }

}
