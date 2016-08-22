/**
 * User: @nadir93
 * Date: 16. 3. 16.
 * Time:
 */
"use strict";

var loglevel = 'debug';
var util = require('util');
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'redis',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

var _ = require('underscore');
var Redis = require('ioredis');
var redis = new Redis(6379, '127.0.0.1');

redis.on('connect', function() {
    log.debug('redis connect');
});

redis.on('ready', function() {
    redisAvailable = true;
    log.info("redis ready");
});

redis.on('error', function(error) {
    log.error("redis error = " + err);
});

redis.on('close', function() {
    log.debug('redis close');
});

redis.on('reconnecting', function(event) {
    log.info('reconnecting event=' + event);
});

redis.on('end', function() {
    redisAvailable = false;
    log.info("redis connection end");
});

var redisAvailable = false;

module.exports = {

    /*
     * 요청처리
     */
    process: function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        var arg = msg.message.text.split(' ');
        log.debug('arg.length = ' + arg.length);
        for (var i = 0; i < arg.length; i++) {
            log.debug('arg[' + i + '] = ' + arg[i]);
        }
        if (!arg[3]) {
            //var resMsg = "redis get [key] \nex) redis get user:+821021804709\n --> {\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":\"mqttbroker:clusterB\",\"created\":\"2016-08-17T02:28:23.621Z\",\"clearedSubscriptions\":\"2016-08-17T02:29:00.348Z\"}\n\nex) redis get mqttbroker:clusterA\n --> {\"token\":\"0123456789\",\"mqttbroker\":[\"ssl://14.63.217.141:18831\",\"ssl://14.63.217.141:28831\"],\"created\":\"2016-08-17T01:38:27.142Z\"}\n\nredis stat\n\n ex) redis stat\n --> clusterB,1,clusterA,1,clusterD,0,clusterC,0";
            msg.send({
                "attachments": [{
                    //"title": "trlog get {전화번호}",
                    "pretext": " *mqttbrokerCluster 사용자 현황* ",
                    //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                    "fields": [{
                        //"title": "mqttbrokerCluster 통계",
                        "value": "현재 mqttbrokerCluster를 사용하는 사용자수를 가져온다",
                        "short": false
                    }, {
                        "title": "사용법",
                        "value": "mon redis stat",
                        "short": false
                    }, {
                        "title": "사용예",
                        "value": "```mon redis stat\n>>> clusterB,1,clusterA,1,clusterD,0,clusterC,0```",
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
        } else if(arg[3] === 'stat'){
            var resMsg = "";
            if (redisAvailable) {
                redis.zrevrange('mqttbroker', 0, -1, 'withscores', function(err, members) {
                    log.debug(members);
                    // the resulting members would be something like
                    // ['userb', '5', 'userc', '3', 'usera', '1']
                    // use the following trick to convert to
                    // [ [ 'userb', '5' ], [ 'userc', '3' ], [ 'usera', '1' ] ]
                    // learned the trick from
                    // http://stackoverflow.com/questions/8566667/split-javascript-array-in-chunks-using-underscore-js
                    var lists = _.groupBy(members, function(a, b) {
                        return Math.floor(b / 2);
                    });

                    _.each(lists, function(a, b) {
                        log.debug('a = ' + a);
                        log.debug('b = ' + b);
                        resMsg += "*" + a[0] + "* = " + a[1] + "\n";
                    });

                    //log.debug(_.toArray(lists));
                    //resMsg += _.toArray(lists);
                    msg.send({
                        "attachments": [{
                            "fallback": "mqttbrokerCluster 사용자 현황",
                            "pretext": "*mqttbrokerCluster 사용자 현황*",
                            "fields": [{
                                //"title": "response",
                                "value": resMsg,
                                "short": false
                            }],
                            "mrkdwn_in": ["text", "pretext", "fields"],
                            "color": "good"
                        }]
                    });
                });
            } else {
                resMsg += "REDIS 서비스를 이용할 수 없습니다";
                msg.send({
                    "attachments": [{
                        "pretext": "*에러발생*",
                        "fields": [{
                            //"title": "에러발생",
                            "value": resMsg,
                            "short": false
                        }],
                        "mrkdwn_in": ["text", "pretext", "fields"],
                        "color": "danger"
                    }]
                });
            }
        }
    }

}
