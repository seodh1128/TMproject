#include <SoftwareSerial.h>
#include <ArduinoJson.h>
#include <string.h>

// 블루투스 통신 관련 변수
SoftwareSerial BTSerial(2, 3); // SoftwareSerial(RX, TX)

// 핀 번호 및 A/M, state 변수 선언
const int luxLedPin = 4; // led 핀
const int photoResistorPin = A0; // 조도센서
const int luxAMState = 1;
const int luxDevState = 0;

int photoResistorValue;  // 조도 센서 값을 저장할 변수

// 주기 5초 설정
unsigned long previousMillis = 0;
const long interval = 5000;

void setup() {
  Serial.begin(9600);
  BTSerial.begin(9600);
  pinMode(luxLedPin, OUTPUT);
}

void loop() {
  unsigned long currentMillis = millis();
  photoResistorValue = analogRead(photoResistorPin);
  if (photoResistorValue >= 700 ) {
    digitalWrite(luxLedPin, LOW);
    luxDevState = 0;
  } else digitalWrite(luxLedPin, HIGH);
  if (currentMillis - previousMillis >= interval) {
    previousMillis = currentMillis;
    luxDevState = 1;
    sendIlluminance(photoResistorValue);
  }
}

void sendIlluminance(int value) {
  StaticJsonBuffer<200> illuminanceBuffer;
  JsonObject &illuminance = illuminanceBuffer.createObject();
  illuminance["sensorCode"] = 1;
  illuminance["sensorValue"] = value
  illuminance["deviceCode"] = 1;
  illuminance["autoMode"] = luxAMState;
  illuminance["deviceState"] = luxDevState;
  illuminance.printTo(BTSerial);    
  BTSerial.println();
}

