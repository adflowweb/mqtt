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
        name: 'provisioning',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

var phantom = require('phantom');
//var request = require('request');
var supertest = require('supertest'),
    api = supertest('http://14.63.217.141:38083');
var async = require('async');

var fs = require('fs');
//var _ = require('underscore.string');
var _ = require('underscore');
var schedule = require('node-schedule');
var util = require('util');
var fs = require('fs');

var WebClient = require('@slack/client').WebClient;
var token = process.env.HUBOT_SLACK_TOKEN;
var web = new WebClient(token);

var contextRoot = '/home/nadir93/dev/CTS/uploads/provisioning/';
var imageCallbackUrl = "http://14.63.217.141:38084/provisioning/";

var postuserq = [];
var getuserq = [];
var putuserq = [];
var deluserq = [];

//var postmqttbrokerq = [];
var getmqttbrokerq = [];
//var putmqttbrokerq = [];
//var delmqttbrokerq = [];

var connectq = [];
var stat = [];
var statReqCountq = [];
var connectReqCountq = [];
var postUserReqCountq = [];
var getUserReqCountq = [];
var putUserReqCountq = [];
var deleteUserReqCountq = [];
var postMqttbrokerReqCountq = [];
var getMqttbrokerReqCountq = [];
var putMqttbrokerReqCountq = [];
var deleteMqttbrokerReqCountq = [];

var queueSize = 3000;
var timeout = 1000; //이미지 생성(async) 후 업로드하기전 대기시간 ms

var requestInterval = 60; // stat 가져오기 주기

var sendChannel;
var title;
var oldPngFile;
var changedCnt = 0;

var imgFileName;

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

    // log.debug(typeof fileName);
    // log.debug('fileName=' + fileName);
    // log.debug(fileName.includes('.png'));

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
 * 1분마다 통계 api를 호출한다
 */
schedule.scheduleJob('*/' + requestInterval + ' * * * * *', function() {
    getStat();
})

/**
 * 1분마다 mqttbroker api를 호출한다
 */
schedule.scheduleJob('*/1 * * * *', function() {
    getMqttbroker();
});

/**
 * 1분마다 user api를 호출한다
 */
schedule.scheduleJob('*/1 * * * *', function() {
    postUser();
    getUser();
    //putUser();
    //deleteUser();
});

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
                    "pretext": " *provisioning api monitoring* ",
                    //"text": "https://www.npmjs.com/package/crstankbot",
                    "fields": [{
                        //"title": "사용법",
                        "value": "provisioning api를 모니터링 합니다",
                        "short": false
                    }, {
                        "title": "사용법",
                        "value": "mon pv { `user` | `mqttbroker` | `userstat` | `mqttbrokerstat` | `stat` }",
                        "short": false
                    }, {
                        "title": "사용예",
                        "value": "mon pv user",
                        "short": false
                    }, {
                        "title": "설명",
                        "value": "*mon pv user* \n1분마다 user api를 호출하여 걸린 응답시간을 ms단위로 보여준다\n\n*provisioning mqttbroker*\n1분마다 mqttbroker api를 호출하여 걸린 응답시간을 ms단위로 보여준다",
                        "short": false
                    }, {
                        "title": "mon pv userstat",
                        "value": "user api의 초당 호출건수를 보여준다",
                        "short": false
                    }, {
                        "title": "mon pv mqttbrokerstat",
                        "value": "mqttbroker api의 초당 호출건수를 보여준다",
                        "short": false
                    }, {
                        "title": "mon pv stat",
                        "value": "connect/stat api의 초당 호출건수를 보여준다",
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }, {
                    //"title": "trlog get {전화번호}",
                    "pretext": " *provisioning api monitoring detail* ",
                    //"text": "https://www.npmjs.com/package/crstankbot",
                    "fields": [{
                        //"title": "사용법",
                        "value": "provisioning api를 상세히 모니터링 합니다",
                        "short": false
                    }, {
                        "title": "사용법",
                        "value": "mon pv { `user` | `mqttbroker` } { `post` | `get` | `put` | `delete` }",
                        "short": false
                    }, {
                        "title": "사용예",
                        "value": "mon pv user get",
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
        } else {
            buildImage(msg);
        }
    }
}

/**
 * [deleteUser description]
 * @return {[type]} [description]
 */
function deleteUser( /*arg3, callback*/ ) {

    var data = {
        created: formatLocalDate()
    };

    var start = new Date().getTime();
    var end;
    var time;
    //유저삭제
    api.del('/user/apitestuser?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .expect(200)
        .end(function(err, res) {
            end = new Date().getTime();
            time = end - start;
            if (err) {
                log.error(err);
                data.result = 0;
                data.error = err.message;
                data.value = -1;
            } else {
                data.result = 1;
                data.value = time;
            }

            if (deluserq.length > queueSize) {
                deluserq.shift();
            }
            deluserq.push(data);
            //log.debug('deluserq=' + util.inspect(deluserq));
            log.debug('deluserq.length=' + deluserq.length);
        });
}

/**
 * [putUser description]
 * @return {[type]} [description]
 */
function putUser( /*arg2, callback*/ ) {

    var data = {
        created: formatLocalDate()
    };

    var start = new Date().getTime();
    var end;
    var time;
    //유저수정
    api.put('/user/apitestuser')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
            token: '11111111111',
            mqttbroker: 'mqttbroker:clusterB'
        })
        .expect(200)
        .end(function(err, res) {
            end = new Date().getTime();
            time = end - start;
            if (err) {
                log.error(err);
                data.result = 0;
                data.error = err.message;
                data.value = -1;
            } else {
                data.result = 1;
                data.value = time;
            }

            if (putuserq.length > queueSize) {
                putuserq.shift();
            }
            putuserq.push(data);
            //log.debug('putuserq=' + util.inspect(putuserq));
            log.debug('putuserq.length=' + putuserq.length);
        });
}

