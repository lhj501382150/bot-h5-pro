<template>
	<view class="result">
		<uni-nav-bar left-icon="left"  title="开奖网" background-color="#FFFFFF" color="#000000" :border="false" @clickLeft="goBack"></uni-nav-bar>
		<view class="draw-item">
			<view class="row-head">
				<view class="select-box">
					<uni-data-select v-model="mode" placeholder="类型选择" :localdata="modes"  @change="changeMode"></uni-data-select>
				</view>
				<view class="play-box" @click="goCar">
					<image src="../../static/images/logos/play.png" mode="aspectFill" style="width: 40upx;height: 40upx;"></image>
					<text>观看直播</text>
				</view>
			</view>
			<view class="draw-info">
				<view class="info-left">
					<image :src="'../../static/images/logos/MODE'+drawItem.type+'.png'" mode="aspectFill"></image>
				</view>
				<view class="info-right">
					<view class="info-row">期:<text>{{drawItem.preDrawIssue}}</text></view>
					<template v-if="drawItem.type=='C'">
						<view class="info-row">开奖时间:{{drawItem.drawTime}} </view>
						<view class="info-row">
							<view class="code-item circle" v-for="(num,index) in drawItem.numbers" :key="index" :class="'color'+num.color">
								{{num.number}}
							</view>
						</view>
					</template>
					<template v-else>
						<view class="info-row">开奖时间:
						<uni-countdown v-if="!isLoad" :showDay="false" :show-hour="false" :filterShow="{}" color="#d81e06" :minute="drawItem.minute" :second="drawItem.second" @timeup="teimeup(drawItem)"></uni-countdown>
						</view>
						<view class="info-row" v-if="drawItem.type=='B'">哈希块:<text class="hash" @click="goHash(drawItem)">{{formatHash(drawItem.preDrawHash)}}</text></view>
						<view class="info-row" v-if="isLoad">
							<image src="../../static/images/load.gif" mode="aspectFill" style="width: 200upx;height: 30upx;"></image>
						</view>
						<view class="info-row" v-else>
							<template v-if="drawItem.mode=='A3'">
								<view class="code-item" v-for="(num,index) in drawItem.data" :key="index" :class="'color0'+num">
									{{num}}
								</view>
							</template>
							<template v-else>
								<view class="code-item" v-for="(num,index) in drawItem.data" :key="index" :class="'color'+num">
									{{num}}
								</view>
							</template>
						</view>
					</template>
				</view>
			</view>
			<view class="row-head">
				 <view class="headName">{{drawItem.preDrawDate}}历史开奖</view>
				<view class="select-box">
					 <uni-datetime-picker type="date" placeholder="选择日期" v-model="drawItem.preDrawDate" @change="changeDate"/>
				</view>
			</view>
			
		</view>
		<view class="table-head">
			  <view class="type">
				  期
			  </view>
			  <view class="draw">开奖号码
				<text v-if="drawItem.type=='B'" style="font-size: 24upx;margin-left:40upx;color: blue;cursor: pointer;"  @click="showHash=!showHash">
					<text v-if="showHash">号码</text>
					<text v-else>哈希</text>
				</text>
			  </view>
		</view>
		<scroll-view scroll-y="true" @scrolltolower="scrolltolower" style="height: 80%;"
		        @refresherrefresh="getRefresherrefresh" :refresher-enabled="false" :refresher-triggered="refresherTriggered"
		        refresher-background="transparent">
			<view class="record-list">
				 <view class="record-item" v-for="(item,index) in records" :key="index">
					 <view class="type">
					 			{{item.preDrawTime}}		
					 </view>
					 <template v-if="item.type=='C'">
						 <view class="draw" >
								<view class="num-item circle" v-for="(num,index) in item.numbers" :key="index" :class="'color'+num.color">
									{{num.number}}
								</view>		
						 </view>
					 </template>
					 <template v-else>
						 <view class="draw" v-if="!showHash">
							 <template v-if="drawItem.mode=='A3'">
							 	<view class="num-item" v-for="(item,index) in item.nums" :key="index" :class="'color0'+item">
							 		{{item}}
							 	</view>	
							 </template>
							 <template v-else>
							 	<view class="num-item" v-for="(item,index) in item.nums" :key="index" :class="'color'+item">
							 		{{item}}
							 	</view>	
							 </template>
								
						 </view>
						 <view class="draw" v-else>
						 	<view class="hash-text" @click="goHash(item)">{{formatHash(item.preDrawHash)}}</view>					 
						 </view>
					 </template>
					 
						
				 </view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import { countdown,formatHash } from '../../utils/util'
	export default {
		data() {
			return {
				formatHash:formatHash,
				drawItem:{},
				records:[],
				search:{
					pageNum:1,
					pageSize:20,
					params:{
						PRE_DRAW_DATE:''
					}
				},
				totalPage:1,
				totalCount:0,
				refresherTriggered:false,
				mode:0,
				modes:[],
				isLoad : false,
				preDrawItem:{
					dataId:0
				},
				timed:'',
				showHash:false
			}
		},
		onLoad(option) {
			this.mode = uni.getStorageSync('QUERY_MODE')
			if(!this.mode){
				this.mode = 'B1'
			}
			this.records = []
			this.loadModes()
			this.getOneResult()
			this.loadData()
		},
		methods: {
			goCar(){
				uni.setStorageSync('QUERY_MODE',this.mode)
				uni.switchTab({
					url:'/pages/car/car'
				})
			},
			changeDate(val){
				this.records = []
				this.search = {
					pageNum:1,
					pageSize:20
				}
				this.loadData()
			},
			changeMode(mode){
				if(!this.timed){
					clearTimeout(this.timed)
				}
				this.search = {
					pageNum:1,
					pageSize:20
				}
				this.mode = mode
				this.records = []
				this.getOneResult()
				this.loadData()
			},
			teimeup(item){
				this.isLoad = true
				this.preDrawItem = item
				this.getOneResult()
			},
			loadModes(){
				this.$http.post('/draw/findType',{},res=>{
					res = res.data || []
					this.modes = res.map(item=>{
						let para = {
							value:item.mode,
							text:item.modeName
						}
						return para
					})
				})
			},
			async getOneResult(){
				let res = await this.$http.post('/draw/getResult',{mode:this.mode})
				res = res.data
				if(res.data.dataId != this.preDrawItem.dataId){
					this.isLoad = false
					let item = res.data
					item.data = item.preDrawCode.split(',')
					item.leftTime = item.leftTime - 15
					item.minute = parseInt(item.leftTime / 60)
					item.second =  parseInt(item.leftTime % 60)
					if(item.type == 'C'){
						item.numbers = JSON.parse(item.memo)
					}
					this.drawItem = item
					if(this.timed){
						clearTimeout(this.timed)
					}
				}else{
					this.timed = setTimeout(()=>{
						this.getOneResult()
					},1000)
				}
				
			},
			goHash(item){
				let url = item.modeUrl + '/#/block?hash=0x' + item.preDrawHash
				window.open(url,'_blank')
			},
			scrolltolower() {
				if (this.records.length >= this.totalCount) return
				this.search.pageNum += 1
				this.loadData()
			},
			//下拉刷新
			getRefresherrefresh(){
				this.refresherTriggered = true
				this.search.pageNum = 0
				this.totalPage = 1
				this.totalCount = 0
				this.records = []
				this.loadData()
			},
			loadData(){
				this.search.params = {
					mode:this.mode,
					PRE_DRAW_DATE:this.drawItem.preDrawDate
				}
				this.$http.post("/draw/findPage",this.search,res => {
					let datas = res.data.content || []
					datas.forEach(item=>{
						const nums = item.preDrawCode.split(',')
						item.nums = nums
						if(item.type=='C'){
							item.numbers = JSON.parse(item.memo)
						}
					})
					this.records = [...this.records,...datas]
					this.totalCount = res.data.totalSize;
					 
					this.refresherTriggered = false
				})
			},
			goBack(){
				uni.switchTab({
					url:'/pages/index/index'
				})
			}
		}
	}
