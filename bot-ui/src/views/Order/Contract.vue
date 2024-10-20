<template>
  <div class="page-container">
	<!--工具栏-->
    <el-row>
      <el-col class="toolbar" :span="20">
		<el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-date-picker
          v-model="filters.fdate"
          type="datetimerange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
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
        <el-select v-model="filters.buyorsal" placeholder="买卖" style="width: 100%;" clearable>
          <el-option label="买" value="B"></el-option>
          <el-option label="卖" value="S"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.uno1" v-if="uno1s.length > 0" placeholder="一级代理" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in uno1s" :key="index" :label="item.username" :value="item.userno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.uno2" v-if="uno2s.length > 0"  placeholder="二级代理" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in uno2s" :key="index" :label="item.username" :value="item.userno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.tjno" placeholder="推荐人编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.status" placeholder="状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in statuss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.sendstatus" placeholder="发货状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in sendstatuss" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.invoicestatus" placeholder="开票状态" style="width: 100%;" clearable>
          <el-option v-for="(item,index) in invoicestatus" :key="index" :label="item.val" :value="item.key"></el-option>
        </el-select>
      </el-form-item>
       <el-form-item>
        <el-input v-model="filters.artid" placeholder="资产编号"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="order:contract:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="order:contract:view" type="primary" @click="exportExcel()"/>
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
		:data="pageResult" :columns="filterColumns" :buttons="buttons"
		@findPage="findPage" @handleEdit="handleEdit">
	</kt-table>
	<!--新增编辑界面-->
	<el-dialog title="发货信息" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
    <Sendinfo :data-form="dataForm" ref="sendinfo" @closeDialog="closeSendDialog"></Sendinfo>

	</el-dialog>
    <el-dialog title="发票信息" width="40%" :visible.sync="invoicDialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
      <invoic :data-form="dataForm" ref="invoic" @closeDialog="closeInvoicDialog"></invoic>
    </el-dialog>
  </div>
</template>

<script>
import PopupTreeInput from "@/components/PopupTreeInput"
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import TableColumnFilterDialog from "@/views/Core/TableColumnFilterDialog"
import Sendinfo from "@/views/Order/Compents/Sendinfo"
import Invoic from "@/views/Order/Compents/Invoic"
import ExportExcel from "@/views/Core/ExportExcel"
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
			  fdate:'',
				contno: '',
				userno: '',
        wareno: '',
        buyorsal:'',
        uno1:'',
        uno2:'',
        status:'',
        sendstatus:'',
        invoicestatus:'',
        tjno:'',
        artid:''
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
      invoicDialogVisible: false,
			editLoading: false,
      statuss:[
        {key:'1',val:'待付款'},
        {key:'2',val:'待收货'},
        {key:'3',val:'已完成'},
        {key:'4',val:'已取消'},
        {key:'5',val:'待使用'},
        {key:'6',val:'已使用'}
      ],
      sendstatuss:[
        {key:'0',val:'未发货'},
        {key:'1',val:'已发货'},
        {key:'2',val:'已收货'}
      ],
      invoicestatus:[
        {key:'0',val:'未开票'},
        {key:'1',val:'已开票'}
      ],
      dataForm:{},
      userAddress:[],
			dataFormRules: {

			},
      exportInfo:{
			  id:'record-table',
        name:'订单.xlsx'
      },
      exportData:[],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      }
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
        'ctime@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'ctime@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        status:this.filters.status,
        contno: this.filters.contno,
        userno: this.filters.userno,
        wareno: this.filters.wareno,
        buyorsal:this.filters.buyorsal,
        uno1:this.filters.uno1,
        uno2:this.filters.uno2,
        sendstatus:this.filters.sendstatus,
        invoicestatus:this.filters.invoicestatus,
        tjno: this.filters.tjno,
        artid:this.filters.artid
      }
			this.$api.contract.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    handleEdit: function (data){},
    // 打开发货信息对话框
    handleSend: function (data) {
      this.dialogVisible = true
      this.dataForm = Object.assign({}, data.row)
      let para = {contno:this.dataForm.contno}
      this.$api.contract.getSend(para).then(res=>{
        if(res.data){
          this.dataForm = res.data
        }else{
          this.dataForm ={
            contno:para.contno,
            transcomp:'',
            transno:'',
            stime:'',
            status:'',
            recvname:'',
            telno:'',
            recvaddr:''
          }
        }
      })
    },
    closeSendDialog: function(){
      this.dialogVisible = false
      this.dataForm = {}
      this.findPage(null);
    },
    //打开发票信息对话框
    handleInvoic: function (data) {
      this.invoicDialogVisible = true
      this.dataForm = Object.assign({}, data.row)
      let para = {contno:this.dataForm.contno}
      let userno = this.dataForm.userno
      this.$api.contract.getInvoic(para).then(res=>{
        if(res.data){
          this.dataForm = res.data
        }else{
          this.dataForm ={
            contno:para.contno,
            userno:userno,
            type:'',
            invoictitle:'',
            dutyno:'',
            regaddr:'',
            regtelno:'',
            openbank:'',
            bankno:'',
            status:''
          }
        }
      })

    },
    closeInvoicDialog: function(){
      this.invoicDialogVisible = false
      this.dataForm = {}
      this.findPage(null);
    },
    async findUserNo1(){
      let para = {clevel:1}
      const {data} = await this.$api.firm.findParent(para)
      this.uno1s = data

    },
    async findUserNo2(){
      let para = {clevel:2}
      const {data} = await this.$api.firm.findParent(para)
      this.uno2s = data
    },


