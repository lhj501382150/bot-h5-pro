<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.mno" placeholder="会议编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.mname" placeholder="会议名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.mstatus" placeholder="状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in mstatuss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="mt:metting:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="mt:metting:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="mt:metting:edit" permsDelete="mt:metting:del"
		:data="pageResult" :columns="filterColumns"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="150px" :rules="dataFormRules" ref="dataForm" :size="size"
			label-position="right">
			<el-form-item label="会议编号" prop="mno" maxlength="16" v-if="!operation">
				<el-input v-model="dataForm.mno" :disabled="!operation" auto-complete="off" placeholder="请输入会议编号"></el-input>
			</el-form-item>
			<el-form-item label="会议名称" prop="mname"  maxlength="8">
				<el-input v-model="dataForm.mname" auto-complete="off"  placeholder="请输入会议名称"></el-input>
			</el-form-item>
      <el-form-item label="日期" prop="fdate">
        <el-date-picker
          v-model="dataForm.fdate"
          type="date"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          style="width: 100%;"
          placeholder="选择日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="开始时间" prop="stime">
        <el-date-picker
          v-model="dataForm.stime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%;"
          placeholder="选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="etime">
        <el-date-picker
          v-model="dataForm.etime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%;"
          placeholder="选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="主持人" prop="userno">
        <el-input v-model="dataForm.userno"  auto-complete="off"  placeholder="请输入主持人编号"></el-input>
      </el-form-item>
      <el-form-item label="状态" prop="mstatus">
        <el-select v-model="dataForm.mstatus" placeholder="请选择" style="width: 100%;">
          <el-option v-for="(item,index) in mstatuss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="展示资源">
          <upload-file-multy model="meeting" :imgPath="dataForm.fpath" ref="picupload" :info="picinfo" v-if="dialogVisible"></upload-file-multy>
        </el-form-item>
			<el-form-item label="当前展示资源编号" prop="curnum">
        <el-input v-model.number="dataForm.curnum"  auto-complete="off" maxlength="1"></el-input>
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
        mname: '',
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
					{ required: true, message: '请输入会议编号', trigger: 'blur' }
				],
        mname: [
          { required: true, message: '请输入会议名称', trigger: 'blur' }
        ],
        fdate: [
          { required: true, message: '请输入日期', trigger: 'blur' }
        ],
        userno: [
          { required: true, message: '请输入主持人', trigger: 'blur' }
        ],
        stime: [
          { required: true, message: '请输入开始时间', trigger: 'blur' }
        ],
        etime: [
          { required: true, message: '请输入结束时间', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {
        mno: '',
        mname: '',
        fdate:'',
        stime:'',
        etime:'',
        userno:'',
        fpath: '',
        mstatus: '0',
        curnum: '0'
			},
      mstatuss:[
        {sval: '0' ,sshow:'未开始'},
        {sval: '1' ,sshow:'已开始'},
        {sval: '2' ,sshow:'已结束'}
      ],
      picinfo:''
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
			  'mno':this.filters.mno,
			  'mstatus':this.filters.mstatus,
        'mname@LIKE':this.filters.mname
			}
			this.$api.meeting.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.meeting.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        mno: '',
        mname: '',
        fdate:'',
        stime:'',
        etime:'',
        userno:'',
        fpath: '',
        mstatus: '0',
        curnum: '0'
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
              this.$api.meeting.save(params).then((res) => {
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
              this.$api.meeting.edit(params).then((res) => {
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
  timeFormatter: function (row, column, cellValue, index){
    return formatWithSeperator(cellValue,'-',':')
  },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"mno", label:"会议编号", minWidth:120},
				{prop:"mname", label:"会议名称", minWidth:120},
				{prop:"fdate", label:"日期", minWidth:120},
				{prop:"stime", label:"开始时间", minWidth:120},
				{prop:"etime", label:"结束时间", minWidth:120},
				{prop:"userno", label:"主持人编号", minWidth:120},
				{prop:"username", label:"主持人名称", minWidth:120},
				{prop:"mstatus", label:"状态", minWidth:120,formatter:this.statusFormatter},
				{prop:"curnum", label:"当前展示资源编号", minWidth:100},
        {prop:"lastuptime", label:"最后更新时间", minWidth:120,formatter:this.timeFormatter}
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
