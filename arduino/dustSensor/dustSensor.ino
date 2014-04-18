#include <SPI.h>
#include <Ethernet.h>
#include <PubSubClient.h>
#include <stdlib.h>

// 참고 : http://arduinodev.woofex.net/2012/12/01/standalone-sharp-dust-sensor/
// gp2y1010 스펙 : https://www.sparkfun.com/datasheets/Sensors/gp2y1010au_e.pdf

int measurePin = 0; //측정된 먼지데이타를 읽는 핀.
int ledPower = 2; //LED 핀 

int samplingTime = 280;
int deltaTime = 40;
int sleepTime = 9680;

float voMeasured = 0;
float calcVoltage = 0;
float dustDensity = 0;

// Update these with values suitable for your network.
byte mac[]    = {  
  0xDE, 0xED, 0xBA, 0xFE, 0xFE, 0xED };
byte server[] = {  
  175, 209, 8, 188 };
byte ip[]     = { 
  192,168,0,101 };

float total =0.0;
float average = 0.0;
int i = 0;

//float to string 전환용 버퍼
char buffer[10];
char msg[512];
//byte buf[1024];


void callback(char* topic, byte* payload, unsigned int length) {
  // handle message arrived
}

EthernetClient ethClient;
PubSubClient client(server, 1883, callback, ethClient);

void setup()
{
  Serial.begin(9600);
  //Ethernet.begin(mac, ip);
  Ethernet.begin(mac);
  if (client.connect("arduinoClient")) {
    //client.publish("user/1c45de7cc1daa896bfd32dc","hello world");
    //client.subscribe("inTopic");
  }
  pinMode(ledPower,OUTPUT);
}

void loop()
{
  digitalWrite(ledPower,LOW); // power on the LED
  delayMicroseconds(samplingTime);

  voMeasured = analogRead(measurePin); // read the dust value

  delayMicroseconds(deltaTime);
  digitalWrite(ledPower,HIGH); // turn the LED off
  delayMicroseconds(sleepTime);

  // 0 - 5.0V mapped to 0 - 1023 integer values
  // recover voltage
  calcVoltage = voMeasured * (5.0 / 1024.0);

  // linear eqaution taken from http://www.howmuchsnow.com/arduino/airquality/
  // Chris Nafis (c) 2012

  if(calcVoltage <= 0.58823529)
  {
    dustDensity = 0.0;
  }
  else
  {
    dustDensity = 0.17 * calcVoltage - 0.1;
  }


  Serial.print("Raw Signal Value (0-1023): ");
  Serial.print(voMeasured);

  Serial.print(" - Voltage: ");
  Serial.print(calcVoltage);

  Serial.print(" - Dust Density(ug/m3): ");
  Serial.println(dustDensity * 1000);
  total += dustDensity * 1000;
  i++;
  if(i > 360)  // 한시간 주기로 측정평균값 푸시 
  {
    // 평균값을 구한다.
    average = total / (i);
    Serial.print(" - average: ");
    Serial.print(average);
    Serial.print(" - count : ");
    Serial.println(i);
    //String dataMsg = "{\"t\":";
    //dataMsg.concat(dtostrf(average, 3, 2, buffer));
    //dataMsg.concat("}");
    //dataMsg.toCharArray(msg, dataMsg.length()+1);
    //sensorClient.publish(topic, msg);       
    String dataMsg1 = "{'notification': {'notificationStyle': 2, 'contentTitle': '','contentText':'먼지측정 : ";
    String dataMsg2 = "(ug/m3)', 'ticker':'먼지 측정 : ";
    String dataMsg3 = "(ug/m3)' ,'summaryText':'', 'image':'', 'contentUri':''}}";
    //dtostrf(FLOAT,WIDTH,PRECSISION,BUFFER);
    //max 문자열 길이 255 읹지확인해야함 255이상이면 푸시안됨
    String dataMsg4 = dataMsg1 + dtostrf(average, 3, 2, buffer) + dataMsg2 + dtostrf(average, 3, 2, buffer) + dataMsg3;
    Serial.print(" - dataMsg: ");
    Serial.println(dataMsg4);

    // char 메소드
    dataMsg4.toCharArray(msg, dataMsg4.length()+1);
    // 먼지 측정 데이터를 푸시한다. 
    client.publish((char*)"user/1c45de7cc1daa896bfd32dc", msg);

    // bytes 메소드
    //dataMsg4.getBytes(buf,dataMsg4.length()+1);
    //client.publish((char*)"user/1c45de7cc1daa896bfd32dc", buf, dataMsg4.length());
    i=0;
    total = 0.0;
  }


  // 샘플링주기 10초
  delay(10000);
  client.loop();
}








