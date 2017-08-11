package com.changjoo.ohyeah.net;

import com.changjoo.ohyeah.model.NaverProfileModel;
import com.changjoo.ohyeah.model.Req;
import com.changjoo.ohyeah.model.Req_Budget;
import com.changjoo.ohyeah.model.Req_Fix;
import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Res;

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




    @POST("users/join")
    Call<Res> join(@Body Req req);

    @POST("users/check_email")
    Call<Res> check_email(@Body Req_email req_email);

    @POST("users/login")
    Call<Res> login(@Body Req req);

    @POST("asset/period")
    Call<Res> pushBudget(@Body Req_Budget req_budget);

    @POST("asset/fix_ex")
    Call<Res> pushFix(@Body Req_Fix fix_ex);


}
