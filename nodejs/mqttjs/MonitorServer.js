/**
 * User: nadir93
 * Date: 2014. 8. 6.
 * Time: 오후 3:57
 */
var net = require('net');

var HOST = '127.0.0.1';
var PORT = 8124;

process.on('uncaughtException', function (err) {
  console.log('에러발생: ' + err);
});

// Create a server instance, and chain the listen function to it
// The function passed to net.createServer() becomes the event handler for the 'connection' event
// The sock object the callback function receives UNIQUE for each connection
net.createServer(function (sock) {

  // We have a connection - a socket object is assigned to the connection automatically
  console.log('소켓이연결되었습니다. ' + sock.remoteAddress + ':' + sock.remotePort);

  // Add a 'data' event handler to this instance of socket
  sock.on('data', function (data) {

    console.log('데이타 ' + sock.remoteAddress + ': ' + data);
    // Write the data back to the socket, the client will receive it as data from the server
    sock.write('You said "' + data + '"');

  });

  // Add a 'close' event handler to this instance of socket
  sock.on('close', function (data) {
    console.log('연결종료 ' + sock.remoteAddress + ' ' + sock.remotePort);
  });

}).listen(PORT, HOST);

console.log('서버가 시작되었습니다. ' + HOST + ':' + PORT);