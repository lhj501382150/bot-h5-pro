<template>
  <div class="page-container">
    <el-row>
      <el-col class="toolbar" :span="20">
        <el-form :inline="true" :model="filters" :size="size">
          <el-form-item>
            <el-input v-model="filters.userno" placeholder="客户编号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model="filters.artid" placeholder="资产编号"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.atype"  placeholder="地块类型" clearable>
              <el-option v-for="(item,index) in atypes" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="filters.astatus"  placeholder="状态" clearable>
              <el-option v-for="(item,index) in astatuss" :key="index" :label="item.val" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input v-model.number="filters.coordx" placeholder="地块坐标X"></el-input>
          </el-form-item>
          <el-form-item>
            <el-input v-model.number="filters.coordy" placeholder="地块坐标Y"></el-input>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:landinfo:view" type="primary" @click="findPage(null)"/>
          </el-form-item>
          <el-form-item>
            <kt-button icon="fa fa-eye" label="预览" perms="trade:landinfo:view" type="primary" @click="showModel"/>
          </el-form-item>
            <el-form-item>
              <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="trade:landinfo:add" type="primary" @click="handleAdd" />
            </el-form-item>
            <el-form-item>
              <upload-excel url='/userLandinfo/importData' @findPage="findPage"></upload-excel>
            </el-form-item>
            <el-form-item>
              <kt-button icon="fa fa-download" label="下载模板" perms="trade:landinfo:view" type="primary" @click="exportExcel"/>
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
	<kt-table permsEdit="trade:landinfo:edit" permsDelete="trade:landinfo:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" >
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" style="height:500px; overflow:auto"
			label-position="right">
			<el-form-item label="资产编号" prop="artid" v-if="!operation">
				<el-input v-model="dataForm.artid" :disabled="!operation" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="客户编号" prop="userno" >
				<el-input v-model="dataForm.userno"  auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
			<el-form-item label="地块坐标X" prop="coordx" >
				<el-input v-model="dataForm.coordx" auto-complete="off"  maxlength="8"></el-input>
			</el-form-item>
			<el-form-item label="地块坐标Y" prop="coordy">
				<el-input v-model="dataForm.coordy" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="面积" prop="area">
				<el-input v-model="dataForm.area" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="地块类型" prop="atype" >
        <el-select v-model="dataForm.atype" style="width:98%">
          <el-option v-for="(item,index) in atypes" :key="index" :label="item.val" :value="item.key"></el-option>
      </el-select>
			</el-form-item>
      <el-form-item label="颜色" prop="color" >
        <el-color-picker v-model="dataForm.color" color-format="hex"></el-color-picker>
      </el-form-item>
      <el-form-item label="时间" prop="date" >
        <el-date-picker
          v-model="dataForm.date"
          type="date"
          placeholder="时间"
          value-format="yyyy-MM-dd" style="width:98%">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="属性" prop="mem">
				<el-input v-model="dataForm.area" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="所在区块" prop="blocknum" v-if="!operation">
        <el-input v-model="dataForm.blocknum" auto-complete="off" maxlength="32" :disabled="!operation"></el-input>
      </el-form-item>
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
  <el-dialog title="地块展示" width="40%" :visible.sync="modeldialogVisible" :close-on-click-modal="false" :destroy-on-close="false" v-dialogDrag>
    <LandinfoModel :items="items" :one="one"></LandinfoModel>
    <div slot="footer" class="dialog-footer">
          <el-button :size="size" @click.native="modeldialogVisible = false">{{$t('action.cancel')}}</el-button>
    </div>
	</el-dialog>
  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import KtTable from "@/views/Core/ColorTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import { format } from "@/utils/datetime"
