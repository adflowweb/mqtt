/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'monitorbot',
        level: loglevel
    });

var provisioning = require('./monitor/provisioning.js');
var crstank = require('./monitor/crstank.js');
var mqttbroker = require('./monitor/mqttbroker.js');

/**
 * [exports description]
 * @param  {[type]} robot [description]
 * @return {[type]}       [description]
 */
module.exports = function(robot) {

    robot.respond(/mon mb(.*)/i, function(msg) {
        mqttbroker.process(msg);
    })

    robot.respond(/mon pv(.*)/i, function(msg) {
        provisioning.process(msg);
    })

    robot.respond(/mon tank(.*)/i, function(msg) {
        crstank.process(msg);
    })

    robot.respond(/mon$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                "pretext": " *monitoring* ",
                //"text": "https://www.npmjs.com/package/crstankbot",
                "fields": [{
                    //"title": "사용법",
                    "value": "모니터링 합니다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "mon { `pv` | `pcs` | `pms` | `mb` | `tank` }",
                    "short": false
                },{
                    "title": "약어설명",
                    "value": "`pv` : provisioning\n`mb` : mqttbroker",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "mon pv",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
    })
}
