<template>
	<view class="report">
		<uni-nav-bar left-icon="left"  title="报表查询" background-color="rgb(40,148,255)" color="#fff" :border="false" @clickLeft="goBack"></uni-nav-bar>
		<view class="title">
			<view class="sub-type">类型：{{getStatus(clevel)}}报表</view>
			<view class="sub-type">账号：{{userno}}</view>
		</view>
		<view class="tabs">
			<view class="tab-item" :class="reportType==item.val?'active':''" v-for="(item,index) in reportTypes" :key="index" @click="changeType(item)">
				{{item.name}}
			</view>
		</view>
		<view class="report-user" v-if="reportType==0">
			<uni-datetime-picker type="datetime" placeholder="开始日期" v-model="searchForm.startDate"/>
			<uni-datetime-picker type="datetime" placeholder="结束日期" v-model="searchForm.endDate"/>
			<view class="select-box">
				<uni-data-select v-model="searchForm.mode" placeholder="类型选择" :localdata="modes" ></uni-data-select>
			</view>
			
			
			<button type="primary" class="query-btn" size="mini" @click="searchUserData" :loading="isLoading" :disabled="isLoading">查询</button>
			<button class="time-btn" size="mini" @click="searchBytime(1)"  :disabled="isLoading">今天</button>
			<button class="time-btn" size="mini" @click="searchBytime(2)"  :disabled="isLoading">本周</button>
			<button class="time-btn" size="mini" @click="searchBytime(3)"  :disabled="isLoading">上周</button>
			
			<view class="table-data">
				 <view class="table-row">
					 <view class="row-col col1">用户</view>
					 <view class="row-col col2">注数</view>
					 <view class="row-col col3">下注金额</view>
					 <view class="row-col col4">有效金额</view>
					 <view class="row-col col5">退水</view>
					 <view class="row-col col6">盈亏</view>
				 </view>
				 <view class="table-row" v-for="(item,index) in userRecord" :key="index">
					<view class="row-col col1">
						<text class="link"  @click="showDetail(item)" v-if="item.orgtype==1">{{item.username}}</text>
						<text v-else>{{item.username}}</text>
					</view>
					<view class="row-col col2">{{item.num}}</view>
					<view class="row-col col3">{{item.bailmoney}}</view>
					<view class="row-col col4" v-if="item.orgtype==1">{{item.realBail }}</view>
					<view class="row-col col4" v-else :class="item.num > 0?'link':''" @click="showRecord(item)">{{item.realBail}}</view>
					<view class="row-col col5">0</view>
					<view class="row-col col6">{{getRealLoss(item.loss , item.comm)}}</view>
				 </view>
				 <view class="table-row" v-if="userRecord.length > 0">
					<view class="row-col col1">合计</view>
					<view class="row-col col2">{{userRecordSum.num}}</view>
					<view class="row-col col3">{{getRealLoss(userRecordSum.bailmoney,0)}}</view>
					<view class="row-col col4">{{getRealLoss(userRecordSum.realBail,0)}}</view>
					<view class="row-col col5">0</view>
					<view class="row-col col6">{{getRealLoss(userRecordSum.loss , userRecordSum.comm)}}</view>
				 </view>
				 <!-- <view class="table-row">
					<view class="row-col col1">合计</view>
					<view class="row-col col2">1876</view>
					<view class="row-col col3">2288732.12</view>
					<view class="row-col col4">1314143.12</view>
					<view class="row-col col5">0</view>
					<view class="row-col col6">-331281.68</view>
				 </view> -->
			 </view>
		</view>
		<view class="report-time" v-if="reportType==1">
			<view class="tabs">
				<view class="tab-item" :class="tabIndex==item.val?'active':''" v-for="(item,index) in types" :key="index" @click="query(item)">
					{{item.name}}
				</view>
			</view>
			 <view class="table-data">
				 <view class="table-row">
					 <view class="row-col col1">日期</view>
					 <view class="row-col col2">注数</view>
					 <view class="row-col col3">下注金额</view>
					 <view class="row-col col4">有效金额</view>
					 <view class="row-col col5">退水</view>
					 <view class="row-col col6">盈亏</view>
				 </view>
				 <view class="table-row" v-for="(item,index) in records" :key="index">
					<view class="row-col col1">{{item.fdate}}</view>
					<view class="row-col col2">{{item.nums}}</view>
					<view class="row-col col3">{{item.sumBAIL}}</view>
					<view class="row-col col4" :class="item.nums > 0?'link':''" @click="showRecord(item)">{{item.realBail}}</view>
					<view class="row-col col5">0</view>
					<view class="row-col col6">{{getRealLoss(item.loss , item.comm)}}</view>
				 </view>
				 <view class="table-row">
					<view class="row-col col1">合计</view>
					<view class="row-col col2">{{sum.nums}}</view>
					<view class="row-col col3">{{getRealLoss(sum.sumBAIL,0)}}</view>
					<view class="row-col col4">{{getRealLoss(sum.realBail,0)}}</view>
					<view class="row-col col5">0</view>
					<view class="row-col col6">{{getRealLoss(sum.loss , sum.comm)}}</view>
				 </view>
			 </view>
		</view>
		
	</view>
