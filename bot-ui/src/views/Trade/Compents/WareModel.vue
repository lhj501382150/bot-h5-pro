<template>
<div>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="商城模式" name="first">
      <el-switch
        style="display: block;margin-bottom: 20px;"
        v-model="wareSc.isOpen"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="开启"
        inactive-text="关闭">
      </el-switch>
      <ware-sc v-if="wareSc.isOpen" :dataForm="wareSc.data" ref="wareScModel"></ware-sc>
    </el-tab-pane>
    <el-tab-pane label="竞价模式" name="second">
      <el-switch
        style="display: block;margin-bottom: 20px;"
        v-model="wareJj.isOpen"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="开启"
        inactive-text="关闭">
      </el-switch>
      <ware-jj v-if="wareJj.isOpen" :dataForm="wareJj.data" ref="wareJjModel"></ware-jj>
    </el-tab-pane>
    <el-tab-pane label="现货预售模式" name="third">
      <el-switch
        style="display: block;margin-bottom: 20px;"
        v-model="wareXhys.isOpen"
        active-color="#13ce66"
        inactive-color="#ff4949"
        active-text="开启"
        inactive-text="关闭">
      </el-switch>
      <ware-xhys v-if="wareXhys.isOpen" :dataForm="wareXhys.data" ref="wareXhysModel"></ware-xhys>
    </el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
import WareSc from "@/views/Trade/Compents/WareSc"
import WareXhys from "@/views/Trade/Compents/WareXhys"
import WareJj from "@/views/Trade/Compents/WareJj"
export default {
name: "WareModel",
props: {
  wareScData:{},
  wareJjData:{},
  wareXhysData:{}
},
components:{
  WareSc,
  WareXhys,
  WareJj
},
data(){
  return {
    activeName: 'first',
    wareSc: {
      isOpen:false,
      data:{}
    },
    wareXhys: {
      isOpen:false,
      data:{}
    },
    wareJj: {
      isOpen:false,
      data:{}
    },

  }
},
methods:{

  //获取商城商品
  getWareScData(){
    let data = {valid:true}
    if(this.wareSc.isOpen){
      data = this.$refs.wareScModel.getFormData()
    }
    return data
  },
  initWareSc(){
      if(this.wareScData == null || this.wareScData.title ==null){
      this.initWareScForm();
    }else{
      this.wareSc.isOpen = true
      this.wareSc.data = this.wareScData
    }
  },
  initWareScForm:function (){
    this.wareSc.data = {
      title:'',
      spec:'',
      flag: 0,
      num: 0,
      salnum: 0,
      price: 0,
      salprice: 0,
      inteprice: 0,
      intenum: 0,
      weight: 0,
      transcomm: 0,
      transcomp:''
    }
  },
  //获取商城商品
  getWareXhysData(){
    let data = null
    if(this.wareXhys.isOpen){
      data = this.$refs.wareXhysModel.getFormData()
    }
    return data
  },
  initWareXhys(){
    if(this.wareXhysData == null){
      this.initWareXhysForm();
    }else{
      this.wareXhys.isOpen = true
      this.wareXhys.data = this.wareXhysData
    }
  },
  initWareXhysForm:function (){
    this.wareXhys.data = {
      title:'',
      seq:1,
      mno: '',
      bno: '',
      point: 1,
      uwflag: 1,
      uwpara: 0,
      hightprice: 0,
      lowprice: 0
    }
  },
  //获取商城商品
  getWareJjData(){
    let data = {valid:true}
    if(this.wareJj.isOpen){
      data = this.$refs.wareJjModel.getFormData()
    }
    return data
  },
  initWareJj(){
    if(this.wareJjData == null){
      this.initWareJjForm();
    }else{
      this.wareJj.isOpen = true
      this.wareJj.data = this.wareJjData
    }
  },
  initWareJjForm:function (){
    this.wareJj.data = {
      title:'',
      seq:1,
      mno: '',
      bno: '',
      unit: '',
      uwflag: 1,
      uwpara: 0,
      hightprice: 0,
      lowprice: 0,
      sprice: 0,
      stepprice: 0,
      stime: '',
      steptime: '',
      step: 0,
      wstatus:0
    }
  }
},
mounted() {
  this.initWareSc()
  this.initWareXhys()
  this.initWareJj()
},
watch:{
  wareScData:{
    immediate: true,    // 这句重要
    handler (val) {
      this.initWareSc()
    }
  },
  wareXhysData:{
    immediate: true,    // 这句重要
    handler (val) {
      this.initWareXhys()
    }
  },
  wareJjData:{
    immediate: true,    // 这句重要
    handler (val) {
      this.initWareJj()
    }
  }
}

}
</script>

<style scoped>

</style>
