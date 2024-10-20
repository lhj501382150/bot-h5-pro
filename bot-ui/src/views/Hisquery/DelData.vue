<template>
  <el-card class="box-card" shadow="never">
    <div slot="header" class="clearfix">
      <span>历史数据清理</span>
    </div>
    <div  class="text item">
      <el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size" label-position="right">
        <el-form-item label="开始日期" prop="bdate" >
          <el-date-picker
            v-model="dataForm.bdate"
            type="date"
            align="right"
            unlink-panels
            placeholder="开始日期"
            value-format="yyyy-MM-dd"
            style="width:100%">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束日期" prop="edate" >
          <el-date-picker
            v-model="dataForm.edate"
            type="date"
            align="right"
            unlink-panels
            placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width:100%">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :size="size" @click.native="clearForm">{{$t('action.cancel')}}</el-button>
        <el-button :size="size" type="danger" @click.native="submitForm" :loading="editLoading">删 除</el-button>
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  data(){
    return {
      size: 'small',
      editLoading: false,
      dataFormRules: {
        bdate: [
          { required: true, message: '请输入开始日期', trigger: 'blur' }
        ],
        edate: [
          { required: true, message: '请输入结束日期', trigger: 'blur' }
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        bdate: '',
        edate: ''
      }

    }
  },
  methods:{
     
    submitForm: function () {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认删除吗？', '提示', {}).then(() => {
            this.editLoading = true
            let params = Object.assign({}, this.dataForm)
            this.$api.hisorder.delData(params).then((res) => {
              this.editLoading = false
              if(res.code == 200) {
                this.$message({ message: '操作成功', type: 'success' })
                this.$refs['dataForm'].resetFields() 
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
