# jest-serializer-vue

Jest Vue snapshot serializer

## Installation

```
npm istall --save-dev jest-serializer-vue
```

## Usage

You need to tell Jest to use the serializer. Add this to your Jest config:

```
"snapshotSerializers": [
  "<rootDir>/node_modules/jest-serializer-vue"
]
```

And your snapshot tests will be pretty printed 💅

```js
import { shallow } from 'avoriaz'
import Basic from './Basic.vue'
import { createRenderer } from 'vue-server-renderer'

describe('Basic.vue', () => {
  it('hasn\'t changed snapshot', () => {
    const renderer = createRenderer()
    const wrapper = shallow(Basic)
    renderer.renderToString(wrapper.vm, (err, str) => {
      expect(str).toMatchSnapshot()
    })
  })
})
```
