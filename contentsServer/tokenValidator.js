/**
 * User: nadir93
 * Date: 15. 6. 3.
 * Time: 오후 6:35
 */
//https 인증스킵
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

var bunyan = require('bunyan'),
    logOptions = {
        name: 'tokenValidator',
        src: false,
        level: "debug",
        serializers: {
            req: bunyan.stdSerializers.req,
            err: bunyan.stdSerializers.err,
            res: bunyan.stdSerializers.res
        }
    };

// Activate this logger only for development and leave the original for production
//if (process.env.NODE_ENV === 'development') {
//    spawn = require('child_process').spawn;
//    bunyanCLI = spawn('bunyan', ['--color'], { stdio: ['pipe', process.stdout] });
//    logOptions.stream = bunyanCLI.stdin;
//}

var log = bunyan.createLogger(logOptions),
    https = require('https'),
    tokenValidator = function () {
    };

tokenValidator.prototype = {
    validate: function (req, cb) {
        var token = req.headers.token;
        //req.url.substring(req.url.lastIndexOf('/') + 1);
        log.debug({token: token});

        log.debug({useragent: req.headers['user-agent']});
        log.debug({value: req.headers['user-agent'].indexOf('Android')});
        //userAgent별로 분기필요 PCS, PMS
        if (req.headers['user-agent'].indexOf('Android') == -1) {
            //for PMS
            options = {
                hostname: '14.63.217.141',
                port: 8081,
                path: '/v1/pms/adm/cmm/auth',
                method: 'GET',
                headers: {
                    'X-Application-Token': token
                }
            };
            log.debug({options: options});
        } else {
            //for PCS
            options = {
                hostname: 'push4.ktp.co.kr',
                port: 8080,
                path: '/v1/validate/' + token,
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'X-ApiKey': 'KTPJAAS'
                }
            }
            log.debug({options: options});
        }

        https.get(options, function (res) {
            var str = '';
            res.on('data', function (chunk) {
                str += chunk;
            });
            res.on('end', function () {
                log.debug({response: str}, '토큰체크응답메시지');
                var obj = JSON.parse(str);
                if (obj.result.data.validation == true) {
                    cb(null, true);
                } else {
                    //토큰이 유효하지 않음
                    cb(null, false);
                }
            });
        }).on('error', function (e) {
            log.error({err: e}, '토큰체크중에러발생');
            cb(e);
        });
//
//        ##  컨텐츠 서버 토큰 인증 API 추가
//        Call
//        ----
//        > *[GET] /v1/pms/adm/cmm/auth*
//        >
//        ex) https://14.63.217.141:8081/v1/pms/adm/cmm/auth
//            >
//        Request Headers
//        *{
//            "X-Application-Token": "b5a95e05b728a5bb91d87d91b328a7b3"
//        }*
//        >
//        Response BodySelect body
//        {"result": {
//            "success": true,
//                "data": {"validation": true}
//        }}


    }//,