/**
 * [getUser description]
 * @return {[type]} [description]
 */
function getUser( /*arg1, callback*/ ) {

    var data = {
        created: formatLocalDate()
    };

    var start = new Date().getTime();
    var end;
    var time;
    //유저조회
    api.get('/user/apitestuser?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .expect(200)
        .expect(function(res) {
            if (!('phone' in res.body)) throw new Error("missing phone key");
            if (!('token' in res.body)) throw new Error("missing token key");
            if (!('mqttbroker' in res.body)) throw new Error("missing mqttbroker key");
            // token: '0123456789',
            // phone: '01040269329'
        })
        .end(function(err, res) {
            end = new Date().getTime();
            time = end - start;
            if (err) {
                log.error(err);

                data.result = 0;
                data.error = err.message;
                data.value = -1;
            } else {
                data.result = 1;
                data.value = time;
            }
            if (getuserq.length > queueSize) {
                getuserq.shift();
            }
            getuserq.push(data);
            //log.debug('getuserq=' + util.inspect(getuserq));
            log.debug('getuserq.length=' + getuserq.length);
        });
}

/**
 * [postUser description]
 * @return {[type]} [description]
 */
function postUser() {

    var data = {
        created: formatLocalDate()
    };

    var start = new Date().getTime();
    var end;
    var time;

    //유저생성
    api.post('/user/apitestuser')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
            token: '0123456789',
            phone: '01040269329',
            mqttbroker: 'mqttbroker:clusterA'
        })
        .expect(200)
        .end(function(err, res) {
            end = new Date().getTime();
            time = end - start;
            if (err) {
                log.error(err);
                data.result = 0;
                data.error = err.message;
                data.value = -1;
            } else {
                data.result = 1;
                data.value = time;
            }
            if (postuserq.length > queueSize) {
                postuserq.shift();
            }
            postuserq.push(data);
            //log.debug('postuserq=' + util.inspect(postuserq));
            log.debug('postuserq.length=' + postuserq.length);
        });
}

/**
 * [getMqttbroker description]
 * @return {[type]} [description]
 */
