/**
 * User: @nadir93
 * Date: 16. 3. 16.
 * Time:
 */
"use strict";

var loglevel = 'debug';
var restify = require('restify');
var util = require('util');
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'user',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });
var request = require('supertest');

module.exports = {
    /*
     * user 생성
     */
    post: function(req, res, next, client) {
        log.debug('req.body:' + util.inspect(req.body));
        //log.debug('token=' + req.body.token);
        if (!req.body || !req.body.token || !req.body.phone || !req.body.mqttbroker) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }
        req.body['created'] = new Date();
        client.set('user:' + req.params.phone, JSON.stringify(req.body), function(err, reply) {
            next.ifError(err);
            log.debug('reply:' + reply);
            res.send(200);
            // res.send({
            //   message: '정상처리되었습니다',
            //   code: 104200
            // });
            next();
        });
    },
    /*
     * user 삭제
     */
    del: function(req, res, next, client) {

        if (!req.params.phone || !req.params.token) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }

        client.get('user:' + req.params.phone, function(err, reply) {
            next.ifError(err);
            log.debug('reply=' + reply);
            var data = JSON.parse(reply);
            // reply is null if the key doesn't exist
            if (reply === null) {
                next(new restify.ResourceNotFoundError(req.params.phone + "가 존재하지 않습니다"));
            } else {
                client.del('user:' + req.params.phone, function(err, reply) {
                    next.ifError(err);
                    log.debug('reply=' + reply);
                    // reply is null if the key doesn't exist
                    if (reply === 0) {
                        next(new restify.ResourceNotFoundError(req.params.phone + "가 존재하지 않습니다"));
                    } else {
                        var broker = data.mqttbroker.substring(data.mqttbroker.indexOf(':') + 1);
                        log.debug('broker=' + broker);
                        client.zincrby('mqttbroker', -1, broker, function(err, reply) {
                            next.ifError(err);
                            log.debug('clientCount=' + reply);
                            res.send(200);
                            next();
                        });
                    }
                });
            }
        });
    },
    /*
     * user 정보 가져오기
     */
    get: function(req, res, next, client) {

        if (!req.params.phone || !req.params.token) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }

        client.get('user:' + req.params.phone, function(err, reply) {
            next.ifError(err);
            log.debug('reply:' + reply);
            // reply is null if the key doesn't exist
            if (reply === null) {
                next(new restify.ResourceNotFoundError(req.params.phone + "가 존재하지 않습니다"));
            } else {
                //res.send(reply);
                //log.debug('token=' + JSON.parse(reply).token);
                res.send(
                    JSON.parse(reply)
                    //reply
                );
                next();
            }
        });
    },
    /*
     * user 정보 번경하기
     */
    put: function(req, res, next, client) {
        if (!req.body.token && !req.body.mqttbroker) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }

        client.get('user:' + req.params.phone, function(err, reply) {
            next.ifError(err);
            log.debug('reply = ' + reply);
            // reply is null if the key doesn't exist
            if (reply === null) {
                next(new restify.ResourceNotFoundError(req.params.phone + "가 존재하지 않습니다"));
            } else {
                var data = JSON.parse(reply);
                var oldMqttbroker = data.mqttbroker;
                log.debug('oldMqttbroker = ' + oldMqttbroker);

                data.mqttbroker = req.body.mqttbroker;

                data['modified'] = new Date().toString();
                log.debug('data:' + util.inspect(data));
                client.set('user:' + req.params.phone, JSON.stringify(data), function(err, reply) {
                    next.ifError(err);
                    log.debug('reply = ' + reply);

                    var broker = oldMqttbroker.substring(oldMqttbroker.indexOf(':') + 1);
                    log.debug('broker = ' + broker);
                    /**
                     * 기존 mqttbroker 카운트 제거
                     * @type {String}
                     */
                    client.zincrby('mqttbroker', -1, broker, function(err, reply) {
                        next.ifError(err);
                        log.debug(broker + ' clientCount = ' + reply);

                        var newBroker = data.mqttbroker.substring(data.mqttbroker.indexOf(':') + 1);
                        log.debug('newBroker = ' + newBroker);
                        /**
                         * mqttbroker 카운트 증가
                         * @type {String}
                         */
                        client.zincrby('mqttbroker', 1, newBroker, function(err, reply) {
                            next.ifError(err);
                            log.debug(newBroker + ' clientCount = ' + reply);
                            var phoneNum = req.params.phone.replace("+", "");
                            log.debug('phoneNum = ' + phoneNum);
                            //request command message
                            request('http://14.63.217.141:3000')
                                .put('/v1/chnchange/message')
                                .set('X-ApiKey', req.body.token)
                                // .set('md5', '7ae54aaf426a7483e2ae54cc17d9880f')
                                // .set('token', 'fffbd697e5354b42a9f6628')
                                // .set('user-agent','Android')
                                // .set('file', 'app-debug.apk')
                                //.set('filename', 'app-debug.apk')
                                //.attach('file', __dirname + '/resource/app-debug.apk')
                                .send({
                                    sender: "rcs/82/200/10002",
                                    //receiver: "mms/821029998341",
                                    receiver: "mms/" + phoneNum,
                                    content: req.body.mqttbroker
                                })
                                .expect(200)
                                // end handles the response
                                .end(function(err, response) {
                                    next.ifError(err);
                                    log.debug('response = ' + response);
                                    res.send(200);
                                    next();
                                });
                        });
                    });
                });
            }
        });
    },
    /*
     * user 존재 유무
     */
    exists: function(req, res, next, client) {
        client.exists('user:' + req.params.phone, function(err, reply) {
            next.ifError(err);
            log.debug('reply:' + reply);
            // reply is null if the key doesn't exist
            if (reply === 0) {
                next(new restify.ResourceNotFoundError(req.params.phone + "가 존재하지 않습니다"));
            } else {
                res.send({
                    message: reply
                });
            }
            next();
        })
    }
};
