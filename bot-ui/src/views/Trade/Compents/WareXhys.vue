<template>
<div>
  <el-form :model="dataForm" label-width="150px" :rules="dataFormRules" ref="dataForm" size="small" label-position="right">
        <el-form-item label="商品标题" prop="title">
          <el-input v-model="dataForm.title"  auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="显示顺序" prop="seq">
          <el-input v-model="dataForm.seq" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="市场编号" prop="mno">
          <el-input v-model="dataForm.mno" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="板块编号" prop="bno">
          <el-input v-model="dataForm.bno" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="交易系数" prop="point">
          <el-input v-model="dataForm.point" auto-complete="off"></el-input>
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
  </el-form>
</div>
</template>

<script>
export default {
name: "WareXhys",
props:{
  dataForm: {}
},
data(){
  return {
    dataFormRules:{

    }
  }
},
methods:{
  getFormData(){
    return this.dataForm
  }
},
mounted() {

}

}
</script>

<style scoped>

</style>
