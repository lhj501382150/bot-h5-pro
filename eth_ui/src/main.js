import Vue from 'vue'
import App from './App'
import router from './router'
import api from './http'
import store from './store'
import global from '@/utils/global'
import ElementUI from 'element-ui'
import '@/utils/directives.js'
import 'element-ui/lib/theme-chalk/index.css'
import 'element-ui/lib/theme-chalk/display.css';
import 'font-awesome/css/font-awesome.min.css'

import '../public/css/styles.css'

Vue.use(ElementUI)  // 注册使用Element
Vue.use(api)  // 注册使用API模块

Vue.prototype.global = global // 挂载全局配置模块

// 设置浏览器标题
Vue.directive('title', {
  inserted: function (el, binding) {
    document.title = el.dataset.title
  }
})

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
