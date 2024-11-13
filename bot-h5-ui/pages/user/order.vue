<template>
	<view class="order">
		<uni-nav-bar left-icon="left"  title="投注记录" background-color="rgb(40,148,255)" color="#fff" :border="false" @clickLeft="goBack"></uni-nav-bar>
		<view class="search-date" v-if="startDate">查询日期：{{startDate}}-{{endDate}}</view>
		<view class="tab-bar">
			<!-- <view class="tab-item"  :class="item.clevel == mode ? 'active':''" v-for="(item,index) in tabs" :key="index" @click="findData(item)">
				{{item.name}}
			</view> -->
			{{modeName}}
		</view>
		<scroll-view scroll-y="true" @scrolltolower="scrolltolower" style="height: 95%"
		        @refresherrefresh="getRefresherrefresh" :refresher-enabled="false" :refresher-triggered="refresherTriggered"
		        refresher-background="transparent">
			<view class="record-list">
				 <view class="record-item" v-for="(item,index) in records" :key="index">
					 <template v-if="getModeType(item.mode)==0">
						 <view class="left" >
							 <view class="row" v-if="userinfo.orgtype==1">所属账号：{{item.userno}}</view>
							  <view class="row">下注单号：No.{{item.orderno}}</view>
							  <view class="row">期数：{{item.issue}}</view>
							  <view class="row">开奖号码：<text class="row-result">{{item.result}}</text></view>
							  <view class="row">龙虎出入：{{getStatus(item.bno)}}</view>
							  <view class="row">游戏：<text class="red">宝斗</text></view>
							  <view class="row">玩法：<text class="red">{{item.artid}}</text></view>
							  <view class="row">倍率：<text class="red">{{item.cpright}}</text></view>
							  <view class="row">金额：<text class="red">{{item.bailmoney}}</text></view>
							  <view class="row" v-if="item.status == 0">中奖金额：<text class="red" v-if="item.bno">{{item.loss + item.bailmoney - item.comm}}</text></view>
							  <view class="row">下注时间：{{item.ordtime}}</view>
							  <view class="row"  v-if="item.status == 1">注销时间：{{item.cantime}}</view>
						 </view>
						 <view class="right">
							 <view class="red" v-if="item.status == 1">已注销</view>
							 <view class="red" v-else-if="!item.bno">未开奖</view>
							 <view v-else>
								  <view class="red" v-if="item.loss < 0 ">未中奖</view>
								  <view class="blue" v-else-if="item.loss==0">打和</view>
								  <view class="blue" v-else>中奖</view>
							  </view>
						 </view>
					 </template>
					 <template v-else-if="getModeType(item.mode)==1">
						 <view class="left" >
							 <view class="row" v-if="userinfo.orgtype==1">所属账号：{{item.userno}}</view>
							  <view class="row">下注单号：No.{{item.orderno}}</view>
							  <view class="row">
							  <uni-collapse>
								<uni-collapse-item :show-animation="true" :name="index">
									<template v-slot:title>
										 <view class="col-title">
											 <view class="left">期数：{{item.issue}}</view>
										 </view>
									</template>
									<view class="col-content">
										<view class="col-item" v-for="(temp,i) in item.bNos" :key="i">
											<view class="left">
												<view :class="'left-name'+i">{{temp.name}}</view>
												<view :class="'left-name'+i">{{temp.status}}</view>
											</view>
											<view class="right">
												<view class="result-item" v-for="(no,n) in temp.nums" :key="n" :class="'color'+no" >
													{{no}}
												</view>						 
											</view>
										</view>
									</view>
								</uni-collapse-item>
								</uni-collapse>
							  </view>
							  <view class="row">开奖号码：<text class="row-result">{{item.result}}</text></view>
							  <view class="row">游戏：<text class="red">牛牛</text></view>
							  <view class="row">玩法：
								<text class="red">{{getTitle(item.artid)}}
									<text v-if="item.recvno==0">（平投）</text>
									<text v-if="item.recvno==1">（倍投）</text>
								</text>
							  </view>
							  <view class="row">倍率：<text class="red">{{item.cpright}}</text></view>
							  <view class="row">金额：<text class="red">{{item.bailmoney}}</text></view>
							  <view class="row" v-if="item.status == 0">本局输赢：	
									<text class="red" v-if="item.loss > 0">{{item.loss + item.bailmoney - item.comm}}</text>
									<text class="red" v-else>{{item.loss - item.comm}}</text>
							  </view>
							  <view class="row">下注时间：{{item.ordtime}}</view>
							  <view class="row"  v-if="item.status == 1">注销时间：{{item.cantime}}</view>
						 </view>
						 <view class="right">
							    <view class="red" v-if="item.status == 1">已注销</view>
						 		<view class="red" v-else-if="item.comm === 0 && item.loss==0">未开奖</view>
						 		<view v-else>
									  <view class="red" v-if="item.loss < 0 ">未中奖</view>
									  <view class="blue" v-else-if="item.loss==0">打和</view>
									  <view class="blue" v-else>中奖</view>
						 		</view>
						 </view>
					 </template>
				 </view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				records:[],
				statusList:[
					{val:1,name:'入'},
					{val:2,name:'龙'},
					{val:3,name:'出'},
					{val:4,name:'虎'}
				],
				nstatusList:[
					{val:0,name:'无牛'},
					{val:1,name:'牛一'},
					{val:2,name:'牛二'},
					{val:3,name:'牛三'},
					{val:4,name:'牛四'},
					{val:5,name:'牛五'},
					{val:6,name:'牛六'},
					{val:7,name:'牛七'},
					{val:8,name:'牛八'},
					{val:9,name:'牛九'},
					{val:10,name:'牛牛'}
				],
				search:{
					pageIdx:0,
					pageSize:10
				},
				totalPage:1,
				totalCount:0,
				loadMore:false,
				refresherTriggered:false,
				userinfo:{},
				orgtype:'',
				userno:'',
				fdate:'',
				startDate:'',
				endDate:'',
				mode:0,
				tabs:[
					{clevel:0,name:'宝斗'},
					{clevel:1,name:'牛牛'}
				],
				modeName:'',
				modeType:0,// 0 宝斗 1 牛牛
			}
		},
		onLoad(option) {
			this.userinfo = JSON.parse(uni.getStorageSync('userinfo'))
			this.orgtype = this.userinfo.orgtype
			this.userno = option.userno || this.userinfo.userno
			this.startDate = option.startDate
			this.endDate = option.endDate
			this.modeName = uni.getStorageSync('record_name')
			this.mode = uni.getStorageSync('record_mode')
			this.records = []
			this.loadData()
		},
		destroyed() {
			this.records = []
			this.orgtype = ''
			this.userno = ''
			this.startDate = ''
			this.endDate = ''
		},
		methods: {
			getModeType(mode){
				let modeType = 0
				if(mode >= 0){
					if(mode <=4 && mode != 1){
						modeType = 0
					}else{
						modeType = 1
					}
				}
				return modeType
			},
			findData(item){
				this.mode = item.clevel
				this.search.pageIdx = 0
				this.totalPage = 1
				this.totalCount = 0
				this.records = []
				this.loadData()
			},
			scrolltolower() {
				if (this.records.length >= this.totalCount) return
				if(!this.loadMore){
					this.loadMore = true
					this.loadData()
				}
			},
			//下拉刷新
			getRefresherrefresh(){
				this.refresherTriggered = true
				this.search.pageIdx = 0
				this.totalPage = 1
				this.totalCount = 0
				this.records = []
				this.loadData()
			},
			loadData(){
				let url = ''
				if(this.orgtype==1){
					url = '/Query/SubOrdList'
					this.search.startDate = this.startDate 
					this.search.endDate = this.endDate
					// this.search.fdate = this.fdate
				}else{
					url = '/Query/GetOrderList'
				}
				this.search.userno = this.userno
				this.search.mode = this.mode
				
				this.$http.post(url,this.search,res => {
					let datas = res.rData || []
					datas.forEach(item=>{
						if(item.bnno){
							let bNos = item.bnno.split(',') || []
							let sresult = item.result.split(',') || []
							bNos = bNos.map((item,index)=>{
								return {
									name:this.getTitle(index),
									status:this.getNstatus(item),
									nums:this.getNums(sresult,index)
								}
							})
							item.bNos = bNos
						} 
					})
					this.records = [...this.records,...datas]
					this.totalCount = res.iCount;
					if (this.search.pageIdx >= this.totalCount) {
						this.search.pageIdx = this.totalCount + 1;
					} else {
						this.search.pageIdx = this.search.pageIdx + this.search.pageSize
					}
					this.refresherTriggered = false
					this.loadMore = false
				})
			},
			 getNums(sresult,index){
			 	let arr = []
			 	for(var i = 0;i<5;i++){
			 		arr[i] = sresult[index+i]
			 	}
			 	return arr
			 }, 
			 getNstatus(status){
			 	const item = this.nstatusList.find(item=> item.val==status) || {}
			 	return item.name
			 },
			 getTitle(index){
			 	let titles = [{val:0,name:'庄'},{val:1,name:'闲一'}
			 				  ,{val:2,name:'闲二'},{val:3,name:'闲三'}
			 				  ,{val:4,name:'闲四'},{val:5,name:'闲五'}]
			 	const item = titles.find(item=> item.val==index) || {}
			 	return item.name
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
			},
			getStatus(status){
				const item = this.statusList.find(item=> item.val==status) || {}
				return item.name
			},
		}
	}
