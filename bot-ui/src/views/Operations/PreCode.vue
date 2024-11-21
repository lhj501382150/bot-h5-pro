<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.drawIssue" placeholder="期数"></el-input>
			</el-form-item>
			<el-form-item>
				<el-select v-model="filters.rkey" placeholder="模式"  clearable>
					<el-option v-for="(item,index) in modes" :key="index" :label="item.val" :value="item.key"></el-option>
				</el-select>
			</el-form-item>
      <el-form-item>
        
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="operations:preCode:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="operations:preCode:add" type="primary" @click="handleAdd" />
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table  permsDelete="operations:preCode:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="150px" :rules="dataFormRules" ref="dataForm" :size="size" 
			label-position="right">
			<el-form-item label="期数" prop="drawIssue" v-if="!operation">
				<el-input v-model="dataForm.drawIssue" :disabled="!operation" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item label="模式" prop="rkey" >
				<el-select v-model="dataForm.rkey" :disabled="!operation" style="width: 98%;" @change="changeMode">
					<el-option v-for="(item,index) in modes" :key="index" :label="item.val" :value="item.key"></el-option>
				</el-select>
			</el-form-item>
			<div v-if="hq">
				<el-form-item label="下期开奖时间" >
						{{dataForm.drawTime}} <el-button type="primary" size="mini" @click="refresh">刷新</el-button>
				</el-form-item>
				<el-form-item label="下单明细" >
					<div class="order-detail">
						<div v-for="(item,index) in hq.orders" :key="index" class="order-item">
							<span>{{ item.artid }}</span>
							<span>{{ item.bailmoney }}</span>
						</div>
					</div>
				</el-form-item>
				<el-form-item label="汇总详情" >
					<el-radio-group v-model="drawName" @input="changeDraw">
						<el-radio v-for="(item,index) in Object.keys(hq.count)" :key="index" :label="item" border>{{ item }}:{{ hq.count[item] }}</el-radio>
					</el-radio-group>
				</el-form-item>
			</div>

			<el-form-item label="行情结果" prop="code"  >
				<el-input v-model="dataForm.code" auto-complete="off" maxlength="30"></el-input>
				<span style="color: red;">*数字之间以，分割 例：07,10,06,08,09,04,03,05,01,02</span>
			</el-form-item>
			<el-form-item label="备注" prop="memo">
				<el-input v-model="dataForm.memo" auto-complete="off" maxlength="64"></el-input>
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
				drawIssue: '',
				mode: ''
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				drawIssue: [
					{ required: true, message: '请输入期数', trigger: 'blur' }
				],
				code: [
					{ required: true, message: '请输入行情结果', trigger: 'blur' }
				]
			},
			// 新增编辑界面数据
			dataForm: {
				id:'',
				dataId:'',
				drawIssue: '',
				code: '',
				memo: ''
			},
			modes:[
				{key:'HXBD1',val:'哈希1分宝斗',mode:2},
				{key:'HXBD3',val:'哈希3分宝斗',mode:3},
				{key:'HXBD5',val:'哈希5分宝斗',mode:4},
				{key:'HXNN1',val:'哈希1分牛牛',mode:5},
				{key:'HXNN3',val:'哈希3分牛牛',mode:6},
				{key:'HXNN5',val:'哈希5分牛牛',mode:7}
			],
			hq:'',
			drawName:''
		}
	},
	methods: {
		changeDraw(val){
			let ret = ''
			if(val=='入'){
				ret = 1
			}else if(val =='龙'){
				ret = 2
			}else if(val =='出'){
				ret = 3
			}else if(val =='虎'){
				ret = 4
			}
			let arr = this.geBdArr(ret) || []
			this.dataForm.code = arr.map(item=>{
				if(item < 10){
					item = '0' + item
				}
				return item
			}).join(',')
		},
		geBdArr(val){
			let arr = []
			let ret = []
			for(var i = 1;i <= 10;i++){
				if(i != val){
					arr.push(i)
				}
			}
			let index = this.getRandomNumber(0,4)
			for(var i = 0;i<10;i++){
				if(i < index){
					let seq = this.getRandomNumber(arr.length-4,arr.length-1)
					ret[i] = arr[seq]
					arr.splice(seq,1)
				}else if(i==index){
					ret[index] = val
				}else{
					let seq = this.getRandomNumber(0,arr.length-1)
					ret[i] = arr[seq]
					arr.splice(seq,1)
				}
			}
			return ret
		},
		getRandomNumber(min, max) {
			return Math.floor(Math.random() * (max - min + 1)) + min;
		},
		refresh(){
			this.hq =  ''
			this.$nextTick(()=>{
				this.changeMode(this.dataForm.rkey)
			})
		},
		changeMode(val){
			 this.$api.preCode.getDraw({rkey:val}).then(res=>{
				this.hq = res.data
				this.dataForm.drawIssue = this.hq.draw.drawIssue
				this.dataForm.drawTime = this.hq.draw.nextTime
			 })
		},
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
				this.pageRequest = {pageNum: 1, pageSize: 50}
			}
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.preCode.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.preCode.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.hq = ''
			this.dataForm= {
				id:'',
				dataId:'',
				drawIssue: '',
				code: '',
				memo: '',
				rkey:''
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
							this.$api.preCode.save(params).then((res) => {
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
							this.$api.preCode.edit(params).then((res) => {
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
	modeFormat: function (row, column, cellValue, index){
      let data = this.modes.find(item=>{
		return item.key == cellValue
	  })
	  return data.val
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"rkey", label:"模式", minWidth:120,formatter:this.modeFormat},
				{prop:"dataId", label:"DATA_ID", minWidth:120},
				{prop:"drawIssue", label:"期数", minWidth:120},
				{prop:"code", label:"行情结果", minWidth:120},
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

<style scoped lang="scss">
.order-detail{
	display: flex;
	justify-content: flex-start;
	align-items: center;
	flex-wrap: wrap;
	.order-item{
		width: 100px;
		display: flex;
		justify-content: space-between;
		margin-right: 20px;
		border-bottom: 1px solid #e2e2e2;
	}
}
</style>
