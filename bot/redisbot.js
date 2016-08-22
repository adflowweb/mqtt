/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'redisbot',
        level: loglevel
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

module.exports = function(robot) {

    robot.respond(/redis get (.*)/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        var key = msg.match[1];
        log.debug('key = ' + key);
        var resMsg = "";
        if (redisAvailable) {
            redis.get(key, function(err, reply) {
                if (err) {
                    log.error('에러발생 = ' + err);
                    resMsg += '' + err;
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
                    return;
                }
                log.debug('reply = ' + reply);
                // reply is null if the key doesn't exist
                if (reply === null) {
                    resMsg += key + " 가 존재하지 않습니다";
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
                } else {
                    var data = JSON.parse(reply);
                    resMsg += JSON.stringify(data);
                    msg.send({
                        "attachments": [{
                            "pretext": "*" + key + "*",
                            "fields": [{
                                //"title": "response",
                                "value": resMsg,
                                "short": false
                            }],
                            "mrkdwn_in": ["text", "pretext", "fields"],
                            "color": "good"
                        }]
                    });
                }
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



        // if (host === os.hostname() || host === 'all') {
        //     var resMsg = "\nhost : " + os.hostname();
        //     // var load = _.map(os.loadavg(), function(num) {
        //     //   return num.toFixed(2);
        //     // });
        //     // resMsg += "\nload : " + load;
        //     resMsg += cpu_used();
        //     resMsg += "\nmemory : " + bToS(os.freemem()) + " free of " + bToS(os.totalmem());
        //
        //     var child = require('child_process');
        //     child.exec('uptime', function(error, stdout, stderr) {
        //         //console.log(stdout);
        //         resMsg += "\nuptime : " + stdout;
        //         msg.reply(resMsg);
        //         log.debug('response', {
        //             message: resMsg
        //         });
        //     });
        //
        //     // get disk usage. Takes mount point as first parameter
        //     disk.check('/', function(err, info) {
        //         log.debug('disk available=' + bytesToSize(info.available));
        //         log.debug('disk free=' + bytesToSize(info.free));
        //         log.debug('disk total=' + bytesToSize(info.total));
        //
        //         resMsg += "\ndisk : \"/\" " + bToS(info.free) + " free of " + bToS(info.total);
        //     });
        // }
    });

    robot.respond(/redis$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        //var resMsg = "redis get [key] \nex) redis get user:+821021804709\n --> {\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":\"mqttbroker:clusterB\",\"created\":\"2016-08-17T02:28:23.621Z\",\"clearedSubscriptions\":\"2016-08-17T02:29:00.348Z\"}\n\nex) redis get mqttbroker:clusterA\n --> {\"token\":\"0123456789\",\"mqttbroker\":[\"ssl://14.63.217.141:18831\",\"ssl://14.63.217.141:28831\"],\"created\":\"2016-08-17T01:38:27.142Z\"}\n\nredis stat\n\n ex) redis stat\n --> clusterB,1,clusterA,1,clusterD,0,clusterC,0";
        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                "pretext": " *redis query* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "",
                    "value": "redis에서 데이타를 가져온다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "redis get {`키`}",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```redis get user:+821021804709\n>>> {\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":\"mqttbroker:clusterB\",\"created\":\"2016-08-17T02:28:23.621Z\",\"clearedSubscriptions\":\"2016-08-17T02:29:00.348Z\"}\n\nredis get mqttbroker:clusterA\n>>> {\"token\":\"0123456789\",\"mqttbroker\":[\"ssl://14.63.217.141:18831\",\"ssl://14.63.217.141:28831\"],\"created\":\"2016-08-17T01:38:27.142Z\"}```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
    });
}
