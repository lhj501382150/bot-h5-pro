<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.actno" placeholder="活动编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.actname" placeholder="活动名称"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="gm:rewar:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="gm:rewar:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="gm:rewar:edit" permsDelete="gm:rewar:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="赛事活动" prop="actno">
        <el-select v-model="dataForm.actno" placeholder="赛事活动" style="width: 100%;" :disabled="!operation">
          <el-option v-for="(item,index) in acts" :key="index" :label="item.actname" :value="item.actno"></el-option>
        </el-select>
			</el-form-item>
      <el-form-item label="参赛人员编号" prop="userno" :disabled="!operation">
        <el-input v-model="dataForm.userno"  maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="门票" prop="money" >
        <el-input v-model="dataForm.money"  maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="胜/负" prop="vorf">
        <el-select v-model="dataForm.vorf" placeholder="胜/负" style="width: 100%;" >
          <el-option v-for="(item,index) in vorfs" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排名" prop="seq" maxlength="256">
        <el-input v-model.number="dataForm.seq" auto-complete="off"></el-input>
      </el-form-item>
			<el-form-item label="奖励积分" prop="rewmn">
				<el-input v-model="dataForm.rewmn" auto-complete="off"></el-input>
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
				actno: '',
        actname: '',
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				actno: [
					{ required: true, message: '请选择活动', trigger: 'blur' }
				],
        userno: [
          { required: true, message: '请输入参赛人员编号', trigger: 'blur' }
        ],
        vorf: [
          { required: true, message: '请选择胜负', trigger: 'blur' }
        ],
        seq: [
          { required: true, message: '请输入排名', trigger: 'blur' }
        ],
        rewmn: [
          { required: true, message: '请输入奖励积分', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
        actno: '',
        userno: '',
        money: '',
        vorf: '',
        seq: '',
        rewmn: '0'
			},
			acts: [],
      vorfs:[
        {key:0,val:'负'},
        {key:1,val:'胜'}
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
			this.pageRequest.params = {'actno':this.filters.actno,'actname@LIKE':this.filters.actname}
			this.$api.userlist.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
        this.findAct()
			}).then(data!=null?data.callback:'')
		},
	  findAct:function(){
		  this.$api.activity.findList().then(res=>{
		    this.acts = res.data
      })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.userlist.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        id: 0,
        actno: '',
        rewtype: '',
        minno: '1',
        manno: '',
        rewmn: ''
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
              this.$api.userlist.save(params).then((res) => {
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
              this.$api.userlist.edit(params).then((res) => {
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

    vorfFormat: function (row, column, cellValue, index){
      let t = this.vorfs.filter(function(item){
        return item.key == cellValue
      });
      let val = cellValue;
      if(t) val = t[0].val
      return val
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
        {prop:"actno", label:"活动编号", minWidth:120},
				{prop:"actname", label:"活动名称", minWidth:120},
        {prop:"userno", label:"参赛人员编号", minWidth:120},
				{prop:"username", label:"参赛人员名称", minWidth:120},
				{prop:"gameno", label:"游戏账号", minWidth:120},
        {prop:"vorf", label:"胜/负", minWidth:100,formatter:this.vorfFormat},
				{prop:"seq", label:"排名", minWidth:100},
				{prop:"rewmn", label:"奖励积分", minWidth:100}
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
