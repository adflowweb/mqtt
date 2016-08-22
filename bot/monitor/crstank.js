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
        name: 'crstank',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

/**
 * [phantom description]
 * @type {[type]}
 */
var phantom = require('phantom');
var request = require('request');
var fs = require('fs');
//var _ = require('underscore.string');
var _ = require('underscore');
var util = require('util');

/**
 * [requestURL description]
 * @type {String}
 */
var requestURL = 'https://api.thingspeak.com/channels/27833/feeds.json?results=6000&timezone=Asia/Seoul';
var contextRoot = '/home/nadir93/dev/CTS/uploads/crstank/';
/**
 * [imageCallbackUrl description]
 * @type {String}
 */
var imageCallbackUrl = "http://14.63.217.141:38084/crstank/";

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

        if (arg[3] == 'help') {

            msg.send({
                "attachments": [{
                    //"title": "trlog get {전화번호}",
                    "pretext": " *수조온도* ",
                    //"text": "https://www.npmjs.com/package/crstankbot",
                    "fields": [{
                        //"title": "사용법",
                        "value": "nadir93's crs새우항의 온도를 실시간으로 보여준다",
                        "short": false
                    }, {
                        "title": "사용법",
                        "value": "mon tank",
                        "short": false
                    }, {
                        "title": "npm",
                        "value": "https://www.npmjs.com/package/crstankbot",
                        "short": false
                    }, {
                        "title": "github",
                        "value": "https://github.com/nadir93/crstankbot",
                        "short": false
                    }],
                    "image_url": "https://raw.githubusercontent.com/nadir93/crstankbot/master/res/crstankbot.png",
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
            return;
        }

        request(requestURL, function(error, response, body) {
            if (!error && response.statusCode == 200) {
                var feeds = JSON.parse(body).feeds;
                //log.debug('feeds=' + util.inspect(feeds));

                var topdate;
                var toptemp = -1000;
                var bottomdate;
                var bottomtemp = 1000;
                var data = _.filter(feeds, function(obj, index) {
                    //if (index % 60 == 0) {
                    var tmp = Number(obj.field1);
                    if (toptemp < tmp) {
                        toptemp = tmp;
                        topdate = obj.created_at;
                    }

                    if (bottomtemp > tmp) {
                        bottomtemp = tmp;
                        bottomdate = obj.created_at;
                    }

                    if (feeds.length - 1 == index) {
                        return true;
                    }
                    //}
                    return index % 60 == 0;
                });
                //log.debug('data=' + util.inspect(data));
                var result = html.replace(/realdata/g, JSON.stringify(data));

                log.debug('최고온도=' + toptemp);
                log.debug('최고온도일시=' + topdate);
                log.debug('최저온도=' + bottomtemp);
                log.debug('최저온도일시=' + bottomdate);
                // 마커생성
                result = result.replace(/markers:/g, "markers:[{'created_at':new Date('" + topdate + "'),'label':'high : " + toptemp +
                    "'},{'created_at':new Date('" + bottomdate + "'),'label':'low : " + bottomtemp + "'}]");

                var tmp = makeid();
                var tmpFile = tmp + '.html';
                log.debug('임시html=' + tmpFile);
                var tmpImgFile = tmp + '.png';
                log.debug('임시이미지=' + tmpImgFile);
                fs.writeFile(contextRoot + tmpFile, result, 'utf8', function(err) {
                    if (err) {
                        log.error(err);
                        //sendmessage(channel, err.message);
                        msg.send({
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
                                    //sendmessage(channel, '이미지생성=' + status);
                                    msg.send({
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
                                }
                                //msg.reply('이미지생성=' + status);
                                page.render(contextRoot + tmpImgFile, {
                                    format: 'png',
                                    quality: '80'
                                });

                                var fullPath = contextRoot + tmpImgFile;
                                log.debug('fullPath=' + fullPath);

                                var data = {
                                    attachments: [{
                                        //"title": "수조온도그래프",
                                        "pretext": " *수조온도그래프* ",
                                        "fields": [{
                                            "title": "최고온도",
                                            "value": toptemp + '°C',
                                            "short": true
                                        }, {
                                            "title": "최저온도",
                                            "value": bottomtemp + '°C',
                                            "short": true
                                        }],
                                        "image_url": imageCallbackUrl + tmpImgFile,
                                        "mrkdwn_in": ["text", "pretext", "fields"],
                                        "color": "good"
                                    }]
                                };
                                msg.send(data);
                                ph.exit();
                            });
                        });
                    });
                });
            } else {
                if (error) {
                    //sendmessage(channel, error.message);
                    msg.send({
                        "attachments": [{
                            "pretext": "*에러발생*",
                            "fields": [{
                                //"title": "에러발생",
                                "value": error.message,
                                "short": false
                            }],
                            "mrkdwn_in": ["text", "pretext", "fields"],
                            "color": "danger"
                        }]
                    });
                } else {
                    //sendmessage(channel, 'thingspeak responsecode=' + response.statusCode);
                    msg.send({
                        "attachments": [{
                            "pretext": "*에러발생*",
                            "fields": [{
                                //"title": "에러발생",
                                "value": 'thingspeak responsecode = ' + response.statusCode,
                                "short": false
                            }],
                            "mrkdwn_in": ["text", "pretext", "fields"],
                            "color": "danger"
                        }]
                    });
                }
            }
        })
    }
}

/**
 * [html description]
 * @type {String}
 */
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
    "<script>hljs.initHighlightingOnLoad();var val=realdata;val=MG.convert.date(val,'created_at',d3.utcParse(\"%Y-%m-%dT%H:%M:%S%Z\"));val=MG.convert.number(val,'field1');MG.data_graphic({title:'nadir93\\'s crstank temperature',description:'This graphic shows a time-series of downloads.'" +
    ",data:val,width:600,height:200,target:'#ufo-sightings',markers:,min_y_from_data:true,x_accessor:'created_at',y_accessor:'field1'})" +
    "</script></body></html>";

/**
 * [makeid description]
 * @return {[type]} [description]
 */
function makeid() {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (var i = 0; i < 5; i++)
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    return text;
}
