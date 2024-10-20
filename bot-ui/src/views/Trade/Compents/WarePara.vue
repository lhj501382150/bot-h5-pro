<template>
<div>
  <el-form label-width="150px"  size="small" label-position="right">
        <el-form-item v-for="(item,index) in paras" :key="index" :label="item.paraname" :prop="item.paraval">
          <el-input v-if="!item.listData" v-model="item.paraval"  auto-complete="off"></el-input>
          <el-select v-else v-model="item.paraval" style="width: 100%">
            <el-option v-for="opt in item.items" :key="opt.key" :value="opt.key" :label="opt.val"></el-option>
          </el-select>
        </el-form-item>
  </el-form>
</div>
</template>

<script>
export default {
name: "WareAttr",
props:{
  paras: {
    type: Array,
    default:[]
  }
},
data(){
  return {

  }
},
computed:{

},
methods:{
  getWareParaData(){
    return this.paras
  }
},
mounted() {

}

}
</script>

<style scoped>

</style>