//    put: function (req, res, client) {
//        try {
//            logger.debug(srcName + ' req.rawBody : ', req.rawBody);
//            var data = JSON.parse(req.rawBody);
//            var index = [];
//            // build the index
//            for (var x in data) {
//                index.push(x);
//                index.push(data[x]);
//            }
//            logger.debug(srcName + ' index ', index);
//            client.mset(index, function (err, reply) {
//                try {
//                    if (err) {
//                        logger.error('error : ', err);
//                        res.send(err.message, 500);
//                        return;
//                    } else {
//                        logger.debug(srcName + ' key inserted ', reply);
//                        res.send(200);
//                    }
//                } catch (e) {
//                    logger.error(e.stack);
//                    res.send(e.message, 500);
//                }
//            });
//        } catch (e) {
//            logger.error(e.stack);
//            res.send(e.message, 500);
//        }
//    },
//    putHash: function (req, res, client) {
//        try {
//            logger.debug(srcName + ' key : ', req.param.id);
//            logger.debug(srcName + ' req.rawBody : ', req.rawBody);
//            var data = JSON.parse(req.rawBody);
//            var index = [];
//            // build the index
//            for (var x in data) {
//                index.push(x);
//            }
//            logger.debug(srcName + ' index ', index);
//            function hashSet(i) {
//                if (i < index.length) {
//                    client.hset(req.param.id, index[i], data[index[i]], function (err) {
//                        try {
//                            if (err) {
//                                logger.error('error : ', err);
//                                res.send(err.message, 500);
//                                return;
//                            } else {
//                                logger.debug(srcName + ' key inserted ', index[i]);
//                                //res.send(200);
//                            }
//                            hashSet(++i);
//                        } catch (e) {
//                            logger.error(e.stack);
//                            //res.send(e.message, 500);
//                        }
//                    });
//                } else {
//                    //최종 response
//                    logger.debug(srcName + ' hash insert done');
//                    res.send(200);
//                }
//                //logging
//            }
//
//            hashSet(0);
//        } catch (e) {
//            logger.error(e.stack);
//            res.send(e.message, 500);
//        }
//    },
//    delete: function (req, res, client) {
//        try {
//            logger.debug(srcName + ' method : delete ');
//            var key = req.url.substring(req.url.lastIndexOf('/v1/redis') + 10);
//            logger.debug(srcName + ' key : ', key);
//
//            if (key) {
//                client.del(key, function (err, replies) {
//                    try {
//                        if (err) {
//                            logger.error('error : ', err);
//                            res.send(err.message, 500);
//                            return;
//                        } else {
//                            logger.debug(srcName + ' key deleted ', replies);
//                            res.end(replies.toString(), 200);
//                        }
//                    } catch (e) {
//                        logger.error(e.stack);
//                        res.send(e.message, 500);
//                    }
//                });
//            } else {
//                if (req.rawBody) {
//                    //multiDelete
//                    var data = JSON.parse(req.rawBody);
//                    logger.debug(srcName + ' data : ', data);
//
//                    client.del(data, function (err, replies) {
//                        try {
//                            if (err) {
//                                logger.error('error : ', err);
//                                res.send(err.message, 500);
//                                return;
//                            } else {
//                                logger.debug(srcName + ' key deleted ', replies);
//                                res.end(replies.toString(), 200);
//                            }
//                        } catch (e) {
//                            logger.error(e.stack);
//                            res.send(e.message, 500);
//                        }
//                    });
//                } else {
//                    //flushAll
//                    client.flushall(function (err, replies) {
//                        try {
//                            if (err) {
//                                logger.error('error : ', err);
//                                res.send(err.message, 500);
//                                return;
//                            } else {
//                                logger.debug(srcName + ' all key deleted ', replies);
//                                res.end(replies.toString(), 200);
//                            }
//                        } catch (e) {
//                            logger.error(e.stack);
//                            res.send(e.message, 500);
//                        }
//                    });
//                }
//            }
//        } catch (e) {
//            logger.debug(e.stack);
//            res.send(e.message, 500);
//        }
//    },
//    deleteHash: function (req, res, client) {
//        try {
//            logger.debug(srcName + ' method : deleteHash ');
//            var key = req.url.substring(req.url.lastIndexOf('/v1/redis/hash') + 15);
//            logger.debug(srcName + ' key : ', key);
//            client.del(key, function (err, replies) {
//                try {
//                    if (err) {
//                        logger.error('error : ', err);
//                        res.send(err.message, 500);
//                        return;
//                    } else {
//                        logger.debug(srcName + ' key deleted ', replies);
//                        res.end(replies.toString(), 200);
//                    }
//                } catch (e) {
//                    logger.error(e.stack);
//                    res.send(e.message, 500);
//                }
//            });
//        } catch (e) {
//            logger.debug(e.stack);
//            res.send(e.message, 500);
//        }
//    },
//    get: function (req, res, client) {
//        try {
//            logger.debug(srcName + ' method : get ');
//            logger.debug(srcName + ' req.url : ', req.url);
//            var qryStr = require('url').parse(req.url, true).query;
//            logger.debug(srcName + ' queryString : ', utils.inspect(qryStr));
//            var keyType = qryStr.keyType;
//            logger.debug(srcName + ' keyType : ', keyType);
//            var index = [];
//
//            // build the index
//            for (var x in qryStr) {
//                index.push(x);
//            }
//            logger.debug(srcName + ' index : ', index);
//
//            if (index.length) {
//                //exist queryString
//                logger.debug(srcName + ' exist queryString ');
//                if (keyType) {
//                    //exist keyType
//                    if (keyType == 'hash') {
//                        var uri = req.url.substring(req.url.lastIndexOf('/v1/redis') + 10, req.url.indexOf('?'));
//                        logger.debug(srcName + ' uri : ', uri);
//                        if (uri.indexOf('/') > 0) {
//                            //logger.debug(srcName + ' count : ', key.indexOf('/'));
//                            var field = uri.substring(uri.indexOf('/') + 1);
//                            var key = uri.substring(0, uri.indexOf('/'));
//                            logger.debug(srcName + ' key : ', key);
//                            logger.debug(srcName + ' field : ', field);
//                            //hget
//                            client.hget(key, field, function (err, reply) {
//                                try {
//                                    if (err) {
//                                        logger.error(err.stack);
//                                        res.send(err.message, 500);
//                                        return;
//                                    }
//                                    if (reply) {
//                                        logger.debug(srcName + ' reply : ', reply);
//                                        res.send('{"' + field + '":"' + reply + '"}');
//                                    } else {
//                                        logger.debug(srcName + ' not found ');
//                                        res.send(404);
//                                    }
//                                } catch (e) {
//                                    logger.debug(e.stack);
//                                    res.send(e.message, 500);
//                                }
//                            });
//                        } else {
//                            logger.debug(srcName + ' key : ', uri);
//                            //hgetall
//                            client.hgetall(uri, function (err, reply) {
//                                try {
//                                    if (err) {
//                                        logger.error(err.stack);
//                                        res.send(err.message, 500);
//                                        return;
//                                    }
//                                    if (reply) {
//                                        logger.debug(srcName + ' reply : ', reply);
//                                        res.send(reply);
//                                    } else {
//                                        logger.debug(srcName + ' not found ');
//                                        res.send(404);
//                                    }
//                                } catch (e) {
//                                    logger.debug(e.stack);
//                                    res.send(e.message, 500);
//                                }
//                            });
//                        }
//                    } else {
//                        //keyType : default (string)
//                        logger.debug(srcName + ' keyType not hash ');
//                        var key = req.url.substring(req.url.lastIndexOf('/v1/redis') + 10, req.url.indexOf('?'));
//                        logger.debug(srcName + ' key : ', key);
//                        client.get(key, function (err, reply) {
//                            try {
//                                if (err) {
//                                    logger.error(err.stack);
//                                    if (err.message == 'ERR Operation against a key holding the wrong kind of value') {
//                                        res.send('keyType invalid', 400);
//                                        return;
//                                    }
//                                    res.send(err.message, 500);
//                                    return;
//                                }
//                                if (reply) {
//                                    logger.debug(srcName + ' reply : ', reply);
//                                    res.send(reply);
//                                } else {
//                                    logger.debug(srcName + ' not found ');
//                                    res.send(404);
//                                }
//                            } catch (e) {
//                                logger.debug(e.stack);
//                                res.send(e.message, 500);
//                            }
//                        });
//                    }
//                } else {
//                    //nonExist keyType
//                    //default string
//                    logger.debug(srcName + ' nonExist keyType ');
//                    if (qryStr.key == 'all') {
//                        logger.debug(srcName + ' return all keys ');
//                        client.keys("*", function (err, keys) {
//                            logger.debug(srcName + " keys : ", utils.inspect(keys));
//                            if (keys.length != 0) {
//                                var results = [];
//                                keys.forEach(function (key, pos) {
//                                    var count = 0;
//                                    client.type(key, function (err, keytype) {
//                                        logger.debug(srcName + " " + key + " is " + keytype);
//                                        results.push('{' + key + ':' + keytype + '}');
//                                        if (pos === (keys.length - 1)) {
//                                            logger.debug(srcName + " results : ", results);
//                                            res.send(results);
//                                            //client.quit();
//                                        }
//                                    });
//                                });
//                            } else {
//                                res.send(404);
//                            }
//                        });
//                    } else {
//                        //??
//                    }
//                }
//            } else {
//                //nonExist queryString
//                logger.debug(srcName + ' nonExist queryString ');
//                //default string
//                var key = req.url.substring(req.url.lastIndexOf('/v1/redis') + 10);
//                logger.debug(srcName + ' key : ', key);
//
//                if (key == '') {
//                    logger.debug(srcName + ' return all keys ');
//                    client.keys("*", function (err, keys) {
//                        logger.debug(srcName + " keys : ", utils.inspect(keys));
//                        if (keys.length != 0) {
//                            var results = [];
//                            keys.forEach(function (key, pos) {
//                                var count = 0;
//                                client.type(key, function (err, keytype) {
//                                    logger.debug(srcName + " " + key + " is " + keytype);
//                                    results.push('{' + key + ':' + keytype + '}');
//                                    if (pos === (keys.length - 1)) {
//                                        logger.debug(srcName + " results : ", results);
//                                        res.send(results);
//                                        //client.quit();
//                                    }
//                                });
//                            });
//                        } else {
//                            res.send(404);
//                        }
//                    });
//                } else {
//                    client.get(key, function (err, reply) {
//                        try {
//                            if (err) {
//                                if (err.message == 'ERR Operation against a key holding the wrong kind of value') {
//                                    logger.debug(srcName + ' hgetall ');
//                                    //hgetall
//                                    client.hgetall(key, function (err, reply) {
//                                        try {
//                                            if (err) {
//                                                logger.error(err.stack);
//                                                res.send(err.message, 500);
//                                                return;
//                                            }
//                                            if (reply) {
//                                                logger.debug(srcName + ' reply : ', utils.inspect(reply));
//                                                res.send(reply);
//                                            } else {
//                                                logger.debug(srcName + ' not found ');
//                                                res.send(404);
//                                            }
//                                        } catch (e) {
//                                            logger.debug(e.stack);
//                                            res.send(e.message, 500);
//                                        }
//                                    });
//                                    //res.send('keyType missing', 400);
//                                    return;
//                                }
//                                logger.error(err.stack);
//                                res.send(err.message, 500);
//                                return;
//                            }
//                            if (reply) {
//                                logger.debug(srcName + ' reply : ', reply);
//                                res.send(reply);
//                            } else {
//                                logger.debug(srcName + ' not found ');
//                                res.send(404);
//                            }
//                        } catch (e) {
//                            logger.debug(e.stack);
//                            res.send(e.message, 500);
//                        }
//                    });
//                }
//
//
//            }
//        } catch (e) {
//            logger.debug(e.stack);
//            res.send(e.message, 500);
//        }
//    },
//    getHash: function (req, res, client) {
//
//        try {
//            logger.debug(srcName + ' method : getHash ');
//            logger.debug(srcName + ' req.url : ', req.url);
//            var queryStr = require('url').parse(req.url, true).query;
//            logger.debug(srcName + ' queryString : ', utils.inspect(queryStr));
//            logger.debug(srcName + ' keyType : ', req.headers['keytype']);
//
//
//            var key = req.url.substring(req.url.lastIndexOf('/v1/redis/hash/') + 15);
//            logger.debug(srcName + ' key : ', key);
//            client.hgetall(key, function (err, reply) {
//                try {
//                    if (err) {
//                        logger.error(err.stack);
//                        res.send(err.message, 500);
//                        return;
//                    }
//                    if (reply) {
//                        logger.debug(srcName + ' reply : ', reply);
//                        res.send(reply);
//                    } else {
//                        logger.debug(srcName + ' not found ');
//                        res.send(404);
//                    }
//                } catch (e) {
//                    logger.debug(e.stack);
//                    res.send(e.message, 500);
//                }
//            });
//        } catch (e) {
//            logger.debug(e.stack);
//            res.send(e.message, 500);
//        }
//
//
//    },
//    flushAll: function (req, res, client) {
//
//    }
}

module.exports = new tokenValidator();
