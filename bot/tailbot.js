/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'tailbot',
        level: loglevel
    });
var os = require('os');
var Tail = require('tail').Tail;
var tail;
var runTail = false;

module.exports = function(robot) {

    robot.respond(/tail start (.*)/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        var file = msg.match[1];
        log.debug('file = ' + file);
        //console.log('host=' + host);
        tail = new Tail(file);
        runTail = true;
        log.debug('tail started');
        msg.send({
            "attachments": [{
                "pretext": "*tail start " + file + "*",
                "fields": [{
                    //"title": "response",
                    "value": 'tail service started',
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
        log.debug('response', {
            message: 'tail service started'
        });

        tail.on("line", function(data) {
            //msg.reply(data);
            msg.send({
                "attachments": [{
                    //"pretext": "장애발생",
                    "text": data,
                    // "fields": [{
                    //     "title": "response",
                    //     "value": '```' + os.hostname() + ' tail service started```',
                    //     "short": false
                    // }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
            log.debug('response', {
                message: data
            });
        });

        tail.on("error", function(error) {
            //msg.reply(error);
            msg.send({
                "attachments": [{
                    //"pretext": "장애발생",
                    "text": error,
                    // "fields": [{
                    //     "title": "에러발생",
                    //     "value": os.hostname() + " 서버에 진행중인 tail 서비스가 없습니다",
                    //     "short": false
                    // }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "danger"
                }]
            });
            log.debug('response', {
                message: error
            });
        });
    });

    robot.respond(/tail stop$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        if (tail) {
            tail.unwatch();
            runTail = false;
            msg.send({
                "attachments": [{
                    "pretext": "*tail stop*",
                    "fields": [{
                        //"title": "response",
                        "value": 'tail service stopped',
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
            log.debug('response', {
                message: 'tail service stopped'
            });
        } else {
            msg.send({
                "attachments": [{
                    "pretext": "*에러발생*",
                    "fields": [{
                        //"title": "에러발생",
                        "value": "서버에 진행중인 tail 서비스가 없습니다",
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "danger"
                }]
            });
            log.debug('response', {
                message: '진행중인 tail 서비스가 없습니다'
            });
        }
    });

    robot.respond(/tail status$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name //,
                //channel: msg.message.user.room
        });

        if (runTail) {
            msg.send({
                "attachments": [{
                    "pretext": "*tail status*",
                    "fields": [{
                        //"title": "response",
                        "value": 'tail service running',
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
            log.debug('response', {
                message: 'tail service running'
            });
        } else {
            msg.send({
                "attachments": [{
                    "pretext": "*tail status*",
                    "fields": [{
                        //"title": "response",
                        "value": 'tail service stopped',
                        "short": false
                    }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": "good"
                }]
            });
            log.debug('response', {
                message: 'tail service stopped'
            });
        }
    });

    robot.respond(/tail$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        //var resMsg = "redis get [key] \nex) redis get user:+821021804709\n --> {\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":\"mqttbroker:clusterB\",\"created\":\"2016-08-17T02:28:23.621Z\",\"clearedSubscriptions\":\"2016-08-17T02:29:00.348Z\"}\n\nex) redis get mqttbroker:clusterA\n --> {\"token\":\"0123456789\",\"mqttbroker\":[\"ssl://14.63.217.141:18831\",\"ssl://14.63.217.141:28831\"],\"created\":\"2016-08-17T01:38:27.142Z\"}\n\nredis stat\n\n ex) redis stat\n --> clusterB,1,clusterA,1,clusterD,0,clusterC,0";
        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                "pretext": " *tail start* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "사용법",
                    "value": "dev1 서버의 파일을 볼 수 있게 tail 서비스를 시작합니다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "tail start {`file`}",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```tail start /home/nadir93/.forever/1X1d.log\n>>> {\"name\":\"mqttsessionclear\",\"hostname\":\"dev1\",\"pid\":15288,\"level\":20,\"msg\":\"8f074839f1be4aa28792fbf은 이미 처리되었습니다\",\"time\":\"2016-08-18T05:12:00.840Z\",\"v\":0} ```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }, {
                //"title": "trlog get {전화번호}",
                "pretext": " *tail stop* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "사용법",
                    "value": "tail 서비스를 종료합니다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "tail stop",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```tail stop\n>>> tail service stopped```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }, {
                //"title": "trlog get {전화번호}",
                "pretext": " *tail status* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "사용법",
                    "value": "tail 서비스 상태를 보여줍니다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "tail status",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```tail status\n>>> tail service running```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
    });

}
