<template>
	<view class="notcie">
		<uni-nav-bar left-icon="left"  title="平台公告" background-color="rgb(40,148,255)" color="#fff" :border="false" @clickLeft="goBack"></uni-nav-bar>
		<view class="notice-detail">
			<view class="top">
				<view class="header">
					{{notice.title}}
				</view>
				<view class="time">
					{{notice.nottime}}
				</view>
			</view>
			<view class="content" v-html="notice.content">
			</view>
			 
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				noticeno:'',
				notice:{}
			}
		},
		onLoad(option) {
			this.noticeno = option.no
			this.loadData()
		},
		methods: {
			loadData(){
				let para = {
					noticeno:this.noticeno
				}
				this.$http.post("/Notice/GetNotice",para,res => {
					 this.notice = res.rData
				})
			},
			goBack(){
				let pages = getCurrentPages()
				if(pages.length > 1){
					let path = pages[pages.length-2].route
					if(!path.startsWith("/")){
						path = "/" + path
					}
					uni.reLaunch({
						url:path
					})
				}else{
					history.go(-1);
				}
				
			}
		}
	}
</script>

<style scoped lang="scss">
.notcie{
	width: 750upx;
	height: 100vh;
	background-color: #fff;
	.notice-detail{
		padding: 40upx;
		.top{
			border-bottom: 2upx solid #e2e2e2;
			padding-bottom: 20upx;
			.header{
				text-align: center;
				font-size: 36upx;
				font-weight: 600;
			}
			.time{
				text-align: right;
				font-size: 24upx;
				margin-top: 20upx;
				color: #7d7d7d;
			}
			
		}
		.content{
			margin-top: 40upx;
		}
	}
}
</style>
