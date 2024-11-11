<template>
  <div id="main-container" class="main-container">
    <el-card class="box-card">
      <el-row :gutter="12">
      <el-col :span="8" :xs="24" >
        <span class="title">
          <el-tag size="large">生产环境链</el-tag>
          </span>
      </el-col>
      <el-col :span="8" class="hidden-sm-and-down">
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

    <el-row :gutter="12" class="row">
      <el-col :span="12" :xs="24">
        <el-card class="info-card">
          <div slot="header" class="header">
            <i class="el-icon-thumb"></i>
            <span>实时交易情况</span>
          </div>
          <div   class="text item text_body">
              <div class="content">
                <div class="item_bg">
                  <p class="p1">节点数</p>
                  <p class="p2">{{netCount}}</p>
                </div>
              </div>
              <div class="info">
                <div v-for="(item,index) in titles" :key="index">
                  <p class="p1">{{item.title}}</p>
                  <p class="p2">{{item.val}}</p>
                </div>
              </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card class="info-card">
          <div slot="header" class="header">
            <i class="el-icon-s-marketing"></i>
            <span>交易历史</span>
          </div>
          <div   class="text item">
            <echartsline v-if="isshow" :xdata="linedate" :ydata="linenum"></echartsline>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="12" class="row">
      <el-col :span="12" :xs="24">
        <el-card class="record-card">
          <div slot="header" class="header">
            <i class="el-icon-tickets"></i>
            <span>区块</span>
          </div>
          <div   class="text item">
            <div v-for="(item,index) in blocklist" :key="index" class="block_item">
                <div class="left">
                  <p class="p1">区块高度：</p>
                  <p class="p2">{{item.bk_int}}</p>
                  <p class="p3">{{item.seconds}} 秒前</p>
                </div>
                <div class="right">
                  <p class="p1">区块哈希：</p>
                  <p class="p2">
                    <a  @click="showBlock(item)">{{item.hash}}</a>
                  </p>
                  <p class="p3">
                    <a  @click="showBlock(item)">{{item.transnum}}交易</a>
                  </p>
                </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card class="record-card">
          <div slot="header" class="header">
            <i class="el-icon-document-copy"></i>
            <span>最近的交易</span>
          </div>
          <div   class="text item">
            <div v-if="translist.length == 0">暂无数据</div>
            <div v-for="(item,index) in translist" :key="index" class="trans">
              <p class="p1">交易号：<a @click="searchTrans(item.hash)">{{item.hash}}</a></p>
              <p class="p2">交易时间：{{item.timestamp}}</p>
              <p class="p3">
                <el-tag size="mini">发送方</el-tag>
                <el-tooltip :content="item.from" placement="top" effect="light">
                  <span>{{item.from | show}}</span>
                </el-tooltip>

                <i class="el-icon-right"></i>
                <el-tag type="success" size="mini">接收方</el-tag>
                <el-tooltip :content="item.to" placement="top" effect="light">
                  <span>{{item.to | show}}</span>
                </el-tooltip>
              </p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getCurrentTime,subTime} from '@/utils/datetime'
