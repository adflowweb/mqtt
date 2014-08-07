/**
 * User: nadir93
 * Date: 2014. 2. 7.
 * Time: 오후 1:33
 */
var mqtt = require('mqtt');
var index = 0;
var loopCount = 1;
//var client = mqtt.createClient(1883, '192.168.0.21');
var client = mqtt.createClient(1883, '14.63.216.249');
var callbackCnt = 1;
//var client = mqtt.createClient(3881, 'adflow.net');
//var client = mqtt.createClient(1883, 'test.mosquitto.org');

//client.subscribe('/push/poll');
//client.on('message', function (topic, message) {
//  console.log(message);
//});

//setInterval(publish, 1000);

console.log('loopCount=' + loopCount);

setTimeout(publish, 1);

var start = new Date().getTime();

function publish() {


  for (var i = 0; i < loopCount; i++) {
    //console.log('index::' + (index + 1));
    client.publish('/push/test002', (index + 1) + ' 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789', {'qos': 2}, function (err) {
      console.log('callbacked' + (callbackCnt++));
      if ((callbackCnt % 100) == 0) {
        var end = new Date().getTime();
        var time = end - start;
        console.log('걸린시간=' + time);
      }
      if (err) throw err;
    });
    index++;
  }


  //  if (index < loopCount) {
  //    console.log('index::' + (index + 1));
  //    client.publish('/push/test', (index+1)+' 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789', {'qos':2});
  //    //client.publish('/push/poll', '{"id":1,"userid":"nadir93","answerid":'+(index+1)+'}', {'qos': 2});
  //
  //    index++;
  //    setTimeout(publish, 1);
  //  } else {
  //    var end = new Date().getTime();
  //    var time = end - start;
  //    console.log('걸린시간='+time);
  //    console.log('theEnd');
  //    //process.exit(0);
  //  }

}

