import { mount, shallow } from 'vue-test-utils'
import Parent from './components/Parent.vue'
import { createRenderer } from 'vue-server-renderer'

describe('Parent.vue', () => {
  it('mount snapshot', () => {
    const renderer = createRenderer()
    const wrapper = mount(Parent)
    renderer.renderToString(wrapper.vm, (err, str) => {
      if (err) throw new Error(err)
      expect(str).toMatchSnapshot()
    })
  })

  it('shallow snapshot', () => {
    const renderer = createRenderer()
    const wrapper = shallow(Parent)
    renderer.renderToString(wrapper.vm, (err, str) => {
      if (err) throw new Error(err)
      expect(str).toMatchSnapshot()
    })
  })
})
