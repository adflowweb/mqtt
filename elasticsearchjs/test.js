var util = require('util');
var schedule = require('node-schedule');
var elasticsearch = require('elasticsearch');
var client = new elasticsearch.Client({
  host: '112.223.76.75:9200',
  log: 'debug' //'trace'
});

var j = schedule.scheduleJob('*/60 * * * * *', function() {
  //console.log('The answer to life, the universe, and everything!'+new Date());
  client.search({
    index: 'logstash-*',
    //type: 'tweets',
    body: {
      query: {
        filtered: {
          query: {
            "query_string": {
              "query": "host:raspberrypi AND plugin:cpu AND type_instance:idle"
            }
          },
          "filter": {
            "bool": {
              "must": [{
                "range": {
                  "@timestamp": {
                    "gte": "now-10s"
                  }
                }
              }]
            }
          }
        }
      }
    }
  }).then(function(resp) {
    var hits = resp.hits;
    // resp.hits.hits;
    console.log('totalCount=' + hits.total);
    console.log('item=' + util.inspect(hits.hits[0]));
    console.log('value=' + hits.hits[0]._source.value);
    // direct message D099QRXLN @nadir93
    var chl = slack.getChannelGroupOrDMByID('D099QRXLN'); //#general C06L80QAV
    console.log('chl=' + chl);
    if (chl && hits.hits[0]._source.value < 50) {
      console.log('cpu 경고메시지를 보냅니다.');
      chl.send('서버 : ' + hits.hits[0]._source.host + '\ncpu사용량(주의단계) : ' + (100 - hits.hits[0]._source.value).toFixed(2) + '%' + '\n발생 : ' + new Date(hits.hits[0]._source['@timestamp']));
    }
  }, function(err) {
    console.trace(err.message);
  });
});

var Slack, autoMark, autoReconnect, slack, token;

Slack = require('./client.js');

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
    response = text.split('').reverse().join('');
    channel.send(response);
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
  return console.error("Error: " + error);
});

slack.login();
