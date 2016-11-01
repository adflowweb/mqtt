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
      api.post('/mqttbroker/clusterA')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
          token: '0123456789',
          mqttbroker: ['ssl://14.63.217.141:18831', 'ssl://14.63.217.141:28831']
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

    it('요청을 수행하고 response 200 을 반환 해야한다', function(done) {
      api.post('/mqttbroker/clusterB')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
          token: '0123456789',
          mqttbroker: ['ssl://14.63.217.141:18832', 'ssl://14.63.217.141:28832']
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

    it('요청을 수행하고 response 200 을 반환 해야한다', function(done) {
      api.post('/mqttbroker/clusterC')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
          token: '0123456789',
          mqttbroker: ['ssl://14.63.217.141:18833', 'ssl://14.63.217.141:28833']
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

    it('요청을 수행하고 response 200 을 반환 해야한다', function(done) {
      api.post('/mqttbroker/clusterD')
        .set('Content-Type', 'application/json')
        .set('Accept-Version', '1.0.0')
        .send({
          token: '0123456789',
          mqttbroker: ['ssl://14.63.217.141:18834', 'ssl://14.63.217.141:28834']
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




  });
});
