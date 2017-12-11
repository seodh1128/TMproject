package com.example.seodh.atootest;


import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

// Class(Activity) 명 - MainActivity(추후 수정 예정)
// 해당 액티비티 주요 기능
// 1. 데이터 전송 기능 (OnSend)
// Send버튼을 누를 시, 텍스트 입력란(EditText)에 입력한 텍스트를
// Writer를 통해 문자열 형태로 Arduino로 전달 통신프로토콜은 블루투스
// 2. 데이터 수신 기능 (connectToSelectedDevice 예하 수신 쓰레트 활용)
// 데이터가 전송될 경우, 쓰레드를 활용하여 데이터를 배열화 하여 조합, 버튼/레이아웃 하단에
// TextView에 출력한다.

public class MainActivity extends AppCompatActivity {
    final static String TAG = "IOT Project";
    BluetoothAdapter bluetoothAdapter;
    final static int REQUEST_ENABLE_BLE = 1;
    BluetoothSocket socket;
    PrintWriter out;
    Thread workerThread;
    EditText editText;
    TextView receiveMsgView;
    // 연결된 블루투스 장비 해시맵
    HashMap<String, BluetoothDevice> btDevices = new HashMap<>();
    BluetoothDevice selectedDevice;			// 선택된 블루투스 장비
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        receiveMsgView = (TextView) findViewById(R.id.receiveMsgView);
        mContext =  getApplicationContext();
        checkBluetooth();
    }

    void checkBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null) {
            // 블루투스 어댑터가 없는 경우
            Toast.makeText(this, "블루투스를 지원하지 않습니다,",
                    Toast.LENGTH_SHORT).show();
            finish();

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
                finish();
            }
        }
    }
    public void selectDevice() {
        // 등록된 블루티스 디바이스 집합 추출
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if(devices.size() == 0) {
            Toast.makeText(this, "연결된 블루투스가 장비가 없습니다.",
                    Toast.LENGTH_SHORT).show();
            finish();
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
                            finish();
                        } else {
                            // 장치 선택 - 연결시도
                            connectToSelectedDevice(items[i].toString());
                        }
                    }
                })
                .setCancelable(false)
                .show();

    }	// selectDevice 끝



    public void connectToSelectedDevice(String deviceName){
        Toast.makeText(this, deviceName, Toast.LENGTH_SHORT).show();

        // 해시맵에서 장치 추출
        selectedDevice = btDevices.get(deviceName);

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

                    Log.d(TAG, "블루투스 연결 성공");
                    handler.sendEmptyMessage(0);
/*                    Toast.makeText(mContext, "연결 성공. 일단은.",
                            Toast.LENGTH_SHORT).show();*/
                    // 데이터 송수신 스트림 얻기
                    OutputStream outputStream = socket.getOutputStream();
                    InputStream inputStream = socket.getInputStream();
                    out = new PrintWriter(new OutputStreamWriter(outputStream));

                    // 데이터 수신 스레드 기동
                    workerThread = new ReceiveThread(inputStream);
                    workerThread.start();
                } catch (Exception e) {
                    Log.d(TAG, "블루투스 연결실패", e);
                    finish();
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
                        public void run() {
                            // UI 갱신
                            String msg = receiveMsgView.getText().toString();
                            receiveMsgView.setText(msg+message+"\n");
                        }
                    });

                } catch(IOException e){
                    Log.d(TAG, "업데이트 실패", e);
                    finish();
                }
            }
        }
    }

    // 보내기 버튼 클릭시 동작
    // editText에 입력했던 문자를 PrintWriter에 실어서 전송
    public void onSend(View view) {
        String msg;

        msg = editText.getText().toString();
        msg += '\n';
        try{
            //out.print(msg);
            out.print(msg);
            out.flush();
            editText.setText("");//텍스트입력창에 입력한 메시지 초기화
        } catch (Exception e ) {
            finish();
        }
    }

    //   종료시 동작조건 (소켓 외 나머지는 자동으로 닫히므로, 소켓만 닫아준다.)
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
        }
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            Toast.makeText(getApplicationContext(), "연결성공. 일단은..", 0).show();

            super.handleMessage(msg);

        }

    };
}
