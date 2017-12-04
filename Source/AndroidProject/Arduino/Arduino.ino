#include <SoftwareSerial.h>
#include <Wire.h> 
#include <LiquidCrystal_I2C.h>
#include <ArduinoJson.h>
#include <string.h>

SoftwareSerial BTSerial(2,3); // SoftwareSerial(RX, TX)
LiquidCrystal_I2C lcd(0x3f,16,2);  

String myString="";   // 받는 문자열
boolean comState="";  // 통신 상태 체크
String comString="";  // 받은 문자열을 담아둠
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
  double reading = analogRead(temperaturePin);  //센서값 읽어옴
  //센서값 섭씨온도로 변환(LM35 계산 공식에 의함)
  double val = (5.0*reading*100.0)/1024.0;

  while(BTSerial.available()) {  //BTSerial에 전송된 값이 있으면
    //BTSerial int 값을 char 형식으로 변환
    char myChar = (char)BTSerial.read(); 
    //수신되는 문자를 myString에 모두 붙임 (1바이트씩 전송되는 것을 연결)
    myString+=myChar; 
    delay(5);   //수신 문자열 끊김 방지
  }
  comString = myString;
  comString.trim();
if(!comString.equals("")) { //myString 값이 있다면
    lcd.setCursor(0,1); // 커서 위치 설정 (x,y)
    lcd.print("");
    lcd.print(comString); 
    BTSerial.println("CommunicationSuccess"); 
    receiveJson(comString);
    
    myString="";    //myString 변수값 초기화
  }


  
}

void sendTemperature(long value) {
  // 온도 센서를 위한 Json 객체 설정
  StaticJsonBuffer<200> temperatureBuffer;
  JsonObject &temperature = temperatureBuffer.createObject();
  temperature["deviceType"] = "temperature";
  temperature["deviceCode"] = "temperature1";
  temperature["deviceValue"] = value;
  temperature["deviceTime"] = millis();
  temperature["pinNumber"] = 0;
  temperature["deviceAM"] = 0;
  temperature.printTo(BTSerial);    
  BTSerial.println();
}

boolean receiveJson(String json){
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(json);
  if (!root.success()) return false;
 
  String type = root["type"];
  if(type=="LED") {
    runLedCommand(root);
  }
}

void runLedCommand(JsonObject& led) {
  int pin = led["target"];
  String command = led["command"];
  Serial.print(pin);
  
  if(command == "ON") {
    Serial.println(command);
    digitalWrite(pin, HIGH);
  }
  else {
    Serial.println(command);
    digitalWrite(pin, LOW);
  }
}

