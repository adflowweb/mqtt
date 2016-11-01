var test = require('./testModule');
//console.log('testA=' + test.a);
//test.a = 200;
//console.log('testA=' + test.a);

setInterval(function() {

  test.count++;
  console.log('testA=' + test.count);
}, 3000);
