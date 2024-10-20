<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.userno" placeholder="发帖账号"></el-input>
			</el-form-item>

			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="info:forumrecv:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="info:forumrecv:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="info:forumrecv:edit" permsDelete="info:forumrecv:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="130px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
      <el-form-item label="流水号" prop="id" v-if="!operation">
        <el-input v-model="dataForm.id" :disabled="!operation" auto-complete="off" ></el-input>
      </el-form-item>
			<el-form-item label="主题编号" prop="titid">
				<el-input v-model="dataForm.titid" :disabled="!operation" auto-complete="off" ></el-input>
			</el-form-item>
      <el-form-item label="发帖账号" prop="userno">
				<el-input v-model="dataForm.userno" :disabled="!operation" auto-complete="off" ></el-input>
			</el-form-item>
      <el-form-item label="排序序号" prop="seqno" >
        <el-input v-model.number="dataForm.seqno" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="回复内容" prop="content" >
        <el-input v-model="dataForm.content" auto-complete="off"  maxlength="32"></el-input>
      </el-form-item>
      <el-form-item label="图片路径" prop="picpath" >
        <upload-file :model="'forumrecv'+dataForm.id" :img-path="dataForm.picpath" @fillpath="handleFillpath"></upload-file>
      </el-form-item>
      <el-form-item label="是否私信" prop="isprivate" >
        <el-select v-model="dataForm.isprivate" style="width: 98%">
          <el-option v-for="(item,index) in isprivates" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="回帖编号" prop="rid" >
        <el-input v-model.number="dataForm.rid" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="回帖账号" prop="ruserno" >
        <el-input v-model="dataForm.ruserno" auto-complete="off"></el-input>
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
        userno: ''
      },
      columns: [],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      editLoading: false,
      dataFormRules: {
        userno: [
          {required: true, message: '发帖账号', trigger: 'blur'}
        ],
        seqno: [
          {required: true, message: '请输入排序序号', trigger: 'blur'}
        ],
        content: [
          {required: true, message: '请输入回复内容', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        id:'',
        titid:'',
        userno: '',
        seqno: '',
        stime:'',
        content:'',
        picpath:'',
        isprivate:'',
        rid:'',
        ruserno:''
      },
      isprivates:[
        {key:'Y',val:'是'},
        {key:'N',val:'否'},
      ]

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
			  'userno':this.filters.userno
			}
			this.$api.forumrecv.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.forumrecv.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        id:'',
        titid:'',
        userno: '',
        seqno: '',
        stime:'',
        content:'',
        picpath:'',
        isprivate:'',
        rid:'',
        ruserno:''
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
              this.$api.forumrecv.save(params).then((res) => {
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
              this.$api.forumrecv.edit(params).then((res) => {
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
  isprivateFormatter: function (row, column, cellValue, index){
      let items = this.isprivates.filter(function(item){
        return item.key == cellValue
      })
      if(items && items.length > 0){
        return items[0].val
      }
      return cellValue
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"id", label:"流水号", minwidth:100},
				{prop:"titid", label:"主题编号", minwidth:100},
				{prop:"userno", label:"发帖账号", minwidth:120},
				{prop:"seqno", label:"排序序号", minwidth:120},
				{prop:"content", label:"内容", minwidth:100},
				{prop:"stime", label:"时间", minwidth:150},
        {prop:"isprivate", label:"是否私信", minwidth:120,formatter:this.isprivateFormatter},
        {prop:"rid", label:"回帖编号", minwidth:120},
        {prop:"ruserno", label:"接受账号", minwidth:120}
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
