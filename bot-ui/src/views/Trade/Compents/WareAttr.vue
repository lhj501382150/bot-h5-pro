<template>
<div>
  <el-alert
    v-if="!isEmpty"
    title="分类未设置属性信息"
    type="error"
    center
    :closable="false"
    show-icon>
  </el-alert>
  <el-form label-width="150px"  size="small" label-position="right">
        <el-form-item v-for="(item,index) in attrs" :key="index" :label="item.attrName" :prop="item.attrVal">
          <el-input v-model="item.attrVal"  auto-complete="off"></el-input>
        </el-form-item>
  </el-form>
</div>
</template>

<script>
export default {
name: "WareAttr",
props:{
  attrs: {
    type: Array,
    default:[]
  }
},
data(){
  return {

  }
},
computed:{
  isEmpty(){
    if(this.attrs){
      return this.attrs.length >0
    }
  }
},
methods:{
  getWareAttrData(){
    return this.attrs
  }
},
mounted() {

}

}
</script>

<style scoped>

</style>
