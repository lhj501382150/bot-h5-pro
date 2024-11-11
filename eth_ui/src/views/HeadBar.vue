<template>
    <el-row class="headbar" type="flex" justify="space-between">
      <el-col :span="6" :xs="8">
        <div class="logo" @click="$router.push('/')">
          <img :src="imgSrc"/>
          </div>
      </el-col>
      <el-col :span="14" :xs="8">
        <!-- <el-menu :default-active="activeIndex" class="el-menu-demo hidden-xs-only" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1">首页</el-menu-item>
          <el-menu-item index="account">账户</el-menu-item>
          <el-menu-item index="wallet">我的资产</el-menu-item>
        </el-menu>

        <el-menu :default-active="activeIndex" class="el-menu-demo hidden-sm-and-up" mode="horizontal" @select="handleSelect">
          <el-submenu index="1">
            <template slot="title">首页</template>
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="account">账户</el-menu-item>
            <el-menu-item index="wallet">我的资产</el-menu-item>
          </el-submenu>
        </el-menu> -->

      </el-col>
      <el-col :span="4" :xs="8">
        <!-- <div class="user">
          <span v-if="!isLogin" class="user-info" @click="login" >登录/注册</span>
          <el-popover v-else  placement="bottom-end" trigger="hover" :visible-arrow="false">
            <personal-panel :user="user"></personal-panel>
            <span  class="user-info" slot="reference">当前账户：{{account}}</span>
          </el-popover>
        </div> -->
      </el-col>
    </el-row>
</template>

<script>
import { mapState,mapGetters } from 'vuex'
import mock from "@/mock/index"
import Hamburger from "@/components/Hamburger"
import ThemePicker from "@/components/ThemePicker"
import PersonalPanel from '@/views/Compoents/PersonalPanel'
export default {
  components:{
    Hamburger,
    ThemePicker,
    PersonalPanel
  },
  props: {
    activeIndex: {
      type:String,
      default:'1'
    }
  },
  data() {
    return {
      user: {
      },
      langVisible: false,
      imgSrc:require('@/assets/logo.jpg'),

    }
  },
  methods: {
    login(){
      this.$router.push({
        path:'/login'
      })
    },
    openWindow(url) {
      window.open(url)
    },
    handleSelect(key, keyPath) {
      //this.activeIndex = keyPath
      if(key==1){
        keyPath = ''
      }
      if(keyPath.length > 1){
        keyPath = keyPath[1]
      }
      this.$router.push('/' + keyPath)
    },
    // 折叠导航栏
    onCollapse: function() {
      this.$store.commit('onCollapse')
    },
    // 切换主题
    onThemeChange: function(themeColor) {
      this.$store.commit('setThemeColor', themeColor)
    },
    //缓存客户类型
    setCurUser: function (user){
      this.$store.commit('setCurUser',user)
    },
    // 语言切换
    changeLanguage(lang) {
      lang === '' ? 'zh_cn' : lang
      this.$i18n.locale = lang
      this.langVisible = false
    },
    loadUser(){
      let isLogin = sessionStorage.getItem('login-type')
      if(isLogin){
        this.$store.commit('refresh','')
      }
    }
  },
  mounted() {

  },
  computed:{
    ...mapState({
      appName: state=>state.app.appName,
      account: state=>state.user.name,
      isLogin: state=>state.user.isLogin
    })
  }
}
</script>

<style scoped lang="scss">
/deep/ .el-menu-item {
  height: 45px!important;
  line-height: 45px!important;
  color: #000000!important;
}
/deep/ .el-submenu .el-submenu__title{
  height: 45px!important;
  line-height: 45px!important;
  color: #000000!important;
}
.headbar {
  height: 45px;
  line-height: 45px;
 // background-image: linear-gradient(to bottom right, red, yellow);
 background-color: #ffffff;
  margin: 1px;
}
.el-menu-demo{
  height: 45px;
  li{
    height: 45px;
  }
}
 .logo {
    height: 45px;
    width: 400px;
    line-height: 45px;
    cursor:pointer;
    img {
        width: 120px;
        height: 36px;
        border-radius: 0px;
        margin: 5px 10px 5px 10px;
        float: left;
    }
    div {
      font-size: 14px;
      color: #000000;
      text-align: left;
      padding-left: 20px;
      font-weight: bolder;
      letter-spacing: 5px;
    }
  }
  .user{
    .user-info{
      color: #000000;
      font-size: 14px;
      letter-spacing: 3px;
      cursor: pointer;
    }
  }
</style>
