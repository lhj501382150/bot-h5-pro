<template>
  <el-card class="box-card" shadow="never">
    <div slot="header" class="clearfix">
      <span>手工调账</span>
    </div>
    <div  class="text item">
      <el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size" label-position="right">
        <el-form-item label="客户编号" prop="userno" >
          <el-select v-model="dataForm.userno" style="width: 100%" filterable clearable @change="getUserMoney">
            <el-option v-for="item in users" :key="item.userno" :value="item.userno" :label="item.userno + '-' + item.username  + '-' + item.nickname"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="当前余额" >
          <el-input v-model="dataForm.leftmoney" :readonly="true"></el-input>
        </el-form-item>
        <!-- <el-form-item label="账户编号" prop="acctno" >
          <el-select v-model="dataForm.acctno" style="width: 100%">
            <el-option v-for="item in accts" :key="item.acctno" :value="item.acctno" :label="item.acctname"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="科目编号" prop="subno">
          <el-select v-model="dataForm.subno" style="width: 100%">
            <el-option v-for="item in subs" :key="item.sunno" :value="item.sunno" :label="item.subname"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="变动金额" prop="chmoney"  >
          <el-input v-model="dataForm.chmoney" maxlength="8" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述信息" prop="memo"  >
          <el-input v-model="dataForm.memo" maxlength="64" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :size="size" @click.native="clearForm">{{$t('action.cancel')}}</el-button>
        <el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  data(){
    var validateNumber = (rule, value, callback) => {
      var regPos = /^\d+(\.\d+)?$/; //非负浮点数
      if (regPos.test(value)) {
        callback();
      } else {
        callback(new Error('变动金额只能输入数字'));
      }
    };
    return {
      size: 'small',
      editLoading: false,
      dataFormRules: {
        userno: [
          { required: true, message: '请输入客户编号', trigger: 'blur' }
        ],
        // acctno: [
        //   { required: true, message: '请选择账户', trigger: 'blur' }
        // ],
        subno: [
          { required: true, message: '请选择科目', trigger: 'blur' }
        ],
        chmoney: [
          { required: true, message: '请输入变动金额', trigger: 'blur' },
          {validator:validateNumber,trigger: 'blur'}
        ],
        // memo: [
        //   { required: true, message: '请输入描述信息', trigger: 'blur' }
        // ]
      },
      // 新增编辑界面数据
      dataForm: {
        userno: '',
        acctno: 100,
        subno: '',
        chmoney: 0,
        exten: '',
        memo: '',
        leftmoney:0
      },
      accts:[],
      subs:[],
      users:[]

    }
  },
  methods:{
    getUserMoney(val){
      this.leftmoney = 0;
      if(val){
        let para = {
          userno:val,
          acctno:'100'
        }
        this.$api.usermoney.getOne(para).then(res =>{
          this.dataForm.leftmoney = res.data.balance
        })
      }
    },
    //查询账本信息
    findAcct(){
      let para = {}
      this.$api.account.findList(para).then(res =>{
        this.accts = res.data
      })
    },
    findSubs(){
      let para = {}
      this.$api.subject.findList(para).then(res =>{
        this.subs = res.data
      })
    },
    findUser(){
      let para = {}
      this.$api.firm.findList(para).then(res =>{
        this.users = res.data
      })
    },
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交吗？', '提示', {}).then(() => {
            this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            this.$api.changeapply.save(params).then((res) => {
              this.editLoading = false
              if(res.code == 200) {
                this.$message({ message: '操作成功', type: 'success' })
                this.$refs['dataForm'].resetFields()
                this.dataForm.leftmoney = 0
              } else {
                this.$message({message: '操作失败, ' + res.msg, type: 'error'})
              }
            })

          })
        }
      })
    },
    clearForm(){
      this.$refs['dataForm'].resetFields()
    }
  },
  mounted() {
    this.findAcct()
    this.findSubs()
    this.findUser()
  }
}
</script>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}
.clearfix{
  text-align: left;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 50%;
}
</style>
