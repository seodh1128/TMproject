package com.example.seodh.smarthomeiot;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by student on 2017-12-13.
 */

public class SensorActivity extends Activity{


    Dashboard d;
    DashboardApi dashboardApi = DashboardApi.retrofit.create(DashboardApi.class);

    private TextView name;
    private TextView value;
    private TextView condition;
    private TextView state;
    private TextView date;


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_sensor);

        Toast.makeText (this, "감지센서 상태 확인 페이지입니다",Toast.LENGTH_LONG).show();

        name = (TextView) findViewById(R.id.name);
        value = (TextView) findViewById(R.id.value);
        value.setPaintFlags(value.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);   //밑줄긋기

        condition = (TextView) findViewById(R.id.condition);
        state = (TextView) findViewById(R.id.state);
        date = (TextView) findViewById(R.id.date);

        name.setTextColor(Color.parseColor("#d41111"));
        value.setTextColor(Color.parseColor("#d41111"));
        condition.setTextColor(Color.parseColor("#d41111"));
        state.setTextColor(Color.parseColor("#d41111"));
        date.setTextColor(Color.parseColor("#d41111"));

        sensor();

    }

    public void sensor() {
        Call<List<Dashboard>> dashboardCall = dashboardApi.getList();
        dashboardCall.enqueue(new Callback<List<Dashboard>>() {
            @Override
            public void onResponse(Call<List<Dashboard>> call, Response<List<Dashboard>> response) {
                List<Dashboard> result = response.body();
                Gson gson = new Gson();

                Dashboard d = result.get(1);    //result.get(0)이 쿼리문에 지정한 값들의 한줄을 읽어오는거 나머지 센서3개는 get1,2,3해주면됌

                name.setText(d.getDeviceName());
                value.setText(Double.toString(d.getSensorValue()));
                condition.setText(Integer.toString(d.getAutoMode()));
                state.setText(Integer.toString(d.getDeviceState()));
                date.setText(newDate(d.getRegDate()));

            }

            @Override
            public void onFailure(Call<List<Dashboard>> call, Throwable t) {

            }
        });
    }

    public String newDate(Date now) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd E HH:mm:ss");        //DateFormat 알아둘것 그냥 Date쓰면 알아보기 이상해서 보기좋게 바꿔줬음
        // System.out.println(format.format(now));
        return format.format(now);
    }
}