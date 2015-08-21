var util = require('util');
var schedule = require('node-schedule');
var Message = require('./message');
var elasticsearch = require('elasticsearch');
var client = new elasticsearch.Client({
  host: '112.223.76.75:9200',
  log: 'trace'
    //'debug'
});

// index용 날짜 포맷
Date.prototype.yyyymmdd = function() {
  var yyyy = this.getFullYear().toString();
  var mm = (this.getMonth() + 1).toString(); // getMonth() is zero-based
  var dd = this.getDate().toString();
  return yyyy + '.' + (mm[1] ? mm : "0" + mm[0]) + '.' + (dd[1] ? dd : "0" + dd[0]); // padding
};

// healthCheck elasticsearch
schedule.scheduleJob('*/5 * * * * *' /* 5초마다 */ , function() {
  client.ping({
    // ping usually has a 3000ms timeout
    //requestTimeout: Infinity,
    requestTimeout: 3000,
    // undocumented params are appended to the query string
    hello: "elasticsearch!"
  }, function(error) {
    if (error) {
      console.trace('elasticsearchDown!!');
      // Alert slack
    } else {
      console.log('elasticsearchAlive');
    }
  });
});

// query elasticsearch
schedule.scheduleJob('*/10 * * * * *' /* 10초마다 */ , function() {
  //console.log('The answer to life, the universe, and everything!'+new Date());
  client.search({
    index: 'logstash-*',
    //type: 'tweets',
    body: {
      "query": {
        "filtered": {
          "query": {
            "query_string": {
              "query": "plugin:cpu OR plugin:memory",
              "analyze_wildcard": true
            }
          },
          "filter": {
            "bool": {
              "must": {
                "range": {
                  "@timestamp": {
                    "gte": "now-1m" // 현재시간에서 일분전까지 데이터를 가져옴
                  }
                }
              }
            }
          }
        }
      },
      "size": 0,
      "aggs": {
        "host": {
          "terms": {
            "field": "host",
            "size": 100
          },
          "aggs": {
            "plugin": {
              "terms": {
                "field": "plugin",
                "size": 100
              },
              "aggs": {
                "type_instance": {
                  "terms": {
                    "field": "type_instance",
                    "size": 100
                  },
                  "aggs": {
                    "avg": {
                      "avg": {
                        "field": "value"
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }).then(function(resp) {
      // elasticsearch에 ALERT데이타를 입력
      console.log('걸린시간=' + resp.took + 'ms');
      var hosts = resp.aggregations.host.buckets;
      for (hostId in hosts) {
        console.log('호스트=' + hosts[hostId].key);
        var plugins = hosts[hostId].plugin.buckets
        for (pluginId in plugins) {
          console.log('플러그인=' + hosts[hostId].key + ':' + plugins[pluginId].key);
          var pluginTypes = plugins[pluginId].type_instance.buckets
          for (pluginTypesId in pluginTypes) {
            console.log('플러그인타입=' + hosts[hostId].key + ':' + plugins[pluginId].key + ':' + pluginTypes[pluginTypesId].key);
            //console.log('해당문서카운트=' + pluginTypes[pluginTypesId].doc_count);
            if (plugins[pluginId].key == 'cpu' && pluginTypes[pluginTypesId].key == 'idle') {
              //cpu 처리
              var idle = pluginTypes[pluginTypesId].avg.value;
              console.log('idle=' + idle.toFixed(2) + '%');
              if (idle < 30) {
                console.log('cpu 경고처리');
                // {
                // host:''
                // timestamp:''
                // value:''
                // grade:''
                //}
                var d = new Date();
                client.create({
                  index: 'alert-' + d.yyyymmdd(),
                  type: 'cpu',
                  // id: '1',
                  body: {
                    host: hosts[hostId].key,
                    type: plugins[pluginId].key,
                    timestamp: d,
                    value: (100 - idle).toFixed(2) + '%',
                    grade: 'danger',
                    status: 'created'
                  }
                }, function(error, response) {
                  if (error) {
                    console.trace(error.message);
                    // Alert slack
                  } else {
                    console.log('Alert생성처리결과=' + util.inspect(response));
                  }
                });
              } else if (idle < 50) {
                console.log('cpu 주의처리');
                // {
                // host:''
                // timestamp:''
                // value:''
                // grade:''
                //}
                var d = new Date();
                client.create({
                  index: 'alert-' + d.yyyymmdd(),
                  type: 'cpu',
                  // id: '1',
                  body: {
                    host: hosts[hostId].key,
                    type: plugins[pluginId].key,
                    timestamp: d,
                    value: (100 - idle).toFixed(2) + '%',
                    grade: 'warning',
                    status: 'created'
                  }
                }, function(error, response) {
                  if (error) {
                    console.trace(error.message);
                    // Alert slack
                  } else {
                    console.log('Alert생성처리결과=' + util.inspect(response));
                  }
                });
              }
            } else if (plugins[pluginId].key == 'memory') {
              // 메모리 처리
            } else {
              // 데이타오류
            }
          }
        }
      }
    },
    function(err) {
      console.trace(err.message);
      //문제발생 slack으로 푸시
    });
});

//todo
// 1분간 데이타가 없을경우 offline 이벤트를 발생시킨다.
//하루 처리 통계치를 슬랙으로 저녁 6시쯤 전송한다.
// delete indices - 일정시간이상 지나는 인덱스 삭제
