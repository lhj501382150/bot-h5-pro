<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.leveno" placeholder="级别"></el-input>
			</el-form-item>

			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="spread:growparas:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="spread:growparas:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="spread:growparas:edit" permsDelete="spread:growparas:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="130px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="级别" prop="leveno">
				<el-input v-model="dataForm.leveno" :disabled="!operation" auto-complete="off" ></el-input>
			</el-form-item>
			<el-form-item label="名称" prop="lname" >
				<el-input v-model="dataForm.lname" auto-complete="off"  maxlength="8"></el-input>
			</el-form-item>
      <el-form-item label="最小值" prop="minnum" >
        <el-input v-model.number="dataForm.minnum" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="最大值" prop="maxnum">
        <el-input v-model.number="dataForm.maxnum" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="特权描述" prop="mem" >
        <el-input v-model="dataForm.mem" auto-complete="off"  maxlength="32"></el-input>
      </el-form-item>
      <el-form-item label="特权描述" prop="rewnum" >
        <el-input v-model="dataForm.rewnum" auto-complete="off"  maxlength="32"></el-input>
      </el-form-item>
      <el-form-item label="等级标签" prop="picpath" >
        <upload-file :model="'userinof'" :img-path="dataForm.picpath" @fillpath="handleFillpath"></upload-file>
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
import UploadFile from "@/views/Trade/Compents/UploadFile"
import { format } from "@/utils/datetime"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadFile
	},
	data() {
		return {
      size: 'small',
      filters: {
        leveno: ''
      },
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        leveno: [
          {required: true, message: '级别', trigger: 'blur'}
        ],
        lname: [
          {required: true, message: '名称', trigger: 'blur'}
        ],
        minnum: [
          {required: true, message: '请输入最小值', trigger: 'blur'}
        ],
        maxnum: [
          {required: true, message: '请输入最大值', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        leveno: '',
        lname: '',
        minnum: '',
        maxnum: '',
        mem:'',
        rewnum:'',
        picpath:''

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
			this.pageRequest.params = {
			  'leveno':this.filters.leveno
			}
			this.$api.growparas.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.growparas.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        leveno: '',
        lname: '',
        minnum: '',
        maxnum: '',
        mem:'',
        rewnum:'',
        picpath:''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.dialogVisible = true
		},
    handleFillpath(path){
      this.dataForm.picpath = path
    },
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.growparas.save(params).then((res) => {
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
              this.$api.growparas.edit(params).then((res) => {
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
				{prop:"leveno", label:"级别", minwidth:120},
        {prop:"lname", label:"名称", minwidth:120},
				{prop:"minnum", label:"最小值", minwidth:120},
				{prop:"maxnum", label:"最大值", minwidth:100},
				{prop:"mem", label:"特权描述", minwidth:100},
				{prop:"rewnum", label:"特权编号", minwidth:100}

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
