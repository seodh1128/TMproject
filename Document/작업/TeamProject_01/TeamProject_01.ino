#include <SoftwareSerial.h>
#include <Wire.h> 
#include <LiquidCrystal_I2C.h>
#include <ArduinoJson.h>
#include <string.h>

/////////////////////////////////////////////////////////////////////////
//  블루투스 통신 관련 변수
SoftwareSerial BTSerial(2,3); // SoftwareSerial(TX, RX)
LiquidCrystal_I2C lcd(0x3f,16,2);  
const int temperaturePin = A0;
/////////////////////////////////////////////////////////////////////////
//  각 시스템별 핀 번호 및 A/M State 변수 선언
/////////////////////////////////////////////////////////////////////////
//  방범
int secuBtnPin = 8;
int secuLedPin = 9;
int secuAMState = 1;
int secuDevState = 0;

//  냉난방
int ehpSensorPin = 0;
int ehpPin = 0;
int ehpAMState = 1;
int ehpDevState = 0;

//  조명
int luxSensorPin = 0;
int luxLedPin = 0;
int luxAMState = 1;
int luxDevState = 0;

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
  int blinkState;
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
    blinkState = LOW;
    blinkPreviousMillis = 0;
  }

  // 외부에서 동작(실행)되는 구문
  void Update()
  {
    long delayTime = 1000;
    long devTime = 50;
    unsigned long btnCurrentMillis = millis();
    buttonState = digitalRead(buttonPin);
    ledState = digitalRead(ledPin);
        
    if(buttonState == LOW)
    {
      blinkState = 0;
      ledState = LOW;
      digitalWrite(ledPin, ledState);  
      if(btnCurrentMillis - btnPreviousMillis >= delayTime)
      {
        sendSecurityJson(buttonState, blinkState, ledPin ,autoMode);
        btnPreviousMillis = btnCurrentMillis;
      }
    }
    else if(buttonState == HIGH)
    { 
      if(btnCurrentMillis - btnPreviousMillis >= devTime)
      {
        blinkState = 1;
        ledBlink(buttonState, ledState);  //Led 동작 Flag 삽입
        if(btnCurrentMillis - btnPreviousMillis >= delayTime)
        {  
           sendSecurityJson(buttonState, blinkState, ledPin ,autoMode);
           btnPreviousMillis = btnCurrentMillis;
        }
      }
    }  
  }
  
  void ledBlink(int buttonState,int ledState)
  {
    unsigned long blinkCurrentMillis = millis();
      sendSecurityJson(buttonState, blinkState, ledPin ,autoMode);
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
  void sendSecurityJson(int btnState, int blinkState, int ledPin, int autoMode) 
  {   
      
      unsigned long sendCurrentMillis = millis();
      unsigned long sendMillis = millis();
      // 방범 시스템을 위한 Json 객체 설정
      StaticJsonBuffer<200> securityBuffer;
      JsonObject &security = securityBuffer.createObject();
      security["sensorCode"] = "proximity";
      security["sensorValue"] = btnState;
      security["deviceCode"] = "security";
      security["deviceValue"] = blinkState;
      security["pinNumber"] = ledPin;
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
  lcd.init();           // LCD 초기화
  lcd.backlight();      // 백라이트 켜기
  lcd.setCursor(0,0);   // 커서 위치 설정 (x,y)
  lcd.print("start");
}


void loop() {
  //  Android로부터 입력 대기중
  receiveBTserial();

  //  Security 관련 동작 구문 - A/M 여부 확인 후 해당 동작 실행
  if(secuAMState == 1){
  //  Security관련 동작 실행 - (버튼 : 근접 센서/감지기)
    buttonCheck.Update();
    secuDevState = 0;
  }else if(secuAMState == 0 && secuDevState == 1){
    buttonCheck.ledBlink(,secuLedPin);
  }else if(secuAMState == 0 && secuDevState == 1){
    sendSecurityloop(digitalRead(secuBtnPin), secuDevState, secuLedPin, secuAMState); 
  }
   //  EHP 관련 동작 구문 - A/M 여부 확인 후 해당 동작 실행
  if(ehpAMState == 1){
    ehpDevState = 0;
  }else if(ehpAMState == 0 && ehpDevState == 1){
    
  }
  //  Lux 관련 동작 구문 - A/M 여부 확인 후 해당 동작 실행
  if(luxAMState == 1){
    luxDevState = 0;
  }else if(luxAMState == 0 && luxDevState == 1){
    
  }
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
    lcd.clear();
    lcd.setCursor(0,1); // 커서 위치 설정 (x,y)
    lcd.print("");
    lcd.print(comString); 
    receiveJson(comString);
    
    myString="";    //myString 변수값 초기화
  }
}
boolean receiveJson(String json) {
  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(json);
  if (!root.success()) return false;
    lcd.clear();
    String controlAutoMode = root["autoMode"];
    String controlDeviceState = root["deviceState"];
    String strPinNum = root["pinNumber"];
 
    //  수신받은 Json 구문에서 해당되는 Signal 전송을 위한 메소드
    int changePinNum = strPinNum.toInt();
            
    int setAMState = controlAutoMode.toInt();
    int setDevState = controlDeviceState.toInt();

    //  전달받은 Pin번호를 가지고 해당 제어내용 수행
    switch(changePinNum){
        case 9 :
        secuAMState = setAMState;
        secuDevState = setDevState;
        break;
        case 2 :
        ehpAMState = setAMState;
        ehpDevState = setDevState;
        break;
        case 3 :
        luxAMState = setAMState;
        luxDevState = setDevState;
        break;  
    }
}
/////////////////////////////////////////////////////////////////

void sendSecurityloop(int btnState, int blinkState, int ledPin, int autoMode) 
  {   
      
      unsigned long sendCurrentMillis = millis();
      unsigned long sendMillis = millis();
      // 방범 시스템을 위한 Json 객체 설정
      StaticJsonBuffer<200> securityBuffer;
      JsonObject &security = securityBuffer.createObject();
      security["sensorCode"] = "proximity";
      security["sensorValue"] = btnState;
      security["deviceCode"] = "security";
      security["deviceValue"] = blinkState;
      security["pinNumber"] = ledPin;
      security["autoMode"] = autoMode;
      security.printTo(BTSerial);    
      BTSerial.println();
  } 

///////////////////////////////////////////////////////////////////

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
  
  StaticJsonBuffer<200> testBuffer;
  
  JsonObject &test = testBuffer.createObject();
  test["autoMode"] = "0";
  test["deviceState"] = digitalRead(secuDevState);
  test["pinNumber"] = secuLedPin;
  test.printTo(testStr);
  
  testStr.trim();
  receiveJson(testStr);
}

