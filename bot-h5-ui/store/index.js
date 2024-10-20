import Vue from 'vue'
import Vuex from 'vuex'
 
Vue.use(Vuex)
 
const store = new Vuex.Store({
  state: {
    sysStatus: 0
  },
  mutations: {
    changeStatus(state,status) {
	  uni.setStorageSync("sysStatus",status)
      state.sysStatus = status
    }
  },
  actions: {
    changeStatus(context,status) {
	  uni.setStorageSync("sysStatus",status)
      context.commit('changeStatus',status)
    }
  },
  getters:{
	  getSysStatus:status=>{
		  return uni.getStorageSync('sysStatus')
	  }
  }
})
 
export default store