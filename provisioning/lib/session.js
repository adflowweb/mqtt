/**
 * User: @nadir93
 * Date: 16. 7. 25.
 * Time:
 */
"use strict";

var loglevel = 'debug';
var restify = require('restify');
var util = require('util');
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'session',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });
var _ = require('underscore');
var mqtt = require('mqtt');
var fs = require('fs');

var KEY = fs.readFileSync(__dirname + '/tls-key.pem');
var CERT = fs.readFileSync(__dirname + '/tls-cert.pem');
var TRUSTED_CA_LIST = fs.readFileSync(__dirname + '/crt.ca.cg.pem');

var Redis = require('ioredis');
var schedule = require('node-schedule');

var redis = new Redis(6379, '127.0.0.1');

redis.on('connect', function() {
    log.debug('redis connect');
});

redis.on('ready', function() {
    log.debug('redis ready');
});

redis.on('error', function(error) {
    log.debug(error);
});

redis.on('close', function() {
    log.debug('redis close');
});

redis.on('reconnecting', function(event) {
    log.debug('reconnecting event=' + event);
});

redis.on('end', function() {
    log.debug('redis end');
});

var internal_mapping_table;
//= {
///    "211.188.11.85": "150.2.126.85",
//    "211.188.11.89": "150.2.126.89"
//};

schedule.scheduleJob('*/60 * * * * *' /* 60초마다 */ , function() {

    redis.get('mqttbroker:ip:mapping', function(err, reply) {
        if (err) {
            log.error('에러발생 = ' + err);
            return;
        }
        log.debug('mqttbroker:ip:mapping 가져오기. response = ' + reply);
        internal_mapping_table = JSON.parse(reply);
    });
});

module.exports = {
    /*
     * mqttbroker 삭제
     */
    del: function(req, res, next, client) {
        var mqttbrokerinfo = JSON.parse(req.headers.mqttbrokerinfo);
        //mqttbrokerinfo: {"token":"5c5237bfdf2444c68ea101b","phone":"+821021805043",
        //"mqttbroker":["ssl://14.63.217.141:18831","ssl://14.63.217.141:28831"],"created":"2016-08-29T05:31:35.777Z","clearedSubscriptions":"2016-08-30T05:14:00.589Z","modified":"2016-08-30T08:23:20.477Z"}
        log.debug('mqttbrokerinfo.mqttbroker = ' + mqttbrokerinfo.mqttbroker);

        if (!req.params.token) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        } else {
            res.send(200);
            next();
        }

        _.each(mqttbrokerinfo.mqttbroker, function(item, index) {
            log.debug('item = ' + item);
            var tmp = item.substring(item.indexOf("//") + 2).split(':');

            log.debug('internal_mapping_table = ' + internal_mapping_table);
            var host = internal_mapping_table[tmp[0]];
            log.debug('host = ' + host);
            var port = tmp[1];
            log.debug('port = ' + port);
            doClear(req.params.token, host, port);
        });
    }
}

function doClear(token, host, port) {
    log.debug('doClear 시작(token = ' + token + ', host = ' + host + ', port = ' + port + ')');

    //var PORT = 28832;
    //var HOST = '14.63.217.141';

    var options = {
        protocol: 'tls',
        clientId: token, //'a0b285aa4fba490793a80ee',
        port: port,
        host: host,
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
        log.debug(' server = ' + host + ':' + port + '서브스크립션 처리완료(' + token + ')');
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

}
