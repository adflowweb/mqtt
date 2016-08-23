/**
 * User: @leechanho
 * Date: 16.08.23
 * Time:
 */
"use strict";
var loglevel = 'debug';
var util = require('util');
var Logger = require('bunyan')
    , log = new Logger.createLogger({
        name: 'call/pcs'
        , level: loglevel
        , serializers: {
            req: Logger.stdSerializers.req
        }
    });
var supertest = require('supertest')
    , api = supertest('http://14.63.217.141:3000');
module.exports = {
    process: function (msg) {
        var arg = msg.message.text.split(' ');
        log.debug('arg.length = ' + arg.length);
        for (var i = 0; i < arg.length; i++) {
            log.debug('arg[' + i + '] = ' + arg[i]);
        }
        if (!arg[3]) {
            msg.send({
                "attachments": [
                    {
                        "pretext": "토큰이 있는지 확인한다"
                        , "text": "*사용법*\n call pcs get validate {token_id}\n *사용예제*\n ```call pcs get validate 01d2378ef7b94af7982d779```\n"
                        , "mrkdwn_in": ["text", "pretext"]
        }
                    , {
                        "pretext": "subscription 정보를 확인한다"
                        , "text": "*사용법*\n call pcs get subscriptions {token_id}\n *사용예제*\n ```call pcs get subscriptions 01d2378ef7b94af7982d779```\n"
                        , "mrkdwn_in": ["text", "pretext"]
        }
                    , {
                        "pretext": "ufmi 정보를 확인한다"
                        , "text": "*사용법*\n call pcs get tokens/ufmi {ufmi_id}\n *사용예제*\n ```call pcs get tokens/ufmi 82*40*1234```\n"
                        , "mrkdwn_in": ["text", "pretext"]
        }
                    , {
                        "pretext": "전화번호의 정보를 확인한다"
                        , "text": "*사용법*\n call pcs get tokenMulti {전화번호}\n *사용예제*\n ```call pcs get tokenMulti +821015460101```\n"
                        , "mrkdwn_in": ["text", "pretext"]
        }
    ]
            });
        }
        else {
            if (!arg[4] || !arg[5]) {
                msg.send({
                    "attachments": [
                        {
                            "pretext": "입력 값 이 잘못되었습니다!"
                            , "text": "*사용법을 확인해 주세요!!!!!!!*"
                            , "mrkdwn_in": ["text", "pretext"]
        }

    ]
                });
            }
            else {
                var method = arg[3];
                var url = arg[4];
                var value = arg[5];
                var start = new Date().getTime();
                var end;
                var time;
                log.debug(method);
                log.debug(url);
                log.debug(value);
                api.get('/v1/' + url + '/' + value).set('Content-Type', 'application/json').set('X-ApiKey', 'KTPJAAS').expect(200).end(function (err, res) {
                    end = new Date().getTime();
                    time = end - start;
                    if (err) {
                        log.error(err);
                        msg.send({
                            "attachments": [{
                                "pretext": "*에러발생*"
                                , "fields": [{
                                    //"title": "에러발생",
                                    "value": err.message
                                    , "short": false
                                    }]
                                , "mrkdwn_in": ["text", "pretext", "fields"]
                                , "color": "danger"
                                }]
                        });
                    }
                    log.debug('time = ' + time);
                    log.debug(res.req.socket._httpMessage._header);
                    log.debug('body = ' + res.text);
                    msg.send({
                        "attachments": [{
                            "pretext": " *call pcs " + url + "/" + value
                            , "fields": [{
                                "title": "소요시간"
                                , "value": time + " ms"
                                , "short": false
                                }, {
                                "title": "요청"
                                , "value": res.req.socket._httpMessage._header
                                , "short": false
                                }, {
                                "title": "응답"
                                , "value": res.text
                                , "short": false
                                }]
                            , "mrkdwn_in": ["text", "pretext", "fields"]
                            , "color": "good"
                            }]
                    });
                });
            }
        }
    }
}