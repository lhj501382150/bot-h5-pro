<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0px;
}
.dialog-height{
  height: calc(100vh - 350px);
  overflow-y: auto;
  overflow-x: hidden;
}
.el-table .cell.el-tooltip{
  white-space: pre !important;;
}
</style>