</template>

<script>
	import {formatDate,getCurTime,getWeekStartEndDates} from '@/utils/util.js'
	export default {
		data() {
			return {
				records:[],
				tabIndex:0,
				types:[
					{val:0,name:'本周'},
					{val:1,name:'上周'}
				],
				tabs:[
					{clevel:1,name:'分公司'},
					{clevel:2,name:'股东'},
					{clevel:3,name:'总代理'},
					{clevel:4,name:'代理'},
					{clevel:99,name:'普通会员'}
				],
				modes:[
					{value:0,text:'宝斗'},
					{value:1,text:'牛牛'}
				],
				userno:'',
				clevel:'',
				orgtype:'',
				userinfo:{},
				sum:{
					nums:0,
					sumBAIL:0,
					realBail:0,
					loss:0,
					comm:0
				},
				reportTypes:[
					{val:0,name:'统计明细'},
					{val:1,name:'统计汇总'}
				],
				reportType:0,
				searchForm:{
					userno:'',
					clevel:'',
					startDate:'',
					endDate:'',
					mode:'',
				},
				userRecord:[],
				userRecordSum:{
					num:0,
					bailmoney:0,
					loss:0,
					comm:0,
					realBail:0
				},
				isLoading:false
			}
		},
		onLoad(option) {
			this.userinfo = JSON.parse(uni.getStorageSync('userinfo'))
			this.userno = option.userno || this.userinfo.userno
			this.clevel = option.clevel || this.userinfo.clevel
			this.orgtype = option.orgtype || this.userinfo.orgtype
			if(option.startDate){
				this.searchForm.startDate = option.startDate
				this.searchForm.endDate = option.endDate
			}else{
				this.getToday()
			}
			this.searchUserData()
		},
		methods: {
			getRealLoss(loss,comm){
				return (loss-comm).toFixed(2)
			},
			getToday(){
				var time = new Date().getTime();
				const curTime = getCurTime()
				if(curTime<'070000'){
					this.searchForm.startDate = formatDate(time - 1000 * 60 * 60 * 24 * 1,2) + ' 07:00:00'
					this.searchForm.endDate = formatDate(time,2) + ' 06:00:00'
				}else{
					this.searchForm.startDate = formatDate(time,2) + ' 07:00:00'
					this.searchForm.endDate = formatDate(time + 1000 * 60 * 60 * 24 * 1,2) + ' 06:00:00'
				}
			},
			searchBytime(type){
				if(type==1){
					this.getToday()
				}else if(type==2){
					let date = getWeekStartEndDates(0)
					this.searchForm.startDate = date.startOfWeek + ' 07:00:00'
					this.searchForm.endDate = date.endOfWeek + ' 06:00:00'
				}else if(type==3){
					let date = getWeekStartEndDates(-1)
					this.searchForm.startDate = date.startOfWeek + ' 07:00:00'
					this.searchForm.endDate = date.endOfWeek + ' 06:00:00'
				}
				this.searchUserData()
			},
			searchUserData(){
				if(this.isLoading) return
				this.isLoading = true
				this.userRecord = []
				this.userRecordSum = {
					num:0,
					bailmoney:0,
					loss:0,
					comm:0,
					realBail:0
				}
				let para = {
					userno:this.userno,
					clevel:parseInt(this.clevel) + 1,
					startDate:this.searchForm.startDate,
					endDate:this.searchForm.endDate
				}
				if(this.orgtype==2){
					para.clevel = 100
				}
				if(this.searchForm.mode === 0 || this.searchForm.mode===1){
					para.mode = this.searchForm.mode
				}
				this.$http.post('/Query/SubLevelSumList',para,res=>{
					this.isLoading = false
					this.userRecord = res.rData || []
					this.userRecord.forEach(item=>{
						this.userRecordSum.num += item.num
						this.userRecordSum.bailmoney += item.bailmoney
						this.userRecordSum.loss += item.loss
						this.userRecordSum.comm += item.comm
						this.userRecordSum.realBail += item.realBail
					})
				})
			},
			showDetail(item){
				let clevel = parseInt(this.clevel) + 1
				let path = './report?userno='+item.userno+'&orgtype='+item.orgtype+'&clevel='+clevel+'&startDate='+this.searchForm.startDate+'&endDate='+this.searchForm.endDate
				uni.navigateTo({
					url:path
				})
			},
			changeType(item){
				this.reportType = item.val
				if(this.reportType==0){
					this.searchUserData()
				}else{
					this.query({val:0})
				}
			},
			showRecord(item){
				let startDate = ''
				let endDate = ''
				let userno = ''
				if(this.reportType == 1){
					startDate = item.fdate + ' 07:00:00'
					endDate = formatDate(Date.parse(new Date(startDate)) + 1000 * 60 * 60 * 24 * 1,2) + ' 06:00:00'
				}else{
					startDate = this.searchForm.startDate
					endDate = this.searchForm.endDate
				}
				if(item.userno){
					userno = item.userno
				}else{
					userno = this.userno
				}
				uni.navigateTo({
					url:'./order?userno='+userno + '&orgtype='+this.orgtype+'&startDate='+startDate+'&endDate='+endDate
				})
			},
			query(item){
				this.tabIndex = item.val
				this.loadData()
			},
			loadData(){
				this.records = []
				this.sum={
					nums:0,
					sumBAIL:0,
					realBail:0,
					loss:0,
					comm:0
				}
				let para = {
					iType:this.tabIndex,
					userno:this.userno
				}
				this.$http.post("/Query/SubSumOrdList",para,res => {
					this.records = res.rData || []
					this.records.forEach(item=>{
						this.sum.nums += item.nums
						this.sum.sumBAIL += item.sumBAIL
						this.sum.realBail += item.realBail
						this.sum.loss += item.loss
						this.sum.comm += item.comm
					})
				})
			},
			getStatus(clevel){
				if(this.orgtype == 2){
					return '普通会员'
				}else{
					let item = this.tabs.find(item=>item.clevel==clevel) || {}
					return item.name
				}
			},
			goBack(){
				const pages = getCurrentPages()
				if(pages.length > 1){
					uni.navigateBack({
						delta:1
					})
				}else{
					history.back()
				}
			}
		}
	}
</script>

<style scoped lang="scss">
.report{
	width: 750upx;
	height: 100vh;
	background-color: #fff;
	margin: 0 auto;
	.title{
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-top: 20upx;
		margin-bottom: 20upx;
		.sub-type{
			font-size: 32upx;
			font-weight: bold;
			margin-bottom: 10upx;
		}
	}
	.tabs{
		width: 670upx;
		padding-left: 40upx;
		padding-right: 40upx;
		display: flex;
		align-items: center;
		.tab-item{
			width:50%;
			text-align: center;
			border-bottom: 6upx solid #eeeeee;
			padding-bottom: 20upx;
		}
		.active{
			border-bottom: 6upx solid rgb(40,148,255);
		}
		
	}
	.select-box{
		padding-left:40upx;
		margin-bottom:30upx;
	}
	.query-btn{
		margin-left: 30upx;
	}
	.time-btn{
		margin-left: 30upx;
		background-color: orange!important;
		color: #fff;
	}
	.table-data{
		width:100%;
		margin-top: 40upx;
		.table-row{
			display: flex;
			justify-content: space-between;
			align-items: center;
			border-bottom: 1upx solid #eeeeee;
			padding:20upx;
			.row-col{
				text-align: right;
				font-size: 24upx;
				word-wrap: break-word;
				word-break: break-all;
			}
			.col1{
				width: 80upx;
				text-align: left;
			}
			.col2{
				width: 80upx;
			}
			.col3{
				width: 150upx;
			}
			.col4{
				width: 150upx;
			}
			.link{
				color:rgb(40,148,255);
				cursor: pointer;
			}
			.col5{
				width: 80upx;
			}
			.col6{
				width: 140upx;
			}
		}
	}
	 
}
</style>
