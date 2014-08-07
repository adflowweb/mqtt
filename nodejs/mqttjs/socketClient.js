/**
 * User: nadir93
 * Date: 2014. 8. 7.
 * Time: 오전 9:54
 */
var net = require('net');
var crypto = require('crypto');
var util = require('util');

//var HOST = '14.63.216.254';
//var HOST = '172.30.1.39';
var HOST = '175.209.8.188';
var PORT = 1883;
var keepalive = 30;
var ping = new Buffer('c000',
  'hex').toString('binary');

//process.on('uncaughtException', function (err) {
//  console.log('에러발생: ' + err);
//});

//for (var i = 0; i < 1; i++) {
//  setTimeout(socketInit, i * 1000);
//}


function socketClient() {
  this.start = 0;
  this.end = 0;
  this.timer = null;
  this.clientID = "";
  this.sock = new net.Socket();
  that = this;


  // Add a 'data' event handler for the client socket
  // data is what the server sent to this socket
  this.sock.on('data', function (data) {
    var dataHex = new Buffer(data, 'binary').toString('hex');
    console.log('DATA: ' + dataHex);

    if (dataHex == '20020000') {
      console.log('mqtt브로커연결에성공하였습니다.');
      that.sendPING();
      console.log('start=' + start);
    } else if (dataHex == 'd000') {
      console.log('핑을받았습니다.');
      that.resetTimer();
      end = new Date();
      console.log('걸린시간=' + (end - start) + 'ms');
      setTimeout(that.sendPING, keepalive * 1000);
    }
    // Close the client socket completely
    //client.destroy();
  });

  // Add a 'close' event handler for the client socket
  this.sock.on('close', function () {
    console.log('Connection closed');
  });


  this.sendPING = function () {
    console.log('핑을보냅니다.c000');
    that.sock.write(ping);
    that.start = new Date();
    that.setTimer();
  };

  this.setTimer = function () {
    console.log('타이머를설정합니다..');
    that.timer = setTimeout(that.closeClient, keepalive * 1000);
  };

  this.resetTimer = function () {
    console.log('타이머를제거합니다.');
    clearTimeout(that.timer);
  };

  this.closeClient = function () {
    console.log('연결을종료합니다.');
    that.sock.destroy();
  };
};

socketClient.prototype.connect = function () {
  this.sock.connect(PORT, HOST, function () {
    console.log('CONNECTED TO: ' + HOST + ':' + PORT);
    //var keepaliveHex = keepalive.toString(16);
    console.log('keepalive=' + keepalive + '초');
    var hex1 = '102500064d5149736470030200' + decimalToHex(keepalive) + '0017';
    clientID = generateClientID();
    console.log('clientID=' + clientID);
    var cliendIDHex = new Buffer(clientID).toString('hex');
    var data = new Buffer(hex1 + cliendIDHex,
      'hex').toString('binary');

    //console.log('that='+that);
    //console.log('that.sock=' + util.inspect(that.sock));
    console.log('addr=' + util.inspect(that.sock.address()));

    that.sock.write(data);
  });
};

var decimalToHex = function (d, padding) {
  var hex = Number(d).toString(16);
  padding = typeof (padding) === "undefined" || padding === null ? padding = 2 : padding;

  while (hex.length < padding) {
    hex = "0" + hex;
  }
  return hex;
};

var generateClientID = function () {
  return 'adflow_' + crypto.randomBytes(8).toString('hex');
};

//function socketInit() {
//  for (var i = 0; i < 2; i++) {
//    new socketClient().connect();
//  }
//}


//socketClient.prototype

//new socketClient();
module.exports = socketClient;

