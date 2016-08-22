/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'callAPIbot',
        level: loglevel
    });

var provisioning = require('./call/provisioning.js');

/**
 * [exports description]
 * @param  {[type]} robot [description]
 * @return {[type]}       [description]
 */
module.exports = function(robot) {

    robot.respond(/call pv(.*)/i, function(msg) {
        provisioning.process(msg);
    })

    robot.respond(/call$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                "pretext": " *Call API* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "시스템 정보 가져오기",
                    "value": "api를 호출한다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "call { `pv` | `pcs` | `pms` }",
                    "short": false
                }, {
                    "title": "약어설명",
                    "value": "`pv` : provisioning",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
    })
}
