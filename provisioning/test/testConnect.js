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

describe('connect', function() {
  describe('조회', function() {
    it('결과에 mqttbroker 속성이 존재 해야한다', function(done) {
      api.get('/connect/apitestuser?token=0123456789')
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
          //expect(res.body).to.have.property("token");
          //expect(res.body).to.have.property("phone");
          expect(res.body).to.have.property("mqttbroker");
          //expect(res.body.message.token).to.equal('0123456789');
          done();
        });
    });

    it('요청 API 버전이 다른경우 response 400, code: \'InvalidVersion\' 을 반환 해야한다', function(done) {
      api.get('/connect/apitestuser?token=0123456789')
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
          expect(res.body.message).to.equal('2.0.0 is not supported by GET /connect/apitestuser');
          done();
        });
    });

    it('요청시 필수항목(token||phone)이 없는 경우 response 409, code: \'InvalidArgument\' 을 반환 해야한다', function(done) {
      //var max = Math.max(1, 2, 10, 3);
      //expect(max).to.equal(10);
      api.get('/connect/1234')
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
});
