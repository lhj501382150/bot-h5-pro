<template>
  <div class="container">
      <!-- 头部区域 -->
      <head-bar></head-bar>
      <el-row>
        <el-col :xs="{span:24,offset:0}" :span="12" :offset="6" class="opera">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <a  @click="goback" class="back"> <i class="el-icon-arrow-left"></i>返回</a>

            <span>合约地址：{{addr}}
               <el-tooltip class="item" effect="dark" content="点击复制" placement="top-start">
                  <i class="el-icon-document-copy" style="cursor:pointer" @click="copyAddr"></i>
                </el-tooltip>
            </span>
          </div>
          <div class="text item">
            <div class="title">资产列表</div>
            <el-popover v-for="(val,key) in tlist" :key="key"
            @show="showPop(val)"
            placement="bottom-start" width="350" trigger="hover" >
            <el-table :data="attrData">
              <el-table-column width="150" property="MEMO" label="属性"></el-table-column>
              <el-table-column width="200" property="WEIGHT" label="数量"></el-table-column>
            </el-table>
            <el-button slot="reference" class="btn_cls">{{val}}</el-button>
          </el-popover>
          </div>
        </el-card>


        </el-col>
      </el-row>
  </div>
</template>

<script>
import HeadBar from "./HeadBar"
import {mapState} from 'vuex'
import {copy} from '@/utils/function'
export default {
  components:{
        HeadBar
  },
  data(){
    return {
      addr:'',
      ctype:'',
      tlist:'',
      attrData:[]
    }
  },
  methods:{
    goback(){
      this.$router.go(-1)
    },
    load(para){
      this.$api.db.token607(para).then(res => {
        if(res.ERRNO == 0){
          res = res.DATA
          this.tlist = res
        }else{
          this.$message.error(res>MSG)
        }
      })
    },
    loadToken(){
    },
    showPop(val){
      this.attrData = []
      let para = {
        ADDRESS: this.addr,
        CTYPE: this.ctype,
        TOKEN: val
      }
      this.$api.db.token608(para).then(res => {
        if(res.ERRNO == 0){
          res = res.DATA
          this.attrData.push(res)
        }else{
          this.$message.error(res>MSG)
        }
      })
    },
    hidePop(val){
    },
    copyAddr(){
      let text = this.addr
      copy(text)
    }
  },
  mounted(){
    let para = this.$route.query
    this.addr = para.ADDRESS
    this.ctype = para.CTYPE
    this.load(para)
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
      background-color: #ffffff;
      .box-card{
        text-align: left;
        .back{
           color: #2f2cf1;
          cursor: pointer;
          margin-right: 50px;
          display: block;
          margin-bottom: 20px;
        }
        .title{
          height: 36px;
          line-height: 36px;
          margin: 10px;
          font-size: 16px;
        }
      }


    }
  }
</style>
