#include <SoftwareSerial.h>
#include <ArduinoJson.h>
#include "DHT.h"       // DHT.h 라이브러리를 포함한다
#define DHTPIN 4      // DHT핀을 2번으로 정의한다(DATA핀)
#define DHTTYPE DHT11  // DHT타입을 DHT11로 정의한다
DHT dht(DHTPIN, DHTTYPE);  // DHT설정 - dht (디지털2, dht11)

SoftwareSerial BTSerial(2,3); // SoftwareSerial(RX, TX)

unsigned long pre_time = 0;
int led_red = HIGH;
int led_green = HIGH;

const long interval = 1000;


void Json(float temp,float humi){
String jsondata = "";
StaticJsonBuffer<200> jsonBuffer;
JsonObject& root = jsonBuffer.createObject();
root["tempvalue"] = temp;
root["humivalue"] = humi;

root.printTo(BTSerial); //String 으로 변환
BTSerial.println();
}
 
void TH(){
   unsigned long cur_time = millis();
 if(cur_time - pre_time >= 1000){
  pre_time = cur_time;
float h = dht.readHumidity();  // 변수 h에 습도 값을 저장 
float t = dht.readTemperature();  // 변수 t에 온도 값을 저장

Serial.print("Temperature: ");  // 문자열 Humidity: 를 출력한다.
Serial.print(t);  // 변수 h(습도)를 출력한다.
Serial.print("C");  // %를 출력한다
Serial.print(" Humidity ");  // 이하생략
Serial.print(h);
Serial.println(" %\t");
 if(t >=31 && h >=9){
          digitalWrite(5,led_green);
       }else{
          digitalWrite(6,led_red);
       }

  }
}

void setup() {
  Serial.begin(9600); //통신속도 설정
  pinMode(5,OUTPUT);
  pinMode(6,OUTPUT);
}

void loop() {
  TH();
}
