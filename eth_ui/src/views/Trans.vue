<template>
  <div class="container" v-title :data-title="appName">
      <!-- 头部区域 -->
      <head-bar></head-bar>
      <HeadTitle :msg="msg" :block.sync="hash" @search="init" :type="2"></HeadTitle>

      <el-row class="row">
        <el-col :span="24">
          <el-card class="record-card">
          <div slot="header" class="header">
            <span>交易结构详情</span>
          </div>
          <div   class="text item">
            <p>交易hash：{{item.hash}}</p>
            <p>时间戳：{{item.timestamp}}</p>
            <p>区块高度：{{parseInt(item.blockNumber,16)}}</p>
            <p>区块hash：{{item.blockHash}}</p>
            <p>Nonce：{{parseInt(item.nonce,16)}}</p>
            <p>发起账户：{{item.from}}</p>
            <p>目标账户：{{item.to}}</p>
            <p>交易金额：{{parseInt(item.value,16)}}</p>
            <!-- <p>签名：</p>
            <p>交易收据：</p> -->
          </div>
        </el-card>
        </el-col>
      </el-row>
  </div>
</template>

<script>
import HeadBar from "./HeadBar"
import HeadTitle from './HeadTitle'
import {mapState} from 'vuex'
import {formatWithSeperator} from '@/utils/datetime'
export default {
  components:{
        HeadBar,
        HeadTitle
  },
  data(){
    return {
      item:'',
      block_time:'',
      hash:'',
      msg:''
    }
  },
  computed:{
    ...mapState({
      appName:state=>state.app.appName
    })
  },
  methods:{
    formatTime(){
      if(this.item.timestamp){
        let time = parseInt(this.item.timestamp,16)
        this.block_time = formatWithSeperator(time,'-',':')
      }
    },
    findTrans(){
        let para = {
          "pageIdx": 0,
          "pageSize": 10,
          hash : this.hash
        }
        this.$api.db.trans(para).then(res => {
          if(res.iCode == 0){
            let temp = res.rData[0]
            this.item = temp
          }else{
            this.$message.error(res.sMsg)
          }
        })
    },
    init(){
      let para = this.$route.query
      this.hash = para.value
      this.msg = '区块链交易hash ' + this.hash
      if(this.hash){
        this.findTrans()
      }
    }
  },
  mounted(){
    this.init()
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
    .row{
      margin-top: 20px;
      padding-left:10px;
      padding-right:10px;
      .record-card{
        min-height: 300px;
        .header{
          text-align: left;
          color: #1d4978;
          letter-spacing: 1px;
          font-size: 16px;
        }
        p{
          text-align: left;
        }
      }
    }
  }
</style>