</script>

<style scoped lang="scss">
.order{
	width: 750upx;
	height: 100vh;
	.tab-bar{
		display: flex;
		width: 670upx;
		padding-left: 40upx;
		padding-right: 40upx;
		.tab-item{
			width:150upx;
			height:80upx;
			line-height: 80upx;
			text-align: center;
			border-bottom:5upx solid #eeeeee;
		}
		.active{
			border-bottom: 5upx solid rgb(40,148,255);
		}
	}
	.search-date{
		padding: 20upx;
	}
	.record-list{
		.record-item{
			background-color: #fff;
			padding: 20upx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			width: 670upx;
			margin: 10upx auto;
			.row-result{
				font-size: 26upx;
			}
			.blue{
				color:blue;
			}
			.red{
				color:red;
			}
			.right{
				font-size: 32upx;
				font-weight: 600;
				padding-right: 40upx;
			}
			.left{
				.col-title{
					height:50upx;
					line-height:50upx;
				}
				.col-content{
					.col-item{
						display: flex;
						justify-content: space-between;
						align-items: center;
						.left{
							width:150upx;
							display: flex;
							justify-content: space-between;
							.left-name0{
								color:red;
							}
						}
						.right{
							display: flex;
							.result-item{
								width: 50upx;
								height: 50upx;
								border-radius: 50%;
								display: flex;
								justify-content: center;
								align-items: center;
								margin-bottom: 20upx;
								color: #fff;
								margin-left: 10upx;
							}
						}
					}
				}
				
				.color01{
					background-color:#FFD700;
				}
				.color02{
					background-color:#00BFFF;
				}
				.color03{
					background-color:#8B4513;
				}
				.color04{
					background-color:#FFA500;
				}
				.color05{
					background-color:#48D1CC;
				}
				.color06{
					background-color:#7B68EE;
				}
				.color07{
					background-color:#778899;
				}
				.color08{
					background-color:#DC143C;
				}
				.color09{
					background-color:#FA8072;
				}
				.color10{
					background-color:#32CD32;
				}
			}
		}
	}
}
</style>