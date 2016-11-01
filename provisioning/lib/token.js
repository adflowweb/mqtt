/**
 * User: @nadir93
 * Date: 16. 3. 16.
 * Time:
 */

"use strict";

var restify = require('restify');
var util = require('util');
module.exports = {
  /*
   * 토큰 생성
   */
  post: function(req, res, next, client) {
    console.log('req.body:' + util.inspect(req.body));
    //+ JSON.stringify(req.body));
    client.set('token:' + req.params.server + ':' + req.params.tokenID, JSON.stringify(req.body), function(err, reply) {
      next.ifError(err);
      console.log('reply:' + reply);
      res.send({
        message: '토큰이생성되었습니다',
        token: req.params.tokenID
      });
      next();
    });
  },
  /*
   * 토큰 삭제
   */
  del: function(req, res, next, client) {
    client.del('token:' + req.params.server + ':' + req.params.tokenID, function(err, reply) {
      next.ifError(err);
      console.log('reply:' + reply);
      // reply is null if the key doesn't exist
      if (reply === 0) {
        next(new restify.ResourceNotFoundError("토큰이존재하지않습니다"));
      } else {
        res.send({
          message: '토큰이삭제되었습니다',
          token: req.params.tokenID
        });
        next();
      }
    });
  },
  /*
   * 토큰 정보 가져오기
   */
  get: function(req, res, next, client) {
    client.get('token:' + req.params.server + ':' + req.params.tokenID, function(err, reply) {
      next.ifError(err);
      console.log('reply:' + reply);
      // reply is null if the key doesn't exist
      if (reply === null) {
        next(new restify.ResourceNotFoundError("토큰이존재하지않습니다"));
      } else {
        res.send(reply);
        next();
      }
    });
  },
  /*
   * 토큰 존재 유무
   */
  exists: function(req, res, next, client) {
    client.exists('token:' + req.params.server + ':' + req.params.tokenID, function(err, reply) {
      next.ifError(err);
      console.log('reply:' + reply);
      // reply is null if the key doesn't exist
      if (reply === 0) {
        next(new restify.ResourceNotFoundError("토큰이존재하지않습니다"));
      } else {
        res.send({
          message: reply
        });
      }
      next();
    })
  }
};

