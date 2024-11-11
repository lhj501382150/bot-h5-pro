<template>
  <div class="container">
      <!-- 头部区域 -->
      <head-bar></head-bar>
      <el-row>
        <el-col :xs="{span:24,offset:0}" :span="12" :offset="6" class="opera">
          <div class="form">
          <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix">
            <span>{{type ==1 ? '登录':'注册'}}</span>
          </div>
          <div class="text item" v-if="type==1">
            <el-form label-position="right" label-width="80px" :model="dataform" ref="dataform" :rules="rules">
              <el-form-item label="账号" prop="USERNO">
                <el-input v-model="dataform.USERNO" prefix-icon="el-icon-user"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="PWD">
                <el-input v-model="dataform.PWD" type="password" prefix-icon="el-icon-lock"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm">登录</el-button>
                <el-button   @click="registUI">去注册<i class="el-icon-arrow-right"></i></el-button>
              </el-form-item>
            </el-form>
          </div>
          <div class="text item" v-if="type==2">
            <el-form label-position="right" label-width="80px" :model="dataform" ref="dataform" :rules="rules">
              <el-form-item label="账号" prop="USERNO">
                <el-input v-model="dataform.USERNO" prefix-icon="el-icon-user"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="PWD">
                <el-input v-model="dataform.PWD" type="password" prefix-icon="el-icon-lock"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="regist">注册</el-button>
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
        USERNO:'',
        PWD:''
      },
      rules: {
        USERNO: [
            { required: true, message: '请输入账号', trigger: 'change' }
          ],
        PWD: [
          { required: true, message: '请输入密码', trigger: 'change' }
        ]
      }
    }
  },
  methods:{
    setUser(addr){
      this.$store.commit('login',this.dataform.USERNO)
      this.$store.commit('setAddr',addr)
    },
    submitForm(){
      this.$refs.dataform.validate((valid) => {
				if (valid) {
          let para = Object.assign({},this.dataform)
          this.$api.db.login(para).then(res => {
            if(res.ERRNO && res.ERRNO != 0){
              this.$message({
                type:'error',
                message:res.MSG
              })
            }else{
              res = res.DATA
              this.setUser(res.ADDRESS)
              this.$message({
                type:'success',
                message:'登录成功'
              })
              this.type = 1
              let para = this.$route.query
              if(para.redirect){
                this.$router.push({
                  path:para.redirect
                })
              }else{
                this.$router.push({
                  path:"/"
                })
              }

            }

          })
        }
      })
    },
    registUI(){
      this.type = 2
    },
    regist(){
      this.$refs.dataform.validate((valid) => {
				if (valid) {
          let para = Object.assign({},this.dataform)
          this.$api.db.regist(para).then(res => {
            if(res.ERRNO == 0){
              this.$message({
                type:'success',
                message:'注册成功，请登录'
              })
              this.type = 1
            }else{
              this.$message({
                type:'error',
                message:res.MSG
              })
            }

          })
        }
      })
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
      height: 80%;
      margin-top: 30px;
      background-color: #ffffff;
      .form{
        width: 350px;
        margin: 120px auto;
      }
    }
  }
</style>
