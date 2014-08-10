/**
 * User: nadir93
 * Date: 2014. 8. 7.
 * Time: 오후 6:10
 */
var net = require('net');
var crypto = require('crypto');
var util = require('util');
var EventEmitter = require('events').EventEmitter;

//var HOST = '14.63.216.254';
//var HOST = '172.30.1.39';
var HOST = '175.209.8.188';
var PORT = 1883;
var keepalive = 15;
var ping = new Buffer('c000',
  'hex').toString('binary');

process.on('uncaughtException', function (err) {
  console.log('에러발생: ' + err);
});

//for (var i = 0; i < 10; i++) {
//  setTimeout(socketInit, i * 10000);
//}

function MqttClient() {
  //console.log('MqttClient생성자시작');
  this.start = 0;
  this.end = 0;
  this.timer = null;
  this.clientID;
  this.client = new net.Socket();

  this.client.on('data', this.emit.bind(this, 'data'));
  this.client.on('close', this.emit.bind(this, 'close'));
  //console.log('MqttClient생성자종료');
}

util.inherits(MqttClient, EventEmitter);

MqttClient.prototype.connect = function () {
  //console.log('connect시작.');
  //console.log('this=' + util.inspect(this));
  var that = this;
  this.client.connect(PORT, HOST, function () {
    //console.log('connect콜백시작');
    //console.log('소켓이연결되었습니다.' + HOST + ':' + PORT);
    //var keepaliveHex = keepalive.toString(16);

    //console.log('keepalive=' + keepalive + '초');
    var connectHexStr = '102500064d51497364700302' + decimalToHex(keepalive, 4) + '0017';
    //console.log('connectHexString=' + connectHexStr);
    that.clientID = generateClientID();
    //console.log('clientID=' + that.clientID);
    var cliendIDHex = new Buffer(that.clientID).toString('hex');
    var data = new Buffer(connectHexStr + cliendIDHex,
      'hex').toString('binary');
    //console.log('data=' + data.toString('hex'));
    //console.log('client=' + util.inspect(this.address()));
    //console.log('that=' + util.inspect(that));


    // console.log('socket=' + util.inspect(that.client.address()));
    that.client.write(data);
    //console.log('connect콜백종료');
  });
  //console.log('connect종료.');
};


MqttClient.prototype.receivedData = function (data) {
  //console.log('receiveData시작');
  var dataHex = new Buffer(data, 'binary').toString('hex');

  //console.log('RECEIVED=' + dataHex);
  //console.log('socket=' + util.inspect(this.client.address()));

  if (dataHex == '20020000') {
    //console.log('mqtt브로커연결에성공하였습니다.');
    this.sendPING();
    //console.log('start=' + this.start);
  } else if (dataHex == 'd000') {
    //console.log('핑을받았습니다.');
    this.resetTimer();
    this.end = new Date();

    var elapsedTime = this.end - this.start;
    //console.log('걸린시간=' + elapsedTime + 'ms');

    global.adflow.avg = global.adflow.avg + elapsedTime;

    if (global.adflow.max < elapsedTime) {
      global.adflow.max = elapsedTime;
    }

    if (global.adflow.min > elapsedTime) {
      global.adflow.min = elapsedTime;
    }

    global.adflow.pingCount++;

    //setTimeout(this.sendPING, keepalive * 1000);
    var that = this;
    setTimeout(function () {
      //console.log('핑을보냅니다.c000');
      //console.log('client=' + that.clientID);
      that.client.write(ping);
      that.start = new Date();
      that.setTimer();
    }, keepalive * 1000);
  }
  // Close the client socket completely
  //client.destroy();

  //console.log('receiveData종료');
};

MqttClient.prototype.sendPING = function () {
  //console.log('sendPING시작');
  //console.log('client=' + this.clientID);
  this.client.write(ping);
  this.start = new Date();
  this.setTimer();
  //console.log('sendPING종료');
};

MqttClient.prototype.setTimer = function () {
  // console.log('setTimer시작');
  var that = this;
  this.timer = setTimeout(function () {
    console.log('destroy시작');
    that.client.destroy();
    console.log('destroy종료');
  }, keepalive * 1000);
  // console.log('setTimer종료');
};

MqttClient.prototype.resetTimer = function () {
  //console.log('resetTimer시작');
  clearTimeout(this.timer);
  //console.log('resetTimer종료');
};

MqttClient.prototype.close = function () {
  console.log('연결이종료됩니댜.');
};

//function socketInit() {
//  for (var i = 0; i < 100; i++) {
//    socketClient();
//  }
//}


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


//socketClient.prototype

//new socketClient();
module.exports = MqttClient;



