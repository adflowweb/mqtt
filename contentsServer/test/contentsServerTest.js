/**
 * User: nadir93
 * Date: 15. 6. 3.
 * Time: 오후 3:53
 */
var should = require('should');
var assert = require('assert');
var request = require('supertest');
//var mongoose = require('mongoose');
//var winston = require('winston');
//var config = require('../../config');
//var url = 'http://127.0.0.1:13532';
var url = 'http://127.0.0.1:8080';

describe('컨텐츠서버\n\t\tenv : http://127.0.0.1:13532\n\t\tfile : contentServerTest.js', function () {

    //테스트 수행전 선행작업
//    before(function (done) {
//        done();
//    });

    describe('파일업로드', function () {
        it('파일업로드 테스트 : 응답코드 200', function (done) {
            this.timeout(5000);
            request(url)
                .post('/v1/users/+821099969797')
                .set('md5', '7ae54aaf426a7483e2ae54cc17d9880f')
                .set('token', 'fffbd697e5354b42a9f6628')
                //.set('filename', 'app-debug.apk')
                .attach('file', __dirname + '/resource/app-debug.apk')
                .expect(200)
                // end handles the response
                .end(function (err, res) {
                    if (err) {
                        console.error(err);
                        //throw err;
                    }
                    console.log({response: res.headers});
//                    // this is should.js syntax, very clear
//                    //res.should.have.status(200);
//                    //res.should.have.property('status', 200);
                    res.status.should.be.equal(200);
                    done();
                });
        });

        it('파일다운로드 테스트 : 응답코드 200', function (done) {
            this.timeout(5000);
            request(url)
                .get('/v1/users/+821099969797/7ae54aaf426a7483e2ae54cc17d9880f.apk')
                .set('token', 'fffbd697e5354b42a9f6628')
                //.set('md5', '7ae54aaf426a7483e2ae54cc17d9880f')
                //.attach('file', __dirname + '/resource/app-debug.apk')
                .expect(200)
                // end handles the response
                .end(function (err, res) {
                    if (err) {
                        console.error(err);
                        //throw err;
                    }
                    //console.log({response: res});
//                    // this is should.js syntax, very clear
//                    //res.should.have.status(200);
//                    //res.should.have.property('status', 200);
                    res.status.should.be.equal(200);
                    done();
                });
        });

        //http://127.0.0.1:8080/v1/users/fffbd697e5354b42a9f6628/7ae54aaf426a7483e2ae54cc17d9880f

//        it('가상페이지 수정 테스트 : 응답코드 200', function (done) {
//            request(url)
//                .put('/v1/virtualpages/1234567890')
//                .set('virtual_page_uri', '/test001/TestServlet')
//                .set('event', '["x=10;y=20;sum(x,y)"]')
//
//                //.set('event', '[{"function1":["param1","param2"]},{"function2":["param1","param2"]}]')
//                //.send(profile)
//                // end handles the response
//                .end(function (err, res) {
//                    if (err) {
//                        throw err;
//                    }
//                    //console.log('response : ',res.text);
//                    // this is should.js syntax, very clear
//                    res.should.have.status(200);
//                    done();
//                });
//        });
//
//        it('가상페이지 가져오기 테스트 : 응답코드 200', function (done) {
//            request(url)
//                .get('/v1/virtualpages/1234567890')
//                // end handles the response
//                .end(function (err, res) {
//                    if (err) {
//                        throw err;
//                    }
//                    //console.log('response : ',res.text);
//                    // this is should.js syntax, very clear
//                    res.should.have.status(200);
//                    done();
//                });
//        });
//
//        it('가상페이지 삭제 테스트 : 응답코드 200', function (done) {
//            request(url)
//                .del('/v1/virtualpages/1234567890')
//                // end handles the response
//                .end(function (err, res) {
//                    if (err) {
//                        throw err;
//                    }
//                    //console.log('response : ',res.text);
//                    // this is should.js syntax, very clear
//                    res.should.have.status(200);
//                    done();
//                });
//        });
    });
});
