<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item v-if="user.type==0">
				<el-input v-model="filters.userno" placeholder="客户编号" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.sname" placeholder="持卡人姓名" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.bankname" placeholder="银行名称" maxlength="16"></el-input>
      </el-form-item>
    <el-form-item>
        <el-select v-model="filters.isauth" placeholder="鉴权状态" clearable>
          <el-option v-for="(item,index) in isauths" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
       <el-form-item>
        <el-select v-model="filters.sigstatus" placeholder="签约状态" clearable>
          <el-option v-for="(item,index) in sigstatuss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="money:usercard:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="money:usercard:add" type="primary" @click="handleAdd" />
			</el-form-item>
		</el-form>
	</div>
	<div class="toolbar" style="float:right;padding-top:10px;padding-right:15px;">
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
	</div>
	<!--表格内容栏-->
		<!--表格内容栏-->
	<kt-table permsEdit="money:usercard:edit" permsDelete="money:usercard:del" :buttons="buttons"
		:data="pageResult" :columns="filterColumns" @handleAuth="handleAuth" @handleReject="handleReject"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="编号" prop="id" v-if="!operation">
				<el-input v-model="dataForm.id" :disabled="!operation" auto-complete="off" maxlength="8"></el-input>
			</el-form-item>
       <el-form-item label="客户编号" prop="userno" v-if="user.type==0">
          <el-input v-model="dataForm.userno" :disabled="!operation" auto-complete="off"></el-input>
        </el-form-item>
			<el-form-item label="持卡人姓名" prop="sname" >
				<el-input v-model="dataForm.sname" auto-complete="off" maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="银行卡号" prop="cardno" >
				<el-input v-model="dataForm.cardno" auto-complete="off" maxlength="32"></el-input>
			</el-form-item>
      <el-form-item label="银行名称" prop="bankname" >
				<el-input v-model="dataForm.bankname" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
		 <el-form-item label="开户行所在地" prop="openbank">
        <el-cascader v-model="openbank" :options="provinces" :props="props" style="width: 100%" ref="cascaderAddr" @change="handleAreaLabel"></el-cascader>
			</el-form-item>
      <el-form-item label="开户支行" prop="subbank" >
				<el-input v-model="dataForm.subbank" auto-complete="off" maxlength="25"></el-input>
			</el-form-item>
			<el-form-item label="预留手机号" prop="telno">
				<el-input v-model="dataForm.telno" auto-complete="off" maxlength="16"></el-input>
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
import {getStatus} from "@/utils/status"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import {mapState} from 'vuex'
export default {
	components:{
		PopupTreeInput,
		KtButton,
		TableColumnFilterDialog,
    KtTable
	},
  computed:{
    ...mapState({
      user: state => state.user.curUser
    })
},
	data() {
		return {
			size: 'small',
			filters: {
				userno: '',
        sname: '',
        bankname:'',
        sigstatus:''
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {
			  content: []
      },
			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
      buttons:[],
			dataFormRules: {
				userno: [
					{ required: true, message: '请输入客户编号', trigger: 'blur' }
				],
        sname: [
          { required: true, message: '请输入科目名称', trigger: 'blur' }
        ],
        cardno: [
          { required: true, message: '请输入银行卡号', trigger: 'blur' }
        ],
        bankname: [
          { required: true, message: '请输入银行名称', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
        userno: '',
        sname: '',
        cardno: '',
        bankname: '',
        openbank: '',
        telno: '',
        subbank: ''
			},
      provinces: [],
      props: {
        value: 'name',
        label: 'name'
      },
      openbank:'',
      isauths:[
        {sval:'N',sshow:'未鉴权'},
        {sval:'Y',sshow:'已鉴权'}
      ],
      sigstatuss:[
        {sval:'0',sshow:'未签约'},
        {sval:'1',sshow:'已签约'},
        {sval:'2',sshow:'签约失败'},
        {sval:'3',sshow:'签约中'}
      ]
		}
	},
	methods: {
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        this.pageRequest = { pageNum: 1, pageSize: 50 }
      }
      this.pageRequest.params = Object.assign({},this.filters)
			//this.pageRequest = Object.assign({},this.filters)
			this.$api.usercard.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
        this.handleSelectArea()
			}).then(data!=null?data.callback:'')
		},
    //查询省市县
    handleSelectArea(){
		  let para = {parentid:0}
      this.$api.area.findList(para).then(res =>{
          this.provinces = res.data
      })
    },
    setSelectArea(){
		  if(!this.$refs.cascaderAddr) return
      this.$refs.cascaderAddr.panel.checkedValue = [];//也可以是指定的值，默认返回值是数组，也可以返回单个值
      this.$refs.cascaderAddr.panel.activePath = [];
      this.$refs.cascaderAddr.panel.syncActivePath();
    },
    handleAreaLabel(row){
      const nodes  = this.$refs['cascaderAddr'].getCheckedNodes()
      if(nodes && nodes.length > 0){
        const area = nodes[0].pathLabels;
        if(area.length === 3){
          this.dataForm.openbank = area[0] + '-' + area[1] + '-' +area[2]
        }else if(area.length === 2){
          this.dataForm.openbank = area[0] + '-' + area[1]
        }
      }
    },
    initAreaid(){
      let areaid = this.dataForm.openbank
      if(areaid ){
        this.openbank = areaid.split('-')
      }
    },
    handleAuth: function(data){
      this.$confirm('确认要鉴权吗？', '提示', {}).then(() => {
          let para = data.row
          this.$api.usercard.auth(para).then(res =>{
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
          this.$api.usercard.reject(para).then(res =>{
            if(res.code == 200) {
                    this.$message({ message: '操作成功', type: 'success' })
                  } else {
                    this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                  }
                  this.findPage(null)
          })
        })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.usercard.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
      this.setSelectArea()
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
         userno: '',
        sname: '',
        cardno: '',
        bankname: '',
        openbank: '',
        telno: '',
        subbank: ''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
			this.dataForm = Object.assign({}, params.row)
      this.initAreaid()
		},
		// 编辑
		submitForm: function () {

			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.usercard.save(params).then((res) => {
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
              this.$api.usercard.edit(params).then((res) => {
                this.editLoading = false
                if(res.code == 200) {
                  this.$message({ message: '操作成功', type: 'success' })
                  this.dialogVisible = false
                  this.$refs['dataForm'].resetFields()
                } else {
                  this.$message({message: '操作失败, ' + res.msg, type: 'error'})
                }
                this.findPage({pageRequest:this.pageRequest})
              })
            }

					})
				}
			})
		},

    // 是否有效格式化
  fieldFormat: function (row, column, cellValue, index){
    var arr =this.clcfields.filter(function(item, index, array){
      return cellValue.indexOf(item.key) > -1
    }).map((item)=>{
        return item.val
    })
    if(arr) return arr.toString()
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
  authFormat: function (row, column, cellValue, index){
    return getStatus(cellValue,this.isauths)
  },
  sigstatusFormat: function (row, column, cellValue, index){
    return getStatus(cellValue,this.sigstatuss)
  },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"sname", label:"持卡人姓名", minWidth:120},
				{prop:"cardno", label:"银行卡号", minWidth:120},
				{prop:"bankname", label:"银行名称", minWidth:120},
				{prop:"openbank", label:"开户行所在地", minWidth:120},
        {prop:"isauth", label:"鉴权状态", minWidth:120,formatter:this.authFormat},
        {prop:"sigstatus", label:"签约状态", minWidth:120,formatter:this.sigstatusFormat}
			]
			this.filterColumns = this.columns;
	},
  initButtons(){
     this.buttons=[
		    {icon:'fa fa-edit',label:'鉴权',perms:'money:usercard:auth',type:'success',callback:'handleAuth'},
        {icon:'fa fa-times',label:'签约驳回',perms:'money:usercard:reject',type:'warning',callback:'handleReject'}
      ];
  },
	},
	mounted() {
		this.initColumns()
    this.initButtons()
	}
}
</script>

<style scoped>

</style>
