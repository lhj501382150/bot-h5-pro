<template>
  <div class="page-container">
	<!--工具栏-->
   <el-row>
      <el-col class="toolbar" :span="20">
        	<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.userno" placeholder="客户编号"></el-input>
			</el-form-item>
	  <el-form-item>
				<el-input v-model="filters.wareno" placeholder="商品编号"></el-input>
			</el-form-item>
      <el-form-item>
				<el-input v-model="filters.cardno" placeholder="卡券编号"></el-input>
			</el-form-item>
       <el-form-item>
         <el-select v-model="filters.reqstatus"  placeholder="申请状态" clearable>
           <el-option v-for="(item,index) in reqstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:digitartApply:view" type="primary" @click="findPage(null)"/>
			</el-form-item>

		</el-form>
      </el-col>
       <el-col class="toolbar" :span="4">
         <el-form :inline="true" :size="size">
			<el-form-item>
				<el-button-group>
				<el-tooltip content="刷新" placement="top">
					<el-button icon="fa fa-refresh" @click="findPage(null)"></el-button>
				</el-tooltip>
				<el-tooltip content="列显示" placement="top">
					<el-button icon="fa fa-filter" @click="displayFilterColumnsDialog"></el-button>
				</el-tooltip>
				</el-button-group>
			</el-form-item>
		</el-form>
		<!--表格显示列界面-->
		<table-column-filter-dialog ref="tableColumnFilterDialog" :columns="columns"
			@handleFilterColumns="handleFilterColumns">
		</table-column-filter-dialog>
      </el-col>
   </el-row>
	<!--表格内容栏-->
	<kt-table permsEdit="trade:digitartApply:edit" permsDelete="trade:digitartApply:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" @handleAudit="handleAudit" @handleReject="handleReject" >
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size" style="height:500px; overflow:auto"
			label-position="right">
			<el-form-item label="资产编号" prop="artid" v-if="!operation">
				<el-input v-model="dataForm.artid" :disabled="!operation" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno"  auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="商品" prop="wareno" >
        <el-select v-model="dataForm.wareno" style="width:98%" filterable>
           <el-option v-for="(item,index) in warenos" :key="index" :label="item.warename" :value="item.wareno"></el-option>
         </el-select>
      </el-form-item>
			<el-form-item label="卡券编号" prop="cardno" >
				<el-input v-model="dataForm.cardno" auto-complete="off"  maxlength="8"></el-input>
			</el-form-item>
			<el-form-item label="卡券名称" prop="cardname">
				<el-input v-model="dataForm.cardname" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="份数" prop="num" >
         <el-input v-model.number="dataForm.num" auto-complete="off" maxlength="16"></el-input>
      </el-form-item>
       <el-form-item label="手续费" prop="tcomm" >
        <el-input v-model="dataForm.tcomm" auto-complete="off" maxlength="8"></el-input>
      </el-form-item>
        <el-form-item label="性别" prop="cardsex" >
         <el-select v-model="dataForm.cardsex" style="width:98%">
           <el-option v-for="(item,index) in sexs" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
			</el-form-item>
      <el-form-item label="材质" prop="cardrace" >
        <el-select v-model="dataForm.cardrace" style="width:98%">
           <el-option v-for="(item,index) in cardraces" :key="index" :label="item" :value="item"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="等级" prop="cardgrade" >
        <el-select v-model="dataForm.cardgrade" style="width:98%">
           <el-option v-for="(item,index) in cardgrades" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="arttype" >
        <el-select v-model="dataForm.arttype" style="width:98%">
           <el-option v-for="(item,index) in arttypes" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
      </el-form-item>
      <el-form-item label="属性" prop="mem" >
        <el-input v-model="dataForm.mem" auto-complete="off" maxlength="128"></el-input>
      </el-form-item>
      <el-form-item label="图片上传" prop="spath" >
        <upload-img :model="'digitart' + dataForm.artid" :img-path="dataForm.spath" @fillpath="handleFillpath" :key="dataForm.artid"></upload-img>
      </el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import { format } from "@/utils/datetime"