import UploadExcel from "@/views/Trade/Compents/UploadExcel"
import LandinfoModel from '@/views/Trade/Compents/LandinfoModel'
import ExportExcel from "@/views/Core/ExportExcel"
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    UploadExcel,
    ExportExcel,
    LandinfoModel
	},
	data() {
		return {
      size: 'small',
      filters: {
        userno: '',
        artid: '',
        atype: '',
        astatus: '',
        coordx:'',
        coordy: ''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      modeldialogVisible: false,
      items:[],
      one:{},//普通地块
      editLoading: false,
      dataFormRules: {
        artid: [
          {required: true, message: '请输入资产编号', trigger: 'blur'}
        ],
        userno: [
          {required: true, message: '请输入客户编号', trigger: 'blur'}
        ],
        coordx: [
          {required: true, message: '请输入地块坐标X', trigger: 'blur'}
        ],
        coordy: [
          {required: true, message: '请输入地块坐标Y', trigger: 'blur'}
        ],
        color: [
          {required: true, message: '请输入颜色', trigger: 'blur'}
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        artid: '',
        userno: '',
        coordx: '',
        coordy: '',
        area: '',
        atype: '',
        astatus: '',
        color:'',
        date: '',
        mem: '',
        blocknum: '',
        txhash: '',
        perhash: ''
      },
      astatuss: [
        {key:'0',val:'未发行'},
        {key:'1',val:'发行'},
        {key:'2',val:'持有'},
        {key:'3',val:'终止'},
        {key:'4',val:'冻结'},
        {key:'5',val:'持有未上链'},
      ],
      atypes:[
        {key:'1',val:'道路'},
        {key:'2',val:'广场'},
        {key:'3',val:'普通'}
      ],
      exportData:[],
			exportInfo:{
				id:'record-table',
				name:'导入模板.xlsx'
			},
			exportColumns:[],
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
			this.$api.userlandinfo.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    showModel(){
       this.modeldialogVisible = true
    },
    loadData(){
      this.$api.userlandinfo.findList({}).then(res => {
        var ret = res.data
        this.items = ret.items
        this.one = ret.one
      })
    },
		// 批量删除
		handleDelete: function (data) {
			this.$api.userlandinfo.batchDelete(data.params).then(data!=null?data.callback:'')
		},
    exportExcel(){

      setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
    },
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        artid: '',
        userno: '',
        coordx: '',
        coordy: '',
        area: '',
        atype: '',
        astatus: '',
        color:'',
        date: '',
        mem: '',
        blocknum: '',
        txhash: '',
        perhash: ''
      }
		},
		// 显示编辑界面
		handleEdit: function (params) {
      this.operation = false
      this.dataForm = Object.assign({}, params.row)
      this.dialogVisible = true
		},
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
            if(this.operation){
              this.$api.userlandinfo.save(params).then((res) => {
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
              this.$api.userlandinfo.edit(params).then((res) => {
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
  atypeFormat: function (row, column, cellValue, index){
    let t = this.atypes.filter(function(item){
      return item.key == cellValue
    });
    let val = cellValue;
    if(t && t.length > 0) val = t[0].val
    return val
	},
  astatusFormat: function (row, column, cellValue, index){
    let item = this.astatuss.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
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
				{prop:"artid", label:"资产编号", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:120},
				{prop:"username", label:"客户名称", minWidth:120},
				{prop:"coordx", label:"地块坐标X", minWidth:120},
				{prop:"coordy", label:"地块坐标Y", minWidth:120},
				{prop:"area", label:"面积", minWidth:100},
				{prop:"atype", label:"类型", minWidth:100,formatter:this.atypeFormat},
				{prop:"astatus", label:"状态", minWidth:100,formatter:this.astatusFormat},
				{prop:"color", label:"颜色", minWidth:100},
				{prop:"date", label:"时间", minWidth:100},
				{prop:"mem", label:"属性", minWidth:100},
				{prop:"blocknum", label:"所在区块", minWidth:100}
			]
      this.exportColumns = [
         {prop:"userno", label:"客户编号", minWidth:120},
				{prop:"coordx", label:"地块坐标X", minWidth:120},
				{prop:"coordy", label:"地块坐标Y", minWidth:120},
				{prop:"area", label:"面积", minWidth:100},
				{prop:"atype", label:"类型", minWidth:100},
				{prop:"color", label:"颜色", minWidth:100},
				{prop:"date", label:"时间", minWidth:100},
				{prop:"mem", label:"属性", minWidth:100},
        {prop:"blocknum", label:"所在区块", minWidth:100}
			]
			this.filterColumns = this.columns;
	},
  initButtons(){
     this.buttons=[
		    {icon:'fa fa-edit',label:'发行',perms:'trade:landinfo:issue',type:'success',callback:'handleIssue'},
		    {icon:'fa fa-edit',label:'终止',perms:'trade:landinfo:issue',type:'warning',callback:'handleStop'}
      ];
  },
  initDict(){

  }
	},
	mounted() {
		this.initColumns()
    this.initButtons()
    this.loadData()
	}
}
</script>

<style scoped>

</style>
