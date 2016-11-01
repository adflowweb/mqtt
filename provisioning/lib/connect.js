/**
 * User: @nadir93
 * Date: 16. 7. 25.
 * Time:
 */
"use strict";

var loglevel = 'debug';
var _ = require('underscore');
var schedule = require('node-schedule');
var restify = require('restify');
var util = require('util');
var Redis = require('ioredis');
//var redis = require("redis"),
//  redisClient = redis.createClient('redis://127.0.0.1:6379');

var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'connect',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

var clusters = {};

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

schedule.scheduleJob('*/60 * * * * *' /* 60초마다 */ , function() {

    redis.keys("mqttbroker:cluster*", function(err, brokers) {
        log.debug('brokers=' + brokers);
        //log.debug('brokers=' + brokers[1]);
        // NOTE: code in this callback is NOT atomic
        // this only happens after the the .exec call finishes.
        redis.mget(brokers, function(err, replies) {
            //log.debug('replies=' + replies);
            for (var i in replies) {
                if (!clusters.hasOwnProperty(replies[i])) {
                    clusters[brokers[i]] = JSON.parse(replies[i]);
                }
            }
            log.debug('clusters=' + JSON.stringify(clusters));
        });
    })
});

module.exports = {
    /*
     * 연결 정보 가져오기
     */
    get: function(req, res, next, client) {
        //log.debug('req.body:' + util.inspect(req.body));
        log.debug('token=' + req.params.token);
        log.debug('phone=' + req.params.phone);
        if (!req.params.token || !req.params.phone) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }

        client.get('user:' + req.params.phone, function(err, reply) {
            next.ifError(err);
            log.debug('reply=' + reply);
            // reply is null if the key doesn't exist
            if (reply === null) {
                // next(new restify.ResourceNotFoundError(req.params.userid + "가존재하지않습니다"));
                // 존재하지 않을 경우 broker및 user 를 새로 저장한다.
                var data = {};
                data['token'] = req.params.token;
                data['phone'] = req.params.phone;
                //data['mqttbroker'] = ["ssl://192.168.0.1:1883", "ssl://192.168.0.2:1883"];

                // select broker
                client.zrange('mqttbroker', 0, /*-1*/ 0, /*'withscores',*/ function(err, members) {
                    log.debug('clusterName=' + members);

                    data['mqttbroker'] = 'mqttbroker:' + members[0]; //clusters[members[0]].mqttbroker;
                    data['created'] = new Date().toString();

                    if (!clusters[data['mqttbroker']] || !clusters[data['mqttbroker']].mqttbroker) {
                        return next(new restify.ResourceNotFoundError('mqttbroker:' + members[0] + " 정보가 없습니다"));
                    }

                    log.debug('사용자 생성=' + JSON.stringify(data));
                    client.set('user:' + req.params.phone, JSON.stringify(data), function(err, reply) {
                        next.ifError(err);
                        log.debug('reply=' + reply);
                        data['mqttbroker'] = clusters['mqttbroker:' + members[0]].mqttbroker;
                        // increase client count
                        client.zincrby('mqttbroker', 1, members[0], function(err, reply) {
                            next.ifError(err);
                            log.debug('clientCount=' + reply);
                            log.debug('response=' + JSON.stringify(data));
                            res.send(data);
                            // res.send({
                            //   message: '정상처리되었습니다',
                            //   code: 104200
                            // });
                            next();
                        });
                    });
                    // the resulting members would be something like
                    // ['userb', '5', 'userc', '3', 'usera', '1']
                    // use the following trick to convert to
                    // [ [ 'userb', '5' ], [ 'userc', '3' ], [ 'usera', '1' ] ]
                    // learned the trick from
                    // http://stackoverflow.com/questions/8566667/split-javascript-array-in-chunks-using-underscore-js
                    // var lists=_.groupBy(members, function(a,b) {
                    //     return Math.floor(b/2);
                    // });
                    // console.log( _.toArray(lists) );
                });
            } else {
                log.debug('response=' + reply);
                // 존재할 경우 해당 cluster 의 실제 주소를 가져와 메시지를 merge해서 전송
                var data = JSON.parse(reply);
                if (!clusters[data['mqttbroker']] || !clusters[data['mqttbroker']].mqttbroker) {
                    return next(new restify.ResourceNotFoundError(data['mqttbroker'] + " 정보가 없습니다"));
                }
                data['mqttbroker'] = clusters[data['mqttbroker']].mqttbroker;
                res.send(data);
                next();
            }
        });
    },
    /*
     * user 존재 유무
     */
    exists: function(req, res, next, client) {
        client.exists('user:' + req.params.userid, function(err, reply) {
            next.ifError(err);
            log.debug('reply:' + reply);
            // reply is null if the key doesn't exist
            if (reply === 0) {
                next(new restify.ResourceNotFoundError(req.params.userid + "가 존재하지 않습니다"));
            } else {
                res.send({
                    message: reply
                });
            }
            next();
        });
    }
};
