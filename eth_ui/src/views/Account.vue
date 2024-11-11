<template>
  <div class="container" v-title :data-title="appName">
      <!-- 头部区域 -->
      <head-bar :activeIndex="'account'"></head-bar>
      <el-row class="row">
        <el-col :span="24">
           <el-table  :data="items" border stripe style="width: 100%" size="medium" height="700">
                <el-table-column type="index" label="序号" width="100" header-align="center"></el-table-column>
                <el-table-column prop="address" label="地址" min-width="400" header-align="center">
                  <template slot-scope="scope">
                    <a style="color:#2f2cf1;cursor:pointer;" @click="findTrans(scope.row)">{{scope.row.address}}</a>
                  </template>
                </el-table-column>
                <el-table-column prop="balance" label="余额" min-width="150" header-align="center">
                  <template slot-scope="scope">
                    {{formatNum(scope.row.balance)}}
                  </template>
                </el-table-column>
                <el-table-column prop="transcount" label="笔数" min-width="100" header-align="center">
                  <template slot-scope="scope">
                    {{parseInt(scope.row.transcount,16)}}
                  </template>
                </el-table-column>

              </el-table>
        </el-col>
      </el-row>
  </div>
</template>

<script>
import HeadBar from "./HeadBar"
import {mapState} from 'vuex'
import {getArr,divid} from '@/utils/function'
export default {
  components:{
        HeadBar
  },
  data(){
    return {
      items:[]
    }
  },
  computed:{
    ...mapState({
      appName:state=>state.app.appName
    })
  },
  methods:{
    formatNum(num){
      return divid(num)
    },
    findAccount(){
        let para = { }
        this.$api.db.account(para).then(res => {
          if(res.ERRNO == 0){
            this.items = getArr(res.DATA)
          }else{
            this.$message.error(res.MSG)
          }
        })
    },
    findTrans(item){
      if(!item) return
      this.$router.push({
        path:'/accountTrans',
        query:{
          addr:item.address,
          bal:item.balance,
          symbol:item.symbol
        }
      })
    }

  },
  mounted(){
    this.findAccount()
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

    }
  }
</style>
