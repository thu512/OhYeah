package com.changjoo.ohyeah.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.view.WindowManager;

import com.changjoo.ohyeah.model.Req_msg;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallAndSmsReceiver extends BroadcastReceiver
{
    Context context;
    WindowManager windowManager;
    //해당 이벤트가 들어오면 호출된다.
    //이벤트를 찾아서 분기해서 개별적 처리
    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context=context;
        //화면처리를 위한 윈도우매니저 획득
        windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        //이벤트========================================================================================
        String evt = intent.getAction();
        switch (evt){
            case "android.provider.Telephony.SMS_RECEIVED":  //문자가 오면 호출
                //인증처리시 자동입력, 문자를 파싱해서 원하는 데이터만 추출
                detectSMS(intent);
                break;
        }
    }

    //문자처리
    public void detectSMS(Intent intent)
    {
        U.getInstance().log("문자왔다"+intent.toString());
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for(Object obj : objects){
            //1.추출
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])(byte[])obj);

            //2.확인
            U.getInstance().log(smsMessage.getOriginatingAddress());
            U.getInstance().log(smsMessage.getMessageBody().toString());

            //3.이벤트 전송으로 특정화면으로 내용전송
            String msgBody = smsMessage.getMessageBody().toString();


            //아이디를 잘받아와야함
            U.getInstance().log("아이디는: "+U.getInstance().getEmail(this.context));
            //아이디 널이면 안읽기

            Req_msg req_msg = new Req_msg(U.getInstance().getEmail(this.context),msgBody);
            Call<Res> res = SNet.getInstance().getAllFactoryIm().sendMsg(req_msg);
            res.enqueue(new Callback<Res>() {
                @Override
                public void onResponse(Call<Res> call, Response<Res> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            U.getInstance().log(response.body().toString());
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

            U.getInstance().getAuthBus().post(msgBody);
        }
    }


}
