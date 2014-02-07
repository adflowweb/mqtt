/**
 * User: nadir93
 * Date: 2014. 2. 7.
 * Time: 오후 1:32
 */
var mqtt = require('mqtt');
var index = 1;
var clients  = new Array();
var clientCount = 7000;

for(i=0;i<clientCount;i++)
{

    //clients[i] = mqtt.createClient(1883, '192.168.0.21');
    clients[i] = mqtt.createClient(1883, '172.30.1.60');
    //clients[i] = mqtt.createClient(1883, 'test.mosquitto.org');

    clients[i].subscribe('testTopicAdflow');
    clients[i].on('message', function (topic, message) {
        //var j = message.substring(0,1);
        //index = index + parseInt(j);
        console.log('index::'+(index++));
        //console.log(message);
    });

}

console.log('clientsInited::'+clients.length);