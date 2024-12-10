<template>
	<view class="result">
		<view class="draw-item">
			<view class="row-head">
				<view class="select-box">
					<uni-data-select v-model="mode" placeholder="类型选择" :localdata="modes"  @change="changeMode"></uni-data-select>
				</view>
			</view>
			<view class="draw-info">
				<view class="info-left">
					<image :src="'../../static/images/logos/MODE'+drawItem.type+'.png'" mode="aspectFill"></image>
				</view>
				<view class="info-right">
					<view class="info-row">期:<text>{{drawItem.preDrawIssue}}</text></view>
					<template v-if="drawItem.type=='C'">
						<view class="info-row">开奖时间:{{drawItem.drawTime}}</view>
						<view class="info-row">
							<view class="code-item circle" v-for="(num,index) in drawItem.data" :key="index" :class="'color'+num.color">
								{{num.number}}
							</view>
						</view>
					</template>
					<template v-else>
						<view class="info-row" v-if="drawItem.type=='B'">哈希块:<text class="hash" @click="showHash(drawItem)">{{formatHash(drawItem.preDrawHash)}}</text></view>
						<view class="info-row" v-if="isLoad">
							<image src="../../static/images/load.gif" mode="aspectFill" style="width: 200upx;height: 30upx;"></image>
						</view>
						<view class="info-row" v-else>
							<view class="code-item" v-for="(num,index) in drawItem.data" :key="index" :class="'color'+num">
								{{num}}
							</view>
						</view>
					</template>
					
					
				</view>
			</view>
			<view class="row-head">
				 <view class="headName">观看直播</view>
			</view>
		</view>
		<template v-if="drawItem.type=='C'">
			<view class="vide-box">
				<video :src="drawItem.videoUrl" controls style="width: 100%; height: 100%;"></video>
				<p>{{drawItem.title}}</p>
			</view>
			<view class="video-link" @click="goUrl">去看直播</view>
		</template>
		<template v-else>
			<draw :mode="mode" v-if="isShow" @updateDraw="updateDraw" @updateRun="changeRun"></draw>
		</template>
	</view>
</template>

<script>
	import { countdown,formatHash } from '../../utils/util'
	import draw from '../../components/draw/draw.vue'
	export default {
		components:{
			draw
		},
		data() {
			return {
				formatHash:formatHash,
				drawItem:{},
				totalPage:1,
				totalCount:0,
				refresherTriggered:false,
				mode:0,
				modes:[],
				isLoad : false,
				preDrawItem:{},
				timed:'',
				isShow:false,
				showCarModes:['A2','B1','B2','B3'],
				isRunning:false
			}
		},
		onShow() {
			this.isShow = false
			this.$nextTick(()=>{
				this.mode = uni.getStorageSync('QUERY_MODE')
				if(!this.mode){
					this.mode = 'C1'
				}
				if(this.showCarModes.includes(this.mode)){
					this.isShow = true
				}
				this.loadModes()
				this.getOneResult()
			})
		},
		onHide() {
			this.isShow = false
		},
		methods: {
			showHash(item){
				window.open(item.modeUrl + '/#/block?hash=0x' + item.preDrawHash ,'_blank')
			},
			changeRun(flag){
				this.isRunning = flag
			},
			updateDraw(item){
				item.data = item.preDrawCode.split(',')
				this.drawItem = item
				this.isLoad = false
			},
			goUrl(){
				window.open(this.drawItem.modeUrl,'_blank')
			},
			changeMode(mode){
				if(this.isRunning) return
				this.isShow = false
				this.search = {
					pageNum:0,
					pageSize:20
				}
				this.mode = mode
				this.getOneResult()
				if(this.showCarModes.includes(this.mode)){
					setTimeout(()=>{
						this.isShow = true	
					},1000)
				}
			},
			teimeup(item){
				this.isLoad = true
				this.preDrawItem = item
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
					if(item.type=='C'){
						item.data = JSON.parse(item.memo)
					}else{
						item.leftTime = item.leftTime - 15
						item.minute = parseInt(item.leftTime / 60)
						item.second =  parseInt(item.leftTime % 60)
					}
					this.drawItem = item
					if(this.timed){
						clearTimeout(this.timed)
					}
				}
				
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
}

.vide-box{
	width: 750upx;
	height: 420upx;
	background-color: #000000;
	color: #fff;
	position: relative;
	p{
		position: absolute;
		top:180upx;
		left:100upx
	}
}
.video-link{
	width: 750upx;
	line-height: 60px;
	text-align: center;
	font-size: 28upx;
	color: #0055ff;
}
</style>