</script>

<style scoped lang="scss">
.result{
	width: 750upx;
	height: 100vh;
	.draw-item{
		width: 100%;
		.row-head{
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 10upx 20upx;
			background-color: #fafafa;
			border-bottom: 1px solid #e2e2e2;
			.select-box{
				width:300upx;
			}
			.play-box{
				display: flex;
				justify-content: space-between;
				align-items: center;
				color: #d81e06;
				text{
					margin-left: 10upx;
				}
			}
		}
		.draw-info{
			display: flex;
			justify-content: space-between;
			align-items: center;
			border-bottom: 1px solid #e2e2e2;
			padding-bottom: 20upx;
			.info-left{
				width: 200upx;
				display: flex;
				justify-content: center;
				align-items: center;
				image{
					width: 120upx;
					height: 120upx;
				}
			}
			.info-right{
				width: 550upx;
				.info-row{
					height: 50upx;
					line-height: 50upx;
					display: flex;
					align-items: center;
					text{
						margin-left: 20upx;
					}
					.hash{
						font-family: 'squarefontregular';
						color:blue;
					}
				}
				.code-item{
					width: 50upx;
					height: 50upx;
					margin-left: 2upx;
					margin-right: 2upx;
					display: flex;
					justify-content: center;
					align-items: center;
					color: #fff;
				}
			}
		}
	}
	.table-head{
		display: flex;
		justify-content: space-between;
		align-items: center;
		background-color: #fafafa;
		border-bottom: 1px solid #e2e2e2;
		.type{
			width: 200upx;
			text-align: center;
		}
		.draw{
			width: 550upx;
			text-align: center;
		}
	}
	.record-list{
		.record-item{
			border-bottom: 1px solid #e2e2e2;
			display: flex;
			min-height: 60upx;
			justify-content: space-between;
			align-items: center;
			 .type{
			 	width: 200upx;
			 	text-align: center;
			 }
			 .draw{
			 	width: 550upx;
			 	text-align: center;
				display: flex;
				justify-content: flex-start;
				align-items: center;
				.num-item{
					width: 45upx;
					height: 45upx;
					margin-left: 2upx;
					margin-right: 2upx;
					border-radius: 50%;
					display: flex;
					justify-content: center;
					align-items: center;
					color: #fff;
				}
				.hash-text{
					color: #0055ff;
					font-family: 'squarefontregular';
				}
			 }
		}
	}
}
</style>
