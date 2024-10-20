<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.loginno" placeholder="登陆编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="客户编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.loginname" placeholder="用户名称"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="firm:trader:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="firm:trader:add" type="primary" @click="handleAdd" />
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
				<!--<el-tooltip content="导出" placement="top">
					<el-button icon="fa fa-file-excel-o" @click="exportUserExcelFile"></el-button>
				</el-tooltip>-->
				</el-button-group>
			</el-form-item>
		</el-form>
		<!--表格显示列界面-->
		<table-column-filter-dialog ref="tableColumnFilterDialog" :columns="columns"
			@handleFilterColumns="handleFilterColumns">
		</table-column-filter-dialog>
	</div>
	<!--表格内容栏-->
	<kt-table permsEdit="firm:trader:edit"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="登陆编号" prop="loginno">
				<el-input v-model="dataForm.loginno" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="客户编号" prop="userno">
        <el-input v-model="dataForm.userno"  auto-complete="off"></el-input>
      </el-form-item>
			<el-form-item label="用户名称" prop="loginname">
				<el-input v-model="dataForm.loginname" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="昵称" prop="nickname">
				<el-input v-model="dataForm.nickname" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="密码" prop="loginpwd">
				<el-input v-model="dataForm.loginpwd" type="password" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="邮箱" prop="emailaddr">
				<el-input v-model="dataForm.emailaddr" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="手机" prop="telno">
				<el-input v-model="dataForm.telno" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="QQ" prop="qqno">
        <el-input v-model="dataForm.qqno" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="微信号" prop="weixinno">
        <el-input v-model="dataForm.weixinno" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="是否启用" prop="isvalid">
        <el-select v-model="dataForm.isvalid" placeholder="请选择" style="width: 100%;">
          <el-option label="启用" value="Y"></el-option>
          <el-option label="禁用" value="N"></el-option>
        </el-select>
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
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {
		return {
			size: 'small',
			filters: {
				loginno: '',
        loginname: '',
        userno:''
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				loginno: [
					{ required: true, message: '请输入登陆账号', trigger: 'blur' }
				],
        userno: [
          { required: true, message: '请输入客户编号', trigger: 'blur' }
        ],
        loginname: [
          { required: true, message: '请输入用户名称', trigger: 'blur' }
        ],
        loginpwd: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        isvalid: [
          { required: true, message: '请选择是否启用', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
        loginno: '',
        userno: '',
        loginname: '',
        nickname: '',
        loginpwd: '',
        emailaddr: '',
        telno: '',
        qqno: '',
        weixinno: '',
        isvalid: 'Y',
        type: 0
			}
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
			this.pageRequest.params = {'loginno':this.filters.loginno,'loginname@LIKE':this.filters.loginname}
			this.$api.trader.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.user.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
          loginno: '',
          userno: '',
          loginname: '',
          nickname: '',
          loginpwd: '',
          emailaddr: '',
          telno: '',
          qqno: '',
          weixinno: '',
          isvalid: 'Y'
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
			this.dataForm = Object.assign({}, params.row)
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)

            if(this.operation){
              this.$api.trader.save(params).then((res) => {
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
              this.$api.trader.edit(params).then((res) => {
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
    isvalidFormat: function (row, column, cellValue, index){
    if(cellValue==='Y') {
      return '启用'
    } else {
      return "禁用"
    }
	},
    // 是否有效格式化
  typeFormat: function (row, column, cellValue, index){
      if(cellValue===1) {
        return '代理'
      } else if(cellValue===2) {
        return "普通用户"
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
				{prop:"loginno", label:"登陆编号", minWidth:120},
        {prop:"loginname", label:"用户名称", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
        {prop:"type", label:"类型", minWidth:120,formatter:this.typeFormat},
				{prop:"nickname", label:"昵称", minWidth:120},
				{prop:"roleName", label:"角色", minWidth:100},
				{prop:"emailaddr", label:"邮箱", minWidth:120},
				{prop:"telno", label:"手机", minWidth:100},
				{prop:"qqno", label:"QQ", minWidth:100},
				{prop:"weixinno", label:"微信号", minWidth:100},
				{prop:"isvalid", label:"是否启用", minWidth:70,formatter:this.isvalidFormat}
			]
			this.filterColumns = this.columns;
	}
	},
	mounted() {
		this.initColumns()
	}
}
</script>

<style scoped>

</style>
