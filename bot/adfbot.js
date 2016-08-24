/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'adfbot',
        level: loglevel
    });
var util = require('util');

module.exports = function(robot) {

    robot.respond(/(.*)/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });
    });

    robot.respond(/is it (weekend|holiday)\s?\?/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        var today = new Date();
        var res = today.getDay() === 0 || today.getDay() === 6 ? "YES" : "NO";
        msg.reply(res);
        log.debug('response', {
            message: res
        });
    });

    robot.respond(/command$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        // var message = 'sys [hostname]\n' +
        //     'test [projectname] [hostname]\n' +
        //     'forever list [hostname]\n' +
        //     'tail status [file] [hostname]\n' +
        //     'redis get [key]\n' +
        //     'redis stat\n' +
        //     'monitor (user|mqttbroker|userstat|mqttbrokerstat|stat)\n' +
        //     'monitor user (get|post|delete|put)\n' +
        //     'monitor mqttbroker (get|post|delete|put)\n' +
        //     'trlog get [phoneNumber]\n' +
        //     '수조온도\n';
        // msg.reply(message);

        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                //"pretext": "trlogbot 사용법",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fallback": "명령어 리스트",
                "fields": [{
                    "title": "명령어 리스트",
                    "value": "`sys` `call` `mon` `redis` `tdd` `forever` `tail` `log`",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });

        // log.debug('response', {
        //     message: message
        // });
    });
}

//var os = require('os');
//var _ = require('underscore');
//var disk = require('diskusage');

// module.exports = function(robot) {
//   robot.respond(/is it (weekend|holiday)\s?\?/i, function(msg) {
//     var today = new Date();
//     msg.reply(today.getDay() === 0 || today.getDay() === 6 ? "YES" : "NO");
//   });
//
//   robot.respond(/status (.*)/i, function(msg) {
//     var host = msg.match[1];
//     if (host === os.hostname()) {
//       var resMsg = "\nhost : " + os.hostname();
//       // var load = _.map(os.loadavg(), function(num) {
//       //   return num.toFixed(2);
//       // });
//       // resMsg += "\nload : " + load;
//       resMsg += cpu_used();
//       resMsg += "\nmemory : " + bToS(os.freemem()) + " free of " + bToS(os.totalmem());
//
//       var child = require('child_process');
//       child.exec('uptime', function(error, stdout, stderr) {
//         //console.log(stdout);
//         resMsg += "\nuptime : "+stdout;
//         msg.reply(resMsg);
//       });
//
//       // get disk usage. Takes mount point as first parameter
//      // disk.check('/', function(err, info) {
//      //   console.log(bytesToSize(info.available));
//      //   console.log(bytesToSize(info.free));
//      //   console.log(bytesToSize(info.total));
//
//       //  resMsg += "\ndisk : \"/\" " + bToS(info.free) + " free of " + bToS(info.total);
//      // });
//
//
//       // //var uptime = (os.uptime() + "").toHHMMSS();
//       // var date = new Date(os.uptime());
//       // var uptime = '';
//       // uptime += date.getUTCDate() - 1 + " days, ";
//       // uptime += date.getUTCHours() + " hours, ";
//       // uptime += date.getUTCMinutes() + " minutes, ";
//       // uptime += date.getUTCSeconds() + " seconds, ";
//       // uptime += date.getUTCMilliseconds() + " millis";
//       //console.log(uptime);
//       //resMsg += "\nuptime : "+getDuration((Date.now() - os.uptime()) * 1000).toString();
//
//
//       // var cpus = os.cpus();
//       // var resMsg = "\nhost : "+os.hostname();
//       // var load = _.map(os.loadavg(), function(num){  return num.toFixed(2);});
//       // resMsg += "\nload : "+load;
//       // for (var i = 0, len = cpus.length; i < len; i++) {
//       //   console.log("cpu %s :", i);
//       //   resMsg += "\ncpu " + i + " :\n";
//       //   var cpu = cpus[i],
//       //     total = 0;
//       //
//       //   for (var type in cpu.times) {
//       //     total += cpu.times[type];
// "adfbot.js" 154 lines, 5354 characters
//       //   for (var type in cpu.times) {
//       //     total += cpu.times[type];
//       //   }
//       //
//       //   for (type in cpu.times) {
//       //     console.log("\t", type, Math.round(100 * cpu.times[type] / total));
//       //     resMsg += " "+type + " : " + Math.round(100 * cpu.times[type] / total + " ");
//       //     //msg.reply("\t", type, Math.round(100 * cpu.times[type] / total));
//       //   }
//       //   resMsg += "\n";
//       // }
//       // resMsg += "\nfreemem : "+os.freemem();
//       //
//       // msg.reply(resMsg);
//     }
//   });
// }
//
//

//
// // String.prototype.toHHMMSS = function() {
// //   var sec_num = parseInt(this, 10); // don't forget the second param
// //   var hours = Math.floor(sec_num / 3600);
// //   var minutes = Math.floor((sec_num - (hours * 3600)) / 60);
// //   var seconds = sec_num - (hours * 3600) - (minutes * 60);
// //
// //   if (hours < 10) {
// //     hours = "0" + hours;
// //   if (hours < 10) {
// //     hours = "0" + hours;
// //   }
// //   if (minutes < 10) {
// //     minutes = "0" + minutes;
// //   }
// //   if (seconds < 10) {
// //     seconds = "0" + seconds;
// //   }
// //   var time = hours + ':' + minutes + ':' + seconds;
// //   return time;
// // }
//
// // var getDuration = function(millis){
// //     var dur = {};
// //     var units = [
// //         {label:"millis",    mod:1000},
// //         {label:"seconds",   mod:60},
// //         {label:"minutes",   mod:60},
// //         {label:"hours",     mod:24},
// //         {label:"days",      mod:31}
// //     ];
// //     // calculate the individual unit values...
// //     units.forEach(function(u){
// //         millis = (millis - (dur[u.label] = (millis % u.mod))) / u.mod;
// //     });
// //     // convert object to a string representation...
// //     dur.toString = function(){
// //         return units.reverse().map(function(u){
// //             return dur[u.label] + " " + (dur[u.label]==1?u.label.slice(0,-1):u.label);
// //         }).join(', ');
// //     };
// //     return dur;
// // };
