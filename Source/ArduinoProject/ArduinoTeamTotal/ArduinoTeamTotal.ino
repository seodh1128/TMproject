#include <DHT.h>
#include <SoftwareSerial.h>
#include <Wire.h> 
#include <LiquidCrystal_I2C.h>
#include <ArduinoJson.h>
#include <string.h>
#define DHTTYPE DHT11  // DHT타입을 DHT11로 정의한다

/////////////////////////////////////////////////////////////////////////
//  블루투스 통신 관련 변수
SoftwareSerial BTSerial(2,3); // SoftwareSerial(TX, RX)
const int temperaturePin = A0;
/////////////////////////////////////////////////////////////////////////
//  각 시스템별 핀 번호 및 A/M State 변수 선언
/////////////////////////////////////////////////////////////////////////
// 공통 & Pin번호
const long interval = 5000;
const int secuBtnPin = 8;
const int secuLedPin = 9;
const int luxLedPin = 7 ; // led 핀
const int photoResistorPin = A0; // 조도센서
const int ehpSensorPin = 4; //온습도 핀 번호
const int ehpGreenPin = 5;  
const int ehpRedPin = 6;
//  방범
int secuAMState = 1;
int secuDevState = 0;
unsigned long secuPre = 0;

//냉난방
int ehpAMState = 1;
int ehpDevState = 0;
int ehpLedState = LOW;
unsigned long pre_time = 0;
DHT dht(ehpSensorPin, DHTTYPE);  // DHT설정 - dht (디지털4, dht11)

// 조명 핀 번호 및 A/M, state 변수 선언
  unsigned long luxPreviousMillis = 0;
int luxAMState = 1;
int luxDevState = 0;
int photoResistorValue;  // 조도 센서 값을 저장할 변수

/////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////
//  방범용 (버튼상태 체크를 위한) Class
class ButtonCheck
{
  // 자동/수동 모드 전용 변수
  int autoMode;

  // Button (근접 센서 대용 버튼) 전용 변수
  int buttonPin;
  int buttonState;
  unsigned long btnPreviousMillis;

  // LED (경보장치) 전용 변수
  int setLed;
  int ledPin;
  long OnTime;
  long OffTime;
  int ledState;
  unsigned long blinkPreviousMillis;

  // 입력받은 변수 데이터들을 내부 변수에 입력
  public : 
  ButtonCheck(int btnPin,int lPin, int secuAMState)
  {
    autoMode = secuAMState;
    
    buttonPin = btnPin;
    pinMode(buttonPin, INPUT);
    buttonState = LOW;
    btnPreviousMillis = 0;
    
    ledPin = lPin;
    pinMode(ledPin , OUTPUT);
    OnTime = 100;
    OffTime = 100;
    ledState = LOW;
    blinkPreviousMillis = 0;
  }

  // 외부에서 동작(실행)되는 구문
  void Update()
  {
    long devTime = 50;
    unsigned long btnCurrentMillis = millis();
    buttonState = digitalRead(buttonPin);
    ledState = digitalRead(ledPin);
        
    if(buttonState == LOW)
    {
      secuDevState = 0;
      ledState = LOW;
      digitalWrite(ledPin, ledState);  
      if(btnCurrentMillis - btnPreviousMillis >= 5000)
      {
        sendSecurityJson(buttonState, secuDevState, secuAMState);
        btnPreviousMillis = btnCurrentMillis;
      }
    }
    else if(buttonState == HIGH)
    { 
      if(btnCurrentMillis - btnPreviousMillis >= 50)
      {
        secuDevState = 1; 
        ledBlink(buttonState, ledState);  //Led 동작 Flag 삽입 
        if(btnCurrentMillis - btnPreviousMillis >= 5000)
        {      
           sendSecurityJson(buttonState, secuDevState, secuAMState);
           btnPreviousMillis = btnCurrentMillis;
        }
      }
    }  
  }

  void ledBlink(int buttonState,int ledState)
  {
      unsigned long blinkCurrentMillis = millis();      
        if((ledState == HIGH) && (blinkCurrentMillis - blinkPreviousMillis >= OnTime))
        {
          ledState = LOW;
          blinkPreviousMillis = blinkCurrentMillis;
          digitalWrite(ledPin, ledState);  
          
        }
        else if ((ledState == LOW) && (blinkCurrentMillis - blinkPreviousMillis >= OffTime))
        {
          ledState = HIGH;
          blinkPreviousMillis = blinkCurrentMillis;
          digitalWrite(ledPin, ledState);
        }
  }
  void sendSecurityJson(int btnState, int blinkState, int autoMode) 
  {   
      // 방범 시스템을 위한 Json 객체 설정
      StaticJsonBuffer<200> securityBuffer;
      JsonObject &security = securityBuffer.createObject();
      security["sensorCode"] = 2;
      security["sensorValue"] = btnState;
      security["deviceCode"] = 2;
      security["deviceState"] = blinkState;
      security["autoMode"] = autoMode;
      security.printTo(BTSerial);    
      BTSerial.println();
  } 
};
/////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////
//  방범(감지기 동작시 LED 점멸) 관련 변수
ButtonCheck buttonCheck(secuBtnPin,secuLedPin,secuAMState);
/////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////
//  본격적인 Setup문 / Void문
void setup() {
  Serial.begin(9600);
  BTSerial.begin(9600); //블루투스 시리얼 개방

  pinMode(luxLedPin, OUTPUT);
}


