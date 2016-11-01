/**
 * User: @nadir93
 * Date: 16. 11. 1.
 */
"use strict";

var loglevel = 'debug';
var Redis = require('ioredis');
var expect = require('chai').expect;

var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'testMqttbrokerIPMapping',
        level: loglevel,
        serializers: {
            req: Logger.stdSerializers.req
        }
    });

var redis = new Redis(6379, '127.0.0.1');

redis.on('connect', function() {
    log.debug('redis connect');
});

redis.on('ready', function() {
    log.debug('redis ready');
});

redis.on('error', function(error) {
    log.debug(error);
});

redis.on('close', function() {
    log.debug('redis close');
});

redis.on('reconnecting', function(event) {
    log.debug('reconnecting event=' + event);
});

redis.on('end', function() {
    log.debug('redis end');
});

describe('mqttbroker:ip:mapping', function() {
    describe('생성', function() {
        it('mqttbroker:ip:mapping 정보 생성', function(done) {
            var mapping = {
                "211.188.11.85": "150.2.126.85",
                "211.188.11.89": "150.2.126.89"
            };
            redis.set('mqttbroker:ip:mapping', JSON.stringify(mapping), function(err, reply) {
                if (err) {
                    return done(err);
                }
                log.debug('mqttbroker:ip:mapping 정보 생성. response = ' + reply);
                expect(reply).to.equal('OK');
                done();
            });
        });
    });

    describe('조회', function() {
        it('mqttbroker:ip:mapping 정보 조회', function(done) {
            redis.get('mqttbroker:ip:mapping', function(err, reply) {
                if (err) {
                    return done(err);
                }
                log.debug('mqttbroker:ip:mapping 정보 조회. response = ' + reply);
                expect(JSON.parse(reply)).to.have.property('211.188.11.89', '150.2.126.89');
                done();
            });
        });
    });
});
