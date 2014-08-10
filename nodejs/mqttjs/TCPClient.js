/**
 * User: nadir93
 * Date: 2014. 8. 6.
 * Time: 오후 10:42
 */
var client = require('./TestClient003');
var util = require('util');
var clients = new Array();

adflow = {
  start: 0,
  end: 0,
  pingCount: 0,
  max: 0,
  min: 0,
  avg: 0,
  clientCount: 100,
  keepalive: 30,
  host: '175.209.8.188',
  port: 1883
};

console.log('adflow=' + util.inspect(adflow));

//process.on('uncaughtException', function (err) {
//  console.log('에러발생: ' + err);
//});

setInterval(function () {
  if (adflow.pingCount > 0) {
    console.log('pingRespCount=' + adflow.pingCount);
    console.log('max=' + adflow.max + ' ms');
    console.log('min=' + adflow.min + ' ms');
    console.log('avg=' + (adflow.avg / adflow.pingCount) + ' ms');

    adflow.min = 0;
    adflow.max = 0;
    adflow.avg = 0;
    adflow.pingCount = 0;
  }
}, 1000);


for (var i = 0; i < adflow.clientCount; i++) {

  setTimeout(initClient, i * 1);
  //clients[i] = new client();
  //clients[i].connect();
  //clients[i].on('data', clients[i].receivedData);
  //clients[i].on('close', clients[i].close);

  //  clients[i] = new client();
  //  clients[i].connect();
  //client();
}

function initClient() {
  var mqttClient = new client();
  mqttClient.connect();
  mqttClient.on('data', mqttClient.receivedData);
  mqttClient.on('close', mqttClient.close);
}
