/**
 * User: nadir93
 * Date: 2014. 3. 26.
 * Time: 오후 11:16
 */

var mqtt = require('mqtt')
  , request = require('request')
  , url = require('url')
  , util = require('util')
  , cheerio = require('cheerio')
  , fs = require('fs');

var jiNum = -1;
var topic = 'user/nadir93';
var images = new Array();

for (i = 0; i < 11; i++) {
  var data = fs.readFileSync('.resources/icon_12starts_' + leadingZeros((i + 1), 2) + '.gif');
  var imageString = new Buffer(data).toString('base64');
  images.push(imageString);
}

client = mqtt.createClient(1883, '175.209.8.188');

client.subscribe(topic);

client.on('message', function (topic, message) {
  console.log('topic=' + topic);
  console.log('received=' + message);
});

setInterval(sendMessage, 1000);

function sendMessage() {
  if (jiNum < 11) {
    jiNum++;
  } else {
    jiNum = 0;
  }
  //console.log('jiNum=' + jiNum);
  request({uri: 'http://fortune.daum.net/external/6/daum/contents/free/todayjiji.jsp?jiji=' + jiNum}, function (err, response, body) {

    //console.log('body=' + body);
    $ = cheerio.load(body, {
      normalizeWhitespace: true,
      xmlMode: true
    });
    //var result = $('.result_jiji').text();
    //var result = $('td').text();
    //var result2 = $('td');
    var result = '';

    var cnt = 0;
    var removeCnt = 0;
    $('td').each(function () {
      cnt++;
      if (cnt > 5) {
        var str = $(this).text();
        if (str.search('19') == 0) {
          if (str < 1970) {
            removeCnt = cnt + 1;
          } else {
            removeCnt = 0;
            result += str;
          }
        } else if (removeCnt == cnt) {
        } else {
          result += str;
        }

        //if(str.str.match(/ain/g);)
        // console.log(cnt + '=' + $(this).text());
      }
    });


    //console.log(typeof result);
    //console.log('result2='+(util.inspect(result2[0])));
    //console.log('result2='+(util.inspect(result2)));
    //console.log('result2='+util.inspect(result2[0].children));
    //result2 = result.replace(/^\s+|\s+$/g,"");

    var sendMsg = {"notification": {"notificationStyle": 1, "contentTitle": "오늘의운세",
      "contentText": result, "ticker": result,
      "summaryText": "오늘의운세", "image": "images[jiNum]"
    }/*,
     "event": {"title": "오늘의운세", "location": "수림연수원", "desc": result,
     "year": "2014", "month": "2", "day": "22"}    */
    };
    console.log('sendMessage=' + JSON.stringify(sendMsg));
    client.publish(topic, JSON.stringify(sendMsg), {'qos': 1});

    //    request({uri: 'http://image.fortune.daum-img.net/external/6/daum/img/contents/todayjiji/12ji_' + leadingZeros((jiNum + 1), 2) + '.gif'}, function (err, response, gif) {
    //      var gif = new Buffer(gif).toString('base64');
    //      //console.log('gif=' + gif);
    //
    //    });
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


