<template>
  <div class="page-container">
  <el-row>
    <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
           <el-form-item>
            <el-input v-model="filters.horseno" placeholder="马匹编号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.userno" placeholder="账号"></el-input>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="gm:horseDigitart:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="gm:horseDigitart:add" type="primary" @click="handleAdd" />
          </el-form-item>
          <el-form-item>
            <upload-excel url='/horseDigitart/importData' @findPage="findPage"></upload-excel>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-download" label="下载模板" perms="gm:horseDigitart:download" type="primary" @click="exportExcel"/>
            <export-excel ref="export" :columns="exportColumns" :table-data="exportData" :info="exportInfo"></export-excel>
          </el-form-item>
        </el-form>
    </el-col>
    <el-col :span="4">
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
    </el-col>
  </el-row>
	<!--表格内容栏-->
	<kt-table permsEdit="gm:horseDigitart:edit" permsDelete="gm:horseDigitart:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons" :cellStyle="cellStyle"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
   <!--  <template #color="scope">
      {{scope.row.color}}
    </template> -->
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
    <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size"
             label-position="right">
      <el-form-item label="马匹编号" prop="horseno">
        <el-input v-model="dataForm.horseno" :disabled="!operation" auto-complete="off"></el-input>
      </el-form-item>
	    <el-form-item label="账号" prop="userno">
        <el-input v-model="dataForm.userno" auto-complete="off" maxlength="16"></el-input>
      </el-form-item>
      <el-form-item label="类型" prop="htype">
        <el-select v-model="dataForm.htype" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in htypes" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="血统" prop="ancestry">
        <el-select v-model="dataForm.ancestry" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in ancestrys" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="马种" prop="hbreed">
        <el-select v-model="dataForm.hbreed" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in hbreeds" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="体质" prop="cons">
        <el-select v-model="dataForm.cons" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in conss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="dataForm.sex" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in sexs" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
	  <el-form-item label="颜色" prop="color">
        <el-select v-model="dataForm.color" placeholder="请选择" style="width: 98%;">
          <el-option v-for="(item,index) in colors" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
	  <el-form-item label="配饰" prop="ornam">
        <el-select v-model="dataForm.ornam" placeholder="请选择" style="width: 98%;" multiple>
          <el-option v-for="(item,index) in ornams" :key="index" :label="item.sshow" :value="item.sval"></el-option>
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
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import {getStatus} from "@/utils/status"
import UploadExcel from '@/views/Trade/Compents/UploadExcel'
import ExportExcel from "@/views/Core/ExportExcel"
export default {
	components:{
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadExcel,
    ExportExcel
	},
	data() {

		return {
			size: 'small',
			filters: {
				horseno: '',
				userno:''
			},
			columns: [],
      buttons: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},
			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
			editLoading: false,
			dataFormRules: {
				horseno: [
					{ required: true, message: '请输入马匹编号', trigger: 'blur' }
				],
				userno: [
				{ required: true, message: '请输入账号', trigger: 'blur' }
				],
				htype: [
				{ required: true, message: '请选择类型', trigger: 'blur' }
				],
        ancestry: [
				{ required: true, message: '请选择血统', trigger: 'blur' }
				],
        hbreed: [
				{ required: true, message: '请选择马种', trigger: 'blur' }
				],
        cons: [
				{ required: true, message: '请选择体制', trigger: 'blur' }
				],
        sex: [
				{ required: true, message: '请选择性别', trigger: 'blur' }
				],
        color: [
				{ required: true, message: '请选择颜色', trigger: 'blur' }
				],
				ornam: [
				{ required: true, message: '请选择配饰', trigger: 'blur' }
				]
			},
			// 新增编辑界面数据
			dataForm: {},
			htypes:[],
			ancestrys:[],
			hbreeds:[],
			conss:[],
			sexs:[],
			colors:[],
			ornams:[],
			exportData:[],
			exportInfo:{
				id:'record-table',
				name:'导入模板.xlsx'
			},
			exportColumns:[]
		}
	},
	methods: {
		// 获取分页数据
		findPage: function (data) {
			if(data !== null) {
				this.pageRequest = data.pageRequest
			}else{
				this.pageRequest = { pageNum: 1, pageSize: 50 }
			}
			this.pageRequest.params = Object.assign({},this.filter)
			this.$api.horseDigitart.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    initDict(){
		  this.$api.dict.findList('DICT_1001').then(res =>{
		    this.htypes = res.data
      })
      this.$api.dict.findList('DICT_1002').then(res =>{
		    this.ancestrys = res.data
      })
      this.$api.dict.findList('DICT_1003').then(res =>{
		    this.hbreeds = res.data
      })
      this.$api.dict.findList('DICT_1004').then(res =>{
		    this.conss = res.data
      })
      this.$api.dict.findList('DICT_1005').then(res =>{
		    this.sexs = res.data
      })
      this.$api.dict.findList('DICT_1006').then(res =>{
		    this.colors = res.data
      })
      this.$api.dict.findList('DICT_1007').then(res =>{
		    this.ornams = res.data
      })

    },
    //单元格样式设置
    cellStyle:function(obj){
      if(obj.column.property=='color'){
        var color = obj.row[obj.column.property]
        const style = {color:'white'}
        switch(color){
          case 'R','5':
            style.backgroundColor = 'red'
            break;
          case 'B','1':
            style.backgroundColor = 'blue'
            break;
          case 'Y','8':
            style.backgroundColor = 'yellow'
            break;
          case 'G','3':
            style.backgroundColor = 'green'
            break;
          case 'W','7':
            style.backgroundColor = 'white'
            style.color ="black"
            break;
          case 'S','6':
            style.backgroundColor = '#EFEFEF'
            style.color ="black"
            break;
          case 'C','2':
            style.backgroundColor = '#B1720B'
            break;
          case 'P','4':
            style.backgroundColor = '#B943EB'
            break;
          default:
            style.backgroundColor = 'white'
            style.color ="black"
        }


        return style;
      }
    },
    //公共详情
    handleShow:function(data){

    },
    //Tab 单机事件
    handleClick(tab, event) {

    },
    exportExcel(){
      setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
    },
		// 删除
		handleDelete: function (data) {
      this.$api.horseDigitart.batchDelete(data.params).then(data!=null?data.callback:'')
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
			this.dataForm.ornam = this.dataForm.ornam.split('#')
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
						params.ornam = params.ornam.join('#')
						if(this.operation){
						this.$api.horseDigitart.save(params).then((res) => {
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
						this.$api.horseDigitart.edit(params).then((res) => {
							this.editLoading = false
							if(res.code == 200) {
							this.$message({ message: '操作成功', type: 'success' })
							this.dialogVisible = false
							this.$refs['dataForm'].resetFields()
							} else {
							this.$message({message: '操作失败, ' + res.msg, type: 'error'})
							}
							this.findPage({pageRequest:this.pageRequest})
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
			horseno: '',
			userno:'',
			htype: '',
			ancestry: '',
			hbreed: '',
			cons:'',
			sex:'',
			color:'',
			ornam: ''
      }
    },
  htypeFormatter: function (row, column, cellValue, index){
    return getStatus(cellValue,this.htypes)
  },
  ancestryFormatter: function (row, column, cellValue, index){
    return getStatus(cellValue,this.ancestrys)
  },
 hbreedFormatter: function (row, column, cellValue, index){
    return getStatus(cellValue,this.hbreeds)
  },
  consFormatter: function (row, column, cellValue, index){
    return getStatus(cellValue,this.conss)
  },
  sexFormatter: function (row, column, cellValue, index){
    return getStatus(cellValue,this.sexs)
  },
  colorFormatter: function (row, column, cellValue, index){
    return getStatus(cellValue,this.colors)
  },
  ornamFormatter: function (row, column, cellValue, index){
	var arr = cellValue.split("#")
	var tip = '';
	for(var i=0;i<arr.length;i++){
		tip += getStatus(arr[i],this.ornams) + " \r\n"
	}
    return tip
  },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
				{prop:"horseno", label:"马匹编号", minWidth:80},
				{prop:"userno", label:"账号", mindth:100},
				{prop:"username", label:"名称", minWidth:120},
				{prop:"htype", label:"类型", minWidth:80,formatter:this.htypeFormatter},
				{prop:"ancestry", label:"血统", minWidth:80,formatter:this.ancestryFormatter},
				{prop:"hbreed", label:"马种", minWidth:80,formatter:this.hbreedFormatter},
				{prop:"cons", label:"体质", minWidth:80,formatter:this.consFormatter},
				{prop:"sex", label:"性别", minWidth:80,formatter:this.sexFormatter},
				{prop:"color", label:"颜色", minWidth:80,formatter:this.colorFormatter},
				{prop:"ornam", label:"配饰", minWidth:80,formatter:this.ornamFormatter}
			]
      this.exportColumns = [
        {prop:"horseno", label:"马匹编号", minWidth:80},
				{prop:"userno", label:"账号", mindth:100},
				{prop:"htype", label:"类型", minWidth:80},
				{prop:"ancestry", label:"血统", minWidth:80},
				{prop:"hbreed", label:"马种", minWidth:80},
				{prop:"cons", label:"体质", minWidth:80},
				{prop:"sex", label:"性别", minWidth:80},
				{prop:"color", label:"颜色", minWidth:80},
				{prop:"ornam", label:"配饰", minWidth:80}
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
		  this.buttons=[
		   // {icon:'fa fa-delete',label:'注销',perms:'firm:member:del',type:'warning',callback:'handleDelete'}
      ];
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
