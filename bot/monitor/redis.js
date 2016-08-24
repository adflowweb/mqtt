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
        name: 'monitor/redis',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

var phantom = require('phantom');
var fs = require('fs');
var schedule = require('node-schedule');

var WebClient = require('@slack/client').WebClient;
var token = process.env.HUBOT_SLACK_TOKEN;
var web = new WebClient(token);

var contextRoot = '/home/nadir93/dev/CTS/uploads/redis/';
var imageCallbackUrl = "http://14.63.217.141:38084/redis/";

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

var queueSize = 3000;
var timeout = 1000; //이미지 생성(async) 후 업로드하기전 대기시간 ms

var requestInterval = 60; // stat 가져오기 주기

var sendChannel;
var title;
var oldPngFile;
var changedCnt = 0;

var imgFileName;

var mqttbroker = {};

/**
 * 메시지 보내기
 * @param  {[type]} channel [description]
 * @param  {[type]} msg     [description]
 * @param  {[type]} data    [description]
 * @return {[type]}         [description]
 */
function sendMessage(channel, msg, data) {
    web.chat.postMessage(channel, msg, data, function(err, res) {
        if (err) {
            log.error('web.chat.postMessage', err);
        }
        log.debug(res);
    });
}

/**
 * 유니크한 아이디 만들기
 * @return {[type]} [description]
 */
function makeID() {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (var i = 0; i < 5; i++)
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

/**
 * 디렉토리에 파일이 올라오면 슬랙으로 메시지전송(이미지링크와함께)
 * @type {String}
 */
fs.watch(contextRoot, {
    encoding: 'buffer'
}, function(eventType, filename) {
    var taday = new Date();
    if (eventType)
        log.debug('eventType=' + eventType + ' ' + taday);
    if (filename)
        log.debug('filename=' + filename + ' ' + taday);

    log.debug('imgFileName=' + imgFileName);
    log.debug('filename=' + filename);
    if (imgFileName == filename && eventType == 'change') {
        changedCnt++;
    } else {
        changedCnt = 0;
    }
    log.debug('changedCnt=' + changedCnt);
    if (imgFileName == filename && eventType == 'change' && oldPngFile != filename && changedCnt == 1) {
        oldPngFile = filename;
        changedCnt = 0;
        log.debug('filePath=' + contextRoot + filename);
        log.debug('title=' + title);
        log.debug('sendChannel=' + sendChannel);

        var data = {
            attachments: [{
                "pretext": " *" + title + "* ",
                "image_url": imageCallbackUrl + filename,
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        };
        sendMessage(sendChannel, null, data);
    }
});

/**
 * 1분마다 mqttbroker client수 를 가져온다
 */
schedule.scheduleJob('*/1 * * * *', function() {
    getCount();
})

/**
 * [putUser description]
 * @return {[type]} [description]
 */
function getCount() {
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

                if (mqttbroker.hasOwnProperty(a[0])) {
                    var data = {
                        value: a[1],
                        created: formatLocalDate()
                    };
                    //log.debug('data = ' + util.inspect(data));
                    if (mqttbroker[a[0]].length > queueSize) {
                        mqttbroker[a[0]].shift();
                    }
                    mqttbroker[a[0]].push(data);
                    //log.debug('mqttbroker = ' + util.inspect(mqttbroker));
                } else {
                    mqttbroker[a[0]] = [];
                    var data = {
                        value: a[1],
                        created: formatLocalDate()
                    };
                    //log.debug('data = ' + util.inspect(data));
                    mqttbroker[a[0]].push(data);
                    //log.debug('mqttbroker = ' + util.inspect(mqttbroker));
                }
            });
            //log.debug(_.toArray(lists));
            //resMsg += _.toArray(lists);
        });
    } else {
        log.error("REDIS 서비스를 이용할 수 없습니다");
    }


    // var data = {
    //     created: formatLocalDate()
    // };
    //
    // var start = new Date().getTime();
    // var end;
    // var time;
    // //유저수정
    // api.put('/user/apitestuser')
    //     .set('Content-Type', 'application/json')
    //     .set('Accept-Version', '1.0.0')
    //     .send({
    //         token: '11111111111',
    //         mqttbroker: 'mqttbroker:clusterB'
    //     })
    //     .expect(200)
    //     .end(function(err, res) {
    //         end = new Date().getTime();
    //         time = end - start;
    //         if (err) {
    //             log.error(err);
    //             data.result = 0;
    //             data.error = err.message;
    //             data.value = -1;
    //         } else {
    //             data.result = 1;
    //             data.value = time;
    //         }
    //
    //         if (putuserq.length > queueSize) {
    //             putuserq.shift();
    //         }
    //         putuserq.push(data);
    //         //log.debug('putuserq=' + util.inspect(putuserq));
    //         log.debug('putuserq.length=' + putuserq.length);
    //     });
}

/**
 * 날짜생성
 * @return {[type]} [description]
 */
