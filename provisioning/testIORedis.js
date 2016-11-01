var Redis = require('ioredis');
var redis = new Redis(6379, '127.0.0.1');


redis.on('connect', function() {
  console.log('redis connect');
});

redis.on('ready', function() {
  console.log('redis ready');

  redis.set('foo', 'bar', function(err, result) {
    console.log(result);
  });
  redis.get('foo', function(err, result) {
    console.log(result);
  });

  // Or using a promise if the last argument isn't a function
  redis.get('foo').then(function(result) {
    console.log(result);
  });

  redis.zincrby('mqttbroker', -1, 'clusterA', function(err, reply) {
    if (err) console.log('err=' + err);
    console.log('zincrby reply=' + reply);
  });

  redis.zscore('mqttbroker', 'clusterA', function(err, reply) {
    if (err) console.log('err=' + err);
    console.log('zscore reply=' + reply);
  });

  redis.zrem('mqttbroker', 'clusterA', function(err, reply) {
    if (err) console.log('err=' + err);
    console.log('zrem reply=' + reply);
  });

  redis.disconnect();

});

redis.on('error', function(error) {
  console.log(error);
});

redis.on('close', function() {
  console.log('redis close');
});

redis.on('reconnecting', function(event) {
  console.log('reconnecting event=' + event);
});

redis.on('end', function() {

  console.log('redis end');
});
