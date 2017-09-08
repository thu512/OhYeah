package com.gogolab.ohyeah.net;

import com.gogolab.ohyeah.model.NaverProfileModel;
import com.gogolab.ohyeah.model.Req;
import com.gogolab.ohyeah.model.Req_Budget;
import com.gogolab.ohyeah.model.Req_Change_pw;
import com.gogolab.ohyeah.model.Req_Fix;
import com.gogolab.ohyeah.model.Req_Main_day;
import com.gogolab.ohyeah.model.Req_ModiFix;
import com.gogolab.ohyeah.model.Req_Nest;
import com.gogolab.ohyeah.model.Req_Purpose;
import com.gogolab.ohyeah.model.Req_email;
import com.gogolab.ohyeah.model.Req_login;
import com.gogolab.ohyeah.model.Req_msg;
import com.gogolab.ohyeah.model.Req_set;
import com.gogolab.ohyeah.model.Req_token;
import com.gogolab.ohyeah.model.Req_use_nest;
import com.gogolab.ohyeah.model.Res;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 통신API를 선언한다. 구현X
 */

public interface AllFactoryIm {

    // login 통신 담당 메소드 구현
    @GET("v1/nid/me")
    Call<NaverProfileModel> profile(@Header("Authorization") String authorization);




    @POST("users/join")
    Call<Res> join(@Body Req req);

    @POST("users/check_email")
    Call<Res> check_email(@Body Req_email req_email);

    @POST("users/login")
    Call<Res> login(@Body Req_login req_login);

    @POST("asset/set_budget")
    Call<Res> pushBudget(@Body Req_Budget req_budget);

    @POST("asset/fix_ex")
    Call<Res> pushFix(@Body Req_Fix fix_ex);

    @POST("/goal")
    Call<Res> pushPurpose(@Body Req_Purpose req_purpose);

    @POST("asset/spare")
    Call<Res> pushNest(@Body Req_Nest req_nest);

    @GET("users/logout")
    Call<Res> logout();

    @POST("main/today_view")
    Call<Res> readDay(@Body Req_Main_day req_main_day);

    @POST("main/month_view")
    Call<Res> readMonth(@Body Req_Main_day req_main_day);

    @POST("main/today_expend")
    Call<Res> sendMsg(@Body Req_msg req_msg);

    @POST("update/budget_spare_view")
    Call<Res> getState(@Body Req_email req_email);

    @POST("users/delete")
    Call<Res> signOut(@Body Req_email req_email);

    @POST("update/pw")
    Call<Res> changePw(@Body Req_Change_pw req_change_pw);

    @POST("update/fix_ex_view")
    Call<Res> updateFix(@Body Req_email req_email);

    @POST("update/fix_ex")
    Call<Res> update_Fix(@Body Req_Fix fix_ex);

    @POST("update/goal_view")
    Call<Res> updatePP(@Body Req_email req_email);

    @POST("update/goal")
    Call<Res> update_PP(@Body Req_Purpose req_purpose);


    @POST("update/use_spare")
    Call<Res> nestAdd(@Body Req_use_nest req_use_nest);

    @POST("update/budget_spare")
    Call<Res> modifyBudget(@Body Req_set req_set);

    @POST("update/new_token")
    Call<Res> refreshToken(@Body Req_token req_token);

    @POST("users/findpw")
    Call<Res> findPwd(@Body Req_email req_email);

    @POST("main/fix_yn")
    Call<Res> modiFix(@Body Req_ModiFix req_modiFix);

}
