package com.example.seodh.iothomegateway;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// 2017.11.28
// Application 명 - AtoOtest(추후 수정 예정)
// 해당 액티비티 주요 기능
// 1. 데이터 전송 기능 (OnSend)
// Send버튼을 누를 시, 텍스트 입력란(EditText)에 입력한 텍스트를
// Writer를 통해 문자열 형태로 Arduino로 전달 (통신프로토콜 : 블루투스)
// 2. 데이터 수신 기능 (connectToSelectedDevice 예하 수신 쓰레트 활용)
// 데이터가 전송될 경우, 쓰레드를 활용하여 데이터를 배열화 하여 조합, 버튼/레이아웃 하단
// TextView에 출력.

// 2017.11.29
// 주요 변경사항
// 목표 Reader로 수신받은 "GSON 데이터"를 Oracle로 전송
// 1. 전송 받는 데이터중 GSON Data만 구분하는 구문 추가 필요
// 2. textView에 표시하는 동시에 Oracle로 어떻게 전송할 것인지 알아볼 것.
// 3. DB로부터 송/수신하는 프로그램을 분리시킬지, 통합할 것인지 선택할 필요가 있다.(현재는 여기에 병행)

// 2017.12.07
// Application 명 - IoTHomeGateWay(기능에 맞게 수정함)
// 해당 액티비티 주요 기능
// 1. Bluetooth 데이터 수신 기능 (connectToSelectedDevice 예하 수신 쓰레트 활용)
// Arduino(디바이스 컨트롤러)의 Bluetooth와 연결에 성공할 경우,
// ReceiveThread 메소드로 부터 메시지 수신,
// 2. Retrofit2를 활용한 API 송신/수신
// Json형식의 데이터인지 확인(유효성 검사) 후,
// 올바른 형식의 파일일 경우, Web Server에 데이터 전송.
// 3. WebServer 데이터 수신 기능
// 서버로부터 요청이 접수될 경우,
// 쓰레드를 활용하여 데이터를 배열화 하여 수신.
// TextView에 출력.

public class MainActivity extends AppCompatActivity {

    //Bluetooth to Android 전용 변수/상수
    //////////////////////////////////////////////////////////////
    final static String TAG = "IOT Project";
    BluetoothAdapter bluetoothAdapter;
    final static int REQUEST_ENABLE_BLE = 1;
    BluetoothSocket socket;
    PrintWriter out;
    Thread workerThread;
    //  연결된 블루투스 장비 해시맵
    HashMap<String, BluetoothDevice> btDevices = new HashMap<>();
    BluetoothDevice selectedDevice;			// 선택된 블루투스 장비
    static Context mContext;
    ///////////////////////////////////////////////////////////////

    private static final int MSG_BLUETOOTH_CONN_SUCESS = 1;
    private static final int MSG_NOT_JSON = 2;
    private static final int MSG_BLUETOOTH_CONN_FAIL = 3;
    private Context thisActivity = (Context)this;

    //  Retrofit2 관련 변수 / 상수
    ///////////////////////////////////////////////////////////////
    Retrofit retrofit;
    IotData iotData;
    IotDataApi iotDataApi = IotDataApi.retrofit.create(IotDataApi.class);
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////
    //  WebSocket 관련 변수 / 상수    - 2017.12.12 삭제
    ///////////////////////////////////////////////////////////////
    //IotWebSocketListener iotWebSocketListener;
    ///////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////
    //  Display 관련 변수 / 상수      - 2017.12.14
    ///////////////////////////////////////////////////////////////
    TextView connectBTName;
    TextView dToARecieve;
    TextView aToSSend;
    TextView logMessage;
    Button connectButton;
    String btnDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectBTName = (TextView)findViewById(R.id.connectBTName);
        dToARecieve = (TextView)findViewById(R.id.dToARecieve);
        aToSSend = (TextView)findViewById(R.id.aToSSend);
        logMessage = (TextView)findViewById(R.id.logMessage);
        connectButton = (Button)findViewById(R.id.connectButton);

        //Bluetooth to Android 코드
        ///////////////////////////////////////////////////////////////
        mContext =  getApplicationContext();
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBluetooth();
            }
        });


        //////////////////////////////////////////////////////////////
        //  Retrofit2 Get(DB Table 수신)코드 - Android App으로 이식됨

