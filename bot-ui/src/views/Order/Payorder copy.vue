<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
				<el-input v-model="filters.contno" placeholder="订单编号"></el-input>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="客户编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.wareno" placeholder="商品编号"></el-input>
      </el-form-item>
       <el-form-item>
        <el-input v-model="filters.payno" placeholder="充值账号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.status" placeholder="状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in statuss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="order:payorder:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="order:payorder:view" type="primary" @click="exportExcel()"/>
      <ExportExcel ref="export" :columns="filterColumns" :table-data="exportData" :info="exportInfo"></ExportExcel>
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
	<kt-table @handleSend="handleSend" @handleInvoic="handleInvoic"
		:data="pageResult" :columns="filterColumns" :buttons="buttons" @handleApply="handleApply"
		@findPage="findPage" @handleEdit="handleEdit">
	</kt-table>
    <el-dialog title="申请修改状态" width="40%" :visible="true" v-if="applyDialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
      <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="priceForm" :size="size" style="height: 380px;overflow: auto;"
              label-position="right">
        <el-form-item label="订单编号" prop="contno" >
          <el-input v-model="dataForm.contno" :disabled="!operation" placeholder="请输入编号" maxlength="16"></el-input>
        </el-form-item>
        <el-form-item label="订单状态" prop="status">
          <el-select v-model="dataForm.status" ref="statusSel" placeholder="状态" style="width: 98%;" >
            <el-option v-for="(item,index) in statuss" :key="index" :label="item.sshow" :value="item.sval * 1"></el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :size="size" @click.native="applyDialogVisible = false">{{$t('action.cancel')}}</el-button>
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
import ExportExcel from "@/views/Core/ExportExcel"
import {getStatus} from "@/utils/status"
import {getCurrentTime} from '@/utils/datetime'
export default {
	components:{
		PopupTreeInput,
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    Sendinfo,
    Invoic,
    ExportExcel
	},
	data() {
		return {
			size: 'small',
			filters: {
				contno: '',
				userno: '',
        wareno: '',
        status:'',
        payno:''
			},
      uno1s:[],
      uno2s:[],
			columns: [],
      buttons: [],
			filterColumns: [],
			pageRequest: { pageNum: 1, pageSize: 50 },
			pageResult: {},

			operation: false, // true:新增, false:编辑
			dialogVisible: false, // 新增编辑界面是否显示
      applyDialogVisible:false,
      showDialogVisible:false,
			editLoading: false,
      statuss:[],
      dataForm:{},
			dataFormRules: {

			},
      exportInfo:{
			  id:'record-table',
        name:'三方订单.xlsx'
      },
      exportData:[],
      supplyData:[],
      userData:[],
      syncs:[
        {sval:'Y',sshow:'已同步'},
        {sval:'N',sshow:'未同步'}
      ],
      paystatuss:[
        {sval:'100',sshow:'未支付'},
        {sval:'200',sshow:'支付成功'},
        {sval:'300',sshow:'支付失败'},
        {sval:'400',sshow:'支付取消'}
      ]
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
			this.pageRequest.params = Object.assign({},this.filters)
			this.$api.payorder.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    handleEdit: function (data){},
    handleShow: function (data){

    },
    async findUserNo1(){
      let para = {clevel:1,orgtype:1}
      const {data} = await this.$api.firm.findParent(para)
      this.uno1s = data

    },
    async findUserNo2(){
      let para = {clevel:2,orgtype:1}
      const {data} = await this.$api.firm.findParent(para)
      this.uno2s = data
    },

    handleApply:function(params){
      this.dataForm = Object.assign({}, params.row)
      this.applyDialogVisible = true
      this.operation = false
    },
    submitForm2: function () {
      this.$refs.priceForm.validate((valid) => {
        if (valid) {
          this.$confirm('确认提交申请吗？', '提示', {}).then(() => {
            this.editLoading = true
            let keys = {
              contno: this.dataForm.contno,
              status: this.dataForm.status
            }
            let vals = {
              '订单编号': this.dataForm.contno,
              '订单状态': this.$refs['statusSel'].selected.currentLabel,
            }
            let params = {
              tplno:'200',
              datakey: this.dataForm.contno,
              curdata: JSON.stringify(keys),
              showdata: JSON.stringify(vals),
              version: this.dataForm.version
            }
            this.$api.checkdata.save(params).then((res) => {
              this.editLoading = false
              if(res.code == 200) {
                this.$message({ message: '申请成功，请等待审核通过', type: 'success' })
                this.applyDialogVisible = false
                this.$refs['priceForm'].resetFields()
              } else {
                this.$message({message: '操作失败, ' + res.msg, type: 'error'})
              }
              this.findPage(null)
            })

          })
        }
      })
    },
    exportExcel(){
      let params = Object.assign({}, this.filters)
      params['cno'] = this.$refs.filter_cno.getData()
      let para = {
        pageSize:1000000,
        pageNum: 1,
        params: params
      }
      this.$api.payorder.findPage(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })

    },
syncformat: function (row, column, cellValue, index){
  return getStatus(cellValue,this.syncs)
},
paystatusformat: function (row, column, cellValue, index){
  return getStatus(cellValue,this.paystatuss)
},
initDict(){

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
        {prop:"wartno", label:"订单编号", minWidth:150},
        {prop:"fdate", label:"订单日期", minWidth:120},
        {prop:"userno", label:"客户编号", minWidth:100},
        {prop:"username", label:"客户名称", minWidth:120},
        {prop:"acctno", label:"账本编号", minWidth:80},
        {prop:"acctname", label:"账本名称", minWidth:120},
        {prop:"chmoney", label:"金额", minWidth:120},
        {prop:"paystatus", label:"支付状态", minWidth:100,formatter: this.paystatusformat},
        {prop:"ctime", label:"时间", minWidth:120},
        {prop:"ip", label:"IP", minWidth:120},
        {prop:"sync", label:"同步状态", minWidth:100,formatter: this.syncformat}
			]
			this.filterColumns = this.columns;
	},
    initButtons(){
      this.buttons=[
        {icon:'el-icon-upload2',label:'修改',perms:'order:payorder:edit',type:'primary',callback:'handleApply'}
      ];
    },
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
