/**
 * User: @nadir93
 * Date: 2014. 3. 28.
 * Time: 오전 2:30
 *
 * 별자리운세 push service
 */
var mqtt = require('mqtt')
  , request = require('request')
  , url = require('url')
  , util = require('util')
  , cheerio = require('cheerio')
  , fs = require('fs'), Iconv = require('iconv').Iconv
  , iconv = new Iconv('EUC-KR', 'UTF-8//TRANSLIT//IGNORE');

var serverIP = '127.0.0.1';
var port = 1883;
//추후 개인데이타는 암호화 하던가 또는 파일이나 디비에서 가져오도록..
var users = {"ba54e5ed3b8aace5e1ce833": {"name": "이찬호", "birth": {"year": 1982, "month": 10, "day": 20}},
  "df33406434de552faf60efa": {"name": "이은영", "birth": {"year": 1978, "month": 3, "day": 8}},
  "1c45de7cc1daa896bfd32dc": {"name": "박택영", "birth": {"year": 1974, "month": 12, "day": 27}}};
//알람시간은 오늘날짜를 구하고 오전7시 이전이면 그냥 세팅, 오늘 7시 이후이면 다음날 7시로 세팅
var start = new Date();
start.setHours(07);
start.setMinutes(0);
start.setSeconds(0);
var images = [];

//푸시에사용할 별자리이미지로딩...
for (var i = 0; i < 12; i++) {
  var data = fs.readFileSync('./nodejs/pushFortune/resources/star_' + leadingZeros((i + 1), 2) + '.gif');
  var imageString = new Buffer(data).toString('base64');
  images.push(imageString);
}
console.log('푸시용이미지로딩완료');

//init mqttClient
client = mqtt.createClient(port, serverIP);
console.log('mqtt서버연결완료');

//유저별 토픽구독
//for dubugging
for (var key in users) {
  client.subscribe('user/' + key);
  console.log('구독완료(' + 'user/' + key + ')');
}

//message receiver
client.on('message', function (topic, message) {
  console.log('토픽=' + topic);
  console.log('메시지=' + message);
});


var remain = remaining(start);
if (remain < 0) {
  start.setDate(start.getDate() + 1);
  remain = remaining(start);
}

console.log('푸시목표시간=' + start);
console.log('남은시간(밀리초)=' + remain);
setTimeout(repeatPush, remain);

/**
 * 하루에한번씩 별자리운세를 푸시한다.
 */
function repeatPush() {
  console.log('repeatPush시작()');
  process();
  setInterval(process, 86400 * 1000);
  console.log('repeatPush종료()');
}

//for debugging
//process();

/**
 * 유저별로 푸시를 수행한다.
 */
function process() {
  console.log('process시작()');
  for (var key in users) {
    //유저의 해당 별자리를 알아낸다.
    console.log('key=' + key);
    var zodiac = getZodiacSign(users[key].birth.day, users[key].birth.month);
    console.log('zodiac=' + JSON.stringify(zodiac));
    pushZodiac(key, zodiac);
  }
  console.log('process종료()');
};

/**
 * 웹에서 별자리운세를 가져와 푸시한다.
 *
 * @param userID
 * @param zodiac
 */
