/**
 * User: nadir93
 * Date: 15. 6. 4.
 * Time: 오후 2:24
 */
var restify = require('restify'),
    formidable = require('formidable'),
    fs = require('fs-extra'),
    tokenValidator = require('./tokenValidator') ,
    bunyan = require('bunyan'),
    logOptions = { name: 'contentsServer',
        src: false,
        level: "debug",
        serializers: {
            req: bunyan.stdSerializers.req,
            err: bunyan.stdSerializers.err,
            res: bunyan.stdSerializers.res
        }
    },
    contextRoot = '/cts/v1/users';

// Activate this logger only for development and leave the original for production
//if (process.env.NODE_ENV === 'development') {
//    spawn = require('child_process').spawn;
//    bunyanCLI = spawn('bunyan', ['--color'], { stdio: ['pipe', process.stdout] });
//    logOptions.stream = bunyanCLI.stdin;
//}
var log = bunyan.createLogger(logOptions),
    server = restify.createServer({
        log: log
    });
server.name = 'contentsServer';

//server.use(
//    function crossOrigin(req, res, next) {
//        res.header("Access-Control-Allow-Origin", "*");
//        res.header("Access-Control-Allow-Headers", "X-Requested-With");
//        return next();
//    }
//);

// This is a simplified example just to give you an idea
// You will probably need more allowed headers
//function unknownMethodHandler(req, res) {
//    if (req.method.toLowerCase() === 'options') {
//        var allowHeaders = ['Accept', 'Accept-Version', 'Content-Type', 'Api-Version'];
//        if (res.methods.indexOf('OPTIONS') === -1) res.methods.push('OPTIONS');
//        res.header("Access-Control-Allow-Origin", "*");
//        res.header("Access-Control-Allow-Headers", "content-type, md5, token, file");
//        return res.send(200);
//    }
//    else {
//        return res.send(new restify.MethodNotAllowedError());
//    }
//}

//server.on('MethodNotAllowed', unknownMethodHandler);
server.use(ckeckToken);
//server.use(restify.bodyParser({ maxBodySize : 5242880 })); /* Limit POST to 5 MB */

server.on('after', restify.auditLogger({
    log: log
//    log: bunyan.createLogger({
//        name: 'audit',
//        stream: bunyanCLI.stdin,
//        level: "debug"
//    })
}));

/**
 * 파일다운로드
 */
var routeDownload = server.get(contextRoot + '/:userid/:hash', restify.serveStatic({
    directory: './uploads'
}));

/**
 * 파일다운로드
 */
var routeThumbDownload = server.get(contextRoot + '/:userid/thumb/:hash', restify.serveStatic({
    directory: './uploads'
}));

/**
 * 파일업로드
 * @type {*|Request}
 */
var routeUpload = server.post(contextRoot + '/:userid', upload);

/**
 * 썸네일업로드
 * @type {*|Request}
 */
var routeUploadThumb = server.post(contextRoot + '/:userid/thumb', upload);

/**
 * 이미지존재유무체크
 * @type {*|Request}
 */
var routeCheckContent = server.head(contextRoot + '/:userid', checkExists);

/**
 * 썸네일존재유무체크
 * @type {*|Request}
 */
var routeCheckThumb = server.head(contextRoot + '/:userid/thumb', checkExists);


if (process.env.NODE_ENV === 'development') {
    server.listen(9329, function () {
        console.log('%s listening at %s', server.name, server.url);
    });
} else {
    server.listen(8080, function () {
        console.log('%s listening at %s', server.name, server.url);
    });
}


/**
 *
 * @param req
 * @param res
 * @param next
 * @returns {*}
 */