function getMqttbroker() {

    var data = {
        created: formatLocalDate()
    };

    var start = new Date().getTime();
    var end;
    var time;

    api.get('/mqttbroker/clusterA?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .expect(200)
        .end(function(err, res) {
            end = new Date().getTime();
            time = end - start;
            if (err) {
                log.error(err);
                data.result = 0;
                data.error = err.message;
                data.value = -1;
            } else {
                data.result = 1;
                data.value = time;
            }

            if (getmqttbrokerq.length > queueSize) {
                getmqttbrokerq.shift();
            }
            getmqttbrokerq.push(data);
            //log.debug('postuserq=' + util.inspect(postuserq));
            log.debug('getmqttbrokerq.length=' + getmqttbrokerq.length);
        });
}

/**
 * [getStat description]
 * @return {[type]} [description]
 */
function getStat() {
    var today = formatLocalDate()
    api.get('/stat')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .expect(200)
        .end(function(err, res) {
            if (err) {
                log.error(err);
                return;
            }

            log.debug('res=' + util.inspect(res.body));

            if (statReqCountq.length > queueSize) {
                statReqCountq.shift();
            }
            statReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.statReqCount)
            });

            // log.debug('statReqCountq.length=' + statReqCountq.length);
            // log.debug('statReqCountq=' + util.inspect(statReqCountq));
            if (connectReqCountq.length > queueSize) {
                connectReqCountq.shift();
            }
            connectReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.connectReqCount)
            });

            // log.debug('connectReqCountq.length=' + connectReqCountq.length);
            // log.debug('connectReqCountq=' + util.inspect(connectReqCountq));
            if (postUserReqCountq.length > queueSize) {
                postUserReqCountq.shift();
            }
            postUserReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.postUserReqCount)
            });

            // log.debug('postUserReqCountq.length=' + postUserReqCountq.length);
            // log.debug('postUserReqCountq=' + util.inspect(postUserReqCountq));
            if (getUserReqCountq.length > queueSize) {
                getUserReqCountq.shift();
            }
            getUserReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.getUserReqCount)
            });

            // log.debug('getUserReqCountq.length=' + getUserReqCountq.length);
            // log.debug('getUserReqCountq=' + util.inspect(getUserReqCountq));
            if (putUserReqCountq.length > queueSize) {
                putUserReqCountq.shift();
            }
            putUserReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.putUserReqCount)
            });

            // log.debug('putUserReqCountq.length=' + putUserReqCountq.length);
            // log.debug('putUserReqCountq=' + util.inspect(putUserReqCountq));
            if (deleteUserReqCountq.length > queueSize) {
                deleteUserReqCountq.shift();
            }
            deleteUserReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.deleteUserReqCount)
            });

            // log.debug('deleteUserReqCountq.length=' + deleteUserReqCountq.length);
            // log.debug('deleteUserReqCountq=' + util.inspect(deleteUserReqCountq));
            if (postMqttbrokerReqCountq.length > queueSize) {
                postMqttbrokerReqCountq.shift();
            }
            postMqttbrokerReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.postMqttbrokerReqCount)
            });

            // log.debug('postMqttbrokerReqCountq.length=' + postMqttbrokerReqCountq.length);
            // log.debug('postMqttbrokerReqCountq=' + util.inspect(postMqttbrokerReqCountq));
            if (getMqttbrokerReqCountq.length > queueSize) {
                getMqttbrokerReqCountq.shift();
            }
            getMqttbrokerReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.getMqttbrokerReqCount)
            });

            // log.debug('getMqttbrokerReqCountq.length=' + getMqttbrokerReqCountq.length);
            // log.debug('getMqttbrokerReqCountq=' + util.inspect(getMqttbrokerReqCountq));
            if (putMqttbrokerReqCountq.length > queueSize) {
                putMqttbrokerReqCountq.shift();
            }
            putMqttbrokerReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.putMqttbrokerReqCount)
            });

            // log.debug('putMqttbrokerReqCountq.length=' + putMqttbrokerReqCountq.length);
            // log.debug('putMqttbrokerReqCountq=' + util.inspect(putMqttbrokerReqCountq));
            if (deleteMqttbrokerReqCountq.length > queueSize) {
                deleteMqttbrokerReqCountq.shift();
            }
            deleteMqttbrokerReqCountq.push({
                created: today,
                result: 1,
                value: Number(res.body.deleteMqttbrokerReqCount)
            });

            // log.debug('deleteMqttbrokerReqCountq.length=' + deleteMqttbrokerReqCountq.length);
            // log.debug('deleteMqttbrokerReqCountq=' + util.inspect(deleteMqttbrokerReqCountq));
        });
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
//,y_label: 'value'
//legend: ['Line 1','Line 2','Line 3'],legend_target: '.legend'
//area:false,missing_is_hidden:true

