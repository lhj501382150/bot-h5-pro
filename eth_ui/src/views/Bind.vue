<template>
  <div class="container">
      <!-- 头部区域 -->
      <head-bar></head-bar>
      <el-row>
        <el-col :xs="{span:24,offset:0}" :span="12" :offset="6" class="opera">
        <div class="back">
            <a  @click="goback" class="back"> <i class="el-icon-arrow-left"></i>返回</a>
          </div>
        <div class="form">
          <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span>绑定合约</span>
          </div>
          <div class="text item">
            <el-form label-position="right" label-width="80px" :model="dataform" ref="dataform" :rules="rules">
              <el-form-item label="合约地址" prop="ADDRESS">
                <el-input v-model="dataform.ADDRESS" placeholder="请输入合约地址"></el-input>
              </el-form-item>
              <el-form-item label="合约类型" prop="CTYPE">
                <el-select v-model="dataform.CTYPE" placeholder="请选择" style="width:100%">
                  <el-option
                    v-for="item in types"
                    :key="item.key"
                    :label="item.val"
                    :value="item.key">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm">提交</el-button>
                <el-button  @click="registUI">重置</el-button>
              </el-form-item>
            </el-form>
          </div>

        </el-card>

        </div>
        </el-col>
      </el-row>
  </div>
</template>

<script>
import HeadBar from "./HeadBar"
import {mapState} from 'vuex'
export default {
  components:{
        HeadBar
  },
  data(){
    return {
      title:'登录',
      type:1,
      dataform:{
        ADDRESS:'',
        CTYPE:''
      },
      rules: {
        ADDRESS: [
            { required: true, message: '请输入合约地址', trigger: 'change' }
          ],
        CTYPE: [
          { required: true, message: '请选择合约类型', trigger: 'change' }
        ]
      },
      types:[
        {key:'0',val:'标准'},
        {key:'1',val:'ERC20'},
        {key:'2',val:'ERC721'}
      ]
    }
  },
  methods:{
    goback(){
      this.$router.go(-1)
    },
    submitForm(){
      this.$refs.dataform.validate((valid) => {
				if (valid) {
          let para = Object.assign({},this.dataform)
          this.$api.db.bindCont(para).then(res => {
            if(res.ERRNO && res.ERRNO != 0){
              this.$message({
                type:'error',
                message:res.MSG
              })
            }else{
              this.setUser(res.ADDRESS)
              this.$message({
                type:'success',
                message:'绑定成功'
              })
              this.$refs['dataform'].resetFields()
            }

          })
        }
      })
    },
    registUI(){
      this.$refs['dataform'].resetFields()
    }
  },
  computed:{
    ...mapState({

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
    bottom: 0px;
    background: #ebeef5;
    .opera{
      height: 600px;
      padding-top: 30px;
      margin-top: 20px;
      background-color: #ffffff;
      .back{
           color: #2f2cf1;
          cursor: pointer;
          text-align: left;
          padding-left: 20px;
        }
      .form{
        width: 350px;
        margin: 120px auto;
      }
    }
  }
</style>
