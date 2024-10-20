<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
        <el-select v-model="filters.mno" placeholder="会议" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in mnos" :key="index" :label="item.mname" :value="item.mno"></el-option>
        </el-select>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="嘉宾编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.mstatus" placeholder="状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in mstatuss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="mt:guest:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="mt:guest:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="mt:guest:edit" permsDelete="mt:guest:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="150px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="会议编号" prop="mno" >
        <el-select v-model="dataForm.mno" placeholder="请选择会议" style="width: 100%;" :disabled="!operation">
          <el-option v-for="(item,index) in mnos" :key="index" :label="item.mname" :value="item.mno"></el-option>
        </el-select>
			</el-form-item>

      <el-form-item label="嘉宾" prop="userno">
        <el-input v-model="dataForm.userno"  auto-complete="off" maxlength="16"  placeholder="请输入嘉宾编号" :disabled="!operation"></el-input>
      </el-form-item>
      <el-form-item label="发言次序" prop="seq">
        <el-input v-model.number="dataForm.seq"  auto-complete="off"  placeholder="请输入发言次序"></el-input>
      </el-form-item>
      <el-form-item label="嘉宾展示资源">
          <upload-file-multy model="guest" :imgPath="dataForm.fpath" ref="picupload" :info="picinfo" v-if="dialogVisible"></upload-file-multy>
        </el-form-item>
			<el-form-item label="当前展示资源编号" prop="curnum">
        <el-input v-model.number="dataForm.curnum"  auto-complete="off" maxlength="1"></el-input>
      </el-form-item>
      <el-form-item label="权限" prop="auth">
        <el-select v-model="dataForm.auth" placeholder="请选择" style="width: 100%;">
          <el-option v-for="(item,index) in auths" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="mstatus">
        <el-select v-model="dataForm.mstatus" placeholder="请选择" style="width: 100%;">
          <el-option v-for="(item,index) in mstatuss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
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
import UploadFileMulty from "@/views/Trade/Compents/UploadFileMulty"
import {getStatus} from "@/utils/status"
import {formatWithSeperator} from "@/utils/datetime"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadFileMulty
	},
	data() {
		return {
			size: 'small',
			filters: {
				mno: '',
        userno: '',
        mstatus:''
			},
			columns: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				mno: [
					{ required: true, message: '请选择会议', trigger: 'blur' }
				],
        seq: [
          { required: true, message: '请输入发言次序', trigger: 'blur' }
        ],
        auth: [
          { required: true, message: '请选择权限', trigger: 'blur' }
        ],
        userno: [
          { required: true, message: '请输入主持人', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
        mno: '',
        userno:'',
        fpath: '',
        mstatus: '0',
        curnum: '0',
        seq:1,
        auth:'1'
			},
      mstatuss:[
        {sval: '0' ,sshow:'未开始'},
        {sval: '1' ,sshow:'已开始'},
        {sval: '2' ,sshow:'已结束'}
      ],
      auths:[
        {sval: '0' ,sshow:'禁言'},
        {sval: '1' ,sshow:'发言'}
      ],
      picinfo:'',
      mnos:[]
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
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.guest.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.guest.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        mno: '',
        userno:'',
        fpath: '',
        mstatus: '0',
        curnum: '0',
        seq:1,
        auth:'1'
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
      let path = this.$refs.picupload.getPath()
      this.dataForm.fpath = path
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.guest.save(params).then((res) => {
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
              this.$api.guest.edit(params).then((res) => {
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
  statusFormatter: function (row, column, cellValue, index){
    return getStatus(cellValue,this.mstatuss)
  },
  authFormatter: function (row, column, cellValue, index){
    return getStatus(cellValue,this.auths)
  },
  timeFormatter: function (row, column, cellValue, index){
    return formatWithSeperator(cellValue,'-',':')
  },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"mno", label:"会议编号", minWidth:120},
				{prop:"mname", label:"会议名称", minWidth:120},
				{prop:"userno", label:"嘉宾编号", minWidth:120},
				{prop:"username", label:"嘉宾名称", minWidth:120},
				{prop:"seq", label:"发言次序", minWidth:120},
				{prop:"auth", label:"权限", minWidth:120,formatter:this.authFormatter},
				{prop:"mstatus", label:"状态", minWidth:120,formatter:this.statusFormatter},
				{prop:"curnum", label:"当前展示资源编号", minWidth:100},
        {prop:"lastuptime", label:"最后更新时间", minWidth:120,formatter:this.timeFormatter}
			]
			this.filterColumns = this.columns;
	},
  initDict(){
    this.$api.meeting.findList({}).then(res=>{
      this.mnos = res.data
    })
  }
	},
	mounted() {
		this.initColumns()
    this.initDict()
	}
}
</script>

<style scoped>

</style>
