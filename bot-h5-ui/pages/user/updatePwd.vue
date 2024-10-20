<template>
	<view class="update-pwd">
		<uni-nav-bar left-icon="left"  title="修改密码" background-color="rgb(40,148,255)" color="#fff" :border="false" @clickLeft="goBack"></uni-nav-bar>
		 <view class="form">
		 	<uni-forms ref="form" :modelValue="formData" :rules="rules" >
		 		<uni-forms-item label="账号" name="userno">
		 			<uni-easyinput type="text" prefixIcon="auth" v-model="formData.userno" placeholder="请输入账号" :disabled="true"/>
		 		</uni-forms-item>
		 		<uni-forms-item label="原密码" name="oldPAYPWD">
		 			<uni-easyinput type="password" prefixIcon="locked" v-model="formData.oldPAYPWD" placeholder="请输入原密码"/>
		 		</uni-forms-item>
				<uni-forms-item label="新密码" name="newPAYPWD">
					<uni-easyinput type="password" prefixIcon="locked" v-model="formData.newPAYPWD" placeholder="请输入新密码"/>
				</uni-forms-item>
				<uni-forms-item label="确认密码" name="newPAYPWD2">
					<uni-easyinput type="password" prefixIcon="locked" v-model="formData.newPAYPWD2" placeholder="请再次输入新密码"/>
				</uni-forms-item>
		 		
		 	</uni-forms>
		 	 
		 	<button class="btn" @click="submit">确 定</button>
		 </view>
	</view>
</template>

<script>
	import md5 from '@/utils/md5.js'
	export default {
		data() {
			return {
				formData:{
					userno :'',
					oldPAYPWD:'',
					newPAYPWD:'',
					newPAYPWD2:''
				},
				rules: {
					userno: {
						rules: [
							{required: true,errorMessage: '请输入账号'}
						]
					},
					 oldPAYPWD: {
					 	rules: [
					 		{required: true,errorMessage: '请输入原密码'}
					 	]
					 },
					 newPAYPWD: {
					 	rules: [
					 		{required: true,errorMessage: '请输入新密码'}
					 	]
					 },
					 newPAYPWD2: {
					 	rules: [
					 		{required: true,errorMessage: '请输入新密码'}
					 	]
					 },
				}
			}
		},
		onLoad() {
			 this.formData.userno = uni.getStorageSync('userno')
		},
		methods: {
			submit(){
				this.$refs.form.validate().then(res=>{
					if(this.formData.newPAYPWD != this.formData.newPAYPWD2){
						uni.showToast({
							title:'两次密码输入不一致',
							icon:'error'
						})
						return
					}
					let para ={
						userno: this.formData.userno,
						oldPAYPWD:md5(this.formData.userno + this.formData.oldPAYPWD),
						newPAYPWD:md5(this.formData.userno + this.formData.newPAYPWD),
						newPAYPWD2:md5(this.formData.userno + this.formData.newPAYPWD2)
					}
					this.$http.post('/User/MdiPwd',para,(res=>{
						if(res.iCode ==0){
							 uni.showToast({
							 	title:'修改成功，请重新登录',
							 	icon:'success',
								duration:3000
							 })
							 setTimeout(()=>{
								 uni.navigateTo({
								 	url:'/pages/login/login'
								 })
							 },2000)
						}else{
							uni.showToast({
								title:res.sMsg,
								icon:'error'
							})
						}
					}))
				}).catch(err =>{
					console.log(err);
				})
			},
			goBack(){
				uni.switchTab({
					url:'/pages/user/user'
				})
			}
		}
	}
</script>

<style scoped lang="scss">
.update-pwd{
	width: 750upx;
	height: 100vh;
	background-color: #fff;
	 .form{
		 padding: 40upx;
		 .btn{
			 background-color: rgb(40,148,255);
			 color: #fff;
		 }
	 }
}
</style>
