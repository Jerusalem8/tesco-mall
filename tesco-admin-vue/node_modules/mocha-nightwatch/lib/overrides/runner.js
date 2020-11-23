var create = require('lodash.create');
var NightwatchClient = require('../nightwatch/client');
var MochaRunner = require('../runner');
module.exports = Runner;

function Runner(suite, delay) {
  MochaRunner.call(this, suite, delay);
}

Runner.prototype = create(MochaRunner.prototype, {
  constructor : Runner
});

Runner.prototype.run = function(nightwatch, test_settings, fn) {
  var self = this;
  var rootSuite = this.suite;
  var client = new NightwatchClient(nightwatch, this, test_settings);

  fn = fn || function() {};

  function uncaught(err) {
    self.uncaught(err);
  }

  function setClient(test) {
    test.setNightwatchClient(client.get());
  }

  function start() {
    self.emit('start');
    self.runSuite(rootSuite, function() {
      self.emit('end');
    });

    self.on('test', setClient);
    self.on('hook', setClient);
  }

  // callback
  this.on('end', function() {
    process.removeListener('uncaughtException', uncaught);
    fn(self.failures);
  });

  // uncaught exception
  process.on('uncaughtException', uncaught);

  if (this._delay) {
    // for reporters, I guess.
    // might be nice to debounce some dots while we wait.
    this.emit('waiting', rootSuite);
    rootSuite.once('run', start);
  } else {
    start();
  }

  return this;
};

Runner.prototype.failOnError = function(err) {
  var runnable = this.currentRunnable;
  if (!runnable) {
    return;
  }

  runnable.clearTimeout();

  // Ignore errors if complete
  if (runnable.state) {
    return;
  }
  this.fail(runnable, err);

  // recover from test
  if (runnable.type === 'test') {
    this.emit('test end', runnable);
    this.hookUp('afterEach', this.next);
    return;
  }

  // bail on hooks
  this.emit('end');
};
