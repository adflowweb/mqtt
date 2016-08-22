/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'foreverbot',
        level: loglevel
    });
var os = require('os');
var child = require('child_process');

module.exports = function(robot) {

    robot.respond(/forever list (.*)/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        var host = msg.match[1];
        //console.log('host=' + host);
        if (host === os.hostname() || host === 'all') {
            var resMsg = '';
            child.exec('forever list', function(error, stdout, stderr) {
                if (error) {
                    log.error(error);
                    resMsg += "" + error;
                    msg.send({
                        "attachments": [{
                            "pretext": "*에러발생*",
                            "fields": [{
                                //"title": "에러발생",
                                "value": resMsg,
                                "short": false
                            }],
                            "mrkdwn_in": ["text", "pretext", "fields"],
                            "color": "danger"
                        }]
                    });
                    return;
                }

                if (stdout) {
                    log.debug(stdout);
                    resMsg += "" + stdout;
                }

                // if(stderr)
                // {
                //   resMsg += "\n stderr=" + stderr;
                // }
                //resMsg += "\n" + stdout;

                msg.send({
                    "attachments": [{
                        "pretext": "*forever list*",
                        "fields": [{
                            //"title": "response",
                            "value": resMsg,
                            "short": false
                        }],
                        "mrkdwn_in": ["text", "pretext", "fields"],
                        "color": "good"
                    }]
                });
            });
        }
    });

    robot.respond(/forever restart (.*) (.*)/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        var id = msg.match[1];
        var host = msg.match[2];
        log.debug('id=' + id);
        log.debug('host=' + host);
        if (host === os.hostname()) {
            var resMsg = '';
            child.exec('forever restart ' + id, function(error, stdout, stderr) {
                if (error) {
                    log.error(error);
                    resMsg += "" + error;
                    msg.send({
                        "attachments": [{
                            "pretext": "*에러발생*",
                            "fields": [{
                                //"title": "에러발생",
                                "value": resMsg,
                                "short": false
                            }],
                            "mrkdwn_in": ["text", "pretext", "fields"],
                            "color": "danger"
                        }]
                    });
                    return;
                }

                if (stderr) {
                    log.debug('stderr=' + stderr);
                    return;
                }

                if (stdout) {
                    log.debug(stdout);
                    resMsg += "" + stdout;
                }

                // if(stderr)
                // {
                //   resMsg += "\n stderr=" + stderr;
                // }
                //resMsg += "\n" + stdout;

                msg.send({
                    "attachments": [{
                        "pretext": "*forever restart " + id + " " + host + "*",
                        "fields": [{
                            //"title": "response",
                            "value": resMsg,
                            "short": false
                        }],
                        "mrkdwn_in": ["text", "pretext", "fields"],
                        "color": "good"
                    }]
                });
            });
        }
    });

    robot.respond(/forever$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        //var resMsg = "redis get [key] \nex) redis get user:+821021804709\n --> {\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":\"mqttbroker:clusterB\",\"created\":\"2016-08-17T02:28:23.621Z\",\"clearedSubscriptions\":\"2016-08-17T02:29:00.348Z\"}\n\nex) redis get mqttbroker:clusterA\n --> {\"token\":\"0123456789\",\"mqttbroker\":[\"ssl://14.63.217.141:18831\",\"ssl://14.63.217.141:28831\"],\"created\":\"2016-08-17T01:38:27.142Z\"}\n\nredis stat\n\n ex) redis stat\n --> clusterB,1,clusterA,1,clusterD,0,clusterC,0";
        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                "pretext": " *forever list* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "사용법",
                    "value": "해당 서버의 forever list를 가져온다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "forever list {`hostname`}",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```forever list dev1\n>>> info:    Forever processes running\ndata:        uid  command                                            script                                  forever pid   id logfile                         uptime         \ndata:    [0] zwAP /home/nadir93/.nvm/versions/node/v0.12.15/bin/node provisioning.js                         4736    26385    /home/nadir93/.forever/zwAP.log 6:19:5:5.95    \ndata:    [1] YBfz /home/nadir93/.nvm/versions/node/v0.12.15/bin/node contentsServer.js                       26183   25145    /home/nadir93/.forever/YBfz.log 5:18:35:43.104 \ndata:    [2] 1X1d /home/nadir93/.nvm/versions/node/v0.12.15/bin/node mqttsessionclear.js                     29566   29572    /home/nadir93/.forever/1X1d.log 0:18:45:58.500 \ndata:    [3] T2v2 coffee                                             node_modules/.bin/hubot --adapter slack 12692   13244    /home/nadir93/.forever/T2v2.log 0:0:5:17.405```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }, {
                //"title": "trlog get {전화번호}",
                "pretext": " *forever restart* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "사용법",
                    "value": "forever 프로세스를 재시작한다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "forever restart {`processId`} {`hostname`}",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```forever restart 0 dev1\n>>> info:    Forever restarted process(es):\ndata:        uid  command                                            script              forever pid   id logfile                         uptime         \ndata:    [0] 1X1d /home/nadir93/.nvm/versions/node/v0.12.15/bin/node mqttsessionclear.js 29566   29572    /home/nadir93/.forever/1X1d.log 0:18:49:20.402```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
    });

    // robot.respond(/forever stop (.*) (.*)/i , function(msg) {
    //   var id = msg.match[1];
    //   var host = msg.match[2];
    //   console.log('id=' + id);
    //   console.log('host=' + host);
    //   if (host === os.hostname()) {
    //     var resMsg = '';
    //     var child = require('child_process');
    //     child.exec('forever stop '+id, function(error, stdout, stderr) {
    //       if(error)
    //       {
    //         console.log('err='+error);
    //         return;
    //       }
    //
    //       if(stderr)
    //       {
    //         console.log('stderr='+stderr);
    //         return;
    //       }
    //       console.log('stdout='+stdout);
    //       resMsg += "\n" + stdout;
    //       msg.reply(resMsg);
    //     });
    //   }
    // });

}
