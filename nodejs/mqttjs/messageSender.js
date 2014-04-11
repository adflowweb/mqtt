/**
 * User: nadir93
 * Date: 2014. 2. 7.
 * Time: 오후 1:33
 */
var mqtt = require('mqtt');
var index = 0;
var loopCount = 100;
//var client = mqtt.createClient(1883, '192.168.0.21');
var client = mqtt.createClient(1883, '172.30.1.60');
//var client = mqtt.createClient(1883, 'test.mosquitto.org');

client.subscribe('testTopicAdflow');
client.on('message', function (topic, message) {
    console.log(message);
});

//setInterval(publish, 1000);

console.log('loopCount::'+loopCount);

setTimeout(publish,500);

function publish(){

    if(index < loopCount)
    {
        console.log('index::'+(index+1));
        client.publish('testTopicAdflow', (index+1)+' 0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789', {'qos':2});
        index++;
        setTimeout(publish,500);
    } else {
      console.log('theEnd');
      //process.exit(0);
    }

}

