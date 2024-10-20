import Vue from 'vue'
import App from './App'
import router from './router'
import api from './http'
import i18n from './i18n'
import store from './store'
import global from '@/utils/global'
import ElementUI from 'element-ui'
import '@/utils/directives.js'
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.min.css'

import VueKindEditor from 'vue-kindeditor'
import 'kindeditor/kindeditor-all-min.js'
import 'kindeditor/themes/default/default.css'
import '../public/css/styles.css'

import uploader from 'vue-simple-uploader'

Vue.use(VueKindEditor)

Vue.use(ElementUI)  // 注册使用Element
Vue.use(uploader)
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
  i18n,
  router,
  store,
  render: h => h(App)
})
