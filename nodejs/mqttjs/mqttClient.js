/**
 * User: nadir93
 * Date: 2014. 2. 7.
 * Time: 오후 1:32
 */
var mqtt = require('../../../MQTT.js');
var util = require('util');
var index = 1;
var clients = new Array();
var clientCount = 1;
var connectedCnt = 1;
var cluster = require('cluster');
var numCPUs = require('os').cpus().length;
pingCnt = 1;

if (cluster.isMaster) {
  // Fork workers.
  for (var i = 0; i < numCPUs; i++) {
    cluster.fork();
  }

  cluster.on('exit', function (worker, code, signal) {
    console.log('worker ' + worker.process.pid + ' died');
  });


} else {
  console.log('workerID=' + cluster.worker.id);
  setTimeout(initConnection, cluster.worker.id * 1000);

}

cluster.on('fork', function (worker) {
  console.log('workerID=' + worker.id);
  console.log('worker=' + worker.process.pid);

});


function initConnection() {
  for (i = 0; i < clientCount; i++) {

    //clients[i] = mqtt.createClient(1883, 'adflow.net');
    //clients[i] = mqtt.createClient(1883, '192.168.0.21', {clientId: 'example' + i, clean: false});
    //clients[i] = mqtt.createClient(1883, '14.63.216.249', {clientId: 'examples' + i, clean: false});
    clients[i] = mqtt.createClient(1883, '14.63.216.249', {keepalive: 60});

    //clients[i] = mqtt.createClient(1883, '172.30.1.60');
    //clients[i] = mqtt.createClient(1883, 'test.mosquitto.org');
    //if (i == 1) {
    //  clients[i].subscribe('/push/test1');
    //} else {
    clients[i].subscribe('/push/test002', {qos: 2}, function (err, granted) {
      if (err) throw err;
      console.log('connected=' + (connectedCnt++) + util.inspect(granted));
    });


    // }

    //clients[i].subscribe('/push/test');
    clients[i].on('message', function (topic, message) {

      if (index == 1) {
        start = new Date().getTime();
        console.log('시작시간=' + start);
      }

      //var j = message.substring(0,1);
      //index = index + parseInt(j);
      console.log('index=' + (index++));
      //console.log(message);

      if ((index % 100) == 0) {
        end = new Date().getTime();
        time = end - start;
        console.log('걸린시간=' + time + " ms");
      }
    });

  }

  console.log('clientsInited=' + clients.length);
};



