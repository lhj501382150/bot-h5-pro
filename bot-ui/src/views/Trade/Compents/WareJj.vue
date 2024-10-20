<template>
<div>
  <el-form :model="dataForm" label-width="150px" :rules="dataFormRules" ref="dataForm" size="small" label-position="right">
        <el-form-item label="商品标题" prop="title">
          <el-input v-model="dataForm.title"  auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="显示顺序" prop="seq">
          <el-input v-model.number="dataForm.seq" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="市场编号" prop="mno">
          <el-input v-model="dataForm.mno" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="板块编号" prop="bno">
          <el-input v-model="dataForm.bno" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="报价单位名称" prop="unit">
          <el-input v-model="dataForm.unit" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="涨跌幅类型" prop="uwflag">
          <el-select v-model="dataForm.uwflag" placeholder="请选择" style="width: 100%;">
            <el-option label="定量" :value="1"></el-option>
            <el-option label="比例" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="涨跌幅参数" prop="uwpara">
          <el-input v-model="dataForm.uwpara" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="最高限价" prop="hightprice">
          <el-input v-model="dataForm.hightprice" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="最低限价" prop="lowprice">
          <el-input v-model="dataForm.lowprice" auto-complete="off"></el-input>
        </el-form-item>
       <!-- <el-form-item label="起竞价" prop="sprice">
          <el-input v-model="dataForm.sprice" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="竞价幅" prop="stepprice">
          <el-input v-model="dataForm.stepprice" auto-complete="off"></el-input>
        </el-form-item>
         <el-form-item label="竞价开始时间" prop="stime">
          <el-date-picker v-model="dataForm.stime"
                          format="yyyy-MM-dd HH:mm:ss"  value-format="yyyy-MM-dd HH:mm:ss"
                          type="datetime" placeholder="选择竞价开始时间" style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="竞价倒计时时间" prop="steptime">
          <el-date-picker v-model="dataForm.steptime"
                          format="yyyy-MM-dd HH:mm:ss"  value-format="yyyy-MM-dd HH:mm:ss"
                          type="datetime" placeholder="选择竞价倒计时时间" style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="倒计时长(秒)" prop="step">
          <el-input v-model.number="dataForm.step" auto-complete="off"></el-input>
        </el-form-item> -->
        <el-form-item label="状态" prop="wstatus">
          <el-select v-model="dataForm.wstatus" placeholder="请选择" style="width: 100%;">
            <el-option label="未上架" :value="0"></el-option>
            <el-option label="上架" :value="1"></el-option>
            <el-option label="下架" :value="2"></el-option>
          </el-select>
        </el-form-item>
  </el-form>
</div>
</template>

<script>
import Format from '@/utils/validate'
export default {
name: "WareJj",
props:{
  dataForm: {}
},
data(){
  return {
    dataFormRules:{
      title: [
        { required: true, message: '请输入商品标题', trigger: 'blur' }
      ],
      seq: [
        { required: true, message: '请输入显示顺序', trigger: 'blur' }
      ],
      mno: [
        { required: true, message: '请输入市场编号', trigger: 'blur' }
      ],
      bno: [
        { required: true, message: '请输入板块编号', trigger: 'blur' }
      ]
      // stime: [
      //   { required: true, message: '请输入竞价开始时间', trigger: 'blur' }
      // ],
      // steptime: [
      //   { required: true, message: '请输入竞价倒计时时间', trigger: 'blur' }
      // ],
      // step: [
      //   { required: true, message: '请输入倒计时长', trigger: 'blur' }
      // ],
      // uwflag: [
      //   { required: true, message: '请选择涨跌幅类型', trigger: 'blur' }
      // ],
      // uwpara: [
      //   { required: true, message: '请输入涨跌幅参数', trigger: 'blur' },
      //   { validator:Format.FormValidate.Form().Number, trigger: 'blur' }
      // ],
      // sprice: [
      //   { required: true, message: '请输入起竞价', trigger: 'blur' },
      //   { validator:Format.FormValidate.Form().Number, trigger: 'blur' }
      // ],
      // stepprice: [
      //   { required: true, message: '请输入竞价幅', trigger: 'blur' },
      //   { validator:Format.FormValidate.Form().Number, trigger: 'blur' }
      //]
    }
  }
},
methods:{
  getFormData(){
    let res = false
    this.$refs.dataForm.validate(valid =>{
      res = valid
    })
    this.dataForm.valid = res
    return this.dataForm
  }
},
mounted() {

}

}
</script>

<style scoped>

</style>