function pushZodiac(userID, zodiac) {
  console.log('pushZodiac시작(userID=' + userID + '|zodiac=' + JSON.stringify(zodiac) + ')');
  //별자리운세를 웹에서 가져온다.
  //http://image.fortune.daum-img.net/external/2/star/star_today.asp?sid=
  //ban당한거같음
  //http://fortune.daum.net/external/2/star/star_today.asp?sid=
  request({uri: 'http://image.fortune.daum-img.net/external/2/star/star_today.asp?sid=' + zodiac.index, encoding: 'binary', headers: {
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.74.9 (KHTML, like Gecko) Version/7.0.2 Safari/537.74.9',
    'Accept-Encoding': 'gzip, deflate',
    'Referer': 'http://fortune.daum.net/external/2/star/star_today.asp'
  }}, function (err, response, body) {
    console.log('response.statusCode=' + response.statusCode);
    if (err) {
      console.error('웹스크래핑중에러발생', e);
      return;
    }
    try {
      body = new Buffer(body, 'binary');
      body = iconv.convert(body).toString();
      //console.log('body='+body);
      $ = cheerio.load(body, {
        normalizeWhitespace: true,
        xmlMode: true
      });

      var result = '';
      var cnt = 0;

      //페이지가 변경될 경우 다시 코드를 봐줘야함!
      $('font').each(function () {
        cnt++;
        if (cnt > 2) {
          var str = $(this).text();
          //console.log(str);
          result += str;
        }
      });

      var sendMsg = {"notification": {"notificationStyle": 1, "contentTitle": "오늘의운세(" + zodiac.desc + ")",
        "contentText": result, "ticker": result,
        "summaryText": "오늘의운세(" + zodiac.desc + ")", "image": images[zodiac.index - 1]
      }};
      console.log('sendMessage=' + JSON.stringify(sendMsg));

      //메시지를 전송한다.
      client.publish('user/' + userID, JSON.stringify(sendMsg), {'qos': 1});
    } catch (e) {
      console.log('body=' + body);
      console.error('에러발생', e);
    }


  });
  console.log('pushZodiac시작()');
}

function leadingZeros(n, digits) {
  var zero = '';
  n = n.toString();

  if (n.length < digits) {
    for (var i = 0; i < digits - n.length; i++)
      zero += '0';
  }
  return zero + n;
}

/**
 * Return zodiac sign by month and day
 *
 * @param day
 * @param month
 * @return {string} name of zodiac sign
 */
function getZodiacSign(day, month) {

  var zodiacSigns = {
    'capricorn': {'index': 12, 'desc': '염소자리'},
    'aquarius': {'index': 1, 'desc': '물병자리'},
    'pisces': {'index': 2, 'desc': '물고기자리'},
    'aries': {'index': 3, 'desc': '양자리'},
    'taurus': {'index': 4, 'desc': '황소자리'},
    'gemini': {'index': 5, 'desc': '쌍둥이자리'},
    'cancer': {'index': 6, 'desc': '게자리'},
    'leo': {'index': 7, 'desc': '사자자리'},
    'virgo': {'index': 8, 'desc': '처녀자리'},
    'libra': {'index': 9, 'desc': '천칭자리'},
    'scorpio': {'index': 10, 'desc': '전갈자리'},
    'sagittarius': {'index': 11, 'desc': '사수자리'}
  }

  if ((month == 1 && day <= 19) || (month == 12 && day >= 25)) {
    return zodiacSigns.capricorn;
  } else if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
    return zodiacSigns.aquarius;
  } else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
    return zodiacSigns.pisces;
  } else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
    return zodiacSigns.aries;
  } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
    return zodiacSigns.taurus;
  } else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
    return zodiacSigns.gemini;
  } else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
    return zodiacSigns.cancer;
  } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
    return zodiacSigns.leo;
  } else if ((month == 8 && day >= 23) || (month == 9 && day <= 23)) {
    return zodiacSigns.virgo;
  } else if ((month == 9 && day >= 24) || (month == 10 && day <= 22)) {
    return zodiacSigns.libra;
  } else if ((month == 10 && day >= 23) || (month == 11 && day <= 22)) {
    return zodiacSigns.scorpio;
  } else if ((month == 11 && day >= 23) || (month == 12 && day <= 24)) {
    return zodiacSigns.sagittarius;
  }
}

/**
 * 목표시간까지의 남은시간을 반환(밀리초)
 * @param target
 * @returns {number}
 */
function remaining(target) {
  var now = new Date();
  var nt = now.getTime();
  var et = target.getTime();
  return parseInt(et - nt)
}


