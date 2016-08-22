/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'logbot',
        level: loglevel
    });

var trlog = require('./log/trlog.js');


/**
 * [exports description]
 * @param  {[type]} robot [description]
 * @return {[type]}       [description]
 */
module.exports = function(robot) {

    robot.respond(/log trlog(.*)/i, function(msg) {
        trlog.process(msg);
    })

    robot.respond(/log$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                "pretext": " *log bot* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "사용법",
                    "value": "로그관련 작업봇",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "log { `trlog` | `tomcatLog` }",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
    })

}
