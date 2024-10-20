<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.msgno" placeholder="短信编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="客户编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.mobileno" placeholder="手机号码"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.ntype" placeholder="短信类型" style="width: 100%;" clearable>
          <el-option v-for="item in messtype" :key="item.key" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.sendflag" placeholder="发送状态" style="width: 100%;" clearable>
          <el-option v-for="item in sendflags" :key="item.key" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="info:mess:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="info:mess:add" type="primary" @click="handleAdd" />
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
	<!--表格内容栏 -->
	<kt-table permsEdit="info:mess:edit" permsDelete="info:mess:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="60%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="短信编号" prop="msgno">
				<el-input v-model="dataForm.msgno" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="客户编号" prop="userno">
        <el-input v-model="dataForm.userno" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="发送手机号" prop="mobileno">
        <el-input v-model="dataForm.mobileno" auto-complete="off"></el-input>
      </el-form-item>
			<!--<el-form-item label="代理级别" prop="userlevel">
				<el-input v-model="dataForm.userlevel" auto-complete="off" @blur="findParent"></el-input>
			</el-form-item>
			<el-form-item v-if="dataForm.userlevel > 0" label="代理对象" prop="userno">
        <el-select v-model="dataForm.userno" placeholder="请选择" clearable style="width: 100%">
            <el-option v-for="(item,index) in parentnos" :key="index" :label="item.username" :value="item.userno"></el-option>
        </el-select>
			</el-form-item>-->
      <el-form-item label="短信类型" prop="ntype">
        <el-select v-model="dataForm.ntype  " placeholder="短信类型" style="width: 100%;" clearable>
          <el-option v-for="item in messtype" :key="item.key" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item label="短信内容" prop="content">
				<el-input v-model="dataForm.content" auto-complete="off"></el-input>
			</el-form-item>
      <el-form-item label="发送状态" prop="sendflag">
        <el-select v-model="dataForm.sendflag" placeholder="发送状态" style="width: 100%;" clearable>
          <el-option v-for="item in sendflags" :key="item.key" :label="item.val" :value="item.key"></el-option>
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
				msgno: '',
        ntype: '',
        userno: '',
        mobileno: '',
        sendflag: ''
			},
      messtype:[
        {key: 1,val:'验证码'},
        {key: 2,val:'风险提醒'},
        {key: 3,val:'业务提醒'}
      ],
      sendflags:[
        {key: 0,val:'未发送'},
        {key: 1,val:'已发送'},
        {key: 2,val:'发送失败'}
      ],
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				msgno: [
					{ required: true, message: '请输入短信编号', trigger: 'blur' }
				],
        content: [
          { required: true, message: '请输入短信内容', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
        msgno: '',
        userlevel: '',
        userno: '',
        ntype: '',
        content: '',
        mobileno: '',
        sendflag:0
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
      let paras = {
        msgno: this.filters.msgno,
        ntype: this.filters.ntype,
        userno: this.filters.userno,
        'mobileno@like': this.filters.mobileno,
        sendflag: this.filters.sendflag
      }
			this.pageRequest.params = paras
			this.$api.mess.findPage(this.pageRequest).then((res) => {
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
        msgno: '',
        userlevel: '',
        userno: '',
        ntype: '',
        content: '',
        mobileno: '',
        sendflag:0
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
			this.dataForm = Object.assign({}, params.row)
      //this.findParent()
      if(params.row.userno){
        this.dataForm.userno = params.row.userno.split(",")
      }
		},
    //查询上级信息
    findParent(){
      this.$data.dataForm.userno = ''
      this.parentnos = []
      let level = this.dataForm.userlevel
      if(level > 14 || level < 1){
        this.$message.error("请输入正确的层级")
        return
      }
      if(level > 0){
        let para = {clevel:level}
        this.$api.firm.findParent(para).then(res =>{
          this.parentnos = res.data
        })
      }
    },
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(params.userno){
              params.userno = params.userno.toString()
            }
            if(this.operation){
              this.$api.mess.save(params).then((res) => {
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
              this.$api.mess.edit(params).then((res) => {
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
  ntypeFormat: function (row, column, cellValue, index){
      return this.messtype[cellValue-1] == null ? cellValue : this.messtype[cellValue-1].val
    },
    sendFlagFormat: function (row, column, cellValue, index){
      return this.sendflags[cellValue] == null ? cellValue : this.sendflags[cellValue].val
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
				{prop:"msgno", label:"短信编号", minWidth:120},
      //  {prop:"userlevel", label:"代理级别", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:150},
        {prop:"mobileno", label:"发送手机号", minWidth:150},
				{prop:"ntype", label:"短信类型", minWidth:100,formatter:this.ntypeFormat},
        {prop:"tplid", label:"模板编号", minWidth:120},
        {prop:"mem", label:"备注", minWidth:300},
        {prop:"content", label:"发送内容", minWidth:200},
        {prop:"sendflag", label:"发送状态", minWidth:100,formatter:this.sendFlagFormat},
        {prop:"sendtime", label:"发送时间", minWidth:150},
        {prop:"msg", label:"失败原因", minWidth:150}

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
