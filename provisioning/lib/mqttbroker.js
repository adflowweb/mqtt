/**
 * User: @nadir93
 * Date: 16. 7. 25.
 * Time:
 */
"use strict";

var loglevel = 'debug';
var restify = require('restify');
var util = require('util');
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'mqttbroker',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

module.exports = {
    /*
     * mqttbroker 생성
     */
    post: function(req, res, next, client) {
        log.debug('req.body:' + util.inspect(req.body));
        //log.debug('token=' + req.body.token);
        if (!req.body || !req.body.token || !req.body.mqttbroker) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }
        req.body['created'] = new Date();
        client.set('mqttbroker:' + req.params.mqttbrokerid, JSON.stringify(req.body), function(err, reply) {
            next.ifError(err);
            log.debug('mqttbroker ' + req.params.mqttbrokerid + ' 정보 생성 = ' + reply);
            client.zadd('mqttbroker', 0, req.params.mqttbrokerid, function(err, reply) {
                next.ifError(err);
                log.debug('mqttbroker ' + req.params.mqttbrokerid + ' sortedSet 생성 = ' + reply);
                res.send(200);
                // res.send({
                //   message: '정상처리되었습니다',
                //   code: 104200
                // });
                next();
            });
        });
    },
    /*
     * mqttbroker 삭제
     */
    del: function(req, res, next, client) {

        if (!req.params.token) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }

        client.del('mqttbroker:' + req.params.mqttbrokerid, function(err, reply) {
            next.ifError(err);
            log.debug('mqttbroker:' + req.params.mqttbrokerid + ' 삭제 카운트 = ' + reply);
            // reply is null if the key doesn't exist
            if (reply === 0) {
                next(new restify.ResourceNotFoundError('mqttbroker:' + req.params.mqttbrokerid + " 가 존재하지 않습니다"));
            } else {
                client.zrem('mqttbroker', req.params.mqttbrokerid, function(err, reply) {
                    next.ifError(err);
                    log.debug('sortedSet:' + req.params.mqttbrokerid + ' 삭제 카운트 = ' + reply);
                    res.send(200);
                    // res.send({
                    //   message: 'user(' + req.params.userid + ')가삭제되었습니다',
                    //   code: 106200
                    // });
                    next();
                })
            }
        });
    },
    /*
     * mqttbroker 정보 가져오기
     */
    get: function(req, res, next, client) {

        if (!req.params.token) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }

        if (req.params.mqttbrokerid === 'all') {

        } else {
            client.get('mqttbroker:' + req.params.mqttbrokerid, function(err, reply) {
                next.ifError(err);
                log.debug('reply:' + reply);
                // reply is null if the key doesn't exist
                if (reply === null) {
                    next(new restify.ResourceNotFoundError('mqttbroker:' + req.params.mqttbrokerid + " 가 존재하지 않습니다"));
                } else {
                    //res.send(reply);
                    //log.debug('token=' + JSON.parse(reply).token);
                    var data = JSON.parse(reply)
                    client.zscore('mqttbroker', req.params.mqttbrokerid, function(err, reply) {
                        next.ifError(err);
                        log.debug('client=' + reply);
                        data['client'] = reply;
                        log.debug('response=' + JSON.stringify(data));
                        res.send(
                            data
                            //reply
                        );
                        next();
                    });
                }
            });
        }
    },
    /*
     * mqttbroker 정보 번경하기
     */
    put: function(req, res, next, client) {
        if (!req.body.token && !req.body.phone && !req.body.mqttbroker) {
            return next(new restify.InvalidArgumentError("입력 데이터 오류입니다"));
        }

        client.get('mqttbroker:' + req.params.mqttbrokerid, function(err, reply) {
            next.ifError(err);
            log.debug('reply:' + reply);
            // reply is null if the key doesn't exist
            if (reply === null) {
                next(new restify.ResourceNotFoundError('mqttbroker:' + req.params.mqttbrokerid + "가 존재하지 않습니다"));
            } else {
                var data = JSON.parse(reply);
                if (req.body.token) {
                    data.token = req.body.token;
                }

                if (req.body.mqttbroker) {
                    data.mqttbroker = req.body.mqttbroker;
                }

                data['modified'] = new Date();
                log.debug('data:' + JSON.stringify(data));
                client.set('mqttbroker:' + req.params.mqttbrokerid, JSON.stringify(data), function(err, reply) {
                    next.ifError(err);
                    log.debug('reply:' + reply);
                    res.send(200);
                    next();
                });
            }
        });
    },
    /*
     * mqttbroker 존재 유무
     */
    exists: function(req, res, next, client) {
        client.exists('mqttbroker:' + req.params.mqttbrokerid, function(err, reply) {
            next.ifError(err);
            log.debug('reply:' + reply);
            // reply is null if the key doesn't exist
            if (reply === 0) {
                next(new restify.ResourceNotFoundError('mqttbroker:' + req.params.mqttbrokerid + " 가 존재하지 않습니다"));
            } else {
                res.send({
                    message: reply
                });
            }
            next();
        })
    }
};
