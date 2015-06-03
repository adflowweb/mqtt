/**
 * User: nadir93
 * Date: 15. 6. 3.
 * Time: 오전 11:12
 */
var http = require('http'),
    url = require("url"),
    util = require('util'),
    formidable = require('formidable'),
    fs = require('fs-extra'),
//Logger = require('bunyan'),
//Stream = require('stream'),
    bunyan = require('bunyan'),
    logOptions = { name: 'contentsServer', src: false, level: "debug", serializers: {req: bunyan.stdSerializers.req, err: bunyan.stdSerializers.err, res: bunyan.stdSerializers.res}};

// Activate this logger only for development and leave the original for production
if (process.env.NODE_ENV === 'development') {
    spawn = require('child_process').spawn;
    bunyanCLI = spawn('bunyan', ['--color'], { stdio: ['pipe', process.stdout] });
    logOptions.stream = bunyanCLI.stdin;
}
var log = bunyan.createLogger(logOptions),
    tokenValidator = require('./tokenValidator'),
    server;

//{name: "contentsServer", src: false, level: "debug", serializers: {req: bunyan.stdSerializers.req}}

//var stream = new Stream();
//stream.writable = true;
//
//stream.write = function (obj) {
//    // pretty-printing your message
//    console.log(obj.msg)
//}
//
//var logger = new Logger({
//    name: 'foo',
//    streams: [
//        {
//            type: "raw",
//            stream: stream
//        }
//    ],
//    serializers: {
//        err: Logger.stdSerializers.err,
//        req: Logger.stdSerializers.req,
//        res: Logger.stdSerializers.res
//    }
//});

server = http.createServer(function (req, res) {

    if (req.method == 'GET' && req.url.indexOf('/v1/users') === 0) {
        //파일다운로드
    } else if (req.method == 'POST' && req.url.indexOf('/v1/users') === 0) {
        //파일업로드
        log.info({req: req}, 'http 요청이 시작되었습니다.');
        //console.log('req=' + util.inspect(req));
        //console.log('req.url=' + util.inspect(url.parse(req.url)));
        var token = req.url.substring(req.url.lastIndexOf('/') + 1);
        log.debug({token: token});

        //check token
        tokenValidator.validate(req, function (err, response) {
            if (err) {
                //log.error({err: err});
                //console.log("에러발생=" + err.message);
                res.writeHead(401, {'content-type': 'text/plain; charset="UTF-8"'});
                //res.write('received fields:\n\n ' + util.inspect(fields));
                res.end(err.message);
                log.debug({res: res}, 'http 응답이 완료되었습니다.');
                return;
            }
            //console.log(util.inspect(res))
            log.debug({res: response}, "토큰유효성체크결과");
            response.setEncoding('utf8');
            //response.on('end',)
            response.on('data', function (chunk) {
                log.debug({data: chunk}, '토큰체크응답메시지');
                var obj = JSON.parse(chunk);

                //응답메시지가 정상이면 진행
                if (obj.result.data.validation == true) {
                    var md5 = req.headers['md5'];
                    log.debug({md5: md5}, "업로드파일해쉬값");

                    fs.exists(__dirname + '/uploads/' + token + '/' + md5, function (exists) {
                        if (exists) {
                            //파일이 이미 존재하면 리턴
                            res.writeHead(200, {'content-type': 'text/plain; charset="UTF-8"'});
                            var responseStr = {"result": {"success": "true", "message": "파일이 이미 존재합니다."}};
                            res.end(JSON.stringify(responseStr));
                            log.info({res: res}, 'http 응답이 완료되었습니다.(이미파일이존재함)');
                            return;
                        } else {
                            //파일업로드진행
                            var form = new formidable.IncomingForm(),
                                files = [],
                                fields = [];

                            //form.uploadDir = './upload1';
                            //form.hash = 'md5';
                            //form.keepExtensions = true;

//                    form.on('fileBegin', function (name, file) {
//                        file.path = './uploads/test/' + file.name;
//                        console.log('name=' + util.inspect(name));
//                        console.log('file=' + util.inspect(file));
//                    });
                            form.on('error', function (err) {
                                log.error({err: err});
                                //throw err;
                                res.writeHead(500, {'content-type': 'text/plain; charset="UTF-8"'});
                                res.end(err.message);
                                log.info({res: res}, 'http 응답이 완료되었습니다.(에러발생)');
                                return;
                            })
                            form.on('field', function (field, value) {
                                log.debug({field: field});
                                log.debug({value: value});
                                fields.push([field, value]);
                            }).on('file', function (field, file) {
                                log.debug({field: field});
                                log.debug({file: file});
                                files.push([field, file]);
                            });
                            //    .on('end', function () {
                            //    console.log('-> upload done');
                            //    res.writeHead(200, {'content-type': 'text/plain; charset="UTF-8"'});
                            //    res.write('received fields:\n\n ' + util.inspect(fields));
                            //    res.write('\n\n');
                            //    res.end('received files:\n\n ' + util.inspect(files));
                            //});

                            form.on('end', function (fields, files) {
                                /* Temporary location of our uploaded file */
                                var temp_path = this.openedFiles[0].path;
                                log.debug({임시저장위치: temp_path});
                                /* The file name of the uploaded file */
                                var file_name = md5;
                                //this.openedFiles[0].name;
                                log.debug({파일명: file_name});
                                /* Location where we want to copy the uploaded file */
                                var new_location = __dirname + '/uploads/' + token + '/';
                                log.debug({저장위치: new_location});
                                fs.copy(temp_path, new_location + file_name, function (err) {
                                    if (err) {
                                        log.error({err: err});
                                    } else {
                                        log.debug("파일카피가완료되었습니다.");
                                        res.writeHead(200, {'content-type': 'text/plain'});
                                        res.write('received upload:\n\n');
                                        res.end(util.inspect({fields: fields, files: files}));
                                        log.info({res: res}, 'http 응답이 완료되었습니다.');
                                    }
                                });
                            });
                            // form.parse(req);
                            form.parse(req, function (err, fields, files) {
                                if (err) {
                                    log.error({err: err});
                                }
                            });
                        }
                    });
                } else {
                    //아니면 리젝
                    res.writeHead(401, {'content-type': 'text/plain; charset="UTF-8"'});
                    //res.write('received fields:\n\n ' + util.inspect(fields));
                    res.write('요청이 잘못되었습니다.\n\n');
                    res.end('\n\n');
                }
            });
        });

//        https.get(options,).on('error', function (e) {
//            console.log("에러발생=" + e.message);
//            res.writeHead(401, {'content-type': 'text/plain; charset="UTF-8"'});
//            //res.write('received fields:\n\n ' + util.inspect(fields));
//            res.write(e.message + '\n\n');
//            res.end('received files:\n\n ' + util.inspect(files));
//        });
    } else {
        res.writeHead(404, {'content-type': 'application/json; charset="UTF-8"'});
        var responseStr = {"result": {"success": "false"}};
        res.end(JSON.stringify(responseStr));
    }
});
server.listen(13532);
log.info('서버가 시작되었습니다. 포트=' + 13532);