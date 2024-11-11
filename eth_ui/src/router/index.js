import Vue from 'vue'
import Router from 'vue-router'
import Block from '@/views/Block'
import NotFound from '@/views/404'
import Home from '@/views/Home'
import Trans from '@/views/Trans'
import Login from '@/views/Login'
import Wallet from '@/views/Wallet'
import ConstToken from '@/views/ConstToken'
import Bind from '@/views/Bind'
import Account from '@/views/Account'
import AccountTrans from '@/views/AccountTrans'
import Intro from '@/views/Intro/Intro'


const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: '首页',
      component: Home
    },
    {
      path: '/block',
      name: 'Block',
      component: Block
    },
    {
      path: '/trans',
      name: 'Trans',
      component: Trans
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/wallet',
      name: 'Wallet',
      component: Wallet
    },
    {
      path: '/bind',
      name: 'Bind',
      component: Bind
    },
    {
      path: '/constToken',
      name: 'ConstToken',
      component: ConstToken
    },
    {
      path: '/account',
      name: 'Account',
      component: Account
    },
    {
      path: '/accountTrans',
      name: 'AccountTrans',
      component: AccountTrans
    },
    {
      path: '/404',
      name: 'notFound',
      component: NotFound
    }
  ]
})



export default router
