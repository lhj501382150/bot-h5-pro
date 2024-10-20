<template>
<div>
  <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" size="small" label-position="right">
    <el-form-item label=""  >
      <el-switch v-model="editOps" active-color="#13ce66" active-text="编辑" inactive-text="查看"> </el-switch>
    </el-form-item>
    <el-form-item label="订单编号" prop="contno" >
        <el-input v-model="dataForm.contno" :disabled="true" auto-complete="off" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="物流公司" prop="transcomp" >
        <el-input v-model="dataForm.transcomp" :disabled="!editOps" maxlength="16" auto-complete="off"></el-input>
      </el-form-item>
    <el-form-item label="快递单号" prop="transno" >
      <el-input v-model="dataForm.transno" :disabled="!editOps" maxlength="32" auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item label="发货时间" prop="stime" >
      <el-date-picker v-model="dataForm.stime" :disabled="!editOps" type="datetime" placeholder="选择日期时间" value-format="yyyy-MM-dd HH:mm:ss" style="width: 100%"> </el-date-picker>
    </el-form-item>
    <el-form-item label="收货人姓名" prop="recvname" >
      <el-input v-model="dataForm.recvname" :disabled="!editOps" maxlength="16" auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item label="手机号" prop="telno" >
      <el-input v-model="dataForm.telno" :disabled="!editOps" maxlength="16" auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item label="收货地址" prop="recvaddr" >
      <el-input v-model="dataForm.recvaddr" :disabled="!editOps" maxlength="64" auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item label="发货状态" prop="status" >
      <el-select v-model="dataForm.status" :disabled="!editOps" placeholder="发货状态" style="width: 100%;">
        <el-option label="未发货" :value="0"></el-option>
        <el-option label="已发货" :value="1"></el-option>
        <el-option label="已收货" :value="2"></el-option>
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
name: "Sendinfo",
props:{
  dataForm: {}
},
data(){
  return {
    editOps:false,
    size: 'small',
    editLoading: false,
    dataFormRules:{

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
    this.$confirm('确认提交吗？', '提示', {}).then(() => {
      this.editLoading = true
      let params = this.dataForm
      this.$api.contract.editSend(params).then((res) => {
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
},
mounted() {

}
}
</script>

<style scoped>

</style>
