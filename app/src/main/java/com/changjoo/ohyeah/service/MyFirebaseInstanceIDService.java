package com.changjoo.ohyeah.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * FCM사용을 위한 구성요소중 유저의 token을 획득하는 서비스임.
 * token의 신규발급부터 변경시 이벤트를 전달하여 서버쪽으로 갱신하는 역활을 담당한다.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM", "Refreshed token: " + refreshedToken);
        //서버쪽으로 보내서 갱신 가능함

    }
}
