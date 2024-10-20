<template>
	<view class="sys-popup">
		<uni-popup ref="sysPopup" :mask-click="false" background-color="#fff" borderRadius="10upx 10upx 0upx 0upx">
			<view class="content">
				<view class="heaer">
					系统公告
				</view>
				<view class="tips">系统维护中......</view>
			</view>
		</uni-popup>
	</view>
</template>

<script>
	// import {systemid} from '@/static/config/config.js'
	import {encrypto,decrypto} from '@/utils/js-aox.js'
	export default {
		data() {
			return {
				systemid:''
			}
		},
		mounted() { 
			this.getSysPara()
			this.getDate()
		},
		methods: {
			getDate(){
				const time = Date.parse(new Date('2025/10/08 06:59:59'));
				let expireTime = time 
				const str = encrypto(expireTime.toString(),88,16);
				console.log(str,'--------------------')
				console.log(new Date(parseInt(decrypto(str,88,16))))
			},
			getSysPara(){
				let para = {
					sysid:10
				}
				this.$http.post('/Query/SysPara',para,res=>{
				  if(res.iCode==0){
					  this.systemid = res.rData.sval
					  this.initStatus()
				  }
				})
			},
			initStatus(){
				let expire = 0
				const time = Date.parse(new Date());
				try{
					expire = decrypto(this.systemid,88,16)
					const reg = /^\d+$/
					if(reg.test(expire)){
						console.log(expire,new Date(parseInt(expire)),time)
					}else{
						expire = 0
					}
				}catch(e){
					
				}
				if(expire < time){
					this.$refs.sysPopup.open()
				}else{
					this.$refs.sysPopup.close()
				}
			}
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
