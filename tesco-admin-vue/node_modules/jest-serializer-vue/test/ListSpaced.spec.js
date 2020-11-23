import { shallow } from 'vue-test-utils'
import ListSpaced from './components/ListSpaced.vue'
import { createRenderer } from 'vue-server-renderer'

describe('ListSpaced.vue', () => {
  it('hasn\'t changed snapshot', () => {
    const renderer = createRenderer()
    const wrapper = shallow(ListSpaced)
    renderer.renderToString(wrapper.vm, (err, str) => {
      if (err) throw new Error(err)
      expect(str).toMatchSnapshot()
    })
  })
})
