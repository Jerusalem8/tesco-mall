const beautify = require('pretty')

module.exports = {
  test (object) {
    return typeof object === 'string' && object[0] === '<'
  },
  print (val) {
    const removedServerRenderedText = val.replace(/ data-server-rendered="true"/, '')
    return beautify(removedServerRenderedText, { indent_size: 4 })
  }
}