void loop() {
  //  Android로부터 입력 대기중
  receiveBTserial();

  lux();

  security();

  th();

  checkSerial();
}
// setup/loop문 종료
/////////////////////////////////////////////////////////////////


//  GateWay로부터 데이터 입력받을 경우 수행될 작업 관련
/////////////////////////////////////////////////////////////////////
void receiveBTserial(){
  String myString="";   // 받는 문자열
  boolean comState="";  // 통신 상태 체크
  String comString="";  // 받은 문자열을 담아둠
// 입력 신호 받을 경우 동작 수행
/////////////////////////////////////////////////////////////////
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
    receiveJson(comString);
    
    myString="";    //myString 변수값 초기화
  }
}
boolean receiveJson(String json) {
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(json);
  if (!root.success()) return false;
    String controlAutoMode = root["autoMode"];
    String controlDeviceState = root["deviceState"];
    String strCodeNum = root["deviceCode"];
 
    //  수신받은 Json 구문에서 해당되는 Signal 전송을 위한 메소드
    int changeCodeNum = strCodeNum.toInt();            
    int setAMState = controlAutoMode.toInt();
    int setDevState = controlDeviceState.toInt();

    //  전달받은 Pin번호를 가지고 해당 제어내용 수행
    switch(changeCodeNum){

      
        case 1 :
        luxAMState = setAMState;
        luxDevState = setDevState;
        break;  

        
        case 2 :
        secuAMState = setAMState;
        secuDevState = setDevState;
        break;


        case 4 :
        ehpAMState = setAMState;
        ehpDevState = setDevState;
        break;
    }
}
/////////////////////////////////////////////////////////////////

void sendSecurityloop(int btnState, int blinkState, int autoMode) 
  {   
      
      unsigned long sendCurrentMillis = millis();
      unsigned long sendMillis = millis();
      // 방범 시스템을 위한 Json 객체 설정
      StaticJsonBuffer<200> securityBuffer;
      JsonObject &security = securityBuffer.createObject();
      security["sensorCode"] = 2;
      security["sensorValue"] = btnState;
      security["deviceCode"] = 2;
      security["deviceState"] = blinkState;
      security["autoMode"] = autoMode;
      security.printTo(BTSerial);    
      BTSerial.println();
  } 

/////////////////////////////////////////////////////////////////////

//  Test용 시리얼 통신 구문
void checkSerial(){
  if(Serial.available()){
    String testStr;
    testStr = Serial.read();
    testStr.trim();
    changeSerial(testStr);

  }
}

void changeSerial(String str){
  String testStr;
  char cTempData[4];
  str.toCharArray(cTempData, 4);
  int changeStr = atoi(cTempData);
  
  Serial.println(changeStr);
  StaticJsonBuffer<200> testBuffer;
  JsonObject &test = testBuffer.createObject();
  switch(changeStr){
    case 49 :
      test["autoMode"] = 0;
      test["deviceState"] = 1;
      test["deviceCode"] = 1;
      test.printTo(testStr);
    
      testStr.trim();
      receiveJson(testStr);
    break;
      
    case 50 :
      test["autoMode"] = 0;
      test["deviceState"] = 1;
      test["deviceCode"] = 2;
      test.printTo(testStr);
    
      testStr.trim();
      receiveJson(testStr);
    break;

    case 52 :
      test["autoMode"] = 0;
      test["deviceState"] = 1;
      test["deviceCode"] = 4;
      test.printTo(testStr);
    
      testStr.trim();
      receiveJson(testStr);
    break;

    case 54:
      test["autoMode"] = 1;
      test["deviceState"] = 0;
      test["deviceCode"] = 1;
      test.printTo(testStr);
    
      testStr.trim();
      receiveJson(testStr);
    break;

    case 55:
      test["autoMode"] = 1;
      test["deviceState"] = 0;
      test["deviceCode"] = 2;
      test.printTo(testStr);
    
      testStr.trim();
      receiveJson(testStr);
    break;

    case 56:
      test["autoMode"] = 1;
      test["deviceState"] = 0;
      test["deviceCode"] = 4;
      test.printTo(testStr);
    
      testStr.trim();
      receiveJson(testStr);
    break;
  }
  
}

