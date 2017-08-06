package com.changjoo.ohyeah.net;

import com.changjoo.ohyeah.model.NaverProfileModel;
import com.changjoo.ohyeah.model.Req_Join;
import com.changjoo.ohyeah.model.Req_login;
import com.changjoo.ohyeah.model.Res_Join;
import com.changjoo.ohyeah.model.Res_login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 통신API를 선언한다. 구현X
 */

public interface MemberFactoryIm {

    // login 통신 담당 메소드 구현
    @GET("v1/nid/me")
    Call<NaverProfileModel> profile(@Header("Authorization") String authorization);




    @POST("aaa")
    Call<Res_Join> join(@Body Req_Join req);

    @POST("bbb")
    Call<Res_login> login(@Body Req_login req);
}