// 编辑
    submitIvoic: function () {
      this.$confirm('确认提交吗？', '提示', {}).then(() => {
        this.editLoading = true
        let params = this.$refs.invoic.getFormData()
        this.$api.contract.editInvoic(params).then((res) => {
          this.editLoading = false
          if(res.code == 200) {
            this.$message({ message: '操作成功', type: 'success' })
            this.invoicDialogVisible = false
            this.dataForm = {}
          } else {
            this.$message({message: '操作失败, ' + res.msg, type: 'error'})
          }
          this.findPage(null)
        })

      })
    },
    exportExcel(){
      let  params = {
        'ctime@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'ctime@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        status:this.filters.status,
        contno: this.filters.contno,
        userno: this.filters.userno,
        wareno: this.filters.wareno,
        buyorsal:this.filters.buyorsal,
        uno1:this.filters.uno1,
        uno2:this.filters.uno2,
        sendstatus:this.filters.sendstatus,
        invoicestatus:this.filters.invoicestatus,
        tjno: this.filters.tjno
      }
      let para = {
        pageSize:1000000,
        pageNum: 1,
        params: params
      }
      this.$api.contract.findPage(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })

    },
		// 是否有效格式化
statusformat: function (row, column, cellValue, index){
  // 1待付款2待收货3已完成4已取消5待使用
  switch (cellValue) {
    case "1" : return "待付款"
    case "2" : return "待收货"
    case "3" : return "已完成"
    case "4" : return "已取消"
    case "5" : return "待使用"
    case "6" : return "已使用"
     default: return cellValue
  }
},
sendformat: function (row, column, cellValue, index){
  // 状态0 未发货1 已发货2 已收货
  switch (cellValue) {
    case "0" : return "未发货"
    case "1" : return "已发货"
    case "2" : return "已收货"
    default: return cellValue
  }
},
invoiceformat: function (row, column, cellValue, index){
  switch (cellValue) {
    case "0" : return "未开票"
    case "1" : return "已开票"
    default: return cellValue
  }
},
buysaleformat: function (row, column, cellValue, index){
    if(cellValue==='B') {
      return "买入"
    } else {
      return "卖出"
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
    var userType = sessionStorage.getItem('userType');

			this.columns = [
        {prop:"fdate", label:"订单日期", minWidth:120},
        {prop:"contno", label:"订单编号", minWidth:150},
        {prop:"userno", label:"客户编号", minWidth:100},
        {prop:"username", label:"客户名称", minWidth:120},
        {prop:"wareno", label:"商品编号", minWidth:80},
        {prop:"warename", label:"商品名称", minWidth:120},
        {prop:"buyorsal", label:"买卖", minWidth:60,formatter: this.buysaleformat},
        {prop:"price", label:"价格", minWidth:60},
        {prop:"flatprice", label:"转让价格", minWidth:80},
        {prop:"num", label:"数量", minWidth:80},
        {prop:"setnum", label:"使用量", minWidth:80},
        {prop:"tradecomm", label:"服务费", minWidth:80},
        {prop:"cpright", label:"版权费", minWidth:80},
        {prop:"transcomm", label:"物流费", minWidth:80},
        {prop:"bailmoney", label:"金额", minWidth:80},
        {prop:"loss", label:"价差", minWidth:80},
        {prop:"ctime", label:"时间", minWidth:150},
        {prop:"status", label:"状态", minWidth:100,formatter: this.statusformat},
        {prop:"sendstatus", label:"发货状态", minWidth:100,formatter: this.sendformat},
        {prop:"invoicestatus", label:"发票状态", minWidth:100,formatter: this.invoiceformat},
         {prop:"artid", label:"资产编号", minWidth:80},
        {prop:"uno1", label:"一级代理", minWidth:150},
        {prop:"uno2", label:"二级代理", minWidth:150},
        {prop:"tjno", label:"推荐人", minWidth:150}
			]
      if(userType != 0){
        this.columns = this.columns.filter(item => {
          return item.prop != 'cpright'
        })
      }
			this.filterColumns = this.columns;
	},
    initButtons(){
      this.buttons=[
        {icon:'fa fa-edit',label:'发货',perms:'order:contract:send',callback:'handleSend'},
        {icon:'fa fa-edit',label:'发票',perms:'order:contract:invoice',type:'danger',callback:'handleInvoic'}
      ];
    },
	},
	mounted() {
		this.initColumns()
    this.initButtons()
    this.findUserNo1()
    this.findUserNo2()
	}
}
</script>

<style scoped>

</style>
