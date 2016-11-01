/**
 * User: @nadir93
 * Date: 16. 9. 1.
 * Time:
 */
"use strict";

var loglevel = 'debug';
var restify = require('restify');
var util = require('util');
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'commandMsgTest',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });
var _ = require('underscore');
var mqtt = require('mqtt');
var fs = require('fs');

var KEY = fs.readFileSync(__dirname + '/lib/tls-key.pem');
var CERT = fs.readFileSync(__dirname + '/lib/tls-cert.pem');
var TRUSTED_CA_LIST = fs.readFileSync(__dirname + '/lib/crt.ca.cg.pem');

var options = {
    protocol: 'tls',
    clientId: '22b58917a5e847699b462fb',
    port: 18832,
    host: '14.63.217.141',
    keyPath: KEY,
    certPath: CERT,
    rejectUnauthorized: false,
    ca: TRUSTED_CA_LIST,
    connectTimeout: 5000,
    reconnectPeriod: 0,
    clean: true
};

var client = mqtt.connect(options);

client.on('connect', function() {
    log.debug('연결 되었습니다');
    //http://14.63.217.141:38084/cts/v1/users/test
    //http://www.google.com
    //
    //mms/821021804709 라져1
    //mms/821021808463 라져2
    //mms/821021805043 라져3
    client.publish('mms/821021804709', '{"msgType":900,"url":"http://14.63.217.141:38084/cts/v1/users/test"}');
    log.debug('메시지가 전송되었습니다');
    //log.debug(' server = ' + host + ':' + port + '서브스크립션 처리완료(' + token + ')');
    client.end();
    log.debug('mqtt세션 종료');
});

client.on('message', function(topic, message) {
    log.debug('message = ' + message.toString());
    // message is Buffer
    //console.log(message.toString());
});

client.on('error', function(e) {
    //next.ifError(err);
    log.error('에러발생 = ' + e);
    client.end();
    log.debug('mqtt세션 종료');
});

client.on('reconnect', function() {
    log.debug('reconnect');
});

client.on('close', function() {
    log.debug('close');
});

client.on('offline', function() {
    //console.log('offline');
    client.end();
    log.debug('offline');
});
