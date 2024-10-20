<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.noticeno" placeholder="公告编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.title" placeholder="公告标题"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.ntype  " placeholder="公告类型" style="width: 100%;" clearable>
          <el-option v-for="item in noticetype" :key="item.key" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="info:notice:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="info:notice:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="info:notice:edit" permsDelete="info:notice:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="60%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" class="dialog-height"
			label-position="right">
			<el-form-item label="公告编号" prop="noticeno">
				<el-input v-model="dataForm.noticeno" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>

			<!-- <el-form-item label="代理级别" prop="userlevel">
				<el-input v-model="dataForm.userlevel" auto-complete="off" @blur="findParent"></el-input>
			</el-form-item>
			<el-form-item v-if="dataForm.userlevel > 0" label="代理对象" prop="userno">
        <el-select v-model="dataForm.userno" multiple placeholder="请选择" clearable style="width: 100%">
            <el-option v-for="(item,index) in parentnos" :key="index" :label="item.username" :value="item.userno"></el-option>
        </el-select>
			</el-form-item> -->
      <el-form-item label="公告类型" prop="ntype">
        <el-select v-model="dataForm.ntype  " placeholder="公告类型" style="width: 100%;" clearable>
          <el-option v-for="item in noticetype" :key="item.key" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item label="公告标题" prop="title">
				<el-input v-model="dataForm.title" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="公告内容" prop="content">
        <Kindeditoruse :content="dataForm.content" ref="editor"></Kindeditoruse>
			</el-form-item>
			<el-form-item label="排序" prop="seq">
				<el-input v-model="dataForm.seq" auto-complete="off"></el-input>
			</el-form-item>

      <el-form-item label="是否有效" prop="isvalid">
        <el-select v-model="dataForm.isvalid" placeholder="请选择" style="width: 100%;">
          <el-option label="有效" value="Y"></el-option>
          <el-option label="无效" value="N"></el-option>
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
import Kindeditoruse from "@/views/Core/Kindeditoruse"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    Kindeditoruse
	},
	data() {
		return {
			size: 'small',
			filters: {
				noticeno: '',
        ntype: '',
        title: ''
			},
      noticetype:[
        {key: 1,val:'公告'},
        {key: 2,val:'规则'},
        {key: 3,val:'风险告知'},
        {key: 4,val:'系统信息'},
        {key: 5,val:'用户须知'},
        {key: 6,val:'用户协议'},
        {key: 7,val:'隐私政策'},
        {key: 8,val:'关于我们'},
        {key: 9,val:'私信消息'},
        {key: 10,val:'物流消息'}
      ],//公告类型
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				noticeno: [
					{ required: true, message: '请输入公告编号', trigger: 'blur' }
				],
        isvalid: [
          { required: true, message: '请选择是否有效', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
			noticeno: '',
			userlevel: '',
			userno: '',
			ntype: '',
			title: '',
			content: '',
			seq: '',
			isvalid: 'Y'
			},
      parentnos: []
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
      let paras = Object.assign({}, this.filters);
			paras['title@like'] = paras.title
      delete paras.title
			this.pageRequest.params = paras
			this.$api.notice.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.notice.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        noticeno: '',
        userlevel: '',
        userno: '',
        ntype: '',
        title: '',
        content: '',
        seq: '',
        isvalid: 'Y',
        path: ''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
			this.dataForm = Object.assign({}, params.row)
			// this.loadContent()
			// this.findParent()
			// if(params.row.userno){
			// 	this.dataForm.userno = params.row.userno.split(",")
			// }
		},
    //加载内容
    loadContent: function(){
		  if(!this.dataForm.path) return;
		  let para = {
		    path: this.dataForm.path
      }
		  this.$api.notice.getContent(para).then(res =>{
		    this.dataForm.content = res.msg
      })
    },
    //查询上级信息
    findParent(){
      this.$data.dataForm.userno = ''
      this.parentnos = []
      let level = this.dataForm.userlevel
      if(level > 14 || level < 1){
        //this.$message.error("请输入正确的层级")
        return
      }
      if(level > 1){
        let para = {clevel:level}
        this.$api.firm.findParent(para).then(res =>{
          this.parentnos = res.data
        })
      }
    },
		// 编辑
		submitForm: function () {
		  this.dataForm.content = this.$refs.editor.getContentData()
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(params.userno){
              params.userno = params.userno.toString()
            }
            if(this.operation){
              this.$api.notice.save(params).then((res) => {
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
              this.$api.notice.edit(params).then((res) => {
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
    // 是否有效格式化
  ntypeFormat: function (row, column, cellValue, index){
      return this.noticetype[cellValue-1] == null ? cellValue : this.noticetype[cellValue-1].val
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
				{prop:"noticeno", label:"公告编号", minWidth:120},
				// {prop:"userlevel", label:"代理级别", minWidth:120},
				// {prop:"userno", label:"代理编号", minWidth:150},
				{prop:"ntype", label:"公告类型", minWidth:100,formatter:this.ntypeFormat},
				{prop:"title", label:"公告标题", minWidth:120},
				{prop:"seq", label:"排序", minWidth:60},
				{prop:"content", label:"公告内容", minWidth:200},
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
