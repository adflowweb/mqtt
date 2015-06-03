/**
 * User: nadir93
 * Date: 15. 6. 3.
 * Time: 오전 11:12
 */
//https 인증스킵
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';

var http = require('http'),
    https = require('https'),
    url = require("url"),
    util = require('util'),
    formidable = require('formidable'),
    fs = require('fs-extra'),
    server;

server = http.createServer(function (req, res) {
    //파일업로드
    if (req.method == 'POST' && req.url.indexOf('/v1/users') === 0) {
        //console.log('req=' + util.inspect(req));
        //console.log('req.url=' + util.inspect(url.parse(req.url)));
        var token = req.url.substring(req.url.lastIndexOf('/') + 1);
        console.log('token=' + token);
        //check token
        var options = {
            hostname: 'push4.ktp.co.kr',
            port: 8080,
            path: '/v1/validate/' + token,
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'X-ApiKey': 'KTPJAAS'
            }
        };

        https.get(options, function (response) {
            //console.log(util.inspect(res))
            console.log("토큰체크응답코드=" + response.statusCode);
            response.setEncoding('utf8');
            response.on('data', function (chunk) {
                console.log('토큰체크응답메시지=' + chunk);
                var obj = JSON.parse(chunk);

                //응답메시지가 정상이면 진행
                if (obj.result.data.validation == true) {
                    var md5 = req.headers['md5'];
                    console.log("md5=" + md5);

                    fs.exists(__dirname + '/uploads/' + token + '/' + md5, function (exists) {
                        if (exists) {
                            //파일이 이미 존재하면 리턴
                            res.writeHead(200, {'content-type': 'text/plain; charset="UTF-8"'});
                            var responseStr = {"result": {"success": "true", "message": "파일이 이미 존재합니다."}};
                            res.end(JSON.stringify(responseStr));
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
                                console.log('에러발생 ', err);
                                throw err;
                            })
                            form.on('field', function (field, value) {
                                console.log(field, value);
                                fields.push([field, value]);
                            }).on('file', function (field, file) {
                                console.log(field, file);
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
                                console.log('temp_path=' + temp_path);
                                /* The file name of the uploaded file */
                                var file_name = md5;
                                //this.openedFiles[0].name;
                                console.log('file_name=' + file_name);
                                /* Location where we want to copy the uploaded file */
                                var new_location = __dirname + '/uploads/' + token + '/';
                                console.log('new_location=' + new_location);
                                fs.copy(temp_path, new_location + file_name, function (err) {
                                    if (err) {
                                        console.error(err);
                                    } else {
                                        console.log("success!")
                                    }
                                });
                            });
                            // form.parse(req);
                            form.parse(req, function (err, fields, files) {
                                res.writeHead(200, {'content-type': 'text/plain'});
                                res.write('received upload:\n\n');
                                res.end(util.inspect({fields: fields, files: files}));
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
        }).on('error', function (e) {
            console.log("에러발생=" + e.message);
            res.writeHead(401, {'content-type': 'text/plain; charset="UTF-8"'});
            //res.write('received fields:\n\n ' + util.inspect(fields));
            res.write(e.message + '\n\n');
            res.end('received files:\n\n ' + util.inspect(files));
        });
    } else {
        res.writeHead(404, {'content-type': 'application/json; charset="UTF-8"'});
        var responseStr = {"result": {"success": "false"}};
        res.end(JSON.stringify(responseStr));
    }
});
server.listen(13532);
console.log('listening on http://localhost:' + 13532 + '/');