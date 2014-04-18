미세먼지 측정하기 
===
![alt tag](https://trello-attachments.s3.amazonaws.com/5342337a41e3af494917340c/5350c73464d6f83f5af01fe2/3264x2448/ff01713691a20087573a11020ff80815/IMG_20140418_145712.jpg)
###개요 
- **미세먼지 농도를 측정한다**

> *미세먼지를 주기적으로 체크하고 일정주기마다 평균값을 push client 에 푸시한다*

> *먼지데이타 샘플링 : 10초*

> *측정단위 : ug/m3*

###구성요소  
- arduino mega adk - [uno](http://arduino.cc/en/Main/arduinoBoardUno) or [leonardo](http://arduino.cc/en/Main/arduinoBoardLeonardo) 둘다 가능  
- [ethernet shield](http://arduino.cc/en/Main/ArduinoBoardEthernet)
- prototype shield - 빵판포함  
- dustSensor [GP2Y1010AU0F](http://www.aliexpress.com/item/GP2Y1010AU0F-100-NEW-SHARP-Optical-Dust-Sensor-GP2Y1010/1347390254.html) - *USD 7.38*
- [220 uF Capacitor](http://www.aliexpress.com/item/50-pcs-Aluminum-Radial-Electrolytic-Capacitor-220uF-25V/1143386595.html)
- 150 Ω Resistor
- push server - [mosquitto](http://mosquitto.org) on [Raspberry Pi](http://www.raspberrypi.org/)
- push client - [android](https://github.com/adflowweb/mqtt/tree/master/pushClient)

###설치 

- 회로도

>![alt tag](http://arduinodev.woofex.net/wp-content/uploads/sharpFromDoc.png)

- 핀연결 
	 
>| Sharp Dust Sensor | Attached To |
| -------------      | ------------- |
| 1 (V-LED)          | 5V Pin (150 Ohm in between)  |
| 2 (LED-GND)        | GND Pin |
| 3 (LED)	     | Digital Pin 2
| 4 (S-GND)	     | GND Pin
| 5 (Vo)             | Analog Pin A0
| 6 (Vcc)	     | 5V Pin (Direct)

###코드
- 먼지센서 

> [*먼지센서모듈*](https://github.com/Trefex/arduino-airquality/tree/master/Module_Dust-Sensor) 
```cpp
  digitalWrite(ledPower,LOW); // LED 전원인가, 측정시작
  delayMicroseconds(samplingTime); // 0.28ms 기다림 
  voMeasured = analogRead(measurePin); // 먼지데이타 읽기 
  delayMicroseconds(deltaTime); // 0.04ms 기다림 
  digitalWrite(ledPower,HIGH); // LED 전원중지  
  delayMicroseconds(sleepTime); // 최소 측정 주기가 10ms 이므로 9680microSeconds를 기다린다(측정에 320us사용함)
  // 0 - 5.0V mapped to 0 - 1023 integer values
  // recover voltage
  calcVoltage = voMeasured * (5.0 / 1024.0);
  // 먼지센서 스펙상 전압이 0.58 이상되어야함 
  if(calcVoltage > 0.58823529)
  {
    // linear eqaution taken from http://www.howmuchsnow.com/arduino/airquality/
    // Chris Nafis (c) 2012
    dustDensity = 0.17 * calcVoltage - 0.1;
  }
```

- mqtt client

> [*mqtt 아두이노 라이브러리*](https://github.com/knolleary/pubsubclient)
>
> **default message size : 128bytes**
>
> **default keepalive interval : 15초**
```cpp
// 먼지 측정 데이터를 푸시한다. 
client.publish((char*)"users/1c45de7cc1daa896bfd32dc", msg);
```
  
###문제점
- 현재 push 메시지크기가 255 를 넘어가면 메시지 전송이 안됨.(현재는 메시지 크기가 255이하임)

###추후작업
- cosm 연동
- node.js 연동 

> *arduino* <--> *node.js(publisher)* --> *mqttServer(mosquitto)* --> *androidClient(subscriber)*
>
> *arduino* --> *cosm*

###참고 
- *http://www.howmuchsnow.com/arduino/airquality/*
- *http://sensorapp.net/?p=479*
- *http://arduinodev.woofex.net/2012/12/01/standalone-sharp-dust-sensor/*
- *https://www.sparkfun.com/datasheets/Sensors/gp2y1010au_e.pdf*
- *http://knolleary.net/arduino-client-for-mqtt/*