function formatLocalDate() {
    var now = new Date(),
        tzo = -now.getTimezoneOffset(),
        dif = tzo >= 0 ? '+' : '-',
        pad = function(num) {
            var norm = Math.abs(Math.floor(num));
            return (norm < 10 ? '0' : '') + norm;
        };
    return now.getFullYear() +
        '-' + pad(now.getMonth() + 1) +
        '-' + pad(now.getDate()) +
        'T' + pad(now.getHours()) +
        ':' + pad(now.getMinutes()) +
        ':' + pad(now.getSeconds()) +
        dif + pad(tzo / 60) +
        ':' + pad(tzo % 60);
}

/**
 * [exports description]
 * @type {Object}
 */
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
        } else if (arg[3] === 'stat') {
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
        } else {
            buildImage(msg);
        }
    }
}

var all_html = "<html lang='en'><head><link href='./js/sans.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/italic.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/font-awesome.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/bootstrap.min.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/metricsgraphics.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/metricsgraphics-demo.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/highlightjs-default.css' rel='stylesheet' type='text/css'>" +
    "<script src='./js/highlight.pack.js'></script><script src='./js/jquery.min.js'>" +
    "</script><script src='./js/d3.v4.min.js' charset='utf-8'></script>" +
    "<script src='./js/metricsgraphics.js'></script></head><body><div id='ufo-sightings' class='mg-main-area-solid'></div><div class='col-lg-7 text-center legend'></div>" +
    "<script>hljs.initHighlightingOnLoad();var val=realdata;for(var i = 0; i < val.length; i++) {val[i] = MG.convert.date(val[i], 'created', d3.utcFormat(\"%Y-%m-%dT%H:%M:%S%Z\"));};MG.data_graphic({title:,description:'This graphic shows a time-series of downloads.'" +
    ",data:val,width:1000,height:300,target:'#ufo-sightings',missing_is_hidden_accessor: 'dead',missing_is_hidden:true,legend:,legend_target: '.legend',x_accessor:'created',y_accessor:'value'})" +
    "</script></body></html>";

/**
 * 이미지 만들기
 * @param  {[type]} msg [description]
 * @return {[type]}     [description]
 */
function buildImage(msg) {

    var arg = msg.message.text.split(' ');
    log.debug('arg.length = ' + arg.length);
    for (var i = 0; i < arg.length; i++) {
        log.debug('arg[' + i + '] = ' + arg[i]);
    }

    var arg1 = arg[3];
    var arg2 = arg[4];
    log.debug('arg1=' + arg1);
    log.debug('arg2=' + arg2);
    sendChannel = msg.message.user.room;
    log.debug('sendChannel=' + sendChannel);

    //var dataLength;
    var result;
    var markers = "";

    if (arg1 == 'test') {

        if (!mqttbroker.hasOwnProperty('clusterA')) {
            sendMessage(sendChannel, null, {
                "attachments": [{
                    "pretext": "*에러발생*",
                    "fields": [{
                        //"title": "에러발생",
                        "value": '데이타가 없습니다',
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "danger"
                }]
            });
            return;
        }

        var data = [mqttbroker['clusterA'], mqttbroker['clusterB'], mqttbroker['clusterC'], mqttbroker['clusterD']];
        log.debug('data = ' + util.inspect(data));

        title = 'mon redis ' + arg1;
        result = all_html.replace(/legend:/g, "legend: ['clusterA', 'clusterB', 'clusterC', 'clusterD']");
        result = result.replace(/realdata/g, JSON.stringify(data));
        result = result.replace(/title:/g, 'title: \'' + title + '\'');
        //legend: ['post', 'get', 'put', 'delete']
    }

    var tmp = makeID();
    var tmpFile = tmp + '.html';
    log.debug('임시html=' + tmpFile);
    imgFileName = tmp + '.png';
    log.debug('임시이미지=' + imgFileName);
    fs.writeFile(contextRoot + tmpFile, result, 'utf8', function(err) {
        if (err) {
            log.error(err);
            sendMessage(sendChannel, null, {
                "attachments": [{
                    "pretext": "*에러발생*",
                    "fields": [{
                        //"title": "에러발생",
                        "value": err.message,
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "danger"
                }]
            });
            return;
        }

        phantom.create().then(function(ph) {
            ph.createPage().then(function(page) {
                page.invokeAsyncMethod('open', contextRoot + tmpFile).then(function(status) {
                    log.debug('이미지생성=' + status);
                    if (status != 'success') {
                        sendMessage(sendChannel, null, {
                            "attachments": [{
                                "pretext": "*에러발생*",
                                "fields": [{
                                    //"title": "에러발생",
                                    "value": '이미지생성 = ' + status,
                                    "short": false
                                }],
                                "mrkdwn_in": ["text", "pretext", "fields"],
                                "color": "danger"
                            }]
                        });
                        return;
                    }
                    //msg.reply('이미지생성=' + status);
                    page.render(contextRoot + imgFileName, {
                        format: 'png',
                        quality: '70'
                    });
                    ph.exit();
                });
            });
        });
    });
}