import UploadImg from '@/views/Trade/Compents/UploadImg'
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadImg
	},
	data() {
		return {
      size: 'small',
      filters: {
        userno: '',
        wareno: '',
        cardno: '',
        reqstatus: ''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        artid: [
          {required: true, message: '请输入资产编号', trigger: 'blur'}
        ],
        userno: [
          {required: true, message: '请输入客户编号', trigger: 'blur'}
        ],
        wareno: [
          {required: true, message: '请选择商品', trigger: 'blur'}
        ],
        cardno: [
          {required: true, message: '请输入卡券编号', trigger: 'blur'}
        ],
        cardname: [
          {required: true, message: '请输入卡券名称', trigger: 'blur'}
        ],
        cardsex: [
          {required: true, message: '请选择性别', trigger: 'blur'}
        ],
        cardrace: [
          {required: true, message: '请选择材质', trigger: 'blur'}
        ],
        cardgrade: [
          {required: true, message: '请选择等级', trigger: 'blur'}
        ],
        arttype: [
          {required: true, message: '请选择类型', trigger: 'blur'}
        ],
        num: [
          {required: true, message: '请输入份数', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        artid: '',
        wareno:'',
        userno: '',
        cardno: '',
        cardname: '',
        cardsex: '',
        cardrace: '',
        reqstatus: '',
        spath:'',
        mem: '',
        num:'',
        tcomm:''
      },
      reqstatuss: [
        {key:'1',val:'未审核'},
        {key:'2',val:'已审核'},
        {key:'3',val:'已驳回'}
      ],
      cardraces:[
        "金卡","银卡","铜卡","普通卡"
      ],
      sexs:[
        {key:'0',val:'男'},
        {key:'1',val:'女'}
      ],
      cardgrades:[
        {key:'1',val:'唯一'},
        {key:'2',val:'传奇'},
        {key:'3',val:'传说'},
        {key:'4',val:'史诗'},
        {key:'5',val:'罕见'},
        {key:'6',val:'稀有'},
        {key:'7',val:'常见'},
        {key:'8',val:'普通'}
      ],
      arttypes:[
        {key:'A01',val:'图片'},
        {key:'A02',val:'3D模型'},
        {key:'A03',val:'声音'}
      ],
      warenos:[]
    }
	},
	methods: {
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = {pageNum: 1, pageSize: 50}
      }
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.userdigitartApply.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
     handleFillpath(ret){
		  this.dataForm.spath  = ret.url
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.userdigitartApply.batchDelete(data.params).then(data!=null?data.callback:'')
		},
    handleAudit: function(data){
	    this.$confirm('确认审核吗？', '提示', {}).then(() => {
        let para = data.row
        this.$api.userdigitartApply.audit(para).then(res =>{
          if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
        })
      })
    },
    handleReject: function(data){
       this.$confirm('确认要驳回吗？', '提示', {}).then(() => {
          let para = data.row
          this.$api.userdigitartApply.reject(para).then(res =>{
            if(res.code == 200) {
                    this.$message({ message: '操作成功', type: 'success' })
                  } else {
                    this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                  }
                  this.findPage(null)
          })
        })
    },
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        artid: '',
        userno: '',
        cardno: '',
        cardname: '',
        cardsex: '',
        cardrace: '',
        reqstatus: '',
        spath:'',
        mem: '',
        cardgrade:'',
        arttype:'',
        num:'',
        wareno:'',
        tcomm:''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.dialogVisible = true
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.userdigitartApply.save(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
              })
            }else{
              this.$api.userdigitartApply.edit(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage(null)
              })
            }

					})
				}
			})
		},

		// 是否有效格式化
  sexFormat: function (row, column, cellValue, index){
    if(cellValue==='0') {
      return "男"
    } else {
      return "女"
    }
	},
  statusFormat: function (row, column, cellValue, index){
    let item = this.reqstatuss.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
  gradeFormat: function (row, column, cellValue, index){
    let item = this.cardgrades.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
  arttypeFormat: function (row, column, cellValue, index){
    let item = this.arttypes.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
   upstatusFormat: function (row, column, cellValue, index){
    let item = this.upstatuss.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
		// 处理表格列过滤显示
	displayFilterColumnsDialog: function () {
			this.$refs.tableColumnFilterDialog.setDialogVisible(true)
	},
		// 处理表格列过滤显示
	handleFilterColumns: function (data) {
			this.filterColumns = data.filterColumns
			this.$refs.tableColumnFilterDialog.setDialogVisible(false)
	},
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"artid", label:"资产编号", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
				{prop:"username", label:"客户名称", minWidth:120},
        {prop:"wareno", label:"商品编号", minWidth:120},
        {prop:"warename", label:"商品名称", minWidth:120},
        {prop:"num", label:"份数", minWidth:150},
        {prop:"tcomm", label:"手续费", minWidth:150},
				{prop:"cardno", label:"卡券编号", minWidth:100},
				{prop:"cardname", label:"卡券名称", minWidth:120},
				{prop:"cardsex", label:"性别", minWidth:100,formatter:this.sexFormat},
				{prop:"cardrace", label:"材质", minWidth:100},
				{prop:"cardgrade", label:"等级", minWidth:100,formatter:this.gradeFormat},
				{prop:"arttype", label:"类型", minWidth:100,formatter:this.arttypeFormat},
				{prop:"reqstatus", label:"申请状态", minWidth:100,formatter:this.statusFormat},
				{prop:"reqtime", label:"时间", minWidth:150},
				{prop:"mem", label:"属性", minWidth:100},
				{prop:"errmg", label:"上链反馈", minWidth:150}
			]
			this.filterColumns = this.columns;
	},
  initButtons(){
     this.buttons=[
		    {icon:'fa fa-check',label:'审核',perms:'trade:digitartApply:audit',type:'success',callback:'handleAudit'},
		    {icon:'fa fa-times',label:'驳回',perms:'trade:digitartApply:reject',type:'warning',callback:'handleReject'}
      ];
  },
  initDict(){
    this.$api.ware.findList({}).then(res => {
      this.warenos = res.data
    })
  }
	},
	mounted() {
		this.initColumns()
    this.initButtons()
    this.initDict()
	}
}
</script>

<style scoped>

</style>
