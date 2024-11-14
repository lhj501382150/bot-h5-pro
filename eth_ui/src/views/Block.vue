<template>
  <div class="container" v-title :data-title="appName">
      <!-- 头部区域 -->
      <head-bar></head-bar>
      <HeadTitle :msg="msg" :block.sync="item.number" @search="init" :type="1"></HeadTitle>

      <el-row class="row">
        <el-col :span="24">
          <el-card class="record-card">
          <div slot="header" class="header">
            <span>区块结构详情</span>
          </div>
          <div   class="text item">
            <p>区块hash：{{item.hash}}</p>
            <p>块高度：{{item.number}}</p>
            <p>Root Hash：{{item.hash}}</p>
            <p>上一块Hash：{{item.parentHash}}</p>
            <p>成块时间：{{item.timestamp}}</p>
            <p>交易个数：{{item.transnum}}</p>
          </div>
        </el-card>
        </el-col>
      </el-row>

      <el-row class="row">
        <el-col :span="24">
          <el-card class="record-card">
            <div slot="header" class="header">
              <span>交易记录</span>
            </div>
            <div   class="text">
              <el-table  :data="transactions" border stripe style="width: 100%" size="mini" height="300">
                <el-table-column type="index" label="序号"></el-table-column>
                <el-table-column prop="hash" label="交易哈希" min-width="400" header-align="center">
                  <template slot-scope="scope">
                    <a style="color:#2f2cf1;cursor:pointer;" @click="findTrans(scope.row.hash)">{{scope.row.hash}}</a>
                  </template>
                </el-table-column>
                <!-- <el-table-column prop="name" label="交易类型" min-width="100" header-align="center"></el-table-column>
                <el-table-column prop="type" label="业务分类" min-width="100" header-align="center">
                  <template slot-scope="scope">
                    {{parseInt(scope.row.type,16)}}
                  </template>
                </el-table-column>-->
                <el-table-column prop="timestamp" label="业务时间" min-width="150" header-align="center"></el-table-column>
              </el-table>
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
      bk_int:'',
      hash:'',
      msg:'',
      transactions:[]
    }
  },
  computed:{
    ...mapState({
      appName:state=>state.app.appName
    })
  },
  methods:{
    findBlock(){
      let num = this.hash
      let para = {
        "pageIdx": 0,
        "pageSize": 10,
        "hash" : num
      }
      this.$api.db.block(para).then(res => {
        if(res.iCode == 0){
          let temp = res.rData[0]
          temp.bk_int = parseInt(temp.number,16)
          this.item = temp
        }else{
          this.$message.error(res.MSG)
        }
      })
      this.$api.db.trans(para).then(res => {
        let that = this
        if(res.iCode == 0){
          that.transactions = res.rData
        }else{
          this.$message.error(res.MSG)
        }
      })

    },
    findTrans(val){
      this.$router.push({
        path:'/trans',
        query:{
          value:val
        }
      })
    },
    init(){
      let para = this.$route.query
      this.bk_int = para.value
      this.hash = para.hash
      this.msg = '区块Hash ' + this.hash
      if(this.hash){
        this.findBlock()
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
