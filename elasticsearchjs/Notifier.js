var util = require('util');
var schedule = require('node-schedule');
var elasticsearch = require('elasticsearch');
var Message = require('./message');
var Slack = require('./client.js');
var client = new elasticsearch.Client({
  host: '112.223.76.75:9200',
  log: 'trace'
    //'debug'
});
var autoMark, autoReconnect, slack, token;
token = 'xoxb-9330755927-TUnHJGFi006qBWO5T9F8byh9';
autoReconnect = true;
autoMark = true;
slack = new Slack(token, autoReconnect, autoMark);

slack.on('open', function() {
  var channel, channels, group, groups, id, messages, unreads;
  channels = [];
  groups = [];
  unreads = slack.getUnreadCount();
  channels = (function() {
    var ref, results;
    ref = slack.channels;
    //console.log('channel='+util.inspect(ref));
    results = [];
    for (id in ref) {
      channel = ref[id];
      if (channel.is_member) {
        results.push("#" + channel.name);
      }
    }
    return results;
  })();
  groups = (function() {
    var ref, results;
    ref = slack.groups;
    results = [];
    for (id in ref) {
      group = ref[id];
      if (group.is_open && !group.is_archived) {
        results.push(group.name);
      }
    }
    return results;
  })();
  console.log("Welcome to Slack. You are @" + slack.self.name + " of " + slack.team.name);
  console.log('You are in: ' + channels.join(', '));
  console.log('As well as: ' + groups.join(', '));
  messages = unreads === 1 ? 'message' : 'messages';
  return console.log("You have " + unreads + " unread " + messages);
});

slack.on('message', function(message) {
  var channel, channelError, channelName, errors, response, text, textError, ts, type, typeError, user, userName;
  console.log('message.channel=' + util.inspect(message.channel));
  channel = slack.getChannelGroupOrDMByID(message.channel);
  //console.log('channel='+util.inspect(channel));
  user = slack.getUserByID(message.user);
  response = '';
  type = message.type, ts = message.ts, text = message.text;
  channelName = (channel != null ? channel.is_channel : void 0) ? '#' : '';
  channelName = channelName + (channel ? channel.name : 'UNKNOWN_CHANNEL');
  userName = (user != null ? user.name : void 0) != null ? "@" + user.name : "UNKNOWN_USER";
  console.log("Received: " + type + " " + channelName + " " + userName + " " + ts + " \"" + text + "\"");
  if (type === 'message' && (text != null) && (channel != null)) {
    //response = text.split('').reverse().join('');
    //channel.send(response);
    return console.log("@" + slack.self.name + " responded with \"" + response + "\"");
  } else {
    typeError = type !== 'message' ? "unexpected type " + type + "." : null;
    textError = text == null ? 'text was undefined.' : null;
    channelError = channel == null ? 'channel was undefined.' : null;
    errors = [typeError, textError, channelError].filter(function(element) {
      return element !== null;
    }).join(' ');
    return console.log("@" + slack.self.name + " could not respond. " + errors);
  }
});

slack.on('error', function(error) {
  return console.error("Error: " + util.inspect(error));
});
slack.login();

// index용 날짜 포맷
Date.prototype.yyyymmdd = function() {
  var yyyy = this.getFullYear().toString();
  var mm = (this.getMonth() + 1).toString(); // getMonth() is zero-based
  var dd = this.getDate().toString();
  return yyyy + '.' + (mm[1] ? mm : "0" + mm[0]) + '.' + (dd[1] ? dd : "0" + dd[0]); // padding
};

