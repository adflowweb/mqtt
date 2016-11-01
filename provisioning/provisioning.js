/**
 * User: @nadir93
 * Date: 16. 3. 16.
 * Time:
 */
'use strict';
var loglevel = 'debug';

var Logger = require('bunyan'),
    restify = require('restify'),
    log = new Logger.createLogger({
        name: 'provisioning',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    }),
    server = restify.createServer({
        name: 'provisioning',
        version: '0.1.0',
        log: log
    });

var util = require('util');
var schedule = require('node-schedule');
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
    log.error("redis error = " + error);
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

var token = require('./lib/token.js');
var user = require('./lib/user.js');
var mqttbroker = require('./lib/mqttbroker.js');
var connect = require('./lib/connect.js');
var stat = require('./lib/stat.js');
var session = require('./lib/session.js');

server.use(restify.acceptParser(server.acceptable));
server.use(restify.queryParser());
server.use(restify.bodyParser());

server.pre(function(request, response, next) {
    request.log.info({
        req: request
    }, 'REQUEST');
    next();
});

// server.get('/', function(request, response, next) {
//   response.send('It worked!');
//   next();
// });
//
// server.get('/echo/:name', function(req, res, next) {
//   //  res.send(req.params);
//   res.send('process.pid:' + process.pid);
//   return next();
// });

var PATHSTAT = '/stat';

function statHandler(req, res, next) {
    log.debug('req.method=' + req.method);
    log.debug('contentType=' + req.headers['content-type']);

    if (req.headers['content-type'] != 'application/json') {
        return next(new restify.WrongAcceptError('올바른 요청이 아닙니다'))
    }
    stat.statReqCount++;
    stat.get(req, res, next, redis);
}

/*
 *  stat정보가져오기
 */
server.get({
    path: PATHSTAT,
    version: '1.0.0'
}, statHandler);

var PATHSESSION = '/session/:token';

function sessionHandler(req, res, next) {
    log.debug('req.method=' + req.method);
    log.debug('contentType=' + req.headers['content-type']);

    if (req.headers['content-type'] != 'application/json') {
        return next(new restify.WrongAcceptError('올바른 요청이 아닙니다'))
    }

    if (redisAvailable) {
        if (req.method === 'DELETE') {
            session.del(req, res, next, redis);
        }
    } else {
        next(new restify.ServiceUnavailableError("REDIS 서비스를 이용할 수 없습니다"));
    }
}

/*
 *  세션정보삭제하기
 */
server.del({
    path: PATHSESSION,
    version: '1.0.0'
}, sessionHandler);


var PATHCONNECT = '/connect/:phone';

function connectHandler(req, res, next) {
    log.debug('req.method=' + req.method);
    log.debug('contentType=' + req.headers['content-type']);

    if (req.headers['content-type'] != 'application/json') {
        return next(new restify.WrongAcceptError('올바른 요청이 아닙니다'))
    }

    if (redisAvailable) {
        if (req.method === 'GET') {
            stat.connectReqCount++;
            connect.get(req, res, next, redis);
        }
    } else {
        next(new restify.ServiceUnavailableError("REDIS 서비스를 이용할 수 없습니다"));
    }
}

/*
 *  connect정보가져오기
 */
server.get({
    path: PATHCONNECT,
    version: '1.0.0'
}, connectHandler);

var PATHUSER = '/user/:phone';
//server.get({path: PATH, version: '1.1.3'}, sendV1);
//server.get({path: PATH, version: '2.0.0'}, sendV2);

function userHandler(req, res, next) {
    log.debug('req.method=' + req.method);
    log.debug('contentType=' + req.headers['content-type']);

    if (req.headers['content-type'] != 'application/json') {
        return next(new restify.WrongAcceptError('올바른 요청이 아닙니다'))
    }

    if (redisAvailable) {
        if (req.method === 'POST') {
            stat.postUserReqCount++;
            user.post(req, res, next, redis);
        } else if (req.method === 'GET') {
            stat.getUserReqCount++;
            user.get(req, res, next, redis);
        } else if (req.method === 'PUT') {
            stat.putUserReqCount++;
            user.put(req, res, next, redis);
        } else if (req.method === 'DELETE') {
            stat.deleteUserReqCount++;
            user.del(req, res, next, redis);
        }
    } else {
        next(new restify.ServiceUnavailableError("REDIS 서비스를 이용할 수 없습니다"));
    }
}

/*
 *  user정보등록하기
 */
server.post({
    path: PATHUSER,
    version: '1.0.0'
}, userHandler);

