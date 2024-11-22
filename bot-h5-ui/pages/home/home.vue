<template>
	<view class="home">
		<view class="bg-img">
			<img src="../../static/images/home/bg.png" class="bg-img"></img>
		</view>
		<view class="notice">
			<uni-notice-bar scrollable single showIcon :text="notice" :speed="50" background-color="rgb(219,202,80)" color="#000" more-color="#000" :showGetMore="true" @getmore="showDetail"></uni-notice-bar>
		</view>
		<view class="games">
			<view  v-for="(item,index) in games" :key="index" class="game-item">
				<view class="title">{{item.name}}</view>
				<view class="oper-btn">
					<view @click="showGame(item)">立即试玩</view>
					<view @click="goRecord(item)">投注记录</view>
					<view @click="goResult(item)">历史开奖</view>
				</view>
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
					{name:'澳洲10宝斗',img:'../../static/images/home/logo.png',path:'./qmbd',mode:0,time:5},
					{name:'澳洲10牛牛',img:'../../static/images/home/logo.png',path:'./hxnn',mode:8,time:5,url:'niu10'},
					{name:'哈希1分宝斗',img:'../../static/images/home/niu.jpg',path:'./hxbd',mode:2,time:1},
					{name:'哈希1分牛牛',img:'../../static/images/home/niu.jpg',path:'./hxnn',mode:5,time:1},
					{name:'哈希3分宝斗',img:'../../static/images/home/niu.jpg',path:'./hxbd',mode:3,time:3},
					{name:'哈希3分牛牛',img:'../../static/images/home/niu.jpg',path:'./hxnn',mode:6,time:3},
					{name:'哈希5分宝斗',img:'../../static/images/home/niu.jpg',path:'./hxbd',mode:4,time:5},
					{name:'哈希5分牛牛',img:'../../static/images/home/niu.jpg',path:'./hxnn',mode:7,time:5},
					{name:'极速赛车牛牛',img:'../../static/images/home/niu.jpg',path:'./qmnn',mode:1,time:1},
					
				],
				search:{
					pageIdx:0,
					pageSize:10
				}
			}
		},
		onShow() {
			this.loadNotice()
			this.getSysPara()
		},
		methods: {
			getSysPara(){
				let para = {
					sysid:30
				}
				this.$http.post('/Query/SysPara',para,res=>{
				  if(res.iCode==0){
					 let data = res.rData.sval
					 uni.setStorageSync('baseUrl',data)
				  }
				})
			},
			showDetail(){
				let no = this.noticeno
				uni.navigateTo({
					url:'/pages/user/noticeDetail?no='+ no
				})
			},
			showGame(item){
				uni.setStorageSync('xz_mode',item.mode)
				uni.setStorageSync('xz_name',item.name)
				uni.setStorageSync('record_mode',item.mode)
				uni.setStorageSync('record_name',item.name)
				uni.setStorageSync('xz_time',item.time)
				uni.setStorageSync("socketUrl",item.url)
				uni.navigateTo({
					url:item.path
				})
			},
			goRecord(item){
				uni.setStorageSync('record_mode',item.mode)
				uni.setStorageSync('record_name',item.name)
				uni.navigateTo({
					url:'/pages/user/order'
				})
			},
			goResult(item){
				uni.setStorageSync('result_mode',item.mode)
				uni.setStorageSync('result_name',item.name)
				uni.navigateTo({
					url:'/pages/user/result'
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
		justify-content: flex-end;
		width: 100%;
		.game-item{
			width: 40%;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			border: 1px solid #e2e2e2;
			margin-bottom: 20upx;
			margin-right: 50upx;
			.title{
				width: 100%;
				line-height: 80upx;
				text-align: center;
				font-size: 40upx;
				font-weight: 800;
				letter-spacing: 2px;
				background-color: #e2e2e2;
				padding: 10upx;
			}
			.oper-btn{
				width: 100%;
				line-height: 40upx;
				display: flex;
				justify-content: space-between;
				align-items: center;
				font-size: 24upx;
				color: #ffffff;
				background: linear-gradient(90deg,#2894ff,#66b3ff);
				padding: 10upx;
				view{
					
				}
			}
		}
	}
}
</style>
