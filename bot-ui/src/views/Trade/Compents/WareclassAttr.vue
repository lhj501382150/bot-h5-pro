<template>
<div>
  <el-table :data="attrcols" header-align="center" align="center" size="small"  style="width:100%;">
    <el-table-column type="index" label="序号" min-width="60" width="60"></el-table-column>
    <el-table-column label="属性编号" min-width="60" width="240">
      <template slot-scope="scop">
        <el-input v-model="scop.row.attrKey" placeholder="属性编号"  size="small" auto-complete="off"></el-input>
      </template>
    </el-table-column>
    <el-table-column label="属性名称" min-width="60" width="240">
      <template slot-scope="scop">
        <el-input v-model="scop.row.attrName" placeholder="属性名称"  size="small" auto-complete="off"></el-input>
      </template>
    </el-table-column>
    <el-table-column label="显示顺序" min-width="60" width="100">
      <template slot-scope="scop">
        <el-input v-model="scop.row.seq" placeholder="显示顺序"  size="small" auto-complete="off"></el-input>
      </template>
    </el-table-column>
    <el-table-column align="right" label="操作">
      <template slot-scope="scope">
        <el-button
          size="mini"
          type="danger"
          @click="handleDelete(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <div>
    <el-button size="small" type="primary" @click.native="addAttr">添加</el-button>
  </div>
</div>
</template>

<script>
export default {
  name: "WareclassAttr",
  props:{
    attrcols:{
      type:Array,
      default:[]
    }
  },
  data(){
    return {
      attrs:[] //商品属性信息
    }
  },
  methods:{
    addAttr(){
      let attr = {
        attrKey:'',
        attrName:'',
        seq:this.attrcols.length +1
      }
      this.attrcols.push(attr)
    },
    handleDelete(index, row) {
      this.attrcols.splice(index,1)
    },
    getAttr(){
      let attrs = this.attrcols
      if(attrs && attrs.length>0){
        for(var i=0; i< attrs.length;i++){
          var attr = attrs[i];
          if(!attr.attrKey || !attr.attrName){
            this.$message({
              message: '属性信息不能为空',
              type: 'error'
            })
            return false
          }
        }
      }
      return this.attrcols
    }
  }

}
</script>

<style scoped>

</style>
