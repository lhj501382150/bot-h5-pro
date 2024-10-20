<template>
  <div class="page-container">
	<!--工具栏-->
	<div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
		<el-form :inline="true" :model="filters" :size="size">
			<el-form-item>
        <el-date-picker
          v-model="filters.fdate"
          type="datetimerange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
			</el-form-item>
      <el-form-item>
        <el-input v-model="filters.userno" placeholder="用户名" clearable></el-input>
      </el-form-item>

			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="hisquery:money:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="hisquery:money:view" type="primary" @click="exportExcel"/>
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
import { format,formatDate ,getCurTime } from "@/utils/datetime"
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
        fdate: [this.getStartDate(),this.getEndDate()],
        acctno: '',
        userno: '',
        subno:'',
        username:'',
        nickname:''
			},
			columns: [
				{prop:"fdate", label:"日期", minWidth:100},
				{prop:"userno", label:"USERID", minWidth:120},
				{prop:"username", label:"用户名", minWidth:120},
        {prop:"nickname", label:"昵称", minWidth:120},
        {prop:"subname", label:"类型", minWidth:100},
        {prop:"chmoney", label:"金额", minWidth:120},
        {prop:"ctime", label:"时间", minWidth:150},
        {prop:"memo", label:"备注", minWidth:100},
        {prop:"", label:"邀请人", minWidth:100}
			],
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:false,
      subs:[],
      exportData:[],
      exportInfo:{
        id:'record-table',
        name:'用户上下分明细.xlsx'
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
        userno: this.filters.userno,
        'username@like':this.filters.username,
        'nickname@like':this.filters.nickname
			}
			this.$api.hisorder.findScorePage(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params = {
        'ctime@ge':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'ctime@le':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'username@like':this.filters.username,
        'nickname@like':this.filters.nickname
			}
      this.$api.hisorder.findScorePage(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })
    },
    getStartDate(){
      var time = new Date().getTime();
      const curTime = getCurTime()
      if(curTime<'070000'){
        return formatDate(time - 1000 * 60 * 60 * 24 * 1,2) + ' 07:00:00'
      }else{
        return formatDate(time,2) + ' 07:00:00'
      }
    },
    getEndDate(){
        var time = new Date().getTime();
				const curTime = getCurTime()
				if(curTime<'070000'){
					return formatDate(time,2) + ' 06:00:00'
				}else{
					return formatDate(time + 1000 * 60 * 60 * 24 * 1,2) + ' 06:00:00'
				}

    }

	},
	mounted() {
	}
}
</script>

<style scoped>

</style>
