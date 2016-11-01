/**
 * User: @nadir93
 * Date: 16. 7. 25.
 * Time:
 */
"use strict";

var loglevel = 'debug';
var _ = require('underscore');
var schedule = require('node-schedule');
var util = require('util');
var redis = require("redis"),
  client = redis.createClient('redis://127.0.0.1:6379');

var Logger = require('bunyan'),
  log = new Logger.createLogger({
    name: 'deleteAllUsers',
    level: loglevel,
    serializers: {
      req: Logger.stdSerializers.req
    }
  });

  client.on("end", function(event) {
    log.info("redis connection end");
  });

  client.on("error", function(err) {
    log.error("redis Error " + err);
  });

  client.on("ready", function(event) {
    log.info("redis ready");
    client.keys("user:*", function(err, users) {
      log.debug('users=' + users);
      client.del(users, function(err, replies) {
        log.debug('replies=' + replies);
         client.quit();
      });
    });
  });

  client.on("connect", function(event) {
    log.info("redis connected");
  });

  client.on("reconnecting", function(event) {
    log.info("redis reconnecting");
  });
