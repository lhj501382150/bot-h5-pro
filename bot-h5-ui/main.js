import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import Req from './http'
import store from './store'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
var http = Req.http;
Vue.prototype.$http = http
App.mpType = 'app'
const app = new Vue({
	store,
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif