package com.example.seodh.iothomegateway;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by SeoDH on 2017-12-07.
 */

public interface IotDataApi {

    //public static final String API_URL ="http://192.168.110.10:8090/iot/api/";

    @POST("iotData/")
    Call<Boolean> insert(@Body IotData iotData);
    //Call<IotData> insert(@Body IotData iotData);

    public static final Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("http://70.12.112.149:8888/iot/api/")
            .baseUrl("http://192.168.10.21:8090/iot/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
