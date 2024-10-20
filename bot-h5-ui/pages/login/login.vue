<template>
	<view class="login">
		<uni-nav-bar  title="登录" background-color="rgb(40,148,255)" color="#fff" :border="false" right-text="客服" @clickRight="goWeb"></uni-nav-bar>
		<view class="content">
			<view class="title">
				四方通宝
			</view>
			<view class="form">
				<uni-forms ref="form" :modelValue="formData" :rules="rules" >
					<uni-forms-item label="账号" name="userno">
						<uni-easyinput type="text" prefixIcon="auth" v-model="formData.userno" placeholder="请输入账号" />
					</uni-forms-item>
					<uni-forms-item label="密码" name="paypwd">
						<uni-easyinput type="password" prefixIcon="locked" v-model="formData.paypwd" placeholder="请输入密码"/>
					</uni-forms-item>
					
				</uni-forms>
				 
				<button class="btn" @click="submit">立即登录</button>
			</view>
		</view>
		<uni-popup ref="pwdPopup" :mask-click="false" background-color="#fff" borderRadius="10upx 10upx 0upx 0upx">
			<view class="pwdform">
				<view class="form-title">修改密码</view>
				<view class="form-sub-title">您的账户初次登陆或密码由后台重新设定，为了安全，请您重新设置密码</view>
				<uni-forms ref="pwdform" :modelValue="pwdformData" :rules="pwdRules" >
					<uni-forms-item label="原密码" name="oldPAYPWD">
						<uni-easyinput type="password" prefixIcon="locked" v-model="pwdformData.oldPAYPWD" placeholder="请输入原密码"/>
					</uni-forms-item>
					<uni-forms-item label="新密码" name="newPAYPWD">
						<uni-easyinput type="password" prefixIcon="locked" v-model="pwdformData.newPAYPWD" placeholder="请输入新密码"/>
					</uni-forms-item>
					<uni-forms-item label="确认密码" name="newPAYPWD2">
						<uni-easyinput type="password" prefixIcon="locked" v-model="pwdformData.newPAYPWD2" placeholder="请再次输入新密码"/>
					</uni-forms-item>
				</uni-forms>
				 
				<button class="btn" @click="submitPwd">确 定</button>
			</view>
		</uni-popup>
		<sys-status></sys-status>
	</view>
</template>

<script>
	import SysStatus from '../../components/sys-status/sys-status.vue'
	import md5 from '@/utils/md5.js'
	export default {
		components:{
			SysStatus
		},
		data() {
			return {
				formData:{
					paypwd :'',
					userno :''
				},
				rules: {
					userno: {
						rules: [
							{required: true,errorMessage: '请输入账号'}
						]
					},
					 paypwd: {
					 	rules: [
					 		{required: true,errorMessage: '请输入密码'}
					 	]
					 }
				},
				pwdformData:{
					userno :'',
					oldPAYPWD:'',
					newPAYPWD:'',
					newPAYPWD2:''
				},
				pwdRules: {
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
		},
		methods: {
			submit(){
				this.$refs.form.validate().then(res=>{
					const para = Object.assign({},this.formData)
					para.paypwd = md5(this.formData.userno + this.formData.paypwd)
					this.$http.post('/User/Login',para,(res=>{
						if(res.iCode ==0){
							uni.setStorageSync("Token",res.rData.token)
							if(res.rData.isFirst==1){
								this.$refs.pwdPopup.open()
							}else{
								let  user = res.rData
								delete user.token
								uni.setStorageSync("userinfo",JSON.stringify(user))
								uni.setStorageSync("userno",res.rData.userno)
								uni.switchTab({
									url:'/pages/home/home'
								})
							}
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
			submitPwd(){
				this.$refs.pwdform.validate().then(res=>{
					if(this.pwdformData.newPAYPWD != this.pwdformData.newPAYPWD2){
						uni.showToast({
							title:'两次密码输入不一致',
							icon:'error'
						})
						return
					}
					let para ={
						userno: this.formData.userno,
						oldPAYPWD:md5(this.formData.userno + this.pwdformData.oldPAYPWD),
						newPAYPWD:md5(this.formData.userno + this.pwdformData.newPAYPWD),
						newPAYPWD2:md5(this.formData.userno + this.pwdformData.newPAYPWD2)
					}
					this.$http.post('/User/MdiPwd',para,(res=>{
						if(res.iCode ==0){
							this.$refs.pwdPopup.close()
							 uni.showToast({
							 	title:'设置成功，请重新登录',
							 	icon:'success',
								duration:3000,
								success() {
									setTimeout(()=>{
										uni.reLaunch({
											url:'/pages/login/login'
										})
									},2000)
								}
							 })
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
			goWeb(){
				
			}
		}	 
	}
</script>

<style lang="scss" scoped>
.login{
	width: 750upx;
	height: 100vh;
	background: repeating-linear-gradient(0deg,#297ce2,#f9f2f2);
	.content{
		display: flex;
		flex-direction: column;
		align-items: center;
		.title{
			color: #297ce2;
			font-size: 36px;
			font-weight: 600;
			margin-top: 100upx;
		}
		.form{
			width: 670upx;
			margin-top: 100upx;
			::v-deep .uni-forms-item__label{
				color: #fff;
				font-size: 30upx;
				height: 100upx;
			}
			::v-deep .uni-easyinput__content{
				height: 100upx;
				font-size: 28upx;
				border-radius: 10upx;
			} 
			.btn{
				background: linear-gradient(90deg,#2894ff,#66b3ff);
				color: #fff;
				border-radius: 20upx;
				margin-top: 20upx;
			}
		}
	}
	.pwdform{
		width: 500upx;
		padding: 40upx;
		.form-title{
			font-size: 32upx;
			font-weight: bolder;
			text-align: center;
			margin-bottom: 40upx;
		}
		.form-sub-title{
			font-size: 24upx;
			text-align: center;
			margin-bottom: 40upx;
		}
		.btn{
			background: linear-gradient(90deg,#2894ff,#66b3ff);
			color: #fff;
			border-radius: 20upx;
			margin-top: 20upx;
		}
	}
}
</style>
