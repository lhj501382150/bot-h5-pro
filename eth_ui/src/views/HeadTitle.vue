<template>
  <div id="main-container" class="main-container">
    <el-card class="box-card">
      <el-row>
        <el-col :span="24" class="back">
          <a  @click="goback"> <i class="el-icon-arrow-left"></i>返回</a>
          <span>查询结果： {{msg}}</span>
        </el-col>
      </el-row>
       <el-row class="row">
       <el-col :span="8" class="hidden-xs-only">
         <span class="title">
           <el-tag size="large">生产环境链</el-tag>
          </span>
       </el-col>
       <el-col :span="8" class="hidden-xs-only">
         <span class="time">
            <i class="el-icon-timer"></i>
           当前时间：{{time}}
         </span>
       </el-col>
        <el-col :span="8" :xs="24" class="search">
           <el-input  placeholder="交易hash，区块高度查询" v-model="para" size="small" clearable> </el-input>
            <el-button type="primary" size="small" @click="search">搜索</el-button>
       </el-col>
     </el-row>
    </el-card>
  </div>
</template>

<script>
import {getCurrentTime} from '@/utils/datetime'
export default {
  name:'HeadTitle',
  props:{
    msg:'',
    block:'',
    type:''// 1 block 2 trans
  },
  data () {
    return {
      time:'',
      para:''
    }
  },
  watch:{
    block: function(newVal,oldVal){
      this.para = newVal
    }
  },
  computed: {

  },
  methods: {
    refresh: function(){
      this.refreshTime()
    },
    refreshTime(){
      this.time = getCurrentTime()
    },
    goback(){
      this.$router.push('/')
    },
    search(){
      if(this.para){
        var regPos = /^\d+$/;
        if(regPos.test(this.para)){
          this.searchBlock()
        }else{
          this.searchTrans()
        }
      }else{
        this.$message({
          type:'error',
          message:'请输入查询条件'
        })
      }
    },
    searchBlock(){
      let num = parseInt(this.para)
        let val = '0x' + num.toString(16)
        this.$router.push({
          path :'/block',
          query:{
            value:val,
            a:Math.random()
            }
        })
        console.log(this.type,'------------')
        if(this.type==1){
          this.$emit('search')
        }
    },
    searchTrans(){
      let val = this.para
      this.$router.push({
          path :'/trans',
          query:{
            value:val,
            a:Math.random()
            }
        })
      if(this.type==2){
        this.$emit('search')
      }
    }
  },
  mounted(){
    this.refresh()
  }
}
</script>

<style scoped lang="scss">
  .main-container{
    padding-left:10px;
    padding-right:10px;
  }
  .box-card{
    margin-top:20px;
    height:130px;
    .title{
      display: block;
      height: 40px;
      line-height: 40px;
      text-align: left;
      font-weight: 800;
      font-size: 18px;
    }
    .time{
      display: block;
      height: 40px;
      line-height: 40px;
      color: #928989;
      text-align: right;
      padding-right: 10px;
    }
    .search{
      padding-top: 5px;
      display: flex;
      justify-content: space-around;
    }
  }
  .row{
    margin-top: 20px;
    padding-top: 10px;
    border-top: 1px solid #d6cdcd;
  }
  .back{
    text-align: left;
    a{
      color: #2f2cf1;
      cursor: pointer;
      margin-right: 50px;
    }
  }
</style>
