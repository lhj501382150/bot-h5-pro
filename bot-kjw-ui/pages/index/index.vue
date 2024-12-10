<template>
	<view class="content">
		<draw v-if="isShow"></draw>
		
		<view class="draw-game-list">
			<view class="draw-game-item" v-for="(item,index) in draws" :key="index">
				<view class="draw-row">
					<view class="draw-main-title">{{item.modeName}}</view>
					<view class="draw-sub-title">期:{{item.drawIssue}}</view>
				</view>
				<view class="draw-row" v-if="!item.load">
					<view class="draw-main-title">哈希块:<text @click="showHash(item)">{{formatHash(item.preDrawHash)}}</text></view>
				</view>
				<view class="draw-row" v-if="!item.load">
					<view class="code-item" v-for="(num,index) in item.data" :key="index" :class="'color'+num">
						{{num}}
					</view>
				</view>
				<view class="draw-row" v-if="item.load">
					<image src="../../static/images/load.gif" mode="aspectFill" style="width: 200upx;height: 30upx;"></image>
				</view>
				<view class="draw-row">
					<view class="draw-btn-item" @click="goHistory(item)">历史开奖</view>
					<view class="draw-btn-item" @click="goLive(item)">开奖视频</view>
					<!-- <view class="draw-btn-item">立即试玩</view> -->
				</view>
				<view class="draw-time">
					 下期开奖：
					 <uni-countdown :showDay="false" :show-hour="false" :filterShow="{}" color="#ffffff" :minute="item.minute" :second="item.second" @timeup="teimeup(item,index)"></uni-countdown>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { countdown,formatHash } from '../../utils/util'
	import draw from '../../components/draw/draw.vue'
	export default {
		components:{
			draw
		},
		data(){
			return {
				formatHash:formatHash,
				 draws:[],
				 times:[],
				 isShow:false
			}
		},
		onLoad() {
			
		},
		onShow(){
			this.loadDraws()
			this.isShow = true
		},
		onHide() {
			this.isShow = false
			if(this.times && this.times.length > 0){
				this.times.forEach(timed=>{
					if(timed){
						clearTimeout(timed)
					}
				})
			}
		},
		methods: {
			showHash(item){
				window.open(item.modeUrl + '/#/block?hash=0x' + item.preDrawHash ,'_blank')
			},
			goHistory(item){
				uni.setStorageSync('QUERY_MODE',item.mode)
				uni.navigateTo({
					url:'/pages/result/result'
				})
			},
			goLive(item){
				uni.setStorageSync('QUERY_MODE',item.mode)
				uni.switchTab({
					url:'/pages/car/car'
				})
			},
			teimeup(item,index){
				item.load = true 
				this.getOneResult(item,index)
			},
			async getOneResult(item,index){
				let res = await this.$http.post('/draw/getResult',{mode:item.mode})
				res = res.data
				if(res.data.dataId != item.dataId){
					 item = res.data
					 item.data = item.preDrawCode.split(',')
					 item.leftTime = item.leftTime - 15
					 item.minute = parseInt(item.leftTime / 60)
					 item.second =  parseInt(item.leftTime % 60)
					 this.draws.splice(index,1)
					 this.draws.splice(index,0,item)
				}else{
					let timed =setTimeout(()=>{
						this.getOneResult(item,index)
					},1000)
					this.times.push(timed)
				}
			},
			loadDraws(){
				this.$http.post('/draw/getNewDraw',{},(res)=>{
					this.draws = res.data.filter(item=>item.type=='B').map(item=>{
						if(item.preDrawCode){
							item.data = item.preDrawCode.split(',')
							item.leftTime = item.leftTime - 15
							item.minute = parseInt(item.leftTime / 60)
							item.second =  parseInt(item.leftTime % 60)
							if(item.leftTime > 0){
								item.load = false
							}else{
								item.load = true
							}
						}
						return item
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
.content{
	background-color:#e2e2e2;
}
 .draw-game-list{
	background-color:#ffffff;
	width:750upx;
	height: 62vh;
	overflow:auto;
	margin-top: 20upx;
	.draw-game-item{
		width:710upx; 
		border: 1px solid #e2e2e2;
		padding-left: 20upx;
		padding-right: 20upx;
		.draw-row{
			width:710upx;
			height: 70upx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			.draw-main-title{
				font-family: 'squarefontregular';
				font-size: 32upx;
				letter-spacing: 1px;
				font-weight: 500;
				text{
					margin-left: 30upx;
				}
			}
			.draw-sub-title{
				font-size: 30upx;
			}
			text{
				color:blue;
			}
			.code-item{
				width: 59upx;
				height: 59upx;
				margin-left: 10upx;
				margin-right: 6upx;
				display: flex;
				justify-content: center;
				align-items: center;
				color: #fff;
			}
			.draw-btn-item{
				margin-top: 20upx;
				width: 200upx;
				border: 1px solid #e2e2e2;
				height: 50upx;
				line-height: 50upx;
				text-align: center;
				font-size: 28upx;
			}
		}
		.draw-time{
			background-color: #0066ec;
			color: #ffffff;
			text-align: center;
			margin-left: -20upx;
			width: 750upx;
			height: 70upx;
			margin-top: 20upx;
			display: flex;
			justify-content: center;
			align-items: center;
		}
	}
 }
</style>
