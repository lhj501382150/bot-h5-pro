<script>
	import initPermission from './utils/permission'
	export default {
		onLaunch: function() {
			this.plate = uni.getSystemInfoSync().platform
			if(this.plate.indexOf('ios') < 0){
				window.addEventListener('beforeunload', this.handleBeforeUnload);
				const closeTime = uni.getStorageSync('close') || 0
				const curTime = new Date().getTime()
				if(closeTime > 0 &&  curTime - closeTime > 5 * 1000){
					uni.removeStorageSync("userinfo")
					uni.removeStorageSync("Token")
					uni.removeStorageSync("userno")
					uni.removeStorageSync("mask")
				}
			}
			initPermission()
		},
		onShow: function() {
			if(this.plate.indexOf('ios') >= 0){
				const curTime = new Date().getTime()
				const leaveTime = uni.getStorageSync('leave') || 0
				if(leaveTime > 0 && curTime -leaveTime > 30 * 60 * 1000){
					uni.removeStorageSync("userinfo")
					uni.removeStorageSync("Token")
					uni.removeStorageSync("userno")
					uni.removeStorageSync("mask")
				}
			}
			const mask = uni.getStorageSync('mask')
			if(!mask){
				uni.reLaunch({
					url:'/pages/index/index'
				})
			}
		},
		onHide: function() {
			if(this.plate.indexOf('ios') >= 0){
				uni.setStorageSync('leave',new Date().getTime())
			}
		},
		onUnload() {
			window.removeEventListener('beforeunload', this.handleBeforeUnload);
		},
		data(){
			return {
				plate : ''
			}
		},
		methods:{
			handleBeforeUnload(){
				uni.setStorageSync('close',new Date().getTime())
			}
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	*{
		padding: 0upx;
		margin: 0upx;
	}
	page{
		background-color: rgb(241,241,241);
	}
</style>
