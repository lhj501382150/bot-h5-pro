<template>
  <div>
    <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" size="small" label-position="right">
      <el-form-item label=""  >
        <el-switch v-model="editOps" active-color="#13ce66" active-text="编辑" inactive-text="查看"> </el-switch>
      </el-form-item>
      <el-form-item label="订单编号" prop="contno" >
        <el-input v-model="dataForm.contno" :disabled="true" auto-complete="off" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="客户编号" prop="userno" >
        <el-input v-model="dataForm.userno" :disabled="true" auto-complete="off" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="type" >
        <el-select v-model="dataForm.type" :disabled="!editOps" placeholder="开票类型" style="width: 100%;">
          <el-option label="个人" value="1"></el-option>
          <el-option label="单位" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="发票抬头信息" prop="invoictitle" >
        <el-input v-model="dataForm.invoictitle" :disabled="!editOps" maxlength="16" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="单位税号" v-if="dataForm.type==2" prop="dutyno" >
        <el-input v-model="dataForm.dutyno" :disabled="!editOps" maxlength="32" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="注册地址" prop="regaddr" >
        <el-input v-model="dataForm.regaddr" :disabled="!editOps" maxlength="16" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="注册电话" prop="regtelno" >
        <el-input v-model="dataForm.regtelno" :disabled="!editOps" maxlength="16" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="开户银行" prop="openbank" >
        <el-input v-model="dataForm.openbank" :disabled="!editOps" maxlength="64" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="银行账号" prop="bankno" >
        <el-input v-model="dataForm.bankno" :disabled="!editOps" maxlength="64" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="开票状态" prop="status" >
        <el-select v-model="dataForm.status" :disabled="!editOps" placeholder="开票状态" style="width: 100%;">
          <el-option label="未开" value="0"></el-option>
          <el-option label="已开" value="1"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button :size="size" @click.native="closeDialog">{{$t('action.cancel')}}</el-button>
      <el-button :size="size" type="primary" :disabled="!editOps" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "Invoic",
  props:{
    dataForm: {}
  },
  data(){
    return {
      editOps:false,
      size: 'small',
      editLoading: false,
      dataFormRules:{
        contno: [
          { required: true, message: '请输入订单编号', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择开票类型', trigger: 'blur' }
        ],
        invoictitle: [
          { required: true, message: '请输入发票抬头信息', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请确认开票状态', trigger: 'blur' }
        ]
      }
    }
  },
  methods:{
    getFormData(){
      return this.dataForm
    },
    closeDialog: function (){
      this.$emit("closeDialog",{});
    },

    // 编辑
    submitForm: function () {
      if(!this.dataForm.contno){
        this.$message.error("未获取合同编号")
        return
      }
      this.$refs.dataForm.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.editLoading = true
              let params = this.dataForm
              this.$api.contract.editInvoic(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.$emit("closeDialog",{});
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
              })

            })
          }
      })

    }
  },
  mounted() {

  }
}
</script>

<style scoped>

</style>
