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
          format="yyyy-MM-dd HH:mm:ss"
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
        <el-select v-model="filters.mode" placeholder="类型" clearable>
          <el-option label="宝斗" value="0"></el-option>
          <el-option label="牛牛" value="1"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="hisquery:result:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="hisquery:result:view" type="primary" @click="exportExcel"/>
        <ExportExcel ref="export" :columns="columns" :table-data="exportData" :info="exportInfo"></ExportExcel>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table :data="pageResult" :columns="columns" :showOperation="showOperation" @findPage="findPage">
    <template #mode="scope">
      <el-tag type="primary" v-if="scope.row.mode==0">通宝</el-tag>
      <el-tag type="success" v-else-if="scope.row.mode==1">牛牛</el-tag>
    </template>
	</kt-table>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import { format,getCurrentDate,formatDate ,getCurTime ,getWeekStartEndDates } from "@/utils/datetime"
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
        fdate: [
          this.getStartDate()
        ,this.getEndDate()
        ],
        username:'',
        nickname:'',
        mode:''
			},
			columns: [
				{prop:"userno", label:"USERID", minWidth:120},
				{prop:"username", label:"用户名", minWidth:120},
        {prop:"nickname", label:"昵称", minWidth:120},
        {prop:"transcomm", label:"返佣", minWidth:120},
        {prop:"bs", label:"庄闲", minWidth:100},
        {prop:"comm", label:"手续费", minWidth:120},
        {prop:"loss", label:"输赢合计", minWidth:100},
        {prop:"diff", label:"净盈亏", minWidth:100},
        {prop:"xznum", label:"局数", minWidth:100},
        {prop:"balance", label:"余额", minWidth:100},
        {prop:"realmoney", label:"有效金额", minWidth:100},
        {prop:"openid", label:"邀请人", minWidth:100},
        // {prop:"mode", label:"类型", minWidth:80},
			],
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:false,
      exportData:[],
      exportInfo:{
        id:'record-table',
        name:'用户输赢.xlsx'
      }
		}
	},
	methods: {
    searchData(type){
        if(type==1){
          this.filters.fdate = [this.getStartDate(),this.getEndDate()]
        }else if(type==2){
          let date = getWeekStartEndDates(0)
          this.filters.fdate = [date.startOfWeek + ' 07:00:00:00' ,date.endOfWeek + ' 06:00:00']
        }else if(type==3){
          let date = getWeekStartEndDates(-1)
          this.filters.fdate = [date.startOfWeek + ' 07:00:00:00' ,date.endOfWeek + ' 06:00:00']
        }
        this.findPage(null)
    },
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
        'nickname':this.filters.nickname,
        'mode':this.filters.mode
			}
			this.$api.hisorder.findCount(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params = {
        'btime':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'etime':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'username':this.filters.username,
        'nickname':this.filters.nickname,
        'mode':this.filters.mode
			}
      this.$api.hisorder.findCount(para).then((res) => {
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