//        Call<List<IotData>> calls = api.listDev();
//        calls.enqueue(new Callback<List<IotData>>() {
//            @Override
//            public void onResponse(Call<List<IotData>> call, Response<List<IotData>> response) {
//                receiveMsgView.setText(response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<List<IotData>> call, Throwable t) {
//
//            }
//        });

    }


    //////////////////////////////////////////////////////////////////
    //  Bluetooth to Android 클래스/메소드
    //////////////////////////////////////////////////////////////////
    void checkBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null) {
            // 블루투스 어댑터가 없는 경우
            Toast.makeText(this, "블루투스를 지원하지 않습니다.",
                    Toast.LENGTH_SHORT).show();
            connectBTName.setText("연결 실패");
            logMessage.setText(logMessage.getText()+"\n"+"블루투스를 지원하지 않습니다.");
        } else {
            // 장치가 블루투스를 지원하는 경우
            if(!bluetoothAdapter.isEnabled())  {//첫 실행시를 위해 인텐트를 통해 상수값 부여
                Intent blEnableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(blEnableIntent, REQUEST_ENABLE_BLE);
            } else {
                selectDevice(); //첫 실행이 아닌 경우, 장치 선택 메소드 바로 실행
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if(requestCode == REQUEST_ENABLE_BLE) {
            if(resultCode == RESULT_OK) {
                selectDevice();
            } else {
                Toast.makeText(this, "블루투스가 비활성화 상태입니다.",
                        Toast.LENGTH_SHORT).show();
                connectBTName.setText("블루투스 연결 실패");
                logMessage.setText(logMessage.getText()+"\n"+"블루투스가 비활성화 상태입니다.");
            }
        }
    }
    public void selectDevice() {
        // 등록된 블루티스 디바이스 집합 추출
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if(devices.size() == 0) {
            Toast.makeText(this, "연결된 블루투스가 장비가 없습니다.",
                    Toast.LENGTH_SHORT).show();
            connectBTName.setText("연결 실패");
            logMessage.setText(logMessage.getText()+"\n"+"블루투스가 비활성화 상태입니다.");
        }
        // 장치명 - 장치 쌍으로 해시맵 구성
        for(BluetoothDevice device : devices) {
            btDevices.put(device.getName(), device);
        }

        // 블루투스 장치맵 으로 배열 만들기 → AlertDialog에서 사용
        final CharSequence[] items = btDevices.keySet().toArray(
                new CharSequence[btDevices.keySet().size()+1]);

        items[items.length-1] = "취소";
        new AlertDialog.Builder(this)
                .setTitle("블루투스 장치 선택")
                .setItems(items, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==btDevices.size()) { // 취소 선택
                            Toast.makeText(getApplicationContext(),
                                    "취소를 선택했습니다.", Toast.LENGTH_SHORT).show();
                            connectBTName.setText("연결 실패");
                            logMessage.setText(logMessage.getText()+"\n"+"취소를 선택했습니다.");
                        } else {
                            // 장치 선택 - 연결시도
                            connectToSelectedDevice(items[i].toString());
                        }
                    }
                })
                .setCancelable(false)
                .show();

    }	// selectDevice 끝



    public void connectToSelectedDevice(final String deviceName){
        Toast.makeText(this, deviceName, Toast.LENGTH_SHORT).show();
        connectBTName.setText(deviceName +" 연결중");
        logMessage.setText(logMessage.getText()+"\n"+"블루투스 연결성공");
        // 해시맵에서 장치 추출
        selectedDevice = btDevices.get(deviceName);
        btnDeviceName = deviceName;
        // ANR을 피하기위해 스레드로 접속 시도
        new Thread() {
            public void run() {
                try {
                    UUID uuid = UUID.fromString(
                            "00001101-0000-1000-8000-00805F9B34FB");
                    socket = selectedDevice
                            .createRfcommSocketToServiceRecord(uuid);
                    //RFCOMM 채널을 통해 연결
                    socket.connect();
                    handler.obtainMessage(MSG_BLUETOOTH_CONN_SUCESS);


                    // 데이터 송수신 스트림 얻기
                    OutputStream outputStream = socket.getOutputStream();
                    InputStream inputStream = socket.getInputStream();
                    out = new PrintWriter(new OutputStreamWriter(outputStream));

                    // 데이터 수신 스레드 기동
                    workerThread = new ReceiveThread(inputStream);
                    workerThread.start();
                } catch (Exception e) {
                    Log.d(TAG, "블루투스 연결실패", e);
                    handler.obtainMessage(MSG_BLUETOOTH_CONN_FAIL);

                }
            }
        }.start();
    }

    class ReceiveThread extends Thread{

        String message;
        BufferedReader br;

        // 생성자 : InputStream을 BufferedReader로 변환
        ReceiveThread(InputStream is) {
            br = new BufferedReader(new InputStreamReader(is));
        }
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try{
                    message = br.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            // UI 갱신
                            //String msg = receiveMsgView.getText().toString();
                            try{
                                Gson gson = new Gson();
                                IotData iotData = gson.fromJson(message, IotData.class);
                                InsertDB(iotData);
                                dToARecieve.setText("수신 중...");
                            }catch(JsonSyntaxException o){
                                Log.d(TAG, "Arduino로부터 Data를 제대로 수신받지 못했습니다.");
                                dToARecieve.setText("수신 중 Error 발생");
                                logMessage.setText(logMessage.getText()+"\n"+"Arduino로부터 Data를 제대로 수신받지 못했습니다. - "+new Date().toString());
                            }
                        }
                    });
                } catch(IOException e){
                    Log.d(TAG, "업데이트 실패", e);
                    dToARecieve.setText("Error 발생");
                    logMessage.setText(logMessage.getText()+"\n"+"업데이트 실패");
                }
            }
        }
    }


    //  어플리케이션 종료시 동작조건 (소켓 외 나머지는 자동으로 닫히므로, 소켓만 닫아준다.)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(workerThread!=null)
                workerThread.interrupt();
                socket.close();
        } catch (Exception e) {
            Toast.makeText(this, "Destroy 실패",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Destroy 실패", e);
            logMessage.setText(logMessage.getText()+"\n"+"Destroy 실패");
        }
    }


    //  쓰레드 내부에서 사용될 Toast 메시지용 Handler 메소드
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case MSG_BLUETOOTH_CONN_SUCESS:
                    Toast.makeText(thisActivity, "연결 성공!", Toast.LENGTH_SHORT).show();

                    break;
                case MSG_NOT_JSON:
                    Toast.makeText(thisActivity, "전달받은 Data가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;

                case MSG_BLUETOOTH_CONN_FAIL:
                    connectBTName.setText(btnDeviceName +" 연결실패");
                    logMessage.setText(logMessage.getText()+"\n"+"블루투스 연결실패");

                default:
                    Toast.makeText(thisActivity, "정의되지 않는 Message 요청 확인", Toast.LENGTH_SHORT).show();// 정의되지 않은 메시지들의 경우 여기로 분기
                    break;
            }
            super.handleMessage(msg);
        }

    };


    public void InsertDB(IotData iotData) {

        iotData.setRegDate(new Date());
        Log.d(TAG, "InsertDB: "+iotData.toString());
        aToSSend.setText("DB로 데이터를 전송중입니다...");
            Call<Boolean> call = iotDataApi.insert(iotData);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    Log.d(TAG, "onRespionse: "+response);
                }
                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    logMessage.setText(logMessage.getText()+"\n"+"DB 전송 과정에서 에러가 발생했습니다.");
                }
            });

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    // WebSocket 연결 관련 메소드
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    // 2017.12.04 - 블루투스로 전송하는 메소드 주석 처리 ( 추후 제어요소 추가시 사용 예정)
    // 2017.12.08 - 주석 처리 해제 / WebSocket으로부터 데이터 전달받아 JSON 형식으로 Arduino에 전송
    // editText에 입력했던 문자를 PrintWriter에 실어서 전송
//    public void onSend(IotData iotData) {
//
//        try{
//            //  out = connectToSelectedDevice에서 선언한 Print 변수
//            out.println(iotData);
//            out.flush();
//        } catch (Exception e ) {
//            finish();
//        }
//    }
}
