/**
 * User: nadir93
 * Date: 2014. 8. 6.
 * Time: 오후 10:42
 */
var client = require('./TestClient003');
var util = require('util');
var clients = new Array();

global.adflow = {
  start: 0,
  end: 0,
  pingCount: 0,
  max: 0,
  min: 0,
  avg: 0,
  clientCount: 100
};

//process.on('uncaughtException', function (err) {
//  console.log('에러발생: ' + err);
//});

setInterval(function () {
  if (global.adflow.pingCount > 0) {
    console.log('pingCount=' + global.adflow.pingCount);
    console.log('max=' + global.adflow.max + ' ms');
    console.log('min=' + global.adflow.min + ' ms');
    console.log('avg=' + (global.adflow.avg / global.adflow.pingCount) + ' ms');

    global.adflow.min = 0;
    global.adflow.max = 0;
    global.adflow.avg = 0;
    global.adflow.pingCount = 0;
  }
}, 1000);


for (var i = 0; i < global.adflow.clientCount; i++) {
  clients[i] = new client();
  clients[i].connect();
  clients[i].on('data', clients[i].receivedData);
  clients[i].on('close', clients[i].close);
  //  clients[i] = new client();
  //  clients[i].connect();
  //client();
}
