/**
 * User: nadir93
 * Date: 2014. 3. 28.
 * Time: 오전 2:30
 */

var mqtt = require('mqtt')
  , request = require('request')
  , url = require('url')
  , util = require('util')
  , cheerio = require('cheerio')
  , fs = require('fs');
var Iconv = require('iconv').Iconv;
var iconv = new Iconv('EUC-KR', 'UTF-8//TRANSLIT//IGNORE');

var jiNum = 0;
var topic = 'user/df33406434de552faf60efa';
var zodiac = ['(1.20~2.18)', '(2.19~3.20)', '(3.21~4.19)', '(4.20~5.20)', '(5.21~6.21)', '(6.22~7.22)', '(7.23~8.22)', '(8.23~9.23)', '(9.24~10.22)', '(10.23~11.22)', '(11.23~12.24)', '(12.25~1.19)'];
var images = new Array();
var users = {"df33406434de552faf60efa":{"name":"이은영","birthday":"1978.3.8"}};

for (i = 0; i < 12; i++) {
  var data = fs.readFileSync('./pushFortune/resources/star_' + leadingZeros((i + 1), 2) + '.gif');
  var imageString = new Buffer(data).toString('base64');
  images.push(imageString);
}

client = mqtt.createClient(1883, '175.209.8.188');

client.subscribe(topic);

client.on('message', function (topic, message) {
  console.log('topic=' + topic);
  console.log('received=' + message);
});

setInterval(sendMessage, 5000);

function sendMessage() {
  if (jiNum < 12) {
    jiNum++;
  } else {
    jiNum = 1;
  }

  var imageNum = jiNum;
  console.log('imageNum=' + imageNum);
  request({uri: 'http://fortune.daum.net/external/2/star/star_today.asp?sid=' + imageNum, encoding: 'binary'}, function (err, response, body) {
    body = new Buffer(body, 'binary');
    body = iconv.convert(body).toString();
    //console.log('body='+body);
    $ = cheerio.load(body, {
      normalizeWhitespace: true,
      xmlMode: true
    });

    var result = '';
    var cnt = 0;
    var removeCnt = 0;

    $('font').each(function () {
      cnt++;
      if (cnt > 2) {
        var str = $(this).text();
        console.log(str);
        result += str;
      }
    });

    var sendMsg = {"notification": {"notificationStyle": 1, "contentTitle": "오늘의운세" + zodiac[imageNum - 1],
      "contentText": result, "ticker": result,
      "summaryText": "오늘의운세", "image": images[imageNum - 1]
    }};
    console.log('sendMessage=' + JSON.stringify(sendMsg));
    client.publish(topic, JSON.stringify(sendMsg), {'qos': 1});
  });
};

function leadingZeros(n, digits) {
  var zero = '';
  n = n.toString();

  if (n.length < digits) {
    for (var i = 0; i < digits - n.length; i++)
      zero += '0';
  }
  return zero + n;
}


