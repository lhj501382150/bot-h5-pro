<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.sunno" placeholder="科目编号" maxlength="8"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.subname" placeholder="科目名称" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.inorout" placeholder="出入标记" style="width: 100%;" clearable>
          <el-option label="入" value="A"></el-option>
          <el-option label="出" value="B"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="money:subject:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="money:subject:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="money:subject:edit" permsDelete="money:subject:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="科目编号" prop="sunno" >
				<el-input v-model="dataForm.sunno" :disabled="!operation" auto-complete="off" maxlength="8"></el-input>
			</el-form-item>
			<el-form-item label="科目名称" prop="subname" >
				<el-input v-model="dataForm.subname" auto-complete="off" maxlength="8"></el-input>
			</el-form-item>
			<el-form-item label="出入标记" prop="inorout" >
        <el-select v-model="dataForm.inorout" placeholder="出入标记" style="width: 100%;">
          <el-option label="入" value="A"></el-option>
          <el-option label="出" value="B"></el-option>
        </el-select>
			</el-form-item>
			<el-form-item label="影响字段" prop="province">
        <el-checkbox-group v-model="dataForm.calcfield" size="small">
          <el-checkbox-button v-for="f in clcfields" :label="f.key" :key="f.key">{{f.val}}</el-checkbox-button>
        </el-checkbox-group>
			</el-form-item>
			<el-form-item label="备注" prop="memo">
				<el-input v-model="dataForm.memo" auto-complete="off"></el-input>
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
				sunno: '',
        subname: '',
        inorout:''
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},
      clcfields:[
        {key:'A',val:'INMONEY'},
        {key:'B',val:'OUTMONEY'},
        {key:'C',val:'IN'},
        {key:'D',val:'OUT'},
        {key:'E',val:'FREEZE'}
      ],
			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				sunno: [
					{ required: true, message: '请输入科目编号', trigger: 'blur' }
				],
        subname: [
          { required: true, message: '请输入科目名称', trigger: 'blur' }
        ],
        inorout: [
          { required: true, message: '请选择输入标记', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
        sunno: '',
        subname: '',
        inorout: 'A',
        calcfield: [],
        memo: ''
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
			  'sunno':this.filters.sunno,
        'subname@LIKE':this.filters.subname,
        'inorout':this.filters.inorout
			}
			this.$api.subject.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.subject.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        sunno: '',
        subname: '',
        inorout: 'A',
        calcfield: [],
        memo: ''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
      params.row.calcfield = params.row.calcfield ? params.row.calcfield.split(","):[];
			this.dataForm = Object.assign({}, params.row)
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            params.calcfield = params.calcfield.toString()
            if(this.operation){
              this.$api.subject.save(params).then((res) => {
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
              this.$api.subject.edit(params).then((res) => {
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
    inoroutFormat: function (row, column, cellValue, index){
    if(cellValue=='A') {
      return "入"
    } else {
      return "出"
    }
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
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"sunno", label:"科目编号", minWidth:120},
				{prop:"subname", label:"科目名称", minWidth:120},
        {prop:"inorout", label:"出入标记", minWidth:70,formatter:this.inoroutFormat},
        {prop:"calcfield", label:"影响字段", minWidth:120,formatter:this.fieldFormat},
        {prop:"memo", label:"备注", minWidth:100}
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
