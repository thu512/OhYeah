package com.changjoo.ohyeah;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.view.WindowManager;

import com.changjoo.ohyeah.utill.U;

public class CallAndSmsReceiver extends BroadcastReceiver
{

    WindowManager windowManager;
    //해당 이벤트가 들어오면 호출된다.
    //이벤트를 찾아서 분기해서 개별적 처리
    @Override
    public void onReceive(Context context, Intent intent)
    {
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
            U.getInstance().getAuthBus().post(msgBody);
        }
    }


}
