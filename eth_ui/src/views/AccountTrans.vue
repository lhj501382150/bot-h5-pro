<template>
  <div class="container">
      <!-- 头部区域 -->
      <head-bar :activeIndex="'account'"></head-bar>
      <div class="opera">
         <el-row class="border">
           <el-col :span="24">
             <div class="back">
              <a  @click="goback" class="back"> <i class="el-icon-arrow-left"></i>返回</a>
            </div>
             <p  class="info">
               地址：{{address}}

                <el-tooltip class="item" effect="dark" content="点击复制" placement="top-start">
                  <i class="el-icon-document-copy" style="cursor:pointer" @click="copyAddr"></i>
                </el-tooltip>

                 <el-popover   placement="bottom-start" width="210" trigger="click" >
                  <QRCode :text="address"></QRCode>
                  <a slot="reference" class="btn_cls">二维码</a>
                </el-popover>

                </p>

           </el-col>
         </el-row>
         <el-row class="border">
           <el-col :span="24">
             <img :src="imgSrc"/>
             <p class="balance">
               <span class="money">{{formatNum(balance)}}</span>
               <span class="fuhao">{{symbol}}</span>
             </p>
           </el-col>
         </el-row>
         <el-row>
           <el-col :span="24">
              <el-table  :data="contlist" border stripe style="width: 100%" size="mini" :height="tabHeight">
                <el-table-column type="index" label="序号"></el-table-column>
                <el-table-column prop="hash" label="交易哈希" min-width="250" header-align="center">
                  <template slot-scope="scope">
                    <a style="color:#2f2cf1;cursor:pointer;" @click="findTrans(scope.row.hash)">{{scope.row.hash}}</a>
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="区块高度" min-width="60" header-align="center">
                  <template slot-scope="scope">
                    {{parseInt(scope.row.blockNumber,16)}}
                  </template>
                </el-table-column>
                <el-table-column prop="timestamp" label="交易时间" min-width="120" header-align="center"></el-table-column>
                <el-table-column prop="from" label="发送方" min-width="150" header-align="center"></el-table-column>
                <el-table-column prop="to" label="接收方" min-width="150" header-align="center"></el-table-column>
                <el-table-column prop="gas" label="燃料费" min-width="80" header-align="center">
                  <template slot-scope="scope">
                    {{parseInt(scope.row.gas,16)}}
                  </template>
                </el-table-column>
              </el-table>
           </el-col>
         </el-row>

      </div>
  </div>
</template>

<script>
import HeadBar from "./HeadBar"
import QRCode from '@/views/Compoents/QRCode'
import {copy,getArr,divid} from '@/utils/function'
export default {
  components:{
        HeadBar,
        QRCode
  },
  data(){
    return {
      address:'',
      balance:'',
      symbol:'',
      contlist:[],
      imgSrc: require('@/assets/gas.png'),
      tabHeight:330
    }
  },
  methods:{
    goback(){
      this.$router.go(-1)
    },
    formatNum(num){
      return divid(num)
    },
    loadInfo(){
      let para = this.$route.query
      this.address = para.addr
      this.balance = para.bal
      this.symbol = para.symbol
      this.$api.db.trans({ADDRESS:para.addr}).then(res =>{
        if(res.ERRNO == 0){
          this.contlist = getArr(res.DATA)
        }
      })
    },
    copyAddr(){
      let text = this.address
      copy(text)
    },
    findTrans(hash){
      this.$router.push({
        path:'/trans',
        query:{
          value:hash
        }
      })
    }
  },
  mounted(){
    this.loadInfo()
    this.$nextTick(() => {
      console.log(window.innerHeight,this.tabHeight)
      this.tabHeight = window.innerHeight-this.tabHeight;
    })
  },
  computed:{

  }
};
</script>

<style scoped lang="scss">
  .container {
    position:absolute;
    top: 0px;
    left: 0px;
    right: 0px;
    bottom: 0px;
    background: #ebeef5;
    .opera{
      width: 98%;
      height: 80%;
      margin: 20px auto;
      padding-top: 30px;
      background-color: #ffffff;
      .bind{
        margin-top: 20px;
        color: #2f2cf1;
        cursor: pointer;
        display: block;
      }
      .border{
        border-bottom: 1px solid #e2e2e2;
        .back{
          color: #2f2cf1;
          cursor: pointer;
          text-align: left;
          padding-left: 20px;
        }
        .info{
          text-align: left;
          padding-left: 30px;
          .btn_cls{
            cursor: pointer;
            margin-left: 10px;
            color: #2f2cf1;
          }
        }
        img{
          width: 50px;
          height: 50px;
          margin-top: 20px;
        }
      }
      .balance{
          line-height: 50px;
          height: 50px;
          font-size: 16px;

          .fuhao{
            margin-left: 20px;
            font-family: '雅黑';
            font-weight: bold;
          }
        }
        .const{
          display: flex;
          justify-content: space-between;
          line-height: 50px;
          border-bottom: 1px solid #ebeef5;
          .item{
            padding-left: 50px;
          }
          .item_icon{
            padding-right: 20px;
            cursor: pointer;
          }
        }
    }
  }
</style>
