#include <SoftwareSerial.h>
#include <Wire.h> 
#include <LiquidCrystal_I2C.h>
#include <ArduinoJson.h>
#include <string.h>

SoftwareSerial BTSerial(2,3); // SoftwareSerial(RX, TX)
LiquidCrystal_I2C lcd(0x3f,16,2);  

String myString="";   // 받는 문자열
boolean comState="";  // 통신 상태 체크
String comString="";
const int temperaturePin = A0;

void setup() {
  pinMode(8, OUTPUT);
  Serial.begin(9600);   //시리얼모니터 
  BTSerial.begin(9600); //블루투스 시리얼 개방
  lcd.init();      // LCD 초기화
  lcd.backlight();    // 백라이트 켜기
  lcd.setCursor(0,0); // 커서 위치 설정 (x,y)
  lcd.print("start");
}
void loop() {
  int reading = analogRead(temperaturePin);  //센서값 읽어옴
  //센서값 섭씨온도로 변환(LM35 계산 공식에 의함)
  int val = (5.0*reading*100.0)/1024.0;

  while(BTSerial.available()) {  //BTSerial에 전송된 값이 있으면
    //BTSerial int 값을 char 형식으로 변환
    char myChar = (char)BTSerial.read(); 
    //수신되는 문자를 myString에 모두 붙임 (1바이트씩 전송되는 것을 연결)
    myString+=myChar; 
    delay(5);   //수신 문자열 끊김 방지
  }
  comString = myString;
  comString.trim();
  
  if(comString.equals("temp")) { //myString 값이 있다면
    lcd.setCursor(0,0); // 커서 위치 설정 (x,y)
    lcd.print("success");
    lcd.setCursor(0,1); // 커서 위치 설정 (x,y)
    lcd.println("");
    lcd.println(val); 
    sendTemperature(val);
    myString="";    //myString 변수값 초기화
  }else if(!comString.equals("")) { //myString 값이 있다면
    lcd.setCursor(0,1); // 커서 위치 설정 (x,y)
    lcd.print("");
    lcd.print(myString); 
    BTSerial.println("fail"); 
    Serial.println(myString.length()); 
    Serial.println(comString.length()); 
    myString="";    //myString 변수값 초기화
  }
}

void sendTemperature(int value) {
  // 온도 센서를 위한 Json 객체 설정
  StaticJsonBuffer<200> temperatureBuffer;
  JsonObject &temperature = temperatureBuffer.createObject();
  temperature["type"] = "temperature";
  temperature["name"] = "temperature1";
  temperature["pinNumber"] = 0;
  temperature["value"] = value;
  temperature["time"] = millis();
  temperature.printTo(BTSerial);    
  BTSerial.println();
}

