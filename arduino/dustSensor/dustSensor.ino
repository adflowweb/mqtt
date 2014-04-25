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
byte mac[] = { 0xDE, 0xED, 0xBA, 0xFE, 0xFE, 0xED };
byte server[] = { 175, 209, 8, 188 };
byte ip[] = { 192,168,0,101 };

float total =0.0;
float average = 0.0;
int i = 0;
boolean result = false;
//float to string 전환용 버퍼
char buffer[10];
char msg[512];
//byte buf[1024];
byte message[230] = "123456789";

unsigned int size = 230;


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
  if (client.connect("dustSensor")) {
    //client.publish("user/XXX","hello world");
    //client.subscribe("inTopic");
  }
  pinMode(ledPower,OUTPUT);
}

void loop()
{
  //먼지 밀도 초기화 
  dustDensity = 0.0;
  result = false;

  digitalWrite(ledPower,LOW); // power on the LED
  delayMicroseconds(samplingTime);

  voMeasured = analogRead(measurePin); // read the dust value

  delayMicroseconds(deltaTime);
  digitalWrite(ledPower,HIGH); // turn the LED off
  delayMicroseconds(sleepTime);

  // 0 - 5.0V mapped to 0 - 1023 integer values
  // recover voltage
  calcVoltage = voMeasured * (5.0 / 1024.0);

  // 먼지센서 스펙상 전압이 0.58823529 이상되어야함 
  if(calcVoltage > 0.58823529)
  {
    // linear eqaution taken from http://www.howmuchsnow.com/arduino/airquality/
    // Chris Nafis (c) 2012
    dustDensity = (0.17 * calcVoltage - 0.1) * 1000;
  }


  Serial.print("Raw Signal Value (0-1023): ");
  Serial.print(voMeasured);
              
  Serial.print(" - Voltage: ");
  Serial.print(calcVoltage);

  Serial.print(" - Dust Density(ug/m3): ");
  Serial.println(dustDensity);
  total += dustDensity;
  i++;
  if(i > 360) // 한시간 주기로 측정평균값 푸시 
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
    // 문자열크기에 제한이 있음 220 이상 오버플로우 남..
    String dataMsg4 = dataMsg1 + dtostrf(average, 3, 2, buffer) + dataMsg2 + dtostrf(average, 3, 2, buffer) + dataMsg3;
    Serial.print(" - dataMsg.length: ");
    Serial.println(dataMsg4.length());

    // char 메소드
    dataMsg4.toCharArray(msg, dataMsg4.length()+1);
    // 먼지 측정 데이터를 푸시한다. 
    result = client.publish((char*)"user/1c45de7cc1daa896bfd32dc", msg);
    //result = client.publish("user/1c45de7cc1daa896bfd32dc", message, size);
    Serial.print(" - push result: ");
    Serial.println(result);
    // bytes 메소드
    //dataMsg4.getBytes(buf,dataMsg4.length()+1);
    //client.publish((char*)"user/1c45de7cc1daa896bfd32dc", buf, dataMsg4.length());
    i = 0;
    total = 0.0;
  }


  // 샘플링주기 10초
  delay(10000);
  client.loop();
}








