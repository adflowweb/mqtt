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

/**
 * 컨텐츠서버
 */
server = http.createServer(function (req, res) {

    if (req.method == 'GET' && req.url.indexOf('/v1/users') === 0) {
        //파일다운로드
    } else if (req.method == 'POST' && req.url.indexOf('/v1/users') === 0) {
        //파일업로드
        log.info({req: req}, 'http요청이시작되었습니다.');
        //console.log('req=' + util.inspect(req));
        //console.log('req.url=' + util.inspect(url.parse(req.url)));
        var token = req.url.substring(req.url.lastIndexOf('/') + 1);
        log.debug({token: token});

        /**
         * 토큰유효성체크
         */
        tokenValidator.validate(req, function (err, response) {
            if (err) {
                res.writeHead(401, {'content-type': 'text/plain; charset="UTF-8"'});
                res.end(err.message);
                log.debug({res: res}, 'http응답이완료되었습니다.(토큰인증실패)');
                return;
            }
            log.debug({res: response}, "토큰유효성체크결과");
            response.setEncoding('utf8');
            response.on('data', function (chunk) {
                log.debug({data: chunk}, '토큰체크응답메시지');
                var obj = JSON.parse(chunk);

                /**
                 * 토큰이 유효하면 파일업로드 진행
                 */
                if (obj.result.data.validation == true) {
                    var md5 = req.headers['md5'];
                    log.debug({md5: md5}, "업로드파일해쉬값");

                    /**
                     * 파일이 이미 존재하면 리턴 아니면 업로드 진행
                     */
                    fs.exists(__dirname + '/uploads/' + token + '/' + md5, function (exists) {
                        if (!exists) {
                            res.writeHead(200, {'content-type': 'text/plain; charset="UTF-8"'});
                            var responseStr = {"result": {"success": "true", "message": "파일이이미존재합니다."}};
                            res.end(JSON.stringify(responseStr));
                            log.info({res: res}, 'http응답이완료되었습니다.(이미파일이존재함)');
                            return;
                        } else {
                            //파일업로드진행
                            var form = new formidable.IncomingForm(),
                                files = [],
                                fields = [];

                            //form.uploadDir = './upload';
                            //form.hash = 'md5';
                            //form.keepExtensions = true;

                            form.on('error', function (err) {
                                log.error({err: err}, '데이터처리중에러발생');
                                res.writeHead(500, {'content-type': 'text/plain; charset="UTF-8"'});
                                res.end(err.message);
                                log.info({res: res}, 'http응답이완료되었습니다.(에러발생)');
                                return;
                            }).on('field', function (field, value) {
                                log.debug({field: field});
                                log.debug({value: value});
                                fields.push([field, value]);
                            }).on('file', function (field, file) {
                                log.debug({field: field});
                                log.debug({file: file});
                                files.push([field, file]);
                            }).on('end', function (fields, files) {
                                /* Temporary location of our uploaded file */
                                var tempPath = this.openedFiles[0].path;
                                log.debug({임시저장위치: tempPath});
                                /* The file name of the uploaded file */
                                var fileName = md5;
                                //this.openedFiles[0].name;
                                log.debug({파일명: fileName});
                                /* Location where we want to copy the uploaded file */
                                var location = __dirname + '/uploads/' + token + '/';
                                log.debug({저장위치: location});
                                fs.copy(tempPath, location + fileName, function (err) {
                                    if (err) {
                                        log.error({err: err}, '파일카피중에러발생');
                                        res.writeHead(500, {'content-type': 'text/plain; charset="UTF-8"'});
                                        res.end(err.message);
                                        log.info({res: res}, 'http응답이완료되었습니다.(에러발생)');
                                        return;
                                    } else {
                                        log.debug("파일카피가완료되었습니다.");
                                        res.writeHead(200, {'content-type': 'text/plain'});
                                        res.write('received upload:\n\n');
                                        res.end(util.inspect({fields: fields, files: files}));
                                        log.info({res: res}, 'http응답이완료되었습니다.');
                                    }
                                });
                            });
                            form.parse(req);
                        }
                    });
                } else {
                    //토큰이 유효하지 않음
                    res.writeHead(401, {'content-type': 'text/plain; charset="UTF-8"'});
                    //{"result":{"success":true,"data":{"validation":true}}}
                    var responseStr = {"result": {"success": "false", "message": "토큰이유효하지않음"}};
                    res.end(JSON.stringify(responseStr));
                }
            });
        });
    } else {
        //잘못된요청
        res.writeHead(404, {'content-type': 'application/json; charset="UTF-8"'});
        var responseStr = {"result": {"success": "false"}};
        res.end(JSON.stringify(responseStr));
        log.info({res: res}, 'http응답이완료되었습니다.(허용되지않은요청)');
    }
});
server.listen(13532);
log.info('서버가시작되었습니다. 포트=' + 13532);