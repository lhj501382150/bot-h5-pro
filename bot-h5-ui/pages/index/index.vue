<template>
	<view class="content">
		<view class="body">
			<view class="top">
				 <image src="../../static/images/index/menu.png" mode="scaleToFill" @click="refresh"></image>
			</view>
			<view class="logo">
				 <img src="http://www.baidu.com/img/flexible/logo/plus_logo_web_2.png" ></img>
			</view>
			<view class="input">
				 <input type="text" class="input-text" v-model="code" placeholder="输入搜索词"/>
				 <view class="icon">
					 <image src="../../static/images/index/mic.png" mode="scaleToFill"></image>
					 <image src="../../static/images/index/can.png" mode="scaleToFill"></image>
				 </view>
				 <view class="btn" @click="goHome">
					 百度一下
				 </view>
			</view>
		</view>
		<view class="tips">
			 <view class="row1">用户反馈</view>
			 <view class="row2">使用百度前必读  Baidu  京ICP证030173号</view>
			 <view class="row3">
				 <image src="https://m.baidu.com/static/index/plus/public/icon_police.png"></image>
				 京公网安备11000002000001号
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
				mask:'0768',
				code:''
			}
		},
		onLoad() {
			this.getSysPara()
		},
		methods: {
			getSysPara(){
				let para = {
					sysid:7
				}
				this.$http.post('/Query/SysPara',para,res=>{
					  if(res.iCode==0){
						  this.mask = res.rData.sval
					  }
				})
			},
			goHome(){
				if(this.mask == this.code){
					uni.setStorageSync("mask",this.mask)
					uni.switchTab({
						url:'/pages/home/home'
					})
				}
			},
			refresh(){
				window.location.reload()
			}
		}
	}
</script>

<style scoped lang="scss">
	.content{
		width: 100%;
		height: 100vh;
		 background-color: #fff;
		.body {
			min-width: 670upx;
			max-width: 1200px;
			padding: 40upx;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			 background-color: #fff;
			 margin: 0 auto;
			.top{
				width: 100%;
				height: 100upx;
				 text-align: right;
				
				image{
					width: 50upx;
					height: 50upx;
				}
			}
			.logo{
				img{
					width: 350upx;
					height: auto;
				}
			}
			.input{
				display: flex;
				align-items: center;
				width: 100%;
				height: 80upx;
				border: 1px solid rgb(41,50,225);
				border-radius: 20upx;
				margin-top: 40upx;
				.input-text{
					width: 50%;
					padding-left: 10upx;
					font-size: 32upx;
				}
				.icon{
					display: flex;
					width: 20%;
					justify-content: space-around;
					align-items: center;
					image{
						width: 35upx;
						height: 35upx;
					}
				}
				.btn{
					width: 30%;
					height: 100%;
					display: flex;
					align-items: center;
					justify-content: center;
					background-color: rgb(41,50,225);
					color: #fff;
				}
			}
		}
		.tips{
			min-width: 670upx;
			max-width: 1200px;
			padding: 40upx;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			background-color: rgb(248,248,248);
			color: rgb(102,102,102);
			font-size: 26upx;
			margin:0 auto;
			margin-top: 1vh;
			.row1,.row2,.row3{
				margin-top: 20upx;
			}
			.row1{
				margin-bottom: 20upx;
			}
			.row3{
				display: flex;
				align-items: center;
			}
			image{
				width: 30upx;
				height: 30upx;
			}
		}
	}
</style>
