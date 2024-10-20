<template>
  <div class="page-container">
     <el-row>
      <el-col class="toolbar" :span="20">
        	<el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
				<el-input v-model="filters.hid" placeholder="编号"></el-input>
			</el-form-item>
       <el-form-item>
				<el-input v-model="filters.artid" placeholder="资产编号"></el-input>
			</el-form-item>
      <el-form-item>
				<el-input v-model="filters.wareno" placeholder="商品编号"></el-input>
			</el-form-item>
       <el-form-item>
				<el-input v-model="filters.suserno" placeholder="委托拍卖人编号"></el-input>
			</el-form-item>
		 
       <el-form-item>
         <el-select v-model="filters.status"  placeholder="状态" clearable>
           <el-option v-for="(item,index) in statuss" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
			</el-form-item>
        <el-form-item>
         <el-select v-model="filters.reqstatus"  placeholder="申请状态" clearable>
           <el-option v-for="(item,index) in reqstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
         </el-select>
			</el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="trade:warehandorder:view" type="primary" @click="findPage(null)"/>
			</el-form-item>
        <el-form-item>
          <kt-button icon="fa fa-plus" :label="$t('action.add')" perms="trade:warehandorder:add" type="primary" @click="handleAdd" />
        </el-form-item>
		</el-form>
      </el-col>
       <el-col class="toolbar" :span="4">
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
	<kt-table permsEdit="trade:warehandorder:edit" permsDelete="trade:warehandorder:del"
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete" @handleAudit="handleAudit" @handleRefuse="handleRefuse">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog :title="operation?'新增':'编辑'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" 
			label-position="right">
			<el-form-item label="编号" prop="hid" v-if="!operation">
				<el-input v-model="dataForm.hid" :disabled="!operation" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="资产编号" prop="artid" >
				<el-input v-model.number="dataForm.artid"  auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="商品" prop="wareno" >
        <el-select v-model="dataForm.wareno" style="width:98%">
           <el-option v-for="(item,index) in warenos" :key="index" :label="item.wareno + '-' + item.warename" :value="item.wareno"></el-option>
         </el-select>
			</el-form-item>
      <el-form-item label="委托拍卖人" prop="suserno" >
				<el-input v-model="dataForm.suserno"  auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="竞价开始时间" prop="stime">
          <el-date-picker v-model="dataForm.stime"
                          format="yyyy-MM-dd HH:mm:ss"  value-format="yyyy-MM-dd HH:mm:ss"
                          type="datetime" placeholder="选择竞价开始时间" style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="竞价倒计时时间" prop="steptime">
          <el-date-picker v-model="dataForm.steptime"
                          format="yyyy-MM-dd HH:mm:ss"  value-format="yyyy-MM-dd HH:mm:ss"
                          type="datetime" placeholder="选择竞价倒计时时间" style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="倒计时长(秒)" prop="setstep">
          <el-input v-model.number="dataForm.setstep" auto-complete="off"></el-input>
        </el-form-item>
		<el-form-item label="起拍价" prop="sprice" >
        <el-input v-model="dataForm.sprice" auto-complete="off" maxlength="128"></el-input>
      </el-form-item>
      <el-form-item label="竞价幅" prop="stepprice" >
        <el-input v-model="dataForm.stepprice" auto-complete="off" maxlength="128"></el-input>
      </el-form-item>
     
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading">{{$t('action.submit')}}</el-button>
		</div>
	</el-dialog>
  	<el-dialog :title="operation?'审核':'驳回'" width="40%" :visible.sync="auditDialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
		<el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" 
			label-position="right">
			<el-form-item label="编号" prop="hid" >
				<el-input v-model="dataForm.hid" :disabled="true" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="资产编号" prop="artid" >
				<el-input v-model.number="dataForm.artid" :disabled="true"  auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="商品" prop="wareno" >
        <el-select v-model="dataForm.wareno" style="width:98%" :disabled="true">
           <el-option v-for="(item,index) in warenos" :key="index" :label="item.wareno + '-' + item.warename" :value="item.wareno"></el-option>
         </el-select>
			</el-form-item>
      <el-form-item label="委托拍卖人" prop="suserno" >
				<el-input v-model="dataForm.suserno" :disabled="true" auto-complete="off" maxlength="16"></el-input>
			</el-form-item>
      <el-form-item label="竞价开始时间" prop="stime">
          <el-date-picker v-model="dataForm.stime" :disabled="true"
                          format="yyyy-MM-dd HH:mm:ss"  value-format="yyyy-MM-dd HH:mm:ss"
                          type="datetime" placeholder="选择竞价开始时间" style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="竞价倒计时时间" prop="steptime">
          <el-date-picker v-model="dataForm.steptime" :disabled="true"
                          format="yyyy-MM-dd HH:mm:ss"  value-format="yyyy-MM-dd HH:mm:ss"
                          type="datetime" placeholder="选择竞价倒计时时间" style="width: 100%;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="倒计时长(秒)" prop="setstep">
          <el-input v-model.number="dataForm.setstep" :disabled="true" auto-complete="off"></el-input>
        </el-form-item>
		<el-form-item label="起拍价" prop="sprice" >
        <el-input v-model="dataForm.sprice" :disabled="true" auto-complete="off" maxlength="128"></el-input>
      </el-form-item>
      <el-form-item label="竞价幅" prop="stepprice" >
        <el-input v-model="dataForm.stepprice" :disabled="true" auto-complete="off" maxlength="128"></el-input>
      </el-form-item>
      <el-form-item label="驳回原因" prop="mem" v-if="!operation">
        <el-input v-model="dataForm.mem" auto-complete="off" maxlength="128"></el-input>
      </el-form-item>
     
		</el-form>
		<div slot="footer" class="dialog-footer">
			<el-button :size="size" @click.native="auditDialogVisible = false">{{$t('action.cancel')}}</el-button>
			<el-button :size="size" type="primary" @click.native="submitForm2" :loading="editLoading">{{$t('action.submit')}}</el-button>
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
        hid:'',
        artid:'',
        suserno: '',
        wareno: '',
        status: '',
        reqstatus: ''
      },
      columns: [],
      buttons:[],
      filterColumns: [],
      pageRequest: {pageNum: 1, pageSize: 50},
      pageResult: {},

      operation: false, // true:新增, false:编辑
      dialogVisible: false, // 新增编辑界面是否显示
      auditDialogVisible:false,
      editLoading: false,
      dataFormRules: {
        artid: [
          {required: true, message: '请输入资产编号', trigger: 'blur'}
        ],
        suserno: [
          {required: true, message: '请输入委托拍卖人编号', trigger: 'blur'}
        ],
        wareno: [
          {required: true, message: '请选择商品', trigger: 'blur'}
        ],
        stime: [
          { required: true, message: '请输入竞价开始时间', trigger: 'blur' }
        ],
        steptime: [
          { required: true, message: '请输入竞价倒计时时间', trigger: 'blur' }
        ],
        setstep: [
          { required: true, message: '请输入倒计时长', trigger: 'blur' }
        ],
        sprice: [
          { required: true, message: '请输入起拍价', trigger: 'blur' }
        ],
        stepprice: [
          { required: true, message: '请输入竞价幅', trigger: 'blur' }
        ]
      },
      // 新增编辑界面数据
      dataForm: {
        hid:'',
        artid: '',
        wareno: '',
        suserno: '',
        stime: '',
        steptime: '',
        setstep: '',
        status: '',
        reqstatus: '',
        mem: ''
      },
      reqstatuss: [
        {key:'0',val:'未申请'},
        {key:'1',val:'已审核'},
        {key:'2',val:'已驳回'}
      ],
      statuss: [
        {key:'1',val:'未开始'},
        {key:'2',val:'竞价中'},
        {key:'3',val:'倒计时'},
        {key:'4',val:'已成交'},
        {key:'5',val:'已流标'}
      ],
      warenos:[]
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
			this.$api.warehandorder.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
		// 批量删除
		handleDelete: function (data) {
			this.$api.warehandorder.batchDelete(data.params).then(data!=null?data.callback:'')
		},
   
		// 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.operation = true
			this.dataForm= {
        hid:'',
        artid: '',
        wareno: '',
        suserno: '',
        stime: '',
        steptime: '',
        setstep: '',
        sprice:0,
        stepprice:0,
        status: '',
        reqstatus: '',
        mem: ''
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
              this.$api.warehandorder.save(params).then((res) => {
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
              this.$api.warehandorder.edit(params).then((res) => {
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
     handleAudit: function(data){
	     this.operation = true
      this.dataForm = Object.assign({}, data.row)
      this.auditDialogVisible = true
    },
    handleRefuse: function(data){
        this.operation = false
      this.dataForm = Object.assign({}, data.row)
      this.auditDialogVisible = true
    },
    submitForm2: function () { 
			 if(this.operation){
            this.$confirm('确认审核吗？', '提示', {}).then(() => {
            let para = Object.assign({},this.dataForm)
            this.$api.warehandorder.audit(para).then(res =>{
              if(res.code == 200) {
                this.$message({ message: '操作成功', type: 'success' })
                this.auditDialogVisible = false
              } else {
                this.$message({message: '操作失败, ' + res.msg, type: 'error'})
              }
              this.findPage(null)
            })
          })
       }else{
          this.$confirm('确认要驳回吗？', '提示', {}).then(() => {
            let para = Object.assign({},this.dataForm)
            this.$api.warehandorder.refuse(para).then(res =>{
              if(res.code == 200) {
                this.$message({ message: '操作成功', type: 'success' })
                this.auditDialogVisible = false
              } else {
                this.$message({message: '操作失败, ' + res.msg, type: 'error'})
              }
              this.findPage(null)
            })
          })
       }
		},

		// 是否有效格式化
  reqstatusFormat: function (row, column, cellValue, index){
     let item = this.reqstatuss.filter(function(s){
      return s.key == cellValue;
    })
    if(item && item.length > 0){
      return item[0].val
    }else{
      return cellValue
    }
	},
  statusFormat: function (row, column, cellValue, index){
    let item = this.statuss.filter(function(s){
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
				{prop:"hid", label:"编号", minWidth:80},
				{prop:"artid", label:"资产编号", minWidth:100},
				{prop:"wareno", label:"商品编号", minWidth:100},
				{prop:"warename", label:"商品名称", minWidth:100},
        {prop:"suserno", label:"拍卖委托人编号", minWidth:120},
				{prop:"susername", label:"拍卖委托人名称", minWidth:120},
				{prop:"stime", label:"竞价开始时间", minWidth:150},
				{prop:"steptime", label:"倒计时开始时间", minWidth:150},
				{prop:"setstep", label:"倒计时长", minWidth:100},
				{prop:"sprice", label:"起拍价", minWidth:100},
				{prop:"stepprice", label:"竞价幅", minWidth:100},
				{prop:"status", label:"状态", minWidth:100,formatter:this.statusFormat},
				{prop:"userno", label:"当前竞得人", minWidth:100},
				{prop:"num", label:"出价次数", minWidth:100},
				{prop:"cprice", label:"当前价", minWidth:150},
				{prop:"contprice", label:"成交价", minWidth:100},
				{prop:"step", label:"倒计时剩余时间", minWidth:100},
				{prop:"reqstatus", label:"申请状态", minWidth:100,formatter:this.reqstatusFormat},
				{prop:"mem", label:"驳回原因", minWidth:100}
			]
			this.filterColumns = this.columns;
	},
  initButtons(){
     this.buttons=[
		    {icon:'fa fa-check',label:'审核',perms:'trade:warehandorder:audit',type:'success',callback:'handleAudit'},
		    {icon:'fa fa-times',label:'驳回',perms:'trade:warehandorder:refuse',type:'warning',callback:'handleRefuse'}
      ];
  },
  initDict(){
    this.$api.ware.findList({}).then(res => {
      this.warenos = res.data
    })
  }
	},
	mounted() {
		this.initColumns() 
    this.initButtons()
    this.initDict()
	}
}
</script>

<style scoped>

</style>
