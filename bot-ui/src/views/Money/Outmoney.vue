<template>
  <div class="page-container">
    <el-row>
      <el-col class="toolbar" :span="20">
		<el-form :inline="true" :model="filters" :size="size">

			<el-form-item>
				<el-date-picker
          v-model="filters.fdate"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd">
        </el-date-picker>
			</el-form-item>
      <el-form-item>
        <el-select v-model="filters.paychan" placeholder="提现方式" clearable>
          <el-option v-for="(item,index) in paychans" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.chkstatus" placeholder="审核状态" clearable>
          <el-option v-for="(item,index) in chkstatuss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.paystatus" placeholder="提现状态" clearable>
          <el-option v-for="(item,index) in paystatuss" :key="index" :label="item.sshow" :value="item.sval"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="money:outmoney:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="money:usermoney:view" type="primary" @click="exportExcel"/>
        <ExportExcel ref="export" :columns="exportColumns" :table-data="exportData" :info="exportInfo"></ExportExcel>
			</el-form-item>
      <el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="money:outmoney:add" type="primary" @click="handleAdd" />
			</el-form-item>
      <el-form-item>
          <kt-button icon="fa fa-check" label="批量付款" perms="money:outmoney:pay" type="warning" @click="handleBatchPay" />
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
	<kt-table :data="pageResult" :columns="filterColumns" :buttons="buttons" :showCheckbox="true" @selectionChange="selectionChange"
		@findPage="findPage" @handleAudit="handleAudit" @handleRefuse="handleRefuse">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog title="提现" width="60%" :visible="true" v-if="dialogVisible" :close-on-click-modal="false" :show-close="false" :destroy-on-close="true"  v-dialogDrag>
    <el-form :model="dataForm" label-width="120px" :rules="dataFormRules" ref="dataForm" :size="size" class="dialog_height"  label-position="right">
       <el-form-item label="客户编号" prop="userno" v-if="user.type==0">
        <el-input type="text" v-model="dataForm.userno" maxlength="16" @blur="getUserInfo"></el-input>
      </el-form-item>
       <el-form-item label="提现银行卡" prop="cardId">
         <el-select v-model="dataForm.cardId" style="width:98%">
          <el-option v-for="(item,index) in banklist" :key="index" :label="item.alias" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="提现金额" prop="chmoney" >
        <el-input type="text" v-model="dataForm.chmoney"></el-input>
        可提现金额 {{balance}}
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
import {mapState} from 'vuex'
import ExportExcel from "@/views/Core/ExportExcel"
export default {
	components:{
		KtTable,
		KtButton,
		TableColumnFilterDialog,
    ExportExcel
	},
  computed:{
    ...mapState({
      user: state => state.user.curUser
    })
},
	data() {

		return {
			size: 'small',
			filters: {
			  fdate:'',
        chkstatus: '',
        paychan:'',
        paystatus:''
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
        userno: [
					{ required: true, message: '请输入客户编号', trigger: 'blur' }
				],
        cardId: [
          { required: true, message: '请选择银行卡', trigger: 'blur' }
        ],
        chmoney: [
          { required: true, message: '请输入提现金额', trigger: 'blur' }
        ]
			},
			// 新增编辑界面数据
			dataForm: {},
      chkstatuss:[
        {sval:'0',sshow:'未审核'},
        {sval:'1',sshow:'已审核'},
        {sval:'2',sshow:'已驳回'}
      ],
      banklist:[],
      paychans:[
        {sval:'A',sshow:'微信'},
        {sval:'B',sshow:'支付宝'},
        {sval:'C',sshow:'银行卡'}
      ],
      paystatuss:[
        {sval:'100',sshow:'待提现'},
        {sval:'101',sshow:'已提交'},
        {sval:'200',sshow:'已提现'},
        {sval:'300',sshow:'提现失败'}
      ],
      balance:'',
      exportColumns:[],
      exportData:[],
      exportInfo:{
        id:'record-table',
        name:'代付信息.xlsx'
      }
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
				this.pageRequest.params = {
        'fdate@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'fdate@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        chkstatus:this.filters.chkstatus,
        paychan:this.filters.paychan,
        paystatus:this.filters.paystatus
      }
			this.$api.outmoney.findExport(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params =  {
         'fdate@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
          'fdate@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
          chkstatus:this.filters.chkstatus,
          paychan:this.filters.paychan
			}
      this.$api.outmoney.findExport(para).then((res) => {
        this.exportData = res.data.content.map(item => {
          item.PRE = 0
          return item
        })
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })
    },
    //公共详情
    handleShow:function(data){

    },
    //Tab 单机事件
    handleClick(tab, event) {

    },
    getUserInfo: function(){
      this.banklist = []
      this.balance = ''
      if(this.user.type > 0){
        this.dataForm.userno = this.user.userno
      }
      if(!this.dataForm.userno) return
      let para = {
        userno : this.dataForm.userno,
        acctno : '100'
      }
      this.$api.usercard.findList(para).then(res => {
        this.banklist = res.data
      })
      this.$api.usermoney.getOne(para).then(res => {
        if(res.data){
          this.balance = res.data.balance
        }
      })
    },
    handleAdd: function () {
        this.dialogVisible = true
        this.operation = true
        this.initFormData()
    },
		// 显示审核界面
    handleAudit: function (params) {
      let para = Object.assign({}, params.row)
      this.$confirm('确定要审核吗？', '提示', {}).then(() => {
        this.$api.outmoney.audit(para).then((res) => {
          if(res.code == 200) {
              this.$message({ message: '操作成功', type: 'success' })
            } else {
              this.$message({message: '操作失败, ' + res.msg, type: 'error'})
            }
            this.findPage({pageRequest:this.pageRequest})
        })
      })
		},
		// 显示驳回界面
    handleRefuse: function (params) {
			let para = Object.assign({}, params.row)
      this.$confirm('确定要驳回吗？', '提示', {}).then(() => {
        this.$api.outmoney.reject(para).then((res) => {
          if(res.code == 200) {
              this.$message({ message: '操作成功', type: 'success' })
            } else {
              this.$message({message: '操作失败, ' + res.msg, type: 'error'})
            }
            this.findPage({pageRequest:this.pageRequest})
        })
      })
		},
    selectionChange(selections){
      this.selections = selections.map(item=>{
        if(item.chkstatus==1){
          return {id:item.id}
        }
      })
    },
    handleBatchPay(){
      if(this.selections.length <= 0){
        this.$message.error('请选择支付数据！')
        return
      }
      this.$confirm('确认发行吗？', '提示', {}).then(() => {
        let para = this.selections
        this.$api.outmoney.batchPay(para).then(res =>{
          if(res.code == 200) {
            this.$message({ message: '操作成功', type: 'success' })
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.findPage(null)
        })
      })
    },
		// 编辑
		submitForm: function () {
			this.$refs.dataForm.validate((valid) => {
				if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
            let params = Object.assign({},this.dataForm)
            this.$api.outmoney.save(params).then((res) => {
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
		    id:'',
        fdate: '',
        userno: '',
        chmoney: '',
        reccustno: '',
        memo:'',
        ctime:'',
        chktime: '',
        chkuserno: '',
        chkip: '',
        chkstatus: '',
        cardId:''
      }
    },
    initBankList(){
      let para = {}
      this.$api.usercard.findList(para).then(res => {
        this.banklist = res.data
      })
    },
    initDict(){
      if(this.user.type > 0){
        this.getUserInfo()
      }
    },
// 是否有效格式化
  chkstatusFormat: function (row, column, cellValue, index){
    return getStatus(cellValue,this.chkstatuss)
  },
  paystatusFormat:function (row, column, cellValue, index){
    return getStatus(cellValue,this.paystatuss)
  },
  paychanFormat: function (row, column, cellValue, index){
    return getStatus(cellValue,this.paychans)
  },
  memoFormat: function (row, column, cellValue, index){
    /* if(cellValue){
      let val = JSON.parse(cellValue);
      let msg = '持卡人：' + val.SNAME + '\n'
              + '银行卡号：' + val.BANKNO + '\n'
              + '银行名称：' + val.BANKNM + '\n'
              + '开户行所在地：' + val.OPENBANK + '\n'
              + '开户支行：' + val.SUBBANK
      return msg;
    } */
    return cellValue;
  },
		// 处理表格列过滤显示
	initColumns: function () {
			this.columns = [
        {prop:"fdate", label:"申请日期", minWidth:100},
        {prop:"userno", label:"客户编号", minWidth:100},
        {prop:"sname", label:"客户名称", minWidth:120},
        {prop:"chmoney", label:"提现金额", minWidth:100},
        {prop:"tcomm", label:"提现服务费", minWidth:100},
        {prop:"outmoney", label:"实际提现金额", minWidth:100},
        {prop:"paychan", label:"提现方式", minWidth:100,formatter:this.paychanFormat},
        {prop:"reccustno", label:"卡号", minWidth:150},
        {prop:"openbank", label:"开户行", minWidth:130},
        {prop:"subbank", label:"支行", minWidth:130},
        {prop:"province", label:"省/直辖市", minWidth:100},
        {prop:"city", label:"城市", minWidth:100},
        {prop:"memo", label:"详细信息", minWidth:100,formatter:this.memoFormat},
        {prop:"ctime", label:"申请时间", minWidth:150},
        {prop:"chkstatus", label:"审核状态", minWidth:100,formatter:this.chkstatusFormat},
        {prop:"chkuserno", label:"审核人", minWidth:100},
        {prop:"chktime", label:"审核时间", minWidth:150},
        {prop:"chkip", label:"审核地址", minWidth:120},
        {prop:"paystatus", label:"提现状态", minWidth:100,formatter:this.paystatusFormat},
			]
			this.filterColumns = this.columns;
      this.exportColumns = [
        {prop:"",type:"index", label:"序号", minWidth:80},
        {prop:"sname", label:"收款人姓名", minWidth:120},
        {prop:"cardno", label:"银行卡号", minWidth:150},
        {prop:"bankno", label:"银行编码", minWidth:100},
        {prop:"province", label:"省/直辖市", minWidth:100},
        {prop:"city", label:"城市", minWidth:100},
        {prop:"bankname", label:"开户行名称", minWidth:150},
        {prop:"outmoney", label:"付款金额", minWidth:100},
        {prop:"PRE", label:"0(对私付款)/1(对公付款)", minWidth:100},
        {prop:"idno", label:"身份证(可空)", minWidth:100}
      ]

	},
    initButtons(){
		  this.buttons=[
		   {icon:'fa fa-check',label:'审核',perms:'money:outmoney:audit',type:'',callback:'handleAudit'},
        {icon:'fa fa-times',label:'驳回',perms:'money:outmoney:reject',type:'danger',callback:'handleRefuse'}
      ];
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
.dialog_height{
  height: 380px;
  overflow: auto;
}
</style>
