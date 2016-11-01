// test/string.js
var expect = require('chai').expect,
  supertest = require('supertest'),
  api = supertest('http://127.0.0.1:8083');

// describe('Math', function () {
//   describe('#max', function () {
//     it('returns the biggest number from the arguments', function () {
//       var max = Math.max(1, 2, 10, 3);
//       expect(max).to.equal(10);
//     });
//   });
// });

describe('mqttbroker', function() {
  describe('생성', function() {
    it('요청을 수행하고 response 200 을 반환 해야한다', function(done) {
      api.post('/mqttbroker/clusterTest')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
          token: '0123456789',
          mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(200)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ message: '정상처리되었습니다', code: 104200 }
          //expect(res.body.code).to.equal(104200);
          //expect(res.body.message).to.equal('정상처리되었습니다');
          done();
        });
    });

    it('요청 API 버전이 다른경우 response 400, code: \'InvalidVersion\' 을 반환 해야한다', function(done) {
      api.post('/mqttbroker/clusterTest')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '2.0.0')
        .send({
          token: '0123456789',
          mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(400)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ code: 'InvalidVersion', message: '2.0.0 is not supported by POST /user/test' }
          expect(res.body.code).to.equal('InvalidVersion');
          expect(res.body.message).to.equal('2.0.0 is not supported by POST /mqttbroker/clusterTest');
          done();
        });
    });

    it('요청시 필수 항목이 누락된 경우 response 409, code: \'InvalidArgument\' 을 반환 해야한다', function(done) {
      //var max = Math.max(1, 2, 10, 3);
      //expect(max).to.equal(10);
      api.post('/mqttbroker/clusterTest')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
          token: '0123456789'
          //mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        //.expect(400, done);
        .expect(409)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ code: 'InvalidArgument', message: '입력데이터오류입니다' }
          expect(res.body.code).to.equal('InvalidArgument');
          expect(res.body.message).to.equal('입력 데이터 오류입니다');
          done();
        });
    });
  });

  describe('조회', function() {
    it('결과에 토큰, 전화번호, mqttbroker 속성이 존재 해야한다', function(done) {
      api.get('/mqttbroker/clusterTest?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        // .send({
        //   token: '0123456789',
        //   phone: '01040269329',
        //   mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        // })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(200)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          // {
          //   "token": "0123456789",
          //   "phone": "01040269329",
          //   "mqttbroker": ["ssl://192.168.0.1:1883", "ssl://192.168.0.2:1883"],
          //   "created": "2016-07-22T02:38:26.863Z"
          // }
          expect(res.body).to.have.property("token");
          //expect(res.body).to.have.property("phone");
          expect(res.body).to.have.property("mqttbroker");
          //expect(res.body.message.token).to.equal('0123456789');
          done();
        });
    });

    it('요청 API 버전이 다른경우 response 400, code: \'InvalidVersion\' 을 반환 해야한다', function(done) {
      api.get('/mqttbroker/clusterTest?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '2.0.0')
        // .send({
        //   token: '0123456789',
        //   phone: '01040269329',
        //   mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        // })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(400)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ code: 'InvalidVersion', message: '2.0.0 is not supported by POST /user/test' }
          expect(res.body.code).to.equal('InvalidVersion');
          expect(res.body.message).to.equal('2.0.0 is not supported by GET /mqttbroker/clusterTest');
          done();
        });
    });

    it('요청시 해당사용자가 없는 경우 response 404, code: \'ResourceNotFound\' 을 반환 해야한다', function(done) {
      //var max = Math.max(1, 2, 10, 3);
      //expect(max).to.equal(10);
      api.get('/mqttbroker/cluster1234?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        // .send({
        //   token: '0123456789',
        //   phone: '01040269329',
        //   //mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        // })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        //.expect(400, done);
        .expect(404)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ code: 'InvalidArgument', message: '입력데이터오류입니다' }
          expect(res.body.code).to.equal('ResourceNotFound');
          //expect(res.body.message).to.equal('입력데이터오류입니다');
          done();
        });
    });
  });
  describe('수정', function() {
    it('요청을 수행하고 response 200 을 반환 해야한다', function(done) {
      api.put('/mqttbroker/clusterTest')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
          token: '11111111111',
          phone: '01195159329',
          mqttbroker: ['ssl://192.168.0.1:1883', 'tcp://192.168.0.2:1883']
        })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(200)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          done();
        });
    });

    it('요청 API 버전이 다른경우 response 400, code: \'InvalidVersion\' 을 반환 해야한다', function(done) {
      api.put('/mqttbroker/clusterTest')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '2.0.0')
        // .send({
        //   token: '0123456789',
        //   phone: '01040269329',
        //   mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        // })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(400)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ code: 'InvalidVersion', message: '2.0.0 is not supported by POST /user/test' }
          expect(res.body.code).to.equal('InvalidVersion');
          expect(res.body.message).to.equal('2.0.0 is not supported by PUT /mqttbroker/clusterTest');
          done();
        });
    });

    it('요청시 필수항목(token||mqttbroker)이 없는 경우 response 409, code: \'InvalidArgument\' 을 반환 해야한다', function(done) {
      //var max = Math.max(1, 2, 10, 3);
      //expect(max).to.equal(10);
      api.put('/mqttbroker/clusterTest')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        // .send({
        //   token: '0123456789',
        //   phone: '01040269329',
        //   //mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        // })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        //.expect(400, done);
        .expect(409)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ code: 'InvalidArgument', message: '입력데이터오류입니다' }
          expect(res.body.code).to.equal('InvalidArgument');
          //expect(res.body.message).to.equal('입력데이터오류입니다');
          done();
        });
    });
  });
  describe('삭제', function() {
    it('요청을 수행하고 response 200 을 반환 해야한다', function(done) {
      api.del('/mqttbroker/clusterTest?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        // .send({
        //   token: '0123456789',
        //   phone: '01040269329',
        //   mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        // })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(200)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ message: 'user(test)가삭제되었습니다', code: 106200 }
          //expect(res.body.code).to.equal(106200);
          //expect(res.body.message).to.equal('user(test)가삭제되었습니다');
          done();
        });
    });

    it('요청 API 버전이 다른경우 response 400, code: \'InvalidVersion\' 을 반환 해야한다', function(done) {
      api.del('/mqttbroker/clusterTest?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '2.0.0')
        // .send({
        //   token: '0123456789',
        //   phone: '01040269329',
        //   mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        // })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(400)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ code: 'InvalidVersion', message: '2.0.0 is not supported by POST /user/test' }
          expect(res.body.code).to.equal('InvalidVersion');
          expect(res.body.message).to.equal('2.0.0 is not supported by DELETE /mqttbroker/clusterTest');
          done();
        });
    });

    it('요청시 해당사용자가 없는 경우 response 404, code: \'ResourceNotFound\' 을 반환 해야한다', function(done) {
      api.del('/mqttbroker/clusterTest0123456789?token=0123456789')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        // .send({
        //   token: '0123456789',
        //   phone: '01040269329',
        //   mqttbroker: ['ssl://192.168.0.1:1883', 'ssl://192.168.0.2:1883']
        // })
        //.attach('data', './test/createuser.data')
        //.field('token', 'my awesome avatar')
        //.field('phone', 'my awesome avatar')
        .expect(404)
        .end(function(err, res) {
          if (err) return done(err);
          //console.log(res.body);
          //{ message: 'user(test)가삭제되었습니다', code: 106200 }
          expect(res.body.code).to.equal('ResourceNotFound');
          //expect(res.body.message).to.equal('test가존재하지않습니다');
          done();
        });
    });
  });
});