function checkExists(req, res, next) {
    var md5 = req.headers['md5'];
    log.debug({md5: md5}, "업로드파일해쉬값");

    //res.header("Access-Control-Allow-Origin", "*");
    //res.header("Access-Control-Allow-Headers", "content-type, md5, token, file");

    fileName = md5 + req.headers['file'].substr(req.headers['file'].lastIndexOf('.'));
    //file.name.substr(file.name.lastIndexOf('.'));
    log.debug({fileName: fileName});

    var fullPath;
    if (req.route.name == routeCheckThumb) {
        fullPath = __dirname + '/uploads' + contextRoot + '/'
            + req.params.userid + '/thumb/' + fileName;
    } else {
        fullPath = __dirname + '/uploads' + contextRoot + '/'
            + req.params.userid + '/' + fileName;
    }

    if (fs.existsSync(fullPath)) {
        return next(new restify.InvalidArgumentError("파일이이미존재합니다"));
    } else {
        return next(new restify.NotFoundError("파일이존재하지않습니다"));
    }
}

/**
 * 파일업로드
 */
function upload(req, res, next) {
    var md5 = req.headers['md5'];
    log.debug({md5: md5}, "업로드파일해쉬값");
    log.debug({headers: req.headers});

    //파일업로드
    var form = new formidable.IncomingForm(),
        fileName, filePath, fullPath;

    //form.uploadDir = './upload';
    //form.hash = 'md5';
    //form.keepExtensions = true;

    form.on('error', function (err) {
        return next(err);
    }).on('field', function (field, value) {
        //log.debug({field: field});
        //log.debug({value: value});
    }).on('fileBegin', function (name, file) {
        //log.debug({name: name});
        //log.debug({file: file});
        fileName = md5 + req.headers['file'].substr(req.headers['file'].lastIndexOf('.'));
        //fileName = md5 + file.name.substr(file.name.lastIndexOf('.'));
        log.debug({fileName: fileName});

        if (req.route.name == routeUploadThumb) {
            fullPath = __dirname + '/uploads' + contextRoot + '/'
                + req.params.userid + '/thumb/' + fileName;
        } else {
            fullPath = __dirname + '/uploads' + contextRoot + '/'
                + req.params.userid + '/' + fileName;
        }

        if (fs.existsSync(fullPath)) {
            return next(new restify.InvalidArgumentError("파일이이미존재합니다"));
        }
        filePath = file.path;
        log.debug({filePath: filePath});
    }).on('file', function (field, file) {
        log.debug({field: field});
        log.debug({file: file});
    }).on('end', function () {
        /* Location where we want to copy the uploaded file */
        var location;
        // = __dirname + '/uploads/v1/users/' + req.params.userid + '/';

        if (req.route.name == routeUploadThumb) {
            location = __dirname + '/uploads' + contextRoot + '/'
                + req.params.userid + '/thumb/';
        } else {
            location = __dirname + '/uploads' + contextRoot + '/'
                + req.params.userid + '/';
        }

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
    //log.debug({agent: req.headers['user-agent']}, "agent");
    //log.debug({params: req.params}, "req.params");
    //log.debug({path: req.route.name}, "req");
    //log.debug({routeUpload: routeUpload});
    //log.debug('토큰=' + req.params.token);

    var token;

    if (req.route.name == routeUpload) {
        //upload
        token = req.headers.token;
    } else if (req.route.name == routeUploadThumb) {
        token = req.headers.token;
    } else if (req.route.name == routeDownload) {
        //download
        token = req.headers.token;
    } else if (req.route.name == routeThumbDownload) {
        //download
        token = req.headers.token;
    } else if (req.route.name == routeCheckContent) {
        //download
        token = req.headers.token;
    } else if (req.route.name == routeCheckThumb) {
        //download
        token = req.headers.token;
    }

    if (token) {
        tokenValidator.validate(req, function (err, validation) {
            next.ifError(err);
            log.debug({validation: validation}, "토큰유효성체크결과");
            if (validation) {
                return next();
            } else {
                //토큰이 유효하지 않음
                return next(new restify.UnauthorizedError("토큰이유효하지않습니다"));
            }
        });
    } else {
        //토큰이 유효하지 않음
        return next(new restify.UnauthorizedError("토큰이유효하지않습니다."));
    }
}