<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-input v-model="filters.issue" placeholder="期数"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="operations:draw:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
       <el-form-item>
          <kt-button icon="fa fa-plus" label="行情补录结算" perms="operations:draw:add" type="primary" @click="handleAdd" />
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
	<div class='tab-div'>
		<el-tabs v-model="mode" @tab-click="findPage(null)">
			<el-tab-pane v-for="(item,index) in modes" :key="index" :label="item.val" :name="item.key"></el-tab-pane>
		</el-tabs>
	</div>
	<!--表格内容栏-->
	<kt-table :data="pageResult" :columns="filterColumns" :buttons="buttons" :showOperation="false"
		@findPage="findPage" @handleEdit="handleEdit">
		<template #bno="scope">
			<span v-if="scope.row.mode==1">{{scope.row.bnno}}</span>
			<span v-else>{{scope.row.bno}}</span>
		</template>
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="mode==0?'通宝行情补录结算':'牛牛行情补录结算'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="80px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
      <el-form-item label="期数" prop="issue" >
				<el-input v-model="dataForm.issue" placeholder="请输入期数"  maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="时间" prop="stime" >
        <el-date-picker
          v-model="dataForm.stime"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss" style="width: 98%;">
        </el-date-picker>
      </el-form-item>
			<el-form-item label="开奖结果" prop="sresult" >
				<el-input v-model="dataForm.sresult" placeholder="请输入开奖结果"  maxlength="32"></el-input>
        <div>输入格式为：01,02,03,04,05,06,07,08,09,10 以 , 分隔</div>
			</el-form-item>
      <el-form-item label="编号" prop="wareno">
        <el-select v-model="dataForm.wareno" placeholder="请选择编号" style="width: 98%;" clearable>
          <el-option v-for="(item,index) in warenos" :key="index" :label="item.chatid" :value="item.chatid"></el-option>
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
        		issue: ''
			},
			columns: [],
			buttons:[],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				issue: [
					{ required: true, message: '请输入期数', trigger: 'blur' }
				],
				stime: [
					{ required: true, message: '请输入开奖时间', trigger: 'blur' }
				],
				sresult: [
					{ required: true, message: '请输入开奖结果', trigger: 'blur' }
				]
			},
			// 新增编辑界面数据
			dataForm: {
				DataId: '',
				stime: '',
				sresult: '',
				issue:'',
        		wareno:''
			},
      		warenos:[],
			mode:'0',
			modes:[
				{key:'0',val:'澳洲10宝斗'},
				{key:'8',val:'澳洲10牛牛'},
				{key:'1',val:'极速赛车牛牛'},
				{key:'2',val:'哈希1分宝斗'},
				{key:'3',val:'哈希3分宝斗'},
				{key:'4',val:'哈希5分宝斗'},
				{key:'5',val:'哈希1分牛牛'},
				{key:'6',val:'哈希3分牛牛'},
				{key:'7',val:'哈希5分牛牛'},
			]
		}
	},
	methods: {
		loadWareno(){
		this.$api.chat.list({}).then(res=>{
			this.warenos = res.data
		})
		},
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
        		this.pageRequest = {pageNum: 1, pageSize: 50}
      		}
			this.pageRequest.params = {
        		'issue':this.filters.issue,
				'mode':this.mode
			}
			this.$api.draw.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.draw.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
				this.dialogVisible = true
				this.operation = true
		},

		// 显示编辑界面
		handleEdit: function (data) {
			this.$confirm('确定要重新发送结算图吗', '提示', {
				type: 'warning'
			}).then(() => {
			this.loading = true
			let callback = res => {
				if(res.code == 200) {
				this.$message({message: '操作成功', type: 'success'})
				} else {
				this.$message({message: '操作失败, ' + res.msg, type: 'error'})
				}
				this.loading = false
				this.findPage(null)
			}
			this.$api.draw.edit(data.row).then(data!=null?callback:'')
			}).catch(() => {
			})
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
						params.mode = this.mode
						if(this.operation){
							this.$api.draw.save(params).then((res) => {
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
							this.$api.bot.edit(params).then((res) => {
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
				{prop:"issue", label:"期数", minWidth:120},
				{prop:"dataId", label:"DataId", minWidth:120},
				{prop:"stime", label:"时间", minWidth:120},
				{prop:"sresult", label:"号码", minWidth:120},
				{prop:"bno", label:"结果", minWidth:120},
			]
			this.filterColumns = this.columns;
	},
	initButtons(){
		  this.buttons=[
			{icon:'fa fa-edit',label:'发送图片',perms:'operations:draw:edit',type:'primary',callback:'handleEdit'}
		]
    }
	},
	mounted() {
		this.initColumns()
		this.initButtons()
    this.loadWareno()
	}
}
</script>

<style scoped lang="scss">
.tab-div{
	clear:both;
	padding-left:50px;
	padding-right:50px;
}
</style>
