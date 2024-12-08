<template>
	<view class="content">
		<view class="game-list">
			<view class="game-item" v-for="(item,index) in games" :key="index">
				<view class="left">
					<image :src="'../../static/images/logos/MODE'+item.type+'.png'" mode="aspectFill"></image>
				</view>
				<view class="right">
					<view class="game-name">{{item.modeName}}</view>
					<view class="play-box" @click="goCar(item)">
						<image src="../../static/images/logos/play.png" mode="aspectFill" style="width: 40upx;height: 40upx;"></image>
						<text>观看直播</text>
					</view>
				</view>
			</view>
			
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				games:[]
			}
		},
		onShow(){
			this.loadGames()
		},
		methods: {
			goCar(item){
				uni.setStorageSync('QUERY_MODE',item.mode)
				uni.switchTab({
					url:'/pages/car/car'
				})
			},
			loadGames(){
				this.$http.post('/draw/findType',{},res=>{
					res = res.data || []
					this.games = res
				})
			}
		}
	}
</script>

<style scoped lang="scss">
.content{
	margin: 0;
	padding: 0;
	width: 750upx;
	height: 100vh;
	overflow: auto;
	.game-list{
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		align-items: center;
		padding: 0upx 20upx;
		width: 710upx;
		.game-item{
			border: 1px solid #e2e2e2;
			width: 350upx;
			height: 120upx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			.left{
				width: 100upx;
				height: 100%;
				display: flex;
				align-items: center;
				justify-content: center;
				image{
					width: 80upx;
					height:80upx;
				}
			}
			.right{
				width: 250upx;
				text-align: left;
				.play-box{
					display: flex;
					justify-content: flex-start;
					align-items: center;
					color: #d81e06;
					text{
						margin-left: 10upx;
					}
				}
			}
		}
	}
}
</style>
