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
        name: 'call/provisioning',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

var supertest = require('supertest'),
    api = supertest('http://14.63.217.141:38083');

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
            msg.send({
                "attachments": [{
                    //"title": "trlog get {전화번호}",
                    "pretext": " *Call provisioning API* ",
                    //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                    "fields": [{
                        //"title": "시스템 정보 가져오기",
                        "value": "provisioning api를 호출한다",
                        "short": false
                    }, {
                        "title": "사용법",
                        "value": "call pv {`user` | `mb` | `connect`} {`post` | `get` | `put` | `delete`}",
                        "short": false
                    }, {
                        "title": "약어설명",
                        "value": "`pv` : provisioning\n`mb` : mqttbroker",
                        "short": false
                    }, {
                        "title": "사용예",
                        "value": "```call pv connect\n>>>소요시간\n122 ms\n\n요청\nGET /connect/apitestuser?token=0123456789 HTTP/1.1\n...\n\n응답\n{\"token\":\"0123456789\",\"phone\":\"01040269329\",\"mqttbroker\": ...```",
                        "short": false
                    }, {
                        "title": "사용예",
                        "value": "```call pv mb get\n>>>소요시간\n122 ms\n\n요청\nGET /mqttbroker/clusterA?token=0123456789 HTTP/1.1\n...\n\n응답\n{\"token\":\"0123456789\",\"mqttbroker\": ...```",
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
        } else {
            var arg1 = arg[3];
            log.debug('arg1 = ' + arg1);
            var arg2 = arg[4];
            log.debug('arg2 = ' + arg2);
            if (arg1 === 'connect') {

                var start = new Date().getTime();
                var end;
                var time;
                api.get('/connect/apitestuser?token=0123456789')
                    .set('Content-Type', 'application/json')
                    .set('Accept-Version', '1.0.0')
                    .expect(200)
                    .end(function(err, res) {
                        end = new Date().getTime();
                        time = end - start;
                        if (err) {
                            log.error(err);
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
                        }
                        log.debug('time = ' + time);
                        log.debug(res.req.socket._httpMessage._header);
                        log.debug('body = ' + res.text);
                        msg.send({
                            "attachments": [{
                                //"title": "trlog get {전화번호}",
                                "pretext": " *call pv " + arg1 + "*",
                                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                                "fields": [{
                                    "title": "소요시간",
                                    "value": time + " ms",
                                    "short": false
                                }, {
                                    "title": "요청",
                                    "value": res.req.socket._httpMessage._header,
                                    "short": false
                                }, {
                                    "title": "응답",
                                    "value": res.text,
                                    "short": false
                                }],
                                "mrkdwn_in": ["text", "pretext", "fields"],
                                "color": "good"
                            }]
                        });
                    });
            } else {
                //arg1 = arg1.split(" ");
                //log.debug('arg1[0] = ' + arg1[0]);
                //log.debug('arg1[1] = ' + arg1[1]);
                if (arg1 === 'user') {

                    if (arg2 === 'post') {
                        msg.send({
                            "attachments": [{
                                "pretext": "*에러발생*",
                                "fields": [{
                                    //"title": "에러발생",
                                    "value": '아직 구현되지 않았습니다',
                                    "short": false
                                }],
                                "mrkdwn_in": ["text", "pretext", "fields"],
                                "color": "danger"
                            }]
                        });
                    } else if (arg2 === 'get') {

                        var start = new Date().getTime();
                        var end;
                        var time;
                        //유저조회
                        api.get('/user/apitestuser?token=0123456789')
                            .set('Content-Type', 'application/json')
                            .set('Accept-Version', '1.0.0')
                            .expect(200)
                            .end(function(err, res) {
                                end = new Date().getTime();
                                time = end - start;
                                if (err) {
                                    log.error(err);
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
                                }
                                log.debug('time = ' + time);
                                log.debug(res.req.socket._httpMessage._header);
                                log.debug('body = ' + res.text);
                                msg.send({
                                    "attachments": [{
                                        //"title": "trlog get {전화번호}",
                                        "pretext": " *call pv " + arg1 + " " + arg2 + "*",
                                        //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                                        "fields": [{
                                            "title": "소요시간",
                                            "value": time + " ms",
                                            "short": false
                                        }, {
                                            "title": "요청",
                                            "value": res.req.socket._httpMessage._header,
                                            "short": false
                                        }, {
                                            "title": "응답",
                                            "value": res.text,
                                            "short": false
                                        }],
                                        "mrkdwn_in": ["text", "pretext", "fields"],
                                        "color": "good"
                                    }]
                                });
                            });
                    } else if (arg2 === 'put') {
                        msg.send({
                            "attachments": [{
                                "pretext": "*에러발생*",
                                "fields": [{
                                    //"title": "에러발생",
                                    "value": '아직 구현되지 않았습니다',
                                    "short": false
                                }],
                                "mrkdwn_in": ["text", "pretext", "fields"],
                                "color": "danger"
                            }]
                        });
                    } else {
                        //delete
                        msg.send({
                            "attachments": [{
                                "pretext": "*에러발생*",
                                "fields": [{
                                    //"title": "에러발생",
                                    "value": '아직 구현되지 않았습니다',
                                    "short": false
                                }],
                                "mrkdwn_in": ["text", "pretext", "fields"],
                                "color": "danger"
                            }]
                        });
                    }
                } else {
                    //mqttbroker
                    if (arg2 === 'post') {
                        msg.send({
                            "attachments": [{
                                "pretext": "*에러발생*",
                                "fields": [{
                                    //"title": "에러발생",
                                    "value": '아직 구현되지 않았습니다',
                                    "short": false
                                }],
                                "mrkdwn_in": ["text", "pretext", "fields"],
                                "color": "danger"
                            }]
                        });
                    } else if (arg2 === 'get') {
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
                                }
                                log.debug('time = ' + time);
                                log.debug(res.req.socket._httpMessage._header);
                                log.debug('body = ' + res.text);
                                msg.send({
                                    "attachments": [{
                                        //"title": "trlog get {전화번호}",
                                        "pretext": " *call pv " + arg1 + " " + arg2 + "*",
                                        //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                                        "fields": [{
                                            "title": "소요시간",
                                            "value": time + " ms",
                                            "short": false
                                        }, {
                                            "title": "요청",
                                            "value": res.req.socket._httpMessage._header,
                                            "short": false
                                        }, {
                                            "title": "응답",
                                            "value": res.text,
                                            "short": false
                                        }],
                                        "mrkdwn_in": ["text", "pretext", "fields"],
                                        "color": "good"
                                    }]
                                });
                            });
                    } else if (arg2 === 'put') {
                        msg.send({
                            "attachments": [{
                                "pretext": "*에러발생*",
                                "fields": [{
                                    //"title": "에러발생",
                                    "value": '아직 구현되지 않았습니다',
                                    "short": false
                                }],
                                "mrkdwn_in": ["text", "pretext", "fields"],
                                "color": "danger"
                            }]
                        });
                    } else {
                        //delete
                        msg.send({
                            "attachments": [{
                                "pretext": "*에러발생*",
                                "fields": [{
                                    //"title": "에러발생",
                                    "value": '아직 구현되지 않았습니다',
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
    }
}