var html = "<html lang='en'><head><link href='./js/sans.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/italic.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/font-awesome.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/bootstrap.min.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/metricsgraphics.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/metricsgraphics-demo.css' rel='stylesheet' type='text/css'>" +
    "<link href='./js/highlightjs-default.css' rel='stylesheet' type='text/css'>" +
    "<script src='./js/highlight.pack.js'></script><script src='./js/jquery.min.js'>" +
    "</script><script src='./js/d3.v4.min.js' charset='utf-8'></script>" +
    "<script src='./js/metricsgraphics.js'></script></head><body><div id='ufo-sightings' class='mg-main-area-solid'></div>" +
    "<script>hljs.initHighlightingOnLoad();var val=realdata;val=MG.convert.date(val,'created',d3.utcFormat(\"%Y-%m-%dT%H:%M:%S%Z\"));MG.data_graphic({title:,description:'This graphic shows a time-series of downloads.'" +
    ",data:val,width:1000,height:300,target:'#ufo-sightings',markers:,x_accessor:'created',y_accessor:'value'})" +
    "</script></body></html>";
//area:false,

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
 * [makeMarker description]
 * @param  {[type]} data [description]
 * @return {[type]}      [description]
 */
function makeMarker(data) {

    var down = false;
    var markers = "";
    _.each(data, function(element, index) {

        //log.debug('element=' + util.inspect(element));
        //log.debug('index=' + index);
        //log.debug('list=' + list);

        if (element.result == 0 && !down) {
            markers += "{'created':new Date('" + element.created + "'),'label':'down'}";
            down = true;
        } else if (element.result == 1 && down) {
            markers += ",{'created':new Date('" + element.created + "'),'label':'up'},";
            down = false;
        }
    });
    return markers;
}

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

    var data;
    //var dataLength;
    var result;
    var markers = "";

    if (arg2) {
        if (arg1 == 'user') {
            if (arg2 == 'post') {
                data = getuserq;
            } else if (arg2 == 'get') {
                data = getuserq;
            } else if (arg2 == 'put') {
                data = putuserq;
            } else if (arg2 == 'delete') {
                data = deluserq;
            }
        } else if (arg1 == 'mqttbroker') {
            if (arg2 == 'get') {
                data = getmqttbrokerq;
            } else {
                sendMessage(sendChannel, null, {
                    "attachments": [{
                        "pretext": "*에러발생*",
                        "fields": [{
                            //"title": "",
                            "value": '잘못된 요청입니다 ' + msg.message,
                            "short": false
                        }],
                        "mrkdwn_in": ["text", "pretext", "fields"],
                        "color": "danger"
                    }]
                });
                return;
            }
        }

        var cnt = data.length;
        log.debug('data.length=' + cnt);
        if (cnt < 1) {
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
        markers = makeMarker(data);
        title = 'mov pv '+arg1 + ' ' + arg2 + ' api';
        result = html.replace(/realdata/g, JSON.stringify(data));
        result = result.replace(/title:/g, 'title: \'' + title + ' elapsedtime(ms)\'');

        log.debug('markers.length=' + markers.length);
        log.debug('markers=' + markers);
        if (markers.length > 0) {
            result = result.replace(/markers:/g, 'markers:[' + markers + ']');
        } else {
            result = result.replace(/,markers:/g, '');
        }
    } else {
        if (arg1 == 'user') {
            data = [
                postuserq,
                getuserq,
                putuserq,
                deluserq
            ];
            var cnt = postuserq.length + getuserq.length + putuserq.length + deluserq.length;
            log.debug('data.length=' + cnt);
            if (cnt < 1) {
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

            title = 'mon pv '+arg1 + ' api';
            result = all_html.replace(/legend:/g, "legend: ['post', 'get', 'put', 'delete']");
            result = result.replace(/realdata/g, JSON.stringify(data));
            result = result.replace(/title:/g, 'title: \'' + title + ' elapsedtime(ms)\'');
            //legend: ['post', 'get', 'put', 'delete']
        } else if (arg1 == 'stat') {
            data = [
                statReqCountq,
                connectReqCountq
            ];

            var cnt = statReqCountq.length + connectReqCountq.length;
            log.debug('data.length=' + cnt);
            if (cnt < 1) {
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

            title = 'mon pv '+arg1;
            //legend: ['post', 'get', 'put', 'delete']
            result = all_html.replace(/legend:/g, "legend: ['stat', 'connect']");
            result = result.replace(/realdata/g, JSON.stringify(data));
            result = result.replace(/title:/g, 'title: \'' + title + ' (request per second)\'');

        } else if (arg1 == 'userstat') {
            data = [
                postUserReqCountq,
                getUserReqCountq,
                putUserReqCountq,
                deleteUserReqCountq
            ];
            var cnt = postUserReqCountq.length + getUserReqCountq.length + putUserReqCountq.length + deleteUserReqCountq.length;
            log.debug('data.length=' + cnt);
            if (cnt < 1) {
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

            title = 'mon pv '+arg1;
            //legend: ['post', 'get', 'put', 'delete']
            result = all_html.replace(/legend:/g, "legend: ['postUser', 'getUser', 'putUser', 'deleteUser']");
            result = result.replace(/realdata/g, JSON.stringify(data));
            result = result.replace(/title:/g, 'title: \'' + title + ' (request per second)\'');

        } else if (arg1 == 'mqttbrokerstat') {
            data = [
                postMqttbrokerReqCountq,
                getMqttbrokerReqCountq,
                putMqttbrokerReqCountq,
                deleteMqttbrokerReqCountq
            ];

            var cnt = postMqttbrokerReqCountq.length + getMqttbrokerReqCountq.length + putMqttbrokerReqCountq.length + deleteMqttbrokerReqCountq.length;
            log.debug('data.length=' + cnt);
            if (cnt < 1) {
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

            title = 'mon pv '+arg1;
            //legend: ['post', 'get', 'put', 'delete']
            result = all_html.replace(/legend:/g, "legend: ['postMqttbroker', 'getMqttbroker', 'putMqttbroker', 'deleteMqttbroker']");
            result = result.replace(/realdata/g, JSON.stringify(data));
            result = result.replace(/title:/g, 'title: \'' + title + ' (request per second)\'');

        } else if (arg1 == 'mqttbroker') {
            data = getmqttbrokerq;

            var cnt = getmqttbrokerq.length;
            log.debug('data.length=' + cnt);
            if (cnt < 1) {
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

            markers = makeMarker(data);
            title = 'mov pv '+arg1 + ' api';
            result = html.replace(/realdata/g, JSON.stringify(data));
            result = result.replace(/title:/g, 'title: \'' + title + ' elapsedtime(ms)\'');

            log.debug('markers.length=' + markers.length);
            log.debug('markers=' + markers);
            if (markers.length > 0) {
                result = result.replace(/markers:/g, 'markers:[' + markers + ']');
            } else {
                result = result.replace(/,markers:/g, '');
            }
        }
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

                    // // Example when handled through fs.watch listener
                    // fs.watch('./res', {
                    //   encoding: 'buffer'
                    // }, function(eventType, filename){
                    //   if (eventType)
                    //     console.log(eventType);
                    //   // Prints: <Buffer ...>
                    // });

                    // var fullPath = __dirname + '/res/' + tmpImgFile;
                    // log.debug('fullPath=' + fullPath);
                    // //log.debug('dataLength=' + dataLength);
                    // log.debug('timeoutsec=' + timeout /*((dataLength * 0.2) + 300)*/ );
                    // setTimeout(function() {
                    //
                    // }, timeout /*dataLength * 0.2) + 300*/ );
                    // // File upload via file param

                });
            });
        });
    });
}