import Echartsline from '@/views/Compoents/Echartsline'
export default {
	components:{
    Echartsline
  },
  data () {
    return {
      time:'',
      para:'',
      netStatus:'',
      netCount:'',
      blockNum:'', //块高度
      blockNum_16:'',
      titles:[],
      blocklist:[],
      translist:[],
      linedate:[],// 折线图
      linenum:[],
      isshow:false
    }
  },
  computed: {

  },
  filters:{
    show(str){
      if(str){
        return str.substring(0,16) + '...' + str.substring(str.length -4)
      }
      return str;
    }
  },
  methods: {
    showBlock:function(item){
      this.$router.push({
        path:'/block',
        query:{value:item.number}
      })
    },
    refresh: function(){
      this.refreshTime()
      this.initData()
      this.findlines()
      setTimeout(() => {
        this.refresh()
      }, 30000);

    },
    refreshTime(){
      this.time = getCurrentTime()
    },
    async initBlock(){
      let list = []
      let trans = []
      let num = parseInt(this.blockNum)
      let para = {
          "pageIdx": 0,
          "pageSize": 10,
          "number": "",  //区块号
          "hash": "" //区块hash
        }
      var {rData} = await this.$api.db.block(para)
      rData.forEach(item=>{
              item.bk_int = parseInt(item.number,16)
              let t = subTime(item.timestamp);
              item.seconds = t
              list.push(item)
      })
      this.blocklist = list

      para = {
        "pageIdx": 0,
        "pageSize": 10,
        "number": "",  //区块号
        "hash": "", //区块hash
        "address": "" //账号
      }
      var {rData} = await this.$api.db.trans(para)
      this.translist = rData

    },
    async findlines(){
      let date = []
      let nums = []
      let that = this
      let para = {
        "pageIdx": 0,
        "pageSize": 20
      }
      this.$api.db.findLine(para).then(res => {
          if(res.iCode != 0) return
          res = res.rData || []
          res.forEach(item=>{
              date.unshift(item.fdate)
              nums.unshift(item.num)
              that.linedate = date
              that.linenum = nums
          })
          that.isshow = true
      })

    },
    async initData(){
       //查询总交易数
      this.$api.db.findLine({ISSUM:'Y'}).then(res => {
        if(res.iCode == 0){
          res = res.rData
          this.titles[0].val = res.maxno
          this.blockNum = res.maxno
          this.titles[1].val = res.tradenum
          this.titles[2].val = res.netstate == 0 ? '正常' :'异常'
          this.titles[3].val = res.peers
          this.netCount = res.peers
          this.initBlock()
        }
      })
    },
    initTitles(){
      this.titles = [
        {title:'块高度',val:''},
        {title:'业务总记录数',val:''},
        {title:'网络状态',val:''},
        {title:'正常/总节点数',val:''}
      ]
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
    },
    searchTrans(val){
      if(!val){
        val = this.para
      }
      this.$router.push({
          path :'/trans',
          query:{
            value:val,
            a:Math.random()
            }
        })
    },
  },
  mounted(){
    this.initTitles()
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
  }
  .info-card{
    height: 300px;
    .header{
      text-align: left;
      color: #1d4978;
      letter-spacing: 1px;
      font-size: 16px;
    }
    .text_body{
      display: flex;
      justify-content: space-between;
      .content{
        width: 50%;
      }
      .item_bg{
        border: #f7bb93 10px solid;
        width: 120px;
        height: 120px;
        border-radius: 50%;
        .p1{
          font-size: 14px;
          color: #ada1a1f6;
        }
        .p2{
          font-size: 26px;
        }
      }
      .info{
        width: 50%;
        display: flex;
        flex-wrap: wrap;
        div{
          width: 50%;
          height: 100px;
          margin: 0px;
          .p1{
            height: 25px;
            font-size: 14px;
            text-align: left;
          }
          .p2{
            font-size: 26px;
            text-align: left;
          }
        }

      }
    }
  }
  .record-card{
    min-height: 300px;
    .header{
      text-align: left;
      color: #1d4978;
      letter-spacing: 1px;
      font-size: 16px;
    }
    .block_item{
      height: 80px;
      display: flex;
      justify-content: space-between;
      .left{
        width: 150px;
        height: 60px;
        line-height: 20px;
        padding: 0px;
        color: #ffffff;

        background-image: linear-gradient(to bottom right, rgb(62, 50, 235), rgba(47, 0, 255, 0.521));
        .p1{
          margin: 0px;
          text-align: left;
          padding-left: 5px;
          font-size: 12px;
        }
        .p2{
          margin:0px;
        }
        .p3{
          margin: 0px;
          text-align: right;
          padding-right: 5px;
          font-size: 12px;
        }
      }
      .right{
        width:70%;
        height: 60px;
        line-height: 20px;
        padding-left: 10px;
        p{
          margin: 0px;
          text-align: left;
          font-size: 14px;
        }
        .p2{
          font-size: 12px;
          color: #107df3;
          a{
            cursor: pointer;
          }
        }
        .p3{
          color: #107df3;
          a{
            cursor: pointer;
          }
        }
      }
    }
    .trans{
        text-align: left;
        border-bottom: 1px solid #e2e2e2;
        line-height: 20px;
        p{
          margin:2px;
        }
        .p1{
          a{
            font-size: 12px;
            color: #107df3 ;
            cursor:pointer;
          }
        }
        .p3{
            span{
              font-size: 14px;
              margin-left: 5px;
              margin-right: 5px;
            }
          }
    }
  }
</style>
