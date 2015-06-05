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
    logOptions = { name: 'contentsServer',
        src: false,
        level: "debug",
        serializers: {
            //req: bunyan.stdSerializers.req,
            err: bunyan.stdSerializers.err,
            res: bunyan.stdSerializers.res
        }
    };

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

/**
 * 파일다운로드
 */
var routeDownload = server.get('/v1/users/:id/:hash', restify.serveStatic({
    directory: './uploads'
}));

var routeUpload = server.post('/v1/users/:token', upload);

server.listen(8080, function () {
    console.log('%s listening at %s', server.name, server.url);
});

/**
 * 파일업로드
 */
function upload(req, res, next) {
    var md5 = req.headers['md5'];
    log.debug({md5: md5}, "업로드파일해쉬값");

    //파일업로드
    var form = new formidable.IncomingForm(),
        fileName, filePath;

    //form.uploadDir = './upload';
    //form.hash = 'md5';
    //form.keepExtensions = true;

    form.on('error', function (err) {
        return next(err);
    }).on('field', function (field, value) {
        log.debug({field: field});
        log.debug({value: value});
    }).on('fileBegin', function (name, file) {
        //log.debug({name: name});
        //log.debug({file: file});
        fileName = md5 + file.name.substr(file.name.lastIndexOf('.'));
        log.debug({fileName: fileName});
        if (fs.existsSync(__dirname + '/uploads/v1/users/'
            + req.params.token + '/' + fileName)) {
            return next(new restify.InvalidArgumentError("파일이이미존재합니다"));
        }
        filePath = file.path;
        log.debug({filePath: filePath});
    }).on('file', function (field, file) {
        log.debug({field: field});
        log.debug({file: file});
    }).on('end', function () {
        /* Location where we want to copy the uploaded file */
        var location = __dirname + '/uploads/v1/users/' + req.params.token + '/';
        log.debug({저장위치: location});
        fs.copy(filePath, location + fileName, function (err) {
            if (err) {
                return next(err);
            } else {
                log.debug("파일카피가완료되었습니다");
                res.send({message: "파일이업로드되었습니다"});
                return next();
            }
        });
    });
    form.parse(req);
}

/**
 * 토큰유효성체크
 */
function ckeckToken(req, res, next) {

    //log.debug({headers: req.headers}, "req.headers");
    //log.debug({params: req.params}, "req.params");
    //log.debug({path: req.route.name}, "req");
    //log.debug({routeUpload: routeUpload});
    //log.debug('토큰=' + req.params.token);
    var token;

    if (req.route.name == routeUpload) {
        //upload
        token = req.params.token;
    } else if (req.route.name == routeDownload) {
        //download
        token = req.headers.token;
    }

    tokenValidator.validate(token, function (err, validation) {
        next.ifError(err);
        log.debug({validation: validation}, "토큰유효성체크결과");

        if (validation) {
            return next();
        } else {
            //토큰이 유효하지 않음
            return next(new restify.UnauthorizedError("토큰이유효하지않습니다"));
        }
    });
}