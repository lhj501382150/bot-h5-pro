<template>
<div>
  <el-form :model="dataForm" label-width="150px" :rules="dataFormRules" ref="dataForm" size="small" label-position="right">
        <el-form-item label="商品标题" prop="title">
          <el-input v-model="dataForm.title" :maxlength="50" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="规格" prop="spec">
          <el-input v-model="dataForm.spec" :maxlength="16" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="推广标签" prop="flag">
          <el-radio-group v-model="dataForm.flag">
            <el-radio :label="0">普通</el-radio>
            <el-radio :label="1">特价</el-radio>
            <el-radio :label="2">热卖</el-radio>
            <el-radio :label="4">盲盒</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="数量" prop="num">
          <el-input v-model="dataForm.num" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="销售数量" prop="salnum">
          <el-input v-model="dataForm.salnum" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="原价" prop="price">
          <el-input v-model="dataForm.price" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="折扣价" prop="salprice">
          <el-input v-model="dataForm.salprice" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="积分报价" prop="inteprice">
          <el-input v-model="dataForm.inteprice" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="赠送积分" prop="interew">
          <el-input v-model="dataForm.interew" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="积分购买最大数量" prop="intenum">
          <el-input v-model="dataForm.intenum" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="重量" prop="weight">
          <el-input v-model="dataForm.weight" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="运费" prop="transcomm">
          <el-input v-model="dataForm.transcomm" :maxlength="10" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="物流公司" prop="transcomp">
          <el-input v-model="dataForm.transcomp" :maxlength="16" auto-complete="off"></el-input>
        </el-form-item>
         <el-form-item label="上架状态" prop="wstatus">
          <el-radio-group v-model="dataForm.wstatus">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="2">下架</el-radio>
          </el-radio-group>
        </el-form-item>
  </el-form>
</div>
</template>

<script>
export default {
name: "WareSc",
props:{
  dataForm: {}
},
data(){
  let validInt = (rule, value, callback) => {
    var reg = /^\d*$/;
    if(value){
      if (!reg.test(value)) {
        callback(new Error('输入格式不正确！'))
      }else{
        callback()
      }
    }else{
      callback()
    }
  }
  let validNum = (rule, value, callback) => {
    var reg = /^\d*.?\d+$/;
    if(value){
      if (!reg.test(value)) {
        callback(new Error('输入格式不正确！'))
      }else{
        callback()
      }
    }else{
      callback()
    }
  }
  return {
    dataFormRules:{
      title: [
        { required: true, message: '请输入商品标题', trigger: 'blur' }
      ],
      spec: [
        { required: true, message: '请输入规格', trigger: 'blur' }
      ],
      flag: [
        { required: true, message: '请选择推广标签', trigger: 'blur' }
      ],
      num: [
        { required: true, message: '请输入数量', trigger: 'blur' },
        { validator: validInt, trigger: 'blur' }
      ],
      salnum: [
        { required: true, message: '请输入数量', trigger: 'blur' },
        { validator: validInt, trigger: 'blur' }
      ],
      intenum: [
        { required: true, message: '请输入数量', trigger: 'blur' },
        { validator: validInt, trigger: 'blur' }
      ],
      price: [
        { required: true, message: '请输入原价', trigger: 'blur' },
        { validator: validNum, trigger: 'blur' }
      ],
      salprice: [
        { required: true, message: '请输入折扣价', trigger: 'blur' },
        { validator: validNum, trigger: 'blur' }
      ],
      inteprice: [
        { required: true, message: '请输入积分报价', trigger: 'blur' },
        { validator: validNum, trigger: 'blur' }
      ],
      interew: [
        { required: true, message: '请输入赠送积分', trigger: 'blur' },
        { validator: validNum, trigger: 'blur' }
      ],
      wstatus: [
        { required: true, message: '请选择上架状态', trigger: 'blur' }
      ]
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
     console.log("getWareScFormData",this.dataForm)
     return this.dataForm
  }
},
mounted() {

}

}
</script>

<style scoped>

</style>
