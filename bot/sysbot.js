/**
 * author : @nadir93
 */
var loglevel = 'debug';
var Logger = require('bunyan'),
    log = new Logger.createLogger({
        name: 'sysbot',
        level: loglevel
    });
var os = require('os');
//var _ = require('underscore');
var disk = require('diskusage');

function bToS(bytes) {
    if (bytes === 0) return '0 Byte';
    var k = 1000;
    var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    var i = Math.floor(Math.log(bytes) / Math.log(k));
    return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
}

function bytesToSize(bytes) {
    var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    if (bytes == 0) return '0 Byte';
    var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
    return Math.round(bytes / Math.pow(1024, i), 2) + sizes[i];
};

function cpu_used() {
    var cpu = os.cpus();
    var counter = 0;
    var total = 0;
    var free = 0;
    var sys = 0;
    var user = 0;

    for (var i = 0; i < cpu.length; i++) {
        counter++;
        total = parseFloat(cpu[i].times.idle) + parseFloat(cpu[i].times.sys) + parseFloat(cpu[i].times.user) + parseFloat(cpu[i].times.irq) + parseFloat(cpu[i].times.nice);
        free += 100 * (parseFloat(cpu[i].times.idle) / total);
        sys += 100 * (parseFloat(cpu[i].times.sys) / total);
        user += 100 * (parseFloat(cpu[i].times.user) / total);
    };

    //console.log('CPU %s : %s + %s + %s',i,(free/counter),(user/counter),(sys/counter));
    return "\n*cpu(" + i + ")* : idle : " + (free / counter).toFixed(1) + "% user : " + (user / counter).toFixed(1) + "% sys : " + (sys / counter).toFixed(1) + "%";
}

module.exports = function(robot) {

    robot.respond(/sys (.*)/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        var host = msg.match[1];

        if (host === os.hostname() || host === 'all') {
            var resMsg = "\n*host* : " + os.hostname();
            // var load = _.map(os.loadavg(), function(num) {
            //   return num.toFixed(2);
            // });
            // resMsg += "\nload : " + load;
            resMsg += cpu_used();
            resMsg += "\n*memory* : " + bToS(os.freemem()) + " free of " + bToS(os.totalmem());

            var child = require('child_process');
            child.exec('uptime', function(error, stdout, stderr) {
                //console.log(stdout);
                stdout = stdout.replace(",  load average", "\n*load average*");
                resMsg += "\n*uptime* : " + stdout;
                msg.send({
                    "attachments": [{
                        "pretext": "*"+os.hostname()+" 시스템 정보*",
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

            // get disk usage. Takes mount point as first parameter
            disk.check('/', function(err, info) {
                log.debug('disk available=' + bytesToSize(info.available));
                log.debug('disk free=' + bytesToSize(info.free));
                log.debug('disk total=' + bytesToSize(info.total));

                resMsg += "\n*disk* : \"/\" " + bToS(info.free) + " free of " + bToS(info.total);
            });


            // //var uptime = (os.uptime() + "").toHHMMSS();
            // var date = new Date(os.uptime());
            // var uptime = '';
            // uptime += date.getUTCDate() - 1 + " days, ";
            // uptime += date.getUTCHours() + " hours, ";
            // uptime += date.getUTCMinutes() + " minutes, ";
            // uptime += date.getUTCSeconds() + " seconds, ";
            // uptime += date.getUTCMilliseconds() + " millis";
            //console.log(uptime);
            //resMsg += "\nuptime : "+getDuration((Date.now() - os.uptime()) * 1000).toString();


            // var cpus = os.cpus();
            // var resMsg = "\nhost : "+os.hostname();
            // var load = _.map(os.loadavg(), function(num){  return num.toFixed(2);});
            // resMsg += "\nload : "+load;
            // for (var i = 0, len = cpus.length; i < len; i++) {
            //   console.log("cpu %s :", i);
            //   resMsg += "\ncpu " + i + " :\n";
            //   var cpu = cpus[i],
            //     total = 0;
            //
            //   for (var type in cpu.times) {
            //     total += cpu.times[type];
            //   }
            //
            //   for (type in cpu.times) {
            //     console.log("\t", type, Math.round(100 * cpu.times[type] / total));
            //     resMsg += " "+type + " : " + Math.round(100 * cpu.times[type] / total + " ");
            //     //msg.reply("\t", type, Math.round(100 * cpu.times[type] / total));
            //   }
            //   resMsg += "\n";
            // }
            // resMsg += "\nfreemem : "+os.freemem();
            //
            // msg.reply(resMsg);
        }
    });

    robot.respond(/sys$/i, function(msg) {

        log.debug('request', {
            message: msg.message.text,
            user: msg.message.user.name,
            channel: msg.message.user.room
        });

        msg.send({
            "attachments": [{
                //"title": "trlog get {전화번호}",
                "pretext": " *시스템 정보 가져오기* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "시스템 정보 가져오기",
                    "value": "해당 서버의 시스템 정보를 가져온다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "sys {`hostname`}",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```sys dev1\n>>> host : dev1\ncpu(8) : idle : 99.7 user : 0.2 sys : 0.1\nmemory : 15.5 GB free of 33.6 GB\ndisk : \"/\" 6.51 GB free of 20.2 GB\nuptime :  23:39:27 up 29 days, 13:53,  1 user,  load average: 0.00, 0.04, 0.05```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }, {
                //"title": "trlog get {전화번호}",
                "pretext": " *모든 시스템 정보 가져오기* ",
                //"text": "```ex) trlog get 821021805043\n response : TRLog 전송 요청이 완료되었습니다```",
                "fields": [{
                    //"title": "모든 시스템 정보 가져오기",
                    "value": "dev1, dev2, dev3 서버의 시스템 정보를 모두 가져온다",
                    "short": false
                }, {
                    "title": "사용법",
                    "value": "sys all",
                    "short": false
                }, {
                    "title": "사용예",
                    "value": "```sys all\n>>> host : dev1\ncpu(8) : idle : 99.7 user : 0.2 sys : 0.1\nmemory : 15.5 GB free of 33.6 GB\ndisk : \"/\" 6.51 GB free of 20.2 GB\nuptime :  23:39:27 up 29 days, 13:53,  1 user,  load average: 0.00, 0.04, 0.05```",
                    "short": false
                }],
                "mrkdwn_in": ["text", "pretext", "fields"],
                "color": "good"
            }]
        });
    });
}
