<template>
	<view class="result">
		<uni-nav-bar left-icon="left"  title="开奖公告" background-color="rgb(40,148,255)" color="#fff" :border="false" @clickLeft="goBack"></uni-nav-bar>
		<scroll-view scroll-y="true" @scrolltolower="scrolltolower" style="height: 95%;"
		        @refresherrefresh="getRefresherrefresh" :refresher-enabled="false" :refresher-triggered="refresherTriggered"
		        refresher-background="transparent">
			<view class="record-list">
				 <view class="record-item" v-for="(item,index) in records" :key="index">
					  <view class="head">
						  <view class="type">
							  {{modeName}}
						  </view>
						  <view class="draw">{{item.issue}}期</view>
						   <view class="time">{{item.sTime}}</view>
					  </view>
					  <view class="row">
					  	<view class="num-item" v-for="(item,index) in item.nums" :key="index" :class="'color'+item">
					  		{{item}}
					  	</view>
					  </view>
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
				search:{
					pageIdx:0,
					pageSize:10
				},
				totalPage:1,
				totalCount:0,
				refresherTriggered:false,
				mode:0,
				tabs:[
					{clevel:0,name:'宝斗'},
					{clevel:1,name:'牛牛'}
				],
				modeName:'',
				type:'',// 0 宝斗  1 牛牛
			}
		},
		onLoad(option) {
			this.modeName = uni.getStorageSync('result_name')
			this.mode = uni.getStorageSync('result_mode')
			this.records = []
			this.loadData()
		},
		methods: {
			scrolltolower() {
				if (this.records.length >= this.totalCount) return
				this.loadData()
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
				this.search.userno = uni.getStorageSync('userno')
				this.search.mode = this.mode
				this.$http.post("/Query/ReustList",this.search,res => {
					let datas = res.rData || []
					datas.forEach(item=>{
						const nums = item.sresult.split(',')
						item.nums = nums
					})
					this.records = [...this.records,...datas]
					this.totalCount = res.iCount;
					if (this.search.pageIdx >= this.totalCount) {
						this.search.pageIdx = this.totalCount + 1;
					} else {
						this.search.pageIdx = this.search.pageIdx + this.search.pageSize
					}
					this.refresherTriggered = false
				})
			},
			goBack(){
				uni.switchTab({
					url:'/pages/home/home'
				})
			}
		}
	}
</script>

<style scoped lang="scss">
.result{
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
	.record-list{
		padding: 20upx;
		.record-item{
			background-color: #fff;
			padding: 20upx;
			margin-bottom: 20upx;
			.head{
				display: flex;
				justify-content: space-between;
				align-items: center;
			}
			.row{
				display: flex;
				justify-content: space-between;
				align-items: center;
				width: 100%;
				height: 80upx;
				padding-top: 20upx;
				padding-bottom: 20upx;
				border-bottom: 2upx solid #e2e2e2;
				.num-item{
					width: 59upx;
					height: 59upx;
					margin-left: 10upx;
					margin-right: 6upx;
					border-radius: 50%;
					display: flex;
					justify-content: center;
					align-items: center;
					color: #fff;
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
