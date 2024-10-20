<template>
	<view class="home">
		<view class="bg-img">
			<img src="../../static/images/home/bg.png" class="bg-img"></img>
		</view>
		<view class="notice">
			<uni-notice-bar scrollable single showIcon :text="notice" :speed="50" background-color="rgb(219,202,80)" color="#000" more-color="#000" :showGetMore="true" @getmore="showDetail"></uni-notice-bar>
		</view>
		<view class="games">
			<view  v-for="(item,index) in games" :key="index" class="game-item" @click="showGame(item)">
				<img :src="item.img"/>
				<view class="game-name">{{item.name}}</view>
			</view>
		</view>
		<sys-status></sys-status>
	</view>
</template>

<script>
	import SysStatus from '../../components/sys-status/sys-status.vue'
	export default {
		components:{
			SysStatus
		},
		data() {
			return {
				notice:'公告：每天下午2:00-2:15系统自动维护15分钟',
				noticeno:'',
				games:[
					{name:'四方通宝',img:'../../static/images/home/logo.png',path:'./qmbd'},
					{name:'四方牛牛',img:'../../static/images/home/niu.jpg',path:'./qmnn'}
					
				],
				search:{
					pageIdx:0,
					pageSize:10
				}
			}
		},
		onShow() {
			this.loadNotice()
		},
		methods: {
			showDetail(){
				let no = this.noticeno
				uni.navigateTo({
					url:'/pages/user/noticeDetail?no='+ no
				})
			},
			showGame(item){
				uni.navigateTo({
					url:item.path
				})
			},
			loadNotice(){
				this.search.userno = uni.getStorageSync('userno')
				this.$http.post("/Notice/GetList",this.search,res => {
					let datas = res.rData || []
					if(datas.length > 0){
						// let para = {
						// 	noticeno:datas[0].noticeno
						// }
						// this.$http.post("/Notice/GetNotice",para,res => {
						// 	 this.notice = res.rData.content
						// })
						this.notice = datas[0].title  
						this.noticeno = datas[0].noticeno
					}
				})
			},
			
		}
	}
</script>

<style scoped lang="scss">
.home{
	background-color: #fff;
	width: 750upx;
	min-height: 100vh;
	.bg-img{
		width: 750upx;
		img{
			width: 750upx;
			height: 600upx;
		}
	}
	.notice{
		margin-top: -10upx;
		z-index: 99;
	}
	.games{
		display: flex;
		flex-wrap: wrap;
		justify-content: space-around;
		width: 100%;
		.game-item{
			width: 40%;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			img{
				width: 100%;
				height: 310upx;
			}
		}
	}
}
</style>
