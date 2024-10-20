<template>
	<view class="money-record">
		<uni-nav-bar left-icon="left"  title="资金明细" background-color="rgb(40,148,255)" color="#fff" :border="false" @clickLeft="goBack"></uni-nav-bar>
		<scroll-view scroll-y="true" @scrolltolower="scrolltolower" style="height: 95%;"
		        @refresherrefresh="getRefresherrefresh" :refresher-enabled="false" :refresher-triggered="refresherTriggered"
		        refresher-background="transparent">
			<view class="record-list">
				 <view class="record-item" v-for="(item,index) in records" :key="index">
					  <view class="row">
						  <view class="left">流水号：{{item.wartno}}</view>
					      <view class="right">金额：{{item.chmoney}}</view>
					  </view>
					  <view class="row2">
						  <view class="left">类型：{{item.subno}}</view>
					      <view class="right">时间：{{item.ctime}}</view>
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
			}
		},
		onLoad() {
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
				this.search.pageIdx = 1
				this.totalPage = 1
				this.totalCount = 0
				this.records = []
				this.loadData()
			},
			loadData(){
				this.search.userno = uni.getStorageSync('userno')
				this.$http.post("/Query/GetMoneyList",this.search,res => {
					let datas = res.rData || []
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
					url:'/pages/user/user'
				})
			}
		}
	}
</script>

<style scoped lang="scss">
.money-record{
	width: 750upx;
	height: 100vh;
	.record-list{
		padding: 40upx;
		.record-item{
			background-color: #fff;
			padding: 20upx;
			margin-bottom: 20upx;
			.row,.row2{
				display: flex;
				justify-content: space-between;
				align-items: center;
			}	
			.row{
				font-size: 30upx;
				padding-bottom: 20upx;
			}
			.row2{
				font-size: 26upx;
				color: #787878;
			}
		}
	}
}
</style>
