package com.yangdoll.android.iotproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    DashboardApi dashboardApi = DashboardApi.retrofit.create(DashboardApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Font.setGlobalFont(this,getWindow().getDecorView()); 폰트적용 안했음

        final Intent intent = new Intent(this,TempActivity.class); //인텐트 객체생성
        final Intent intent2 = new Intent(this,HumiActivity.class);
        final Intent intent3 = new Intent(this,LuxActivity.class);
        final Intent intent4 = new Intent(this,SensorActivity.class);

        Button tempbutton = (Button) findViewById(R.id.tempButton);
        Button humibutton = (Button) findViewById(R.id.humiButton);
        Button luxbutton = (Button) findViewById(R.id.luxButton);
        Button sensorbutton = (Button) findViewById(R.id.sensorButton);



        tempbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //list();

                intent.putExtra("보낼이름이랑","넘겨줄값 적어주면됌");

                startActivity(intent);
            }
        });

        humibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intent2.putExtra(" " ," ");
                startActivity(intent2);
            }
        });

        luxbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intent3.putExtra(" ", " ");
                startActivity(intent3);
            }
        });

        sensorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intent4.putExtra(" ", " ");
                startActivity(intent4);
            }
        });
    }
    protected  void onPostExecute(String result){
       // final TextView textView = (TextView)findViewById(R.id.textView01);
       // textView.setText(result);
    }

}