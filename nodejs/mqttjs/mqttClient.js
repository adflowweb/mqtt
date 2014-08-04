/**
 * User: nadir93
 * Date: 2014. 2. 7.
 * Time: 오후 1:32
 */
var mqtt = require('mqtt');
var util = require('util');
var index = 1;
var clients = new Array();
var clientCount = 1;

for (i = 0; i < clientCount; i++) {

  //clients[i] = mqtt.createClient(1883, 'adflow.net');

  //clients[i] = mqtt.createClient(1883, '192.168.0.21', {clientId: 'example' + i, clean: false});
  clients[i] = mqtt.createClient(1883, '14.63.216.249', {clientId: 'example' + i, clean: false});

  //  clients[i] = mqtt.createClient(1883, '192.168.0.21', function (err, client) {
  //    if (err) throw err;
  //
  //    client.connect({
  //      protocolId: 'MQIsdp',
  //      protocolVersion: 3,
  //      clientId: 'example' + i,
  //      keepalive: 30000,
  //      clean: false
  //    });
  //  });
  //clients[i] = mqtt.createClient(1883, '172.30.1.60');
  //clients[i] = mqtt.createClient(1883, 'test.mosquitto.org');
  //if (i == 1) {
  //  clients[i].subscribe('/push/test1');
  //} else {
  clients[i].subscribe('/push/test001', {qos: 2}, function (err, granted) {
    if (err) throw err;
    console.log('granted=' + util.inspect(granted));
  });


  // }

  //clients[i].subscribe('/push/test');
  clients[i].on('message', function (topic, message) {

    //    if (index == 1) {
    //      start = new Date().getTime();
    //      console.log('시작시간=' + start);
    //    }

    //var j = message.substring(0,1);
    //index = index + parseInt(j);
    console.log('index=' + (index++));
    //console.log(message);

    //    if (index == 100000) {
    //      end = new Date().getTime();
    //      time = end - start;
    //      console.log('걸린시간=' + time + " ms");
    //    }
  });

}

console.log('clientsInited=' + clients.length);