schedule.scheduleJob('*/10 * * * * *' /* 60초마다 */ , function() {
  //console.log('The answer to life, the universe, and everything!'+new Date());
  client.search({
    index: 'alert-*',
    //type: 'tweets',
    body: {
      "query": {
        "filtered": {
          "filter": {
            "or": [{
              "query": {
                "filtered": {
                  "query": {
                    "term": {
                      "status": "notified"
                    }
                  },
                  "filter": {
                    "bool": {
                      "must": {
                        "range": {
                          "timestamp": {
                            "gte": "now-15m"
                          }
                        }
                      }
                    }
                  }
                }
              }
            }, {
              "query": {
                "filtered": {
                  "filter": {
                    "bool": {
                      "must": {
                        "range": {
                          "timestamp": {
                            "gte": "now-1m"
                          }
                        }
                      }
                    }
                  }
                }
              }
            }]
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
            "type": {
              "terms": {
                "field": "type",
                "size": 100
              },
              "aggs": {
                "grade": {
                  "terms": {
                    "field": "grade",
                    "size": 100
                  },
                  "aggs": {
                    "status": {
                      "terms": {
                        "field": "status",
                        "size": 100
                      },
                      "aggs": {
                        "top_tag_hits": {
                          "top_hits": {
                            "sort": [{
                              "timestamp": {
                                "order": "desc"
                              }
                            }],
                            "_source": {
                              "include": []
                            },
                            "size": 1
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
      }
    }
  }).then(function(resp) {
      console.log('걸린시간=' + resp.took + 'ms');
      var hosts = resp.aggregations.host.buckets;
      for (hostId in hosts) {
        console.log('호스트=' + hosts[hostId].key);
        var types = hosts[hostId].type.buckets;
        for (typesId in types) {
          console.log('타입=' + hosts[hostId].key + ':' + types[typesId].key);
          var grades = types[typesId].grade.buckets;
          for (gradeId in grades) {
            console.log('중요도=' + hosts[hostId].key + ':' + types[typesId].key + ':' + grades[gradeId].key);
            var status = grades[gradeId].status.buckets;
            console.log('상태크기=' + status.length);
            var alarm = false;
            if (status.length == 1 && status[0].key == 'created') {
              //alarm
              console.log('알람을시작합니다');
              //send alarm
              alarm = true;
            } else {
              //console.log('처리할알람이없습니다');
            }
            for (statusId in status) {
              console.log('상태=' + hosts[hostId].key + ':' + types[typesId].key + ':' + grades[gradeId].key + ':' + status[statusId].key);
              var hits = status[statusId].top_tag_hits.hits.hits[0];
              console.log('hits=' + util.inspect(hits));
              if (hits && alarm) {
                //send alarm
                //send slack
                var chl = slack.getChannelGroupOrDMByID('D099QRXLN'); //#general C06L80QAV // direct message D099QRXLN @nadir93
                console.log('slackChannel=' + chl);
                if (chl) {
                  var msg = new Message(slack, {
                    //type: 'message',
                    //text: ' ',
                    username: '에이디플로우알림이',
                    //subtype: 'bot_message',
                    //channel: 'D099QRXLN',
                    //id: 001,
                    attachments: [{
                      "fallback": hosts[hostId].key + ' ' + types[typesId].key + '사용량:' + hits._source.value,
                      //"pretext": resp.aggregations.host.buckets[0].key,
                      "title": hosts[hostId].key,
                      "fields": [{
                        "title": types[typesId].key + '사용량',
                        "value": hits._source.value,
                        "short": true
                      }, {
                        "title": "중요도",
                        "value": grades[gradeId].key == 'danger' ? '위험' : '경고',
                        "short": true
                      }],
                      "color": grades[gradeId].key
                    }]
                  });
                  chl.postMessage(msg);
                  //create notified record
                  var d = new Date();
                  client.create({
                    index: 'alert-' + d.yyyymmdd(),
                    type: types[typesId].key,
                    // id: '1',
                    body: {
                      host: hosts[hostId].key,
                      type: types[typesId].key,
                      timestamp: d,
                      grade: grades[gradeId].key,
                      status: 'notified'
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
                alarm = false;
              }
            }
          }
          //send alarm
        }
      }
    },
    function(err) {
      console.trace(err.message);
    });
});
