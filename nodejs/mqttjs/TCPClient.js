/**
 * User: nadir93
 * Date: 2014. 8. 6.
 * Time: 오후 10:42
 */
var client = require('./socketClient');

var clients = new Array();

//process.on('uncaughtException', function (err) {
//  console.log('에러발생: ' + err);
//});


for (var i = 0; i < 20; i++) {
  //clients[i] = new client();
  clients[i] = new client();
  clients[i].connect();
}
