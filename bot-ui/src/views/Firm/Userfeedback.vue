<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.userno" placeholder="客户编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-select v-model="filters.stype" placeholder="反馈类型" style="width: 100%;" clearable>
          <el-option v-for="item in stypes" :key="item.sval" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.status" placeholder="处理状态" style="width: 100%;" clearable>
          <el-option v-for="item in statuss" :key="item.sval" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="firm:userfeedback:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="firm:userfeedback:add" type="primary" @click="handleAdd" />
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
	<kt-table permsEdit="firm:userfeedback:edit" permsDelete="firm:userfeedback:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons" @handleShow="handleShow"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
    <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size"
             label-position="right">
      <el-form-item label="客户编号" prop="userno">
        <el-input v-model="dataForm.userno" :disabled="!operation" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="反馈类型" prop="stype">
        <el-select v-model="dataForm.stype" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in stypes" :key="index" :label="item.sshow" :value="item.sval * 1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="内容" prop="message">
        <el-input v-model="dataForm.message" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="反馈时间" prop="stime">
        <el-date-picker
          v-model="dataForm.stime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 100%;"
          placeholder="选择日期时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="图片" prop="picpath" >
        <upload-file :model="'userfeedback' + dataForm.userno" :img-path="dataForm.picpath" @fillpath="handleFillpath"></upload-file>
      </el-form-item>
      <el-form-item label="处理状态" prop="status">
        <el-select v-model="dataForm.status" placeholder="请选择" style="width: 100%;">
          <el-option v-for="(item,index) in statuss" :key="index" :label="item.sshow" :value="item.sval * 1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="处理结果" prop="result">
        <el-input v-model="dataForm.result" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="备注" prop="mem">
        <el-input v-model="dataForm.mem" auto-complete="off"></el-input>
      </el-form-item>
    </el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>

  <el-dialog title="反馈列表" width="80%" :visible="true" v-if="showDalogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
     <el-table :data="showData" :highlight-current-row="true" :border="true" :stripe="true"
          :show-overflow-tooltip="true" :max-height="500" size="mini" align="small" style="width:100%;">
     <el-table-column type="index" label="序号" min-width="60" width="60"></el-table-column>
      <el-table-column v-for="column in columns" header-align="center" align="center"
        :prop="column.prop" :label="column.label" :width="column.width" :min-width="column.minWidth"
        :key="column.prop" :type="column.type" :formatter="column.formatter"  show-overflow-tooltip>
      </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="showDalogVisible = false">{{$t('action.cancel')}}</el-button>
		</div>
	</el-dialog>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import UploadFile from "@/views/Trade/Compents/UploadFile";
import {getStatus} from "@/utils/status"

export default {
	components:{
    UploadFile,
		KtTable,
		KtButton,
		TableColumnFilterDialog
	},
	data() {

		return {
			size: 'small',
			filters: {
				userno: '',
        stype: '',
        status:''
			},
			columns: [],
      buttons: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},
			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
      showDalogVisible:false,
			editLoading: false,
			dataFormRules: {
				userno: [
					{ required: true, message: '请输入客户编号', trigger: 'blur' }
				],
        stype: [
          { required: true, message: '请选择反馈类型', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择处理状态', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {},
      statuss:[
        {sval: 0 ,sshow:'未处理'},
        {sval: 1 ,sshow:'已处理'}
      ],
      stypes:[
        {sval: 1 ,sshow:'功能异常'},
        {sval: 2 ,sshow:'闪退卡顿'},
        {sval: 3 ,sshow:'建议优化'},
        {sval: 4 ,sshow:'其它'}
      ],
      showData:[]
		}
	},
	methods: {
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.userfeedback.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    //公共详情
    handleShow:function(data){
      let para = data.row
      this.$api.userfeedback.findList(para).then(res => {
        this.showDalogVisible = true
        this.showData = res.data
      })
    },
		// 删除
		handleDelete: function (data) {
      this.$api.userfeedback.batchDelete(data.params).then(data!=null?data.callback:'')
		},
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.initFormData()
		},
		// 显示编辑界面
		handleEdit: function (params) {
			this.dialogVisible = true
			this.operation = false
			this.dataForm = Object.assign({}, params.row)
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
              this.$api.userfeedback.save(params).then((res) => {
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
              this.$api.userfeedback.edit(params).then((res) => {
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
    initFormData(){
		  this.dataForm = {
		    fid: 0,
        userno: '',
        recvno: '',
        stime: '',
        stype: '',
        picpath: '',
        message: '',
        result: '',
        mem: '',
        status: 0
      }
    },
  statusFormatter: function (row, column, cellValue, index){
     return getStatus(cellValue,this.statuss)
    },
    stypeFormatter: function (row, column, cellValue, index){
      return getStatus(cellValue,this.stypes)
    },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"userno", label:"客户编号", minWidth:120},
				{prop:"username", label:"客户名称", minWidth:120},
        {prop:"stype", label:"反馈类型", minWidth:120,formatter:this.stypeFormatter},
        {prop:"message", label:"反馈内容", minWidth:180},
        {prop:"stime", label:"反馈时间", minWidth:150},
        {prop:"status", label:"状态", minWidth:120,formatter:this.statusFormatter},
        {prop:"optuser", label:"操作人", minWidth:120},
        {prop:"opttime", label:"处理时间", minWidth:150},
        {prop:"result", label:"处理结果", minWidth:180},
        {prop:"mem", label:"备注", minWidth:120},
        {prop:"num", label:"反馈次数", minWidth:80}
      ]
			this.filterColumns = this.columns;
	},
    initButtons(){
		  this.buttons=[
		   {icon:'fa fa-eyes',label:'查看',perms:'firm:userfeedback:dtl',type:'warning',callback:'handleShow'}
      ];
    }
	},
	mounted() {
		this.initColumns()
    this.initButtons()
	}
}
</script>

<style scoped>

</style>
