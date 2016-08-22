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
        name: 'log/trlog',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

var _ = require('underscore');
var request = require('supertest');

/**
 * [exports description]
 * @type {Object}
 */
module.exports = {

    /**
     * 요청처리
     * @param  {[type]} msg [description]
     * @return {[type]}     [description]
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
                    "pretext": " *무전기 log 가져오기* ",
                    //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                    "fields": [{
                        //"title": "사용법",
                        "value": "무전기의 트랜잭션로그/에러로그를 서버로 전송한다",
                        "short": false
                    }, {
                        "title": "사용법",
                        "value": "log trlog {`전화번호`}",
                        "short": false
                    }, {
                        "title": "사용예",
                        "value": "```log trlog 821021805043\n>>> TRLog 전송 요청이 완료되었습니다```",
                        "short": false
                    }, {
                        "title": "trlog 저장서버",
                        "value": "dev1",
                        "short": false
                    }, {
                        "title": "저장위치",
                        "value": "/home/nadir93/dev/CTS/uploads/cts/v1/users/+{`전화번호`}",
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
        } else {
            var phone = arg[3];
            log.debug('phone = ' + phone);
            log.debug('sender = rcs/82/200/10002');
            log.debug('receiver = mms/' + phone);
            var resMsg = "";

            request('http://14.63.217.141:3000')
                .put('/v1/system/message')
                .set('X-ApiKey', 'KTPJAAS')
                // .set('md5', '7ae54aaf426a7483e2ae54cc17d9880f')
                // .set('token', 'fffbd697e5354b42a9f6628')
                // .set('user-agent','Android')
                // .set('file', 'app-debug.apk')
                //.set('filename', 'app-debug.apk')
                //.attach('file', __dirname + '/resource/app-debug.apk')
                .send({
                    sender: "rcs/82/200/10002",
                    //receiver: "mms/821029998341",
                    receiver: "mms/" + phone,
                    content: "http://14.63.217.141:38084"
                })
                .expect(200)
                // end handles the response
                .end(function(err, res) {
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

                    //성공
                    resMsg += 'TRLog 전송 요청이 완료되었습니다';
                    msg.send({
                        "attachments": [{
                            "pretext": "*log trlog " + phone + "*",
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
        }
    }
}
