<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
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
          value-format="yyyy-MM-dd"
          :picker-options="pickerOptions">
        </el-date-picker>
			</el-form-item>
      <el-form-item>
        <el-select v-model="filters.acctno" placeholder="账本编号" style="width: 100%;" clearable>
          <el-option v-for="acct in acctlist" :key="acct.acctno" :label="acct.acctname" :value="acct.acctno"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="客户编号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.username" placeholder="客户名称" clearable></el-input>
      </el-form-item>
      <!-- <el-form-item>
        <el-input v-model="filters.nickname" placeholder="客户昵称" clearable></el-input>
      </el-form-item> -->
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="money:usermoney:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="money:usermoney:view" type="primary" @click="exportExcel"/>
        <ExportExcel ref="export" :columns="columns" :table-data="exportData" :info="exportInfo"></ExportExcel>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table
    :data="pageResult" :columns="columns" :showOperation="showOperation" @findPage="findPage">
	</kt-table>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import { format,getCurrentDate } from "@/utils/datetime"
import ExportExcel from "@/views/Core/ExportExcel"
export default {
	components: {
		KtTable,
		KtButton,
    ExportExcel
	},
	data() {
		return {
			size: 'small',
      acctlist: [],//账本列表
			filters: {
        fdate: [getCurrentDate(),getCurrentDate()],
        userno: '',
        username:'',
        nickname:''
			},
			columns: [
				{prop:"fdate", label:"日期", minWidth:100},
				{prop:"userno", label:"客户编号", minWidth:120},
        {prop:"username", label:"客户名称", minWidth:120},
				{prop:"nickname", label:"客户昵称", minWidth:120},
				{prop:"acctno", label:"账户编号", minWidth:120},
				{prop:"acctname", label:"账户名称", minWidth:120},
				{prop:"start", label:"期初", minWidth:120},
				{prop:"inmoney", label:"入金", minWidth:100},
        {prop:"outmoney", label:"出金", minWidth:100},
        {prop:"in", label:"收入", minWidth:100},
        {prop:"out", label:"支出", minWidth:100},
        {prop:"balance", label:"期末", minWidth:100},
        {prop:"freeze", label:"冻结", minWidth:100},
        // {prop:"lose", label:"盈亏", minWidth:100},
        {prop:"enable", label:"可用", minWidth:100},
        {prop:"allquity", label:"权益", minWidth:100},
        {prop:"limtout", label:"限出", minWidth:100},
        {prop:"canout", label:"可出", minWidth:100}
			],
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:false,
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
      },
      exportData:[],
      exportInfo:{
        id:'record-table',
        name:'账户资金信息.xlsx'
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
        'fdate@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'fdate@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'acctno':this.filters.acctno,
        'userno':this.filters.userno,
        'username@like':this.filters.username,
        'nickname@like':this.filters.nickname
			}
			this.$api.usermoney.findPage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params =  {
        'fdate@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'fdate@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'acctno':this.filters.acctno,
        'userno':this.filters.userno,
        'username@like':this.filters.username,
        'nickname@like':this.filters.nickname
			}
      this.$api.usermoney.findPage(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })
    },
    findacct(){
		  this.$api.account.findList({}).then(res =>{
		    this.acctlist = res.data
      })
    }


	},
	mounted() {
	  this.findacct()
	}
}
</script>

<style scoped>

</style>
