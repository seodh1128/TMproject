#include <SoftwareSerial.h>
#include <ArduinoJson.h>
#include "DHT.h"       // DHT.h 라이브러리를 포함한다
#define DHTPIN 4      // DHT핀을 4번으로 정의한다(DATA핀)
#define DHTTYPE DHT11  // DHT타입을 DHT11로 정의한다
DHT dht(DHTPIN, DHTTYPE);  // DHT설정 - dht (디지털4, dht11)

SoftwareSerial BTSerial(2,3); // SoftwareSerial(RX, TX)

//냉난방
int ehpSensorPin = 4; //온습도 핀 번호
int ehpgreenPin = 5;  
int ehpredPin = 6;
int ehpAMState = 1;
int ehpDevState = 0;


unsigned long pre_time = 0;
int led_red = HIGH;
int led_green = HIGH;
int red_state = LOW;

void temparate(float temp){ //온도 데이터 json으로 변환
String jsondata = "";
StaticJsonBuffer<200> jsonBuffer;
JsonObject& root = jsonBuffer.createObject();
    root["sensorCode"] = "4";
    root["sensorValue"] = temp;
    root["deviceCode"] = "4";
    root["deviceValue"] = "tempState";
    root["pinNumber"] = "tempPin";
    root["autoMode"] = "autoMode";
    
root.printTo(BTSerial); //String 으로 변환
BTSerial.println();
}

void humidity(float humi){  //습도 데이터 json으로 변환
String jsondata = "";
StaticJsonBuffer<200> jsonBuffer;
JsonObject& root = jsonBuffer.createObject();
    
    root["sensorCode"] = "4";
    root["sensorValue"] = humi;
    root["deviceCode"] = "4";
    root["deviceValue"] = "humiState";
    root["pinNumber"] = "humiPin";
    root["autoMode"] = "autoMode";
   
root.printTo(BTSerial); //String 으로 변환
BTSerial.println();
}

void red(int red){ //redPin 데이터 json으로 변환
   String reddata = "";
   StaticJsonBuffer<200> jsonBuffer;
   JsonObject& root = jsonBuffer.createObject();

    root["sensorCode"] = "6";
    root["sensorValue"] = red;
    root["deviceCode"] = "4";
    root["deviceValue"] = "redState";
    root["pinNumber"] = "redPin";
    root["autoMode"] = "autoMode";
    

   root.printTo(BTSerial); //String 으로 변환
    BTSerial.println();
  }
void green(){ //greenPin json으로 변환
    int green = digitalRead(5);
   
   String greendata = "";
   StaticJsonBuffer<200> jsonBuffer;
   JsonObject& root = jsonBuffer.createObject();
    
    root["sensorCode"] = "5";
    root["sensorValue"] = green;
    root["deviceCode"] = "4";
    root["deviceValue"] = "greenState";
    root["pinNumber"] = "greenPin";
    root["autoMode"] = "autoMode";
  
    root.printTo(BTSerial); //String 으로 변환
    BTSerial.println();
    
}
  
void TH(){
   unsigned long cur_time = millis();
 if(cur_time - pre_time >= 1000){
  pre_time = cur_time;
float humi = dht.readHumidity();  // 변수 h에 습도 값을 저장 
float temp = dht.readTemperature();  // 변수 t에 온도 값을 저장

 if(temp >=31 && humi >=9){
          digitalWrite(5,led_green);
       }else{
          digitalWrite(6,led_red);
          red_state = digitalRead(6);
       }
  temparate(temp);
  humidity(humi);
  red(red_state);
  green();
  }
}

void setup() {
  Serial.begin(9600); //통신속도 설정
  BTSerial.begin(9600);
  pinMode(5,OUTPUT);
  pinMode(6,OUTPUT);
}

void loop() {
  TH();
}
