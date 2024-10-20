<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.loginno" placeholder="登陆编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.loginname" placeholder="用户名称"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:user:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="sys:user:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="sys:user:edit" permsDelete="sys:user:del"
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

			<el-form-item label="角色" prop="userRoles" v-if="!operation">
				<el-select v-model="dataForm.userRole" placeholder="请选择"
					 style="width: 100%;">
					<el-option v-for="item in roles" :key="item.id"
						:label="item.roleName + item.remark" :value="item.roleId">
					</el-option>
				</el-select>
			</el-form-item>
      <el-form-item label="是否启用" prop="isvalid">
        <el-select v-model="dataForm.isvalid" placeholder="请选择" style="width: 100%;">
          <el-option label="启用" value="Y"></el-option>
          <el-option label="禁用" value="N"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="账号类型" prop="type">
        <el-select v-model="dataForm.type" placeholder="请选择" style="width: 100%;">
          <el-option label="管理员" :value="0"></el-option>
          <el-option label="普通用户" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="dataForm.type=='C'" label="客户编号" prop="userno">
        <el-input v-model="dataForm.userno" auto-complete="off"></el-input>
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
        loginname: [
          { required: true, message: '请输入用户名称', trigger: 'blur' }
        ],
        loginpwd: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        isvalid: [
          { required: true, message: '请选择是否启用', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择账号类型', trigger: 'blur' }
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
        type: 0,
				userRole: -1
			},
			roles: []
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
			this.$api.user.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
				this.findUserRoles()
			}).then(data!=null?data.callback:'')
		},
		// 导出Excel用户信息
		exportUserExcelFile: function () {
			this.pageRequest.pageSize = 100000
			this.pageRequest.params = {name:this.filters.name}
			this.$api.user.exportUserExcelFile(this.pageRequest).then((res) => {
				this.$alert(res.data, '导出成功', {
					confirmButtonText: '确定',
					callback: action => {
					}
				})
			})
		},
		// 加载用户角色信息
		findUserRoles: function () {
			this.$api.role.findAll().then((res) => {
				// 加载角色集合
				this.roles = res.data
			})
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
          isvalid: 'Y',
          type: 0,
          userRole: -1
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
			this.dataForm = Object.assign({}, params.row)
			let userRole = params.row.userRole
      if(userRole){
        this.dataForm.userRole = userRole.roleId
      }
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
						let userRole = {
              loginno: params.loginno,
							roleId: params.userRole
						}
						params.userRole = userRole
            if(this.operation){
              this.$api.user.save(params).then((res) => {
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
              this.$api.user.edit(params).then((res) => {
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
