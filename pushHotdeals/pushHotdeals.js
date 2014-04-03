/**
 * User: nadir93
 * Date: 2014. 3. 31.
 * Time: 오후 4:47
 */
var mqtt = require('mqtt')
  , request = require('request')
  , url = require('url')
  , util = require('util')
  , fs = require('fs')
  , cheerio = require('cheerio');

var serverIP = '175.209.8.188';
var port = 1883;
//추후 개인데이타는 암호화 하던가 또는 파일이나 디비에서 가져오도록..
var users = {"df33406434de552faf60efa": {"name": "이은영", "birth": {"year": 1978, "month": 3, "day": 8}},
  "1c45de7cc1daa896bfd32dc": {"name": "박택영", "birth": {"year": 1974, "month": 12, "day": 27}}};
var images = [];
var hotdealsCheckInterval = 600000;

var hotdeals = JSON.parse(fs.readFileSync('./pushHotdeals/resources/hotdeals', 'utf8'));
console.log('기존파일핫딜데이타=' + util.inspect(hotdeals));

//푸시에사용할 이미지로딩...
//for (var i = 0; i < 12; i++) {
var data = fs.readFileSync('./pushHotdeals/resources/hot-deal-icon.png');
var imageString = new Buffer(data).toString('base64');
images.push(imageString);
//}


//init mqttClient
var client = mqtt.createClient(port, serverIP);
//</editor-fold>

//유저별 토픽구독
//for dubugging
for (var key in users) {
  client.subscribe('user/' + key);
}
//endregion

//message receiver
client.on('message', function (topic, message) {
  console.log('토픽=' + topic);
  console.log('메시지=' + message);
});

//주기적으로 핫딜정보 체크
setInterval(function () {
  checkHotdeals(pushHotdeals);
}, hotdealsCheckInterval);


/**
 * 핫딜정보를 체크한다.
 * @param callback
 */
function checkHotdeals(callback) {
  console.log('핫딜정보체크시작');
  //잠자는 사간 제외
  var now = new Date();
  console.log('시간=' + now.getHours() + '시입니다');
  if (0 < now.getHours() && now.getHours() < 8) {
    console.log('취침시간입니다...');
    console.log('핫딜정보체크종료');
    return;
  }


  //오늘날짜의 딜정보를 가져온다.
  request({uri: 'http://www.clien.net/cs2/bbs/board.php?bo_table=jirum'}, function (err, response, body) {
    //console.log('body=' + body);
    console.log('response.statusCode=' + response.statusCode);
    if (err) {
      console.error('웹스크래핑중에러발생', err);
      callback(err, response);
      return;
    }

    $ = cheerio.load(body, {
      normalizeWhitespace: true,
      xmlMode: true
    });

    $('tr').each(function () {
      //console.log('레코드정보=' + $(this).html());
      var $this = $(this);

      var key;
      var category;
      var title;
      var uri;
      var writer;
      var time;
      var count;

      $this.children('td').each(function (index) {
        //console.log('index=' + index);

        switch (index) {
          case 0:
            key = $(this).text();
            break;
          case 1:
            category = $(this).text();
            break;
          case 2:
            title = $(this).text();
            //http://www.clien.net/cs2/bbs/board.php?bo_table=jirum&wr_id=399223
            var tmp = '' + $(this).children('a').attr('href');
            //console.log('str=' + str);
            //console.log('strTypeOf=' + typeof str);
            uri = 'http://www.clien.net/cs2/' + (tmp.substr(3));
            //console.log('uri=' + uri);
            break;
          case 3:
            writer = $(this).text();
            break;
          case 4:
            time = $(this).text();
            break;
          case 5:
            count = $(this).text();

            //오늘날짜 항목일경우 추가
            if (time.indexOf(':') > 0) {
              if (hotdeals.hasOwnProperty(key)) {
                //이미있는항목일경우 조회수 업데이트
                hotdeals[key].count = count;
              } else {
                hotdeals[key] = {category: category, uri: uri, title: title, writer: writer, time: time, count: count};
              }
            } else {
              //  날짜가 지난 아이템 삭제
              console.log((hotdeals.hasOwnProperty(key) ? "존재합니다.키=" : "존재하지않습니다.키=") + key);
              if (hotdeals.hasOwnProperty(key)) {
                delete hotdeals[key];
                console.log('삭제되었습니다.키=' + key);
              }
            }
            break;
          default:
            console.log('비정상케이스입니다.data=' + $(this).html());
        }
      });
    });
    console.log('핫딜정보=' + util.inspect(hotdeals));
    callback(err, response, hotdeals);
  });
  console.log('핫딜정보체크종료');
}

/**
 * 등록된 사용자에게 핫딜정보를 푸시한다.
 * @param err
 * @param response
 * @param deals
 */
function pushHotdeals(err, response, deals) {
  console.log('pushHotdeals시작(err=' + err + '|response=' + response.statusCode + '|deals=' + util.inspect(deals) + ')');
  if (err) {
    console.error('에러발생', err);
    return;
  }

  /**
   * 로직=조회수3000건이상 추후 로직을 적용바람 ex) 단위 시간당 조회수를 기울기로 환산하여 임계치 이상이면 핫딜로 판단..
   */
  for (var key in deals) {
    console.log('key=' + key);
    if (hotdeals[key].count >= 3000) {
      //보낼 메시지 조립
      var sendMsg = {"notification": {"notificationStyle": 2, "contentTitle": "오늘의핫딜" + hotdeals[key].category,
        "contentText": hotdeals[key].title, "ticker": hotdeals[key].title,
        "summaryText": "오늘의핫딜" + hotdeals[key].category, "image": images[0], contentUri: hotdeals[key].uri
      }};
      console.log('sendMessage=' + JSON.stringify(sendMsg));

      for (var userKey in users) {
        //전송정보업데이트
        if (hotdeals[key].hasOwnProperty('pushed')) {
          var sent = false;
          hotdeals[key].pushed.forEach(function (element) {
            console.log('element=' + element);
            if (userKey == element) {
              console.log('이미전송하였습니다');
              sent = true;
            }
          });

          //이미 보낸사용자는 스킵
          if (!sent) {
            console.log('메시지를전송합니다.');
            client.publish('user/' + userKey, JSON.stringify(sendMsg), {'qos': 1});
            hotdeals[key].pushed.push(userKey);
          }
        } else {
          //최초메시지푸시
          client.publish('user/' + userKey, JSON.stringify(sendMsg), {'qos': 1});
          hotdeals[key].pushed = [userKey];
        }
      }
    }
  }
  //파일에저장한다.
  fs.writeFileSync('./pushHotdeals/resources/hotdeals', JSON.stringify(deals));
  console.log('핫딜저장정보=' + util.inspect(hotdeals));
  console.log('pushHotdeals종료()');
}