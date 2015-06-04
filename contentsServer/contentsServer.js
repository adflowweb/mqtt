/**
 * User: nadir93
 * Date: 15. 6. 4.
 * Time: 오후 2:24
 */
var restify = require('restify'),
    formidable = require('formidable'),
    fs = require('fs-extra'),
    tokenValidator = require('./tokenValidator'),
    bunyan = require('bunyan'),
    logOptions = { name: 'contentsServer', src: false, level: "debug", serializers: {req: bunyan.stdSerializers.req, err: bunyan.stdSerializers.err, res: bunyan.stdSerializers.res}};

// Activate this logger only for development and leave the original for production
if (process.env.NODE_ENV === 'development') {
    spawn = require('child_process').spawn;
    bunyanCLI = spawn('bunyan', ['--color'], { stdio: ['pipe', process.stdout] });
    logOptions.stream = bunyanCLI.stdin;
}
var log = bunyan.createLogger(logOptions),
    server = restify.createServer({
        log: log
    })
server.name = 'contentsServer';

server.use(ckeckToken);

server.on('after', restify.auditLogger({
    log: bunyan.createLogger({
        name: 'audit',
        stream: bunyanCLI.stdin,
        level: "debug"
    })
}));

server.get('/v1/users/:token', download);

server.post('/v1/users/:token', upload);

server.listen(8080, function () {
    console.log('%s listening at %s', server.name, server.url);
});

/**
 * 파일다운로드
 */
function download(req, res, next) {
    res.send({hello: req.params.name});
    next();
}

/**
 * 파일업로드
 */
function upload(req, res, next) {
    var md5 = req.headers['md5'];
    log.debug({md5: md5}, "업로드파일해쉬값");

    /**
     * 파일이 이미 존재하면 리턴 아니면 업로드 진행
     */
    fs.exists(__dirname + '/uploads/' + req.params.token + '/' + md5, function (exists) {
        if (exists) {
            return next(new restify.InvalidArgumentError("파일이이미존재합니다"));
        } else {
            //파일업로드진행
            var form = new formidable.IncomingForm(),
                files = [],
                fields = [];

            //form.uploadDir = './upload';
            //form.hash = 'md5';
            //form.keepExtensions = true;

            form.on('error', function (err) {
                return next(err);
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
                var location = __dirname + '/uploads/' + req.params.token + '/';
                log.debug({저장위치: location});
                fs.copy(tempPath, location + fileName, function (err) {
                    if (err) {
                        return next(err);
                    } else {
                        log.debug("파일카피가완료되었습니다.");
                        res.send({message: "파일이업로드되었습니다"});
                        return next();
                    }
                });
            });
            form.parse(req);
        }
    });
}

/**
 * 토큰유효성체크
 */
function ckeckToken(req, res, next) {
    tokenValidator.validate(req, function (err, response) {
        next.ifError(err);
        log.debug({res: response}, "토큰유효성체크결과");
        response.setEncoding('utf8');
        response.on('data', function (chunk) {
            log.debug({data: chunk}, '토큰체크응답메시지');
            var obj = JSON.parse(chunk);

            /**
             * 토큰이 유효하면 nextTask 수행 아니면 종료
             */
            if (obj.result.data.validation == true) {
                return next();
            } else {
                //토큰이 유효하지 않음
                return next(new restify.UnauthorizedError("토큰이유효하지않습니다"));
            }
        });
    });
}