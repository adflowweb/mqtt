module.exports = function(params, options, client, callback) {

  var message = '{"hi": "ho"}';
  options.headers['Content-Length'] = message.length;
  options.headers['Content-Type'] = 'application/json';
  options.body = 'YourPostData';
  options.path = '/connect/'+Math.random()+'?token=0123456789';
  var request = client(options, callback);
  request.write(message);
  return request;

  // generateMessageAsync(function(message) {
  //
  //   if (message)
  //   {
  //     options.headers['Content-Length'] = message.length;
  //     options.headers['Content-Type'] = 'application/x-www-form-urlencoded';
  //   }
  //   request = client(options, callback);
  //   if (message){
  //     request.write(message);
  //   }
  //
  //   return request;
  // });
};


// function(params, options, client, callback)
// 	{
// 		var message = '{"hi": "ho"}';
// 		options.headers['Content-Length'] = message.length;
// 		options.headers['Content-Type'] = 'application/json';
// 		options.body = 'YourPostData';
// 		options.path = 'YourURLPath';
// 		var request = client(options, callback);
// 		request.write(message);
// 		return request;
// 	}
