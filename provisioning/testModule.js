var schedule = require('node-schedule');
//monitoring mqttbroker api
schedule.scheduleJob('*/5 * * * * *' /* 1분마다 */ , function() {
  Counter.count = Counter.count + 100;
  //b(a++);
  console.log('Counter.count=' + Counter.count);

});

// counter.js
var Counter = module.exports = {
  count: 1,
  add: function() {
    Counter.count += 10;
  },
  remove: function() {
    Counter.count -= 10;
  }
}
