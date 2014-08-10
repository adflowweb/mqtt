/**
 * User: nadir93
 * Date: 2014. 8. 7.
 * Time: 오후 5:56
 */
var net = require('net');
var crypto = require('crypto');
var util = require('util');

//var HOST = '14.63.216.254';
//var HOST = '172.30.1.39';
var HOST = '175.209.8.188';
var PORT = 1883;
var keepalive = 600;
var ping = new Buffer('c000',
  'hex').toString('binary');

process.on('uncaughtException', function (err) {
  console.log('에러발생: ' + err);
});

//for (var i = 0; i < 10; i++) {
//  setTimeout(socketInit, i * 10000);
//}

function mqttClient() {
  var start;
  var end;
  var timer;
  var clientID;
  var client = new net.Socket();

  client.connect(PORT, HOST, function () {
    console.log('소켓이연결되었습니다.' + HOST + ':' + PORT);
    //var keepaliveHex = keepalive.toString(16);
    console.log('keepalive=' + keepalive + '초');
    var connectHexStr = '102500064d51497364700302' + decimalToHex(keepalive, 4) + '0017';
    console.log('connectHexString=' + connectHexStr);
    clientID = generateClientID();
    console.log('clientID=' + clientID);
    var cliendIDHex = new Buffer(clientID).toString('hex');
    var data = new Buffer(connectHexStr + cliendIDHex,
      'hex').toString('binary');

    //console.log('data=' + data.toString('hex'));
    console.log('socket=' + util.inspect(client.address()));
    client.write(data);
  });

  // Add a 'data' event handler for the client socket
  // data is what the server sent to this socket
  client.on('data', function (data) {
    var dataHex = new Buffer(data, 'binary').toString('hex');
    console.log('RECEIVED=' + dataHex);

    if (dataHex == '20020000') {
      console.log('mqtt브로커연결에성공하였습니다.');
      sendPING();
      console.log('start=' + start);
    } else if (dataHex == 'd000') {
      console.log('핑을받았습니다.');
      resetTimer();
      end = new Date();
      console.log('걸린시간=' + (end - start) + 'ms');
      setTimeout(sendPING, keepalive * 1000);
    }
    // Close the client socket completely
    //client.destroy();
  });

  // Add a 'close' event handler for the client socket
  client.on('close', function () {
    console.log('Connection closed');
  });

  var generateClientID = function () {
    return 'adflow_' + crypto.randomBytes(8).toString('hex');
  };

  var decimalToHex = function (d, padding) {
    var hex = Number(d).toString(16);
    padding = typeof (padding) === "undefined" || padding === null ? padding = 2 : padding;

    while (hex.length < padding) {
      hex = "0" + hex;
    }
    return hex;
  };

  var sendPING = function () {
    console.log('핑을보냅니다.c000');
    client.write(ping);
    start = new Date();
    setTimer();
  };

  var setTimer = function () {
    console.log('타이머를설정합니다..');
    timer = setTimeout(closeClient, keepalive * 1000);
  };

  var resetTimer = function () {
    console.log('타이머를제거합니다.');
    clearTimeout(timer);
  };

  var closeClient = function () {
    console.log('연결을종료합니다.');
    client.destroy();
  };
};

//function socketInit() {
//  for (var i = 0; i < 100; i++) {
//    socketClient();
//  }
//}


//socketClient.prototype

//new socketClient();
module.exports = mqttClient;


