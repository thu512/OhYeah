package com.changjoo.ohyeah.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tacademy on 2017-06-30.
 */

public class Net {
    private static final Net ourInstance = new Net();
    public static Net getInstance() {
        return ourInstance;
    }
    private Net() {}


    //retrofit 생성

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://openapi.naver.com") //기본도메인 설정
            .addConverterFactory(GsonConverterFactory.create())  //응답데이터를 json 자동 변환
            .build();

    public Retrofit getRetrofit() {
        return retrofit;
    }

    //API담당 인터페이스 생성
    //API담당 인터페이스의 객체를 생성
    AllFactoryIm allFactoryIm;


    //객체를 리턴해주는 getter 준비
    public AllFactoryIm getAllFactoryIm() {
        if(allFactoryIm == null){
            allFactoryIm =retrofit.create(AllFactoryIm.class);
        }
        return allFactoryIm;
    }




}
