<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.sysid" placeholder="参数编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.sysname" placeholder="参数名称"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:role:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="sys:para:add" type="primary" @click="handleAdd" />
			</el-form-item>
		</el-form>
	</div>

	<!--表格内容栏-->
	<para-table permsEdit="sys:para:edit" permsDelete="sys:para:del"
    :tabledata="tabledata"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</para-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
      <el-form-item label="分组编号" prop="systype" v-show="operation">
        <el-input v-model="dataForm.systype"  :disabled="!operation" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="分组名称" prop="groupname">
        <el-input v-model="dataForm.groupname" :disabled="!operation" auto-complete="off"></el-input>
      </el-form-item>
			<el-form-item label="参数编号" prop="sysid">
				<el-input v-model="dataForm.sysid" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="参数名称" prop="sysname">
				<el-input v-model="dataForm.sysname" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="参数值" prop="sval">
				<el-input v-model="dataForm.sval" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="图片上传" v-if="isfile && dialogVisible">
        <upload-file :model="'app'" :img-path="filepath" @fillpath="handleFillpath"></upload-file>
      </el-form-item>
			<el-form-item label="描述信息" prop="memo">
				<el-input v-model="dataForm.memo" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="描述信息上传" v-if="dialogVisible">
        <upload-file :model="'appMemo'" :img-path="memoPath" @fillpath="handleFillMemo"></upload-file>
      </el-form-item>

      <el-form-item label="是否有效" prop="isvalid">
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
import ParaTable from "@/views/Sys/components/ParaTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import UploadFile from "@/views/Trade/Compents/UploadFile"
export default {
	components:{
    ParaTable,
		KtButton,
		TableColumnFilterDialog,
    UploadFile
	},
	data() {
		return {
			size: 'small',
			filters: {
				sysid: '',
        sysname: ''
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 8 },
			pageResult: {},
      tabledata:[],
			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				systype: [
					{ required: true, message: '请输入分组编号', trigger: 'blur' }
				],
        sysid: [
          { required: true, message: '请输入参数编号', trigger: 'blur' }
        ],
        sysname: [
          { required: true, message: '请输入参数名称', trigger: 'blur' }
        ],
        sval: [
          { required: true, message: '请输入参数值', trigger: 'blur' }
        ],
        isvalid: [
          { required: true, message: '请选择是否启用', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
			  systype:'',
        sysid: '',
        sysname: '',
        groupname:'',
        sval: '',
        memo: '',
        isvalid: 'Y'
			},
      isfile: false,
      filepath:'',
      memoPath:''
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
			this.pageRequest = {sysid:this.filters.sysid,'sysname':this.filters.sysname}
			this.$api.para.findPage(this.pageRequest).then((res) => {
        this.pageResult.content = res.data
        this.tabledata = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.para.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
			  systype:'',
        sysid: '',
        sysname: '',
        groupname: '',
        sval: '',
        memo: '',
        isvalid: 'Y'
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {

			this.dataForm = Object.assign({}, params.row)
      let memo = this.dataForm.memo
      this.memoPath = ''
      if(memo && memo.indexOf('.')>-1){
        if(memo.indexOf("path=") > -1){
          memo = memo.substring(memo.indexOf("path=")+5)
          if(memo.length > 0){
            this.memoPath = memo
          }
        }
      }
      let path = this.dataForm.sval
      this.isfile = false
      this.filepath = ''
      if(path.indexOf("path=") > -1){
        path = path.substring(path.indexOf("path=")+5)
        if(path.length > 0){
          this.isfile = true
          this.filepath = path
        }
      }else if(path.indexOf("file/") > -1){
        path = path.substring(path.indexOf("file/")+5)
        if(path.length > 0){
          this.isfile = true
          this.filepath = path
        }
      }
      this.dialogVisible = true
			this.operation = false

		},
    handleFillpath(path){
      this.filepath = path
      let temp = this.dataForm.sval
      if(temp.indexOf("path=") > -1){
        this.dataForm.sval = temp.substring(0,temp.indexOf("path=")+5) + path
      }else if(temp.indexOf("file/") > -1){
        this.dataForm.sval = temp.substring(0,temp.indexOf("file/")+5) + path
      }
    },
    handleFillMemo(path){
      this.memoPath = path
      let temp = this.dataForm.sval
      if(temp.indexOf('path=')> -1){
        path = temp.substring(0,temp.indexOf("path=")+5) + path
      }else if(temp.indexOf("file/") > -1){
        path = temp.substring(0,temp.indexOf("file/")+5) + path
      }
      this.dataForm.memo = path
    },
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.para.save(params).then((res) => {
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
              this.$api.para.edit(params).then((res) => {
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
      return '有效'
    } else {
      return "无效"
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
				{prop:"groupname", label:"分组名称", minWidth:120},
				{prop:"sysid", label:"参数编号", minWidth:120},
        {prop:"sysname", label:"参数名称", minWidth:120},
				{prop:"sval", label:"参数值", minWidth:100},
				{prop:"memo", label:"描述", minWidth:120},
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
