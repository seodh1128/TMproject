package com.yangdoll.android.iotproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by student on 2017-12-12.
 */

public interface DashboardApi {

    @GET("view/{deviceCode}/{sensorCode}")
    Call<Dashboard> getDetailList(@Path("deviceCode") int deviceCode, @Path("sensorCode") int sensorCode);

    @GET("list/")
    Call<List<Dashboard>> getList();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.10.21:8090/iot/dashboard/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
