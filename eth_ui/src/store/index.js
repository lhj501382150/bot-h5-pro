import Vue from 'vue'
import vuex from 'vuex'

Vue.use(vuex);

// 引入子模块
import app from './modules/app'
import user from './modules/user'

const store = new vuex.Store({
    modules: {
        app: app,
        user:user
    }
})

export default store
