/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'tddbot',
        level: loglevel
    });
var os = require('os');
var child = require('child_process');

module.exports = function(robot) {
    robot.respond(/tdd provisioning (user|mqttbroker|connect|all)$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        var arg = msg.match[1];
        var testItem;
        //console.log('host=' + host);
        if (arg === 'user') {
            testItem = 'testUser.js';
        } else if (arg === 'mqttbroker') {
            testItem = 'testMqttbroker.js'
        } else if (arg === 'connect') {
            testItem = 'testConnect.js'
        } else {
            testItem = "";
        }

        var resMsg = "";
        child.exec('mocha /home/nadir93/dev/provisioning/test/' + testItem, function(error, stdout, stderr) {

            var color = "good";
            if (error) {
                log.error(error);
                //resMsg += "" + error;
                color = "danger";
                // msg.send({
                //     "attachments": [{
                //         "pretext": "*에러발생*",
                //         "fields": [{
                //             //"title": "에러발생",
                //             "value": resMsg,
                //             "short": false
                //         }],
                //         "mrkdwn_in": ["text", "pretext", "fields"],
                //         "color": "danger"
                //     }]
                // });
                //return;
            }

            if (stdout) {
                log.debug(stdout);
                resMsg = "" + stdout;
            }

            // if(stderr)
            // {
            //   resMsg += "\n stderr=" + stderr;
            // }
            //resMsg += "\n" + stdout;

            msg.send({
                "attachments": [{
                    "pretext": "*tdd provisioning " + arg + "*",
                    //"title": "response",
                    "text": resMsg,
                    // "fields": [{
                    //     "title": "response",
                    //     "value": '```' + resMsg + '```',
                    //     "short": false
                    // }],
                    "mrkdwn_in": ["text", "pretext", "fields"],
                    "color": color
                }]
            });
        });
        //}
    });

    robot.respond(/tdd$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        //var resMsg = "redis get [key] \nex) redis get user:+821021804709\n --> {\"token\":\"a0b285aa4fba490793a80ee\",\"mqttbroker\":\"mqttbroker:clusterB\",\"created\":\"2016-08-17T02:28:23.621Z\",\"clearedSubscriptions\":\"2016-08-17T02:29:00.348Z\"}\n\nex) redis get mqttbroker:clusterA\n --> {\"token\":\"0123456789\",\"mqttbroker\":[\"ssl://14.63.217.141:18831\",\"ssl://14.63.217.141:28831\"],\"created\":\"2016-08-17T01:38:27.142Z\"}\n\nredis stat\n\n ex) redis stat\n --> clusterB,1,clusterA,1,clusterD,0,clusterC,0";
        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                "pretext": " *TDD (test driven development)* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "사용법",
                    "value": "테스트를 수행하고 결과를 보여준다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "tdd provisioning {`user` | `mqttbroker` | `connect` | `all`}",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```tdd provisioning connect\n>>> connect\n조회\n✓ 결과에 mqttbroker 속성이 존재 해야한다\n✓ 요청 API 버전이 다른경우 response 400, code: 'InvalidVersion' 을 반환 해야한다\n✓ 요청시 필수항목(token||phone)이 없는 경우 response 409, code: 'InvalidArgument' 을 반환 해야한다\n3 passing (48ms) ```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
    });

}
