/**
 * User: nadir93
 * Date: 2014. 8. 11.
 * Time: 오후 1:08
 */
var mqtt = require('mqtt');
var util = require('util');
var client = mqtt.createClient(1883, '175.209.8.188');
client.subscribe('/push/test002', {qos: 2}, function (err, granted) {
  if (err) throw err;
  console.log('granted=' + util.inspect(granted));
});

client.on('message', function (topic, message) {


  console.log('topic=' + topic);
  console.log('message='+message);

});

