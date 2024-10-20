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
        <el-input v-model="filters.username" placeholder="用户名" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="filters.nickname" placeholder="用户昵称" clearable></el-input>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="invite:inviteCount:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="invite:inviteCount:view" type="primary" @click="exportExcel"/>
        <ExportExcel ref="export" :columns="columns" :table-data="exportData" :info="exportInfo"></ExportExcel>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table
    :data="pageResult" :columns="columns" :showOperation="showOperation" @findPage="findPage">
    <template #sex="scope">
      <el-tag type="success" v-if="scope.row.sex=='0'">正常</el-tag>
      <el-tag type="danger" v-if="scope.row.sex=='1'">冻结</el-tag>
    </template>
	</kt-table>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import { format } from "@/utils/datetime"
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
        fdate: '',
        username:'',
        nickname:''
			},
			columns: [
				{prop:"userno", label:"USERID", minWidth:120},
				{prop:"username", label:"用户名", minWidth:120},
        {prop:"nickname", label:"昵称", minWidth:120},
        {prop:"tjnum", label:"邀请人数", minWidth:120},
        {prop:"transcomm", label:"邀请收益", minWidth:100},
        {prop:"sex", label:"状态", minWidth:150}
			],
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:false,
      exportData:[],
      exportInfo:{
        id:'record-table',
        name:'邀请统计.xlsx'
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
        'btime':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'etime':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'username':this.filters.username,
        'nickname':this.filters.nickname
			}
			this.$api.order.findFYCount(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params = {
        'btime':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'etime':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'username':this.filters.username,
        'nickname':this.filters.nickname
			}
      this.$api.order.findFYCount(para).then((res) => {
        this.exportData = res.data.content
        setTimeout(()=>{
          this.$refs.export.exportExcel();
        },500)
      })
    },


	},
	mounted() {
	}
}
</script>

<style scoped>

</style>