void security(){
  unsigned long secuCurrent = millis();

  
  //  Security 관련 동작 구문 - A/M 여부 확인 후 해당 동작 실행
  if(secuAMState == 1){
  //  Security관련 동작 실행 - (버튼 : 근접 센서/감지기)
    buttonCheck.Update();
    secuDevState = 0;
  }else if(secuAMState == 0 && secuDevState == 1){
    if(secuCurrent - secuPre  >= 5000){
      sendSecurityloop(digitalRead(secuBtnPin), secuDevState, secuAMState);
      secuPre = secuCurrent;
    }
    buttonCheck.ledBlink(digitalRead(secuBtnPin),digitalRead(secuLedPin));
  }else if(secuAMState == 0 && secuDevState == 0){
    if(secuCurrent - secuPre  >= 5000){
    sendSecurityloop(digitalRead(secuBtnPin), secuDevState, secuAMState); 
    secuPre = secuCurrent;
    }
  }
}

// 주기 5초 설정
///////////////////////////////////////////////////////////////////
void lux(){

  unsigned long currentMillis = millis();
  photoResistorValue = analogRead(photoResistorPin);
  
  if(luxAMState==1){
      if (photoResistorValue >= 700 ) {
        digitalWrite(luxLedPin, LOW);
        luxDevState = 0;
      } else digitalWrite(luxLedPin, HIGH);
      if (currentMillis - luxPreviousMillis >= interval) {
        luxPreviousMillis = currentMillis;
        luxDevState = 1;
        sendIlluminance(photoResistorValue);
      }
  }else if(luxAMState==0 && luxDevState == 1){
    if (currentMillis - luxPreviousMillis >= interval) {
      luxPreviousMillis = currentMillis;
      digitalWrite(luxLedPin, HIGH);
      sendIlluminance(photoResistorValue);
    }
  }else {
    if (currentMillis - luxPreviousMillis >= interval) {
      luxPreviousMillis = currentMillis;
      sendIlluminance(photoResistorValue);
    }
  }
  
}

void sendIlluminance(int value) {
  StaticJsonBuffer<200> illuminanceBuffer;
  JsonObject &illuminance = illuminanceBuffer.createObject();
  illuminance["sensorCode"] = 1;
  illuminance["sensorValue"] = value;
  illuminance["deviceCode"] = 1;
  illuminance["autoMode"] = luxAMState;
  illuminance["deviceState"] = luxDevState;
  illuminance.printTo(BTSerial);    
  BTSerial.println();
}

///////////////////////////////////////////////////////////////////


void temparate(float temp){ //온도 데이터 json으로 변환
String jsondata = "";
StaticJsonBuffer<200> jsonBuffer;
JsonObject& root = jsonBuffer.createObject();
    root["sensorCode"] = "4";
    root["sensorValue"] = temp;
    root["deviceCode"] = "4";
    root["autoMode"] = ehpAMState;
    root["deviceState"] = ehpDevState;
root.printTo(BTSerial); 
BTSerial.println();
}

void humidity(float humi){  //습도 데이터 json으로 변환
String jsondata = "";
StaticJsonBuffer<200> jsonBuffer;
JsonObject& root = jsonBuffer.createObject();
    
    root["sensorCode"] = "5";
    root["sensorValue"] = humi;
    root["deviceCode"] = "4";
    root["autoMode"] = ehpAMState;
    root["deviceState"] = ehpDevState;
root.printTo(BTSerial); 
BTSerial.println();
}
  
void th(){
    unsigned long cur_time = millis();
    if(cur_time - pre_time >= interval){
      pre_time = cur_time;
      float humi = dht.readHumidity();  // 변수 h에 습도 값을 저장 
      float temp = dht.readTemperature();  // 변수 t에 온도 값을 저장
  
      if(ehpAMState == 1){
        ehpDevState = 0;
        if(temp <= 30 && humi <= 20){
              digitalWrite(ehpGreenPin,HIGH);
              digitalWrite(ehpRedPin,LOW);
              ehpLedState = digitalRead(ehpRedPin);
              temparate(temp);
              humidity(humi);
             }else if(temp > 30 && humi > 20){
              digitalWrite(ehpGreenPin,LOW);
              digitalWrite(ehpRedPin,HIGH);
              ehpLedState = digitalRead(ehpRedPin);
              temparate(temp);
              humidity(humi);
             }
      }else if(ehpAMState == 0 && ehpDevState == 1){
        digitalWrite(ehpGreenPin,LOW);
        digitalWrite(ehpRedPin,HIGH);
        ehpLedState = digitalRead(ehpRedPin);
        temparate(temp);
        humidity(humi);
      }else{
        temparate(temp);
        humidity(humi);
      }
  }
}
///////////////////////////////////////////////////////////////////
