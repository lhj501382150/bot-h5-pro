<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
        <el-date-picker
          v-model="filters.fdate"
          type="month"
          placeholder="统计月份"
          style="width: 150px;"
          value-format="yyyy-MM">
        </el-date-picker>
			</el-form-item>
      <el-form-item>
        <el-select v-model="filters.acctno" placeholder="账本" style="width: 100%;" clearable>
          <el-option v-for="acct in acctlist" :key="acct.acctno" :label="acct.acctname" :value="acct.acctno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="客户编号"></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="money:moneymonth:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="money:moneymonth:view" type="primary" @click="exportExcel"/>
        <ExportExcel ref="export" :columns="columns" :table-data="exportData" :info="exportInfo"></ExportExcel>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table :buttons="buttons" @handleShow="handleShow"
    :data="pageResult" :columns="columns" :showOperation="showOperation" @findPage="findPage">
	</kt-table>
    <el-dialog title="财务汇总详情" width="60%" :visible.sync="dialogVisible" :close-on-click-modal="false" :destroy-on-close="true" v-dialogDrag>
      <el-card class="" shadow="never" id="pdfContent">
        <div slot="header" class="clearfix">
          <span style="font-size: 24px;font-weight: bolder;">{{ dataForm.fdate }}财务汇总详情</span>
        </div>
        <div  class="">
           <el-row>
             <el-col :span="12" style="text-align: left">
               <span>商户名称：{{dataForm.username}} </span>
               <span>商户号：{{dataForm.userno}} </span>
               <span>账户类型：{{dataForm.acctname}} </span>
             </el-col>
             <el-col :span="12" style="text-align: right;padding-right:10px;">
               <span>账单周期：{{ bdate }} - {{edate}}</span>
             </el-col>
           </el-row>
            <el-row style="margin-top: 5px;">
              <el-col :span="24">
                <el-table :data="content" :border="true"  :max-height="300" :size="size" :align="align" style="width:100%;">
                  <el-table-column  header-align="center" align="center" prop="start" label="期初"  :min-width="80"></el-table-column>
                  <el-table-column  header-align="center" align="center" prop="inmoney" label="入金"  :min-width="80"></el-table-column>
                  <el-table-column  header-align="center" align="center" prop="outmoney" label="出金"  :min-width="80"></el-table-column>
                  <el-table-column  header-align="center" align="center" prop="income" label="收入"  :min-width="80"></el-table-column>
                  <el-table-column  header-align="center" align="center" prop="payment" label="支出"  :min-width="80"></el-table-column>
                  <el-table-column  header-align="center" align="center" prop="balance" label="期末"  :min-width="80"></el-table-column>
                  <el-table-column  header-align="center" align="center" prop="freeze" label="冻结"  :min-width="80"></el-table-column>
                  <el-table-column  header-align="center" align="center" prop="enable" label="可用"  :min-width="80"></el-table-column>
                </el-table>
              </el-col>
            </el-row>
          <el-row style="margin-top: 10px;">
            <el-col :span="24">
              <el-table :data="dtlData" :border="true"  :max-height="300" :size="size" :align="align" style="width:100%;">
               <!-- <el-table-column  header-align="center" align="center" prop="sonno" label="科目编号"  :min-width="80">
                </el-table-column>-->
                <el-table-column  header-align="center" align="center" prop="subname" label="科目"  :min-width="80">
                </el-table-column>
                <el-table-column  header-align="center" align="center" prop="chmoney" label="金额"  :min-width="80">
                </el-table-column>
              </el-table>
            </el-col>
          </el-row>
        </div>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button :size="size" @click.native="dialogVisible = false">{{$t('action.cancel')}}</el-button>
        <el-button :size="size" type="primary" @click.native="submitForm" :loading="editLoading"> 下载PDF </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import ExportExcel from "@/views/Core/ExportExcel"
import { format,getCurrentMonth } from "@/utils/datetime"
import htmlToPdf from "@/utils/htmlToPdf";

export default {
	components: {
		KtTable,
		KtButton,
    ExportExcel
	},
	data() {
		return {
			size: 'small',
      align:'center',
      acctlist: [],//账本列表
			filters: {
        fdate: getCurrentMonth(),
        acctno: '',
        userno: '',
        orgtype: ''
			},
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:true,
      dialogVisible:false,
      editLoading:false,
      buttons:[],
      exportData:[],
      dataForm:{
			  fdate:'',
        userno:'',
        username:'',
        acctname:'',
        inmoney:0,
        outmoney:0,
        income:0,
        payment:0,
        balance:0,
        freeze:0,
        enable:0
      },
      columns: [
        {prop:"fdate", label:"月份", minWidth:100},
        {prop:"userno", label:"客户编号", minWidth:120},
        {prop:"username", label:"客户名称", minWidth:120},
        {prop:"acctno", label:"账户编号", minWidth:120},
        {prop:"acctname", label:"账户名称", minWidth:120},
        {prop:"start", label:"期初", minWidth:120},
        {prop:"inmoney", label:"入金", minWidth:80},
        {prop:"outmoney", label:"出金", minWidth:100},
        {prop:"income", label:"收入", minWidth:100},
        {prop:"payment", label:"支出", minWidth:100},
        {prop:"freeze", label:"冻结", minWidth:100},
        {prop:"enable", label:"可用", minWidth:100},
        {prop:"balance", label:"期末", minWidth:100}
      ],
      exportInfo:{
        id:'record-table',
        name:'账户资金月统计.xlsx'
      },
      content:[],
      dtlData:[],
      bdate:'',
      edate:'',
      orgtypes:[]
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
			this.$api.usermoney.findMonthPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    findacct(){
		  this.$api.account.findList({}).then(res =>{
		    this.acctlist = res.data
      })
    },
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params = Object.assign({},this.filters)
      this.$api.usermoney.findMonthPage(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })
    },
    handleShow(params){
      this.dataForm = Object.assign({}, params.row)
      this.content.push(this.dataForm)
      this.$api.usermoney.findChange(this.dataForm).then(res =>{
        let item = res.data;
        this.dtlData = item.data
        this.bdate = item.bdate
        this.edate = item.edate
      })
      this.dialogVisible = true
    },
    submitForm: function () {
		  var title = this.dataForm.userno+ "_" + this.dataForm.fdate + "财务汇总详情"
      htmlToPdf(title)
    },
    initButtons(){
      this.buttons = [
        {icon:'fa fa-eye',label:'详情',perms:'money:moneymonth:view',type:'warning',callback:'handleShow'}
      ]
    }

	},
	mounted() {
	  this.findacct()
    this.initButtons()
	}
}
</script>

<style scoped>
.tab_row{
  border: #e2e2e2 0.1px solid;
  display: block;
}
.tab_col{
  border:  #e2e2e2 0.1px solid;
  display: inline-block;
  height:35px;
  line-height: 35px;
}
.tab_col_h{
  border:  #e2e2e2 0.1px solid;
  display: inline-block;
  height:95px;
  padding: 2px;
}
.tab_col_h span{
  display: block;
  margin-top: 3px;
  padding: 1px;
  height: 25px;
}
</style>
