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
    name: 'stat',
    level: loglevel,
    serializers: {
      req: Logger.stdSerializers.req
    }
  });

var schedule = require('node-schedule');

var requestInterval = 60; //stat 통계 주기 10 초

//monitoring mqttbroker api
schedule.scheduleJob('*/' + requestInterval + ' * * * * *' /* 1분마다 */ , function() {

  log.debug('statReqCount=' + Stat.statReqCount);
  log.debug('connectReqCount=' + Stat.connectReqCount);
  log.debug('postUserReqCount=' + Stat.postUserReqCount);
  log.debug('getUserReqCount=' + Stat.getUserReqCount);
  log.debug('putUserReqCount=' + Stat.putUserReqCount);
  log.debug('deleteUserReqCount=' + Stat.deleteUserReqCount);
  log.debug('postMqttbrokerReqCount=' + Stat.postMqttbrokerReqCount);
  log.debug('getMqttbrokerReqCount=' + Stat.getMqttbrokerReqCount);
  log.debug('putMqttbrokerReqCount=' + Stat.putMqttbrokerReqCount);
  log.debug('deleteMqttbrokerReqCount=' + Stat.deleteMqttbrokerReqCount);

  Stat.oldStatReqCount = Stat.statReqCount;
  Stat.oldConnectReqCount = Stat.connectReqCount;

  Stat.oldPostUserReqCount = Stat.postUserReqCount;
  Stat.oldGetUserReqCount = Stat.getUserReqCount;
  Stat.oldPutUserReqCount = Stat.putUserReqCount;
  Stat.oldDeleteUserReqCount = Stat.deleteUserReqCount;

  Stat.oldPostMqttbrokerReqCount = Stat.postMqttbrokerReqCount;
  Stat.oldGetMqttbrokerReqCount = Stat.getMqttbrokerReqCount;
  Stat.oldPutMqttbrokerReqCount = Stat.putMqttbrokerReqCount;
  Stat.oldDeleteMqttbrokerReqCount = Stat.deleteMqttbrokerReqCount;

  Stat.reset();

});

var Stat = module.exports = {
  statReqCount: 0,
  connectReqCount: 0,
  postUserReqCount: 0,
  getUserReqCount: 0,
  putUserReqCount: 0,
  deleteUserReqCount: 0,
  postMqttbrokerReqCount: 0,
  getMqttbrokerReqCount: 0,
  putMqttbrokerReqCount: 0,
  deleteMqttbrokerReqCount: 0,

  oldStatReqCount: 0,
  oldConnectReqCount: 0,

  oldPostUserReqCount: 0,
  oldGetUserReqCount: 0,
  oldPutUserReqCount: 0,
  oldDeleteUserReqCount: 0,

  oldPostMqttbrokerReqCount: 0,
  oldGetMqttbrokerReqCount: 0,
  oldPutMqttbrokerReqCount: 0,
  oldDeleteMqttbrokerReqCount: 0,

  get: function(req, res, next, client) {
    res.send({
      statReqCount: (Stat.oldStatReqCount != 0) ? (Stat.oldStatReqCount / requestInterval).toFixed(2) : Stat.oldStatReqCount,
      connectReqCount: (Stat.oldConnectReqCount != 0) ? (Stat.oldConnectReqCount / requestInterval).toFixed(2) : Stat.oldConnectReqCount,
      postUserReqCount: (Stat.oldPostUserReqCount != 0) ? (Stat.oldPostUserReqCount / requestInterval).toFixed(2) : Stat.oldPostUserReqCount,
      getUserReqCount: (Stat.oldGetUserReqCount != 0) ? (Stat.oldGetUserReqCount / requestInterval).toFixed(2) : Stat.oldGetUserReqCount,
      putUserReqCount: (Stat.oldPutUserReqCount != 0) ? (Stat.oldPutUserReqCount / requestInterval).toFixed(2) : Stat.oldPutUserReqCount,
      deleteUserReqCount: (Stat.oldDeleteUserReqCount != 0) ? (Stat.oldDeleteUserReqCount / requestInterval).toFixed(2) : Stat.oldDeleteUserReqCount,
      postMqttbrokerReqCount: (Stat.oldPostMqttbrokerReqCount != 0) ? (Stat.oldPostMqttbrokerReqCount / requestInterval).toFixed(2) : Stat.oldPostMqttbrokerReqCount,
      getMqttbrokerReqCount: (Stat.oldGetMqttbrokerReqCount != 0) ? (Stat.oldGetMqttbrokerReqCount / requestInterval).toFixed(2) : Stat.oldGetMqttbrokerReqCount,
      putMqttbrokerReqCount: (Stat.oldPutMqttbrokerReqCount != 0) ? (Stat.oldPutMqttbrokerReqCount / requestInterval).toFixed(2) : Stat.oldPutMqttbrokerReqCount,
      deleteMqttbrokerReqCount: (Stat.oldDeleteMqttbrokerReqCount != 0) ? (Stat.oldDeleteMqttbrokerReqCount / requestInterval).toFixed(2) : Stat.oldDeleteMqttbrokerReqCount
    });
    next();
  },

  reset: function() {
    Stat.statReqCount = 0;
    Stat.connectReqCount = 0;
    Stat.postUserReqCount = 0;
    Stat.getUserReqCount = 0;
    Stat.putUserReqCount = 0;
    Stat.deleteUserReqCount = 0;
    Stat.postMqttbrokerReqCount = 0;
    Stat.getMqttbrokerReqCount = 0;
    Stat.putMqttbrokerReqCount = 0;
    Stat.deleteMqttbrokerReqCount = 0;
  }
}
