/**
 * User: nadir93
 * Date: 2014. 8. 11.
 * Time: 오후 1:08
 */
var mqtt = require('mqtt');
var client = mqtt.createClient(1883, '175.209.8.188');

setInterval(publishMsg, 10000);

function publishMsg() {
  client.publish('/push/test002', '0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789', {'qos': 2}, function (err) {
    if (err) throw err;
    console.log('messageSend');

  });
}