<template>
	<view class="sys-popup">
		<uni-popup ref="sysPopup" :mask-click="false" background-color="#fff" borderRadius="10upx 10upx 0upx 0upx">
			<view class="content">
				<view class="heaer">
					{{notice.title}}
				</view>
				<view class="tips" v-html="notice.content"></view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	 
	export default {
		props:{
			isShow:{
				type:Boolean,
				default:false
			}
		},
		data() {
			return {
				notice:{}
			}
		},
		mounted() { 
			 this.loadNotice()
		},
		watch:{
			isShow:{
				handler(newVal){
					if(newVal){
						this.$refs.sysPopup.open()
					}else{
						this.$refs.sysPopup.close()
					}
				}
			}
		},
		methods: {
			 loadNotice(){
			 	let userno = uni.getStorageSync('userno')
				let para = {
					userno:userno,
					pageIdx:0,
					pageSize:10
				}
			 	this.$http.post("/Notice/GetList",para,res => {
			 		let datas = res.rData || []
			 		if(datas.length > 0){
			 			let para = {
			 				noticeno:datas[0].noticeno
			 			}
			 			this.$http.post("/Notice/GetNotice",para,res => {
			 				 this.notice = res.rData
			 			})
			 		}
			 	})
			 },
		}
	}
</script>

<style scoped lang="scss">
.content{
	width: 670upx;
	height: 50vh;
	.heaer{
		font-size: 36upx;
		font-weight: 600;
		margin-top: 50upx;
		border-bottom: 2upx solid #e2e2e2;
		text-align: center;
		padding-bottom: 20upx;
	}
	.tips{
		padding: 40upx;
	}
	
}
</style>
