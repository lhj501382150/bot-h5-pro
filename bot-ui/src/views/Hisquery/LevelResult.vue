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
      <!-- <el-form-item>
        <el-input v-model="filters.username" placeholder="分公司名称" clearable></el-input>
      </el-form-item> -->
      <el-form-item>
        <el-input v-model="filters.parentno" placeholder="上级编号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="filters.mode" placeholder="类型" clearable>
          <el-option label="宝斗" value="0"></el-option>
          <el-option label="牛牛" value="1"></el-option>
        </el-select>
      </el-form-item>
			<el-form-item>
				<kt-button icon="fa fa-search" :label="$t('action.search')" perms="hisquery:levelResult:view" type="primary" @click="findPage(null)"/>
        <kt-button icon="fa fa-download" :label="$t('action.export')" perms="hisquery:levelResult:view" type="primary" @click="exportExcel"/>
        <ExportExcel ref="export" :columns="columns" :table-data="exportData" :info="exportInfo"></ExportExcel>
			</el-form-item>
		</el-form>
	</div>
	<!--表格内容栏-->
	<kt-table
    :data="pageResult" :columns="columns" :showOperation="showOperation" @findPage="findPage">
    <template #orgtype="scope">
      <el-tag type="danger" v-if="scope.row.orgtype==1">
          {{ getClevel(scope.row.clevel) }}
        </el-tag>
        <el-tag v-if="scope.row.orgtype==2">普通会员</el-tag>
    </template>
    <template #USERNO="scope">
      <span v-if="scope.row.orgtype==2">{{ scope.row.USERNO }}</span>
      <span style="color:blue;cursor: pointer;" @click="showSub(scope.row)" v-else>{{ scope.row.USERNO }}</span>
    </template>
    <template #loss="scope">
      {{ scope.row.LOSS-scope.row.COMM }}
    </template>
	</kt-table>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import { formatWithSeperator,getCurrentDate,getWeekStartEndDates,getCurTime,formatDate } from "@/utils/datetime"
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
        username:'',
        parentno:'',
        mode:''
			},
      clevels:[
        {key:1,val:'分公司'},
        {key:2,val:'股东'},
        {key:3,val:'总代理'},
        {key:4,val:'代理'}
      ],
			columns: [
        {prop:"orgtype", label:"类型", width:120},
				{prop:"USERNO", label:"用户编号", minWidth:120},
				{prop:"username", label:"用户名称", minWidth:120},
        {prop:"NUM", label:"注数", minWidth:120},
        {prop:"BAILMONEY", label:"下注金额", minWidth:100},
        {prop:"REALMONEY", label:"有效金额", minWidth:120},
        {prop:"COMM", label:"退水", minWidth:100},
        {prop:"LOSS", label:"盈亏", minWidth:100}
			],
			pageRequest: { pageNum: 1, pageSize: 50 },
      pageResult: {},
      showOperation:false,
      exportData:[],
      exportInfo:{
        id:'record-table',
        name:'分公司统计.xlsx'
      }
		}
	},
	methods: {
    getClevel(level){
      return this.clevels.find(item=>item.key==level).val
    },
    showSub(data){
      this.filters.parentno = data.USERNO
      this.findPage(null)
    },
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
        "parentno":this.filters.parentno,
        'mode':this.filters.mode
			}
			this.$api.hisorder.findLevelCount(this.pageRequest).then((res) => {
				this.pageResult = res.data
			}).then(data!=null?data.callback:'')
		},
    exportExcel(){
      let para = { pageNum: 1, pageSize: 10000000 }
      para.params = {
        'btime':this.filters.fdate == null ? '' : this.filters.fdate[0],
        'etime':this.filters.fdate == null ? '' : this.filters.fdate[1],
        'username':this.filters.username
			}
      this.$api.hisorder.findLevelCount(para).then((res) => {
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
