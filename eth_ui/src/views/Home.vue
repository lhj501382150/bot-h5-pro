<template>
  <div class="container" v-title :data-title="appName">
      <!-- 头部区域 -->
      <head-bar></head-bar>
      <!-- 主内容区域 -->
      <main-content></main-content>
  </div>
</template>

<script>
import HeadBar from "./HeadBar"
import NavBar from "./NavBar"
import MainContent from "./MainContent"
import {mapState} from 'vuex'
export default {
  components:{
        HeadBar,
        NavBar,
        MainContent
  },
  computed:{
    ...mapState({
      appName:state=>state.app.appName
    })
  }
};
</script>

<style scoped lang="scss">
  .container {
    position:absolute;
    top: 0px;
    left: 0px;
    right: 0px;
    background: #ebeef5;
  }
</style>