/*
 *  user정보변경하기
 */
server.put({
    path: PATHUSER,
    version: '1.0.0'
}, userHandler);

/*
 *  user정보가져오기
 */
server.get({
    path: PATHUSER,
    version: '1.0.0'
}, userHandler);

/*
 *  user정보삭제하기
 */
server.del({
    path: PATHUSER,
    version: '1.0.0'
}, userHandler);

// /*
//  *  user존재유무
//  */
// server.get('/exists/token/:server/:tokenID', function(req, res, next) {
//   if (redisAvailable) {
//     user.exists(req, res, next, redis);
//   } else {
//     next(new restify.ServiceUnavailableError("서비스를이용할수없습니다"));
//   }
// });

var PATHMQTTBROKER = '/mqttbroker/:mqttbrokerid';
//server.get({path: PATH, version: '1.1.3'}, sendV1);
//server.get({path: PATH, version: '2.0.0'}, sendV2);

function mqttbrokerHandler(req, res, next) {
    log.debug('req.method=' + req.method);
    log.debug('contentType=' + req.headers['content-type']);

    if (req.headers['content-type'] != 'application/json') {
        return next(new restify.WrongAcceptError('올바른 요청이 아닙니다'))
    }

    if (redisAvailable) {
        if (req.method === 'POST') {
            stat.postMqttbrokerReqCount++;
            mqttbroker.post(req, res, next, redis);
        } else if (req.method === 'GET') {
            stat.getMqttbrokerReqCount++;
            mqttbroker.get(req, res, next, redis);
        } else if (req.method === 'PUT') {
            stat.putMqttbrokerReqCount++;
            mqttbroker.put(req, res, next, redis);
        } else if (req.method === 'DELETE') {
            stat.deleteMqttbrokerReqCount++;
            mqttbroker.del(req, res, next, redis);
        }
    } else {
        next(new restify.ServiceUnavailableError("REDIS 서비스를 이용할 수 없습니다"));
    }
}

/*
 *  mqttbroker정보등록하기
 */
server.post({
    path: PATHMQTTBROKER,
    version: '1.0.0'
}, mqttbrokerHandler);

/*
 *  mqttbroker정보변경하기
 */
server.put({
    path: PATHMQTTBROKER,
    version: '1.0.0'
}, mqttbrokerHandler);

/*
 *  mqttbroker정보가져오기
 */
server.get({
    path: PATHMQTTBROKER,
    version: '1.0.0'
}, mqttbrokerHandler);

/*
 *  mqttbroker정보삭제하기
 */
server.del({
    path: PATHMQTTBROKER,
    version: '1.0.0'
}, mqttbrokerHandler);



server.post('/token/:server/:tokenID', function(req, res, next) {
    if (redisAvailable) {
        token.post(req, res, next, redis);
    } else {
        next(new restify.ServiceUnavailableError("REDIS 서비스를 이용할 수 없습니다"));
    }
});

/*
 *  토큰정보가져오기
 */
server.get('/token/:server/:tokenID', function(req, res, next) {
    if (redisAvailable) {
        token.get(req, res, next, redis);
    } else {
        next(new restify.ServiceUnavailableError("REDIS 서비스를 이용할 수 없습니다"));
    }
});

/*
 *  토큰정보삭제하기
 */
server.del('/token/:server/:tokenID', function(req, res, next) {
    if (redisAvailable) {
        token.del(req, res, next, redis);
    } else {
        next(new restify.ServiceUnavailableError("REDIS 서비스를 이용할 수 없습니다"));
    }
});

/*
 *  토큰존재유무
 */
server.get('/exists/token/:server/:tokenID', function(req, res, next) {
    if (redisAvailable) {
        token.exists(req, res, next, redis);
    } else {
        next(new restify.ServiceUnavailableError("REDIS 서비스를 이용할 수 없습니다"));
    }
});

// server.on('after', restify.auditLogger({
//     log: log
// //    log: bunyan.createLogger({
// //        name: 'audit',
// //        stream: bunyanCLI.stdin,
// //        level: "debug"
// //    })
// }));

server.on('after', restify.auditLogger({
    log: Logger.createLogger({
        name: 'audit',
        level: loglevel,
        stream: process.stdout
    })
}));

server.on('uncaughtException', function(req, res, route, err) {
    var auditer = restify.auditLogger({
        log: log
    });
    auditer(req, res, route, err);
    res.send(500, "Unexpected error occured");
});

server.listen(8083, function() {
    log.info('%s listening at %s', server.name, server.url);
});
