<template>
  <div class="container">
      <!-- 头部区域 -->
      <head-bar :activeIndex="'wallet'"></head-bar>
      <el-row v-if="isLogin">
        <el-col :xs="{span:24,offset:0}" :span="12" :offset="6" class="opera">
         <el-row class="border">
           <el-col :span="24">
             <p class="info">账号：{{account}}</p>
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
             <p>
               <span class="balance">{{formatNum(balance)}}</span>
               <span class="fuhao">{{symbol}}</span>
             </p>
           </el-col>
         </el-row>
         <el-row>
           <el-col :span="24">
             <div class="const" v-for="(item,index) in contlist" :key="index" @click="queryToken(item.ADDRESS,item.CTYPE)">
               <div class="item">{{item.BALANCE}} {{item.SYMBOL}}</div>
               <div v-if="item.CTYPE==2" class="item_icon" >
                 <i class="el-icon-arrow-right"></i>
               </div>
             </div>
           </el-col>
         </el-row>
         <el-row>
           <el-col :span="24">
             <a @click="bindCont" class="bind">绑定合约</a>
           </el-col>
         </el-row>
         </el-col>
         </el-row>
  </div>
</template>

<script>
import HeadBar from "./HeadBar"
import QRCode from '@/views/Compoents/QRCode'
import {copy,divid} from '@/utils/function'
export default {
  components:{
        HeadBar,
        QRCode
  },
  data(){
    return {
      account:'',
      address:'',
      isLogin: false,
      balance:'',
      symbol:'',
      contlist:[],
      imgSrc: require('@/assets/gas.png')
    }
  },
  methods:{
    formatNum(num){
      return divid(num)
    },
    loadInfo(){
      this.address = sessionStorage.getItem('login-addr')
      this.account = sessionStorage.getItem('login-account')
    },
    query(){
      this.$api.db.findBalance().then(res => {
        if(res.ERRNO == 0){
          res = res.DATA
          this.balance = res.BALANCE
        }else{
          this.$message.error(res.MSG)
        }
      })
       this.$api.db.contSymbol().then(res => {
         if(res.ERRNO != 0) {
           this.$message.error(res.MSG)
           return
         }
         let that = this
         let data = []
        res = res.DATA
        Object.getOwnPropertyNames(res).forEach(function(key){
            data.unshift(res[key])
            if(res[key].CTYPE == 0){
              that.symbol = res[key].SYMBOL
            }
        })
        this.loadBalance(data)
      })
    },
    async loadBalance(data){
      for(var i=0;i<data.length;i++){
        let {DATA} = await this.$api.db.contBalance(data[i])
        data[i]['BALANCE'] =  DATA.BALANCE
      }
      this.contlist = data
    },
    queryToken(addr,ctype){
      this.$router.push({
        path:'/constToken',
        query:{
          ADDRESS: addr,
          CTYPE: ctype
        }
      })
    },
    bindCont(){
      this.$router.push('/bind')
    },
    copyAddr(){
      let text = this.address
      copy(text)
    }
  },
  mounted(){
    this.isLogin = sessionStorage.getItem('login-type')
    if(this.isLogin){
      this.loadInfo()
      this.query()
    }else{
      this.$router.push({
        path:'/login',
        query:{
          redirect: '/wallet'
        }
      })
    }
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
      height: 80%;
      margin-top: 30px;
      padding-bottom: 50px;
      background-color: #ffffff;
      .bind{
        margin-top: 20px;
        color: #2f2cf1;
        cursor: pointer;
        display: block;
      }
      .border{
        border-bottom: 1px solid #e2e2e2;
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
        }
        .fuhao{
            margin-left: 20px;
            font-family: '雅黑';
             font-size: 20px;
             font-weight: bold;
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
