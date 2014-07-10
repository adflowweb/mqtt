/**
 * User: nadir93
 * Date: 2014. 6. 24.
 * Time: 오후 2:07
 */
var net = require('net');

var HOST = '127.0.0.1';
var PORT = 3000;

// Create a server instance, and chain the listen function to it
// The function passed to net.createServer() becomes the event handler for the 'connection' event
// The sock object the callback function receives UNIQUE for each connection
net.createServer(function (sock) {

  // We have a connection - a socket object is assigned to the connection automatically
  console.log('CONNECTED: ' + sock.remoteAddress + ':' + sock.remotePort);

  // Add a 'data' event handler to this instance of socket
  sock.on('data', function (data) {

    console.log('DATA ' + sock.remoteAddress + ': ' + data);
    // Write the data back to the socket, the client will receive it as data from the server
    //sock.write('You said "' + data + '"');
    sock.write('000000000010010000');
    console.log('보낸메시지=000000000010010000');

  });

  // Add a 'close' event handler to this instance of socket
  sock.on('close', function (data) {
    console.log('CLOSED: ' + sock.remoteAddress + ' ' + sock.remotePort);
  });

  sock.on('error', function (data) {
    console.log('error: ' + data);
  });

  sock.on('end', function () {
    console.log('server disconnected');
  });

}).listen(PORT, HOST);

console.log('Server listening on ' + HOST + ':' + PORT);