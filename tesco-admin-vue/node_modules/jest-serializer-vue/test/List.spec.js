import { shallow } from 'vue-test-utils'
import List from './components/List.vue'
import { createRenderer } from 'vue-server-renderer'

describe('List.vue', () => {
  it('hasn\'t changed snapshot', () => {
    const renderer = createRenderer()
    const wrapper = shallow(List, {
      propsData: {
        items: ['one', 'two', 'three']
      }
    })
    renderer.renderToString(wrapper.vm, (err, str) => {
      if (err) throw new Error(err)
      expect(str).toMatchSnapshot()
    })
  })
})
