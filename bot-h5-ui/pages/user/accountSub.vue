<template>
	<view class="account">
		<uni-nav-bar left-icon="left"  title="代理中心" background-color="rgb(40,148,255)" color="#fff" :border="false" @clickLeft="goBack"></uni-nav-bar>
		<view class="row">
			<view class="text">当前账号： {{getStatus(curClevel)}} - {{userno}}</view>
		</view>
		<view class="tab-bar">
			<view class="tab-item"  :class="item.clevel == tabIndex ? 'active':''" v-for="(item,index) in tabs" :key="index" @click="findData(item)" v-if="item.clevel > curClevel">
				{{item.name}}
			</view>
		</view>
		<view class="row" >
			<button class="add-btn" v-if="tabIndex == 99 || tabIndex==curClevel + 1" @click="open" size="mini">新建{{getStatus(tabIndex)}}</button>
			<view class="search-status">
				<uni-data-select v-model="search.sex" placeholder="状态筛选" :localdata="sexs"  @change="changeSex" ></uni-data-select>
			</view>
		</view>
		<scroll-view scroll-y="true" @scrolltolower="scrolltolower" style="height: 80%;"
		        @refresherrefresh="getRefresherrefresh" :refresher-enabled="false" :refresher-triggered="refresherTriggered"
		        refresher-background="transparent">
			<view class="record-list">
				 <view class="record-item" v-for="(item,index) in records" :key="index">
					  <view class="user-info">
					  	<view class="left">
					  		<image src="../../static/images/user/tou-login.png" mode="scaleToFill" v-if="item.isLogin==1"></image>
					  		<image src="../../static/images/user/tou.png" mode="scaleToFill" v-else></image>
					  	</view>
					  	<view class="right">
							<view class="row-item">
								<view>ID：{{item.userno}}</view>
								<view>昵称：{{item.nickname}}</view>
							</view>
							<view class="row-item">
								<view>所属上级：{{item.parentno}}</view>
								<view v-if="item.orgtype==2">类型：<text v-if="item.parentno == userno">直属</text><text v-else>普通</text>会员</view>
								<view v-else>类型：{{getStatus(tabIndex)}}</view>
							</view>
							<view class="row-item">
								<view v-if="item.orgtype==1">现金额度：{{item.allquity}}/<text style="color:red">{{item.usequity}}</text></view>
								<view v-else>现金额度：{{item.enable}}</view>
								<view>状态：<text :class="'sex'+item.sex">{{getSexLabel(item.sex)}}</text></view>
							</view>
					  	</view>
					  </view>
					  
					  <view class="report-row" v-if="tabIndex==99">
					  	<view>本周下注金额：{{item.curBail}}</view>
					  	<view>本周盈亏：<text class="loss">{{item.curLoss}}</text></view>
					  </view>
					  <view class="report-row"  v-if="tabIndex==99">
					  	<view>上周下注金额：{{item.befBail}}</view>
					  	<view>上周盈亏：<text class="loss">{{item.befLoss}}</text></view>
					  </view>
					 <view class="btn-row">
						<view class="btn-item" @click="editBalance(item)">修改现金额度</view>
						<view class="btn-item btn-more" @click="changeMenu(item)">
							更多
							<uni-icons type="down" size="16" color="rgb(40,148,255)"></uni-icons>
							<view class="drop-menu" v-show="item.menuMore">
								<view class="drop-item" @click="openPwdPopup(item)">密码重置</view>
								<view class="drop-item" @click="openNamePopup(item)">昵称修改</view>
								<view class="drop-item" @click="openLimitPopup(item)">单项额度限制</view>
								<view class="drop-item" v-if="item.sex!=0" @click="changeStatus(item.userno,0)">启用</view>
								<view class="drop-item" v-if="item.sex!=1" @click="changeStatus(item.userno,1)">冻结</view>
								<view class="drop-item" v-if="item.sex!=2" @click="changeStatus(item.userno,2)">停用</view>
							</view>
						</view>
					 						
					 </view>
					 <view class="btn-row">
					 	<view class="btn-item" @click="showSub(item)" v-if="tabIndex <= 4">查看下级</view>
					 	<view class="btn-item" @click="showReport(item)">报表查询</view>
					 </view>
				 </view>
				 
				 <view class="load-more" v-if="loadMore">加载中...</view>
				 <view class="load-more" v-if="totalCount > 0 && records.length >= totalCount">---到底了---</view>
			</view>
			 
		</scroll-view>
		
		 <uni-popup ref="scorePopup" type="center" background-color="#fff">
			 <view class="form">
				 <view class="form-title">修改现金额度</view>
				 <view class="form-sub-title">操作账号：{{scoreForm.userId}}-{{scoreForm.userName}}</view>
				<uni-forms ref="scoreForm" :modelValue="scoreForm" :rules="scoreFormRules" >
					<uni-forms-item  name="type">
						<view class="radio-box">
							<radio-group @change="radioChange">
								<label>
									<radio value="1" :checked="scoreForm.type==1"/><text>上分</text>
								</label>
								<label>
									<radio value="2" :checked="scoreForm.type==2"/><text>下分</text>
								</label>
							</radio-group>
						</view>
					</uni-forms-item>
					<uni-forms-item label="分数" name="money">
						<uni-easyinput type="number" v-model="scoreForm.money" placeholder="请输入分数"/>
					</uni-forms-item>
				</uni-forms>
				 
				<view class="form-btn">
					<button class="cal-btn" size="mini" @click="closeScorePopup">取消</button>
					<button class="sub-btn"  size="mini" @click="submitScore">确认</button>
				</view>
			 </view>
		 </uni-popup>
		 <uni-popup ref="pwdPopup" type="center" background-color="#fff">
		 			 <view class="form">
		 				 <view class="form-title">重置密码</view>
		 				 <view class="form-sub-title">操作账号：{{pwdForm.userId}}-{{pwdForm.userName}}</view>
		 				<uni-forms ref="pwdForm" :modelValue="pwdForm" :rules="pwdFormRules" >
		 					<uni-forms-item label="新密码" name="paypwd">
		 						<uni-easyinput type="password" prefixIcon="locked" v-model="pwdForm.paypwd" placeholder="请输入新密码"/>
		 					</uni-forms-item>
		 					<uni-forms-item label="确认密码" name="paypwd2">
		 						<uni-easyinput type="password" prefixIcon="locked" v-model="pwdForm.paypwd2" placeholder="请再次输入新密码"/>
		 					</uni-forms-item>
		 				</uni-forms>
		 				 
		 				<view class="form-btn">
		 					<button class="cal-btn" size="mini" @click="closePwdPopup">取消</button>
		 					<button class="sub-btn"  size="mini" @click="submitPwd">确认</button>
		 				</view>
		 			 </view>
		 </uni-popup>
		 <uni-popup ref="namePopup" type="center" background-color="#fff">
		 			 <view class="form">
		 				 <view class="form-title">昵称修改</view>
		 				 <view class="form-sub-title">操作账号：{{nameForm.userId}}-{{nameForm.userName}}</view>
		 				<uni-forms ref="nameForm" :modelValue="nameForm" :rules="nameFormRules" >
		 					<uni-forms-item label="新昵称" name="userName">
		 						<uni-easyinput type="text" prefixIcon="person" v-model="nameForm.userName" placeholder="请输入昵称"/>
		 					</uni-forms-item>
		 				</uni-forms>
		 				 
		 				<view class="form-btn">
		 					<button class="cal-btn" size="mini" @click="closeNamePopup">取消</button>
		 					<button class="sub-btn"  size="mini" @click="submitName">确认</button>
		 				</view>
		 			 </view>
		 </uni-popup>
		 <uni-popup ref="popup" type="bottom" background-color="#fff">
			 <view class="form">
				 <view class="form-title">新建{{getStatus(tabIndex)}}</view>
			 	<uni-forms ref="form" :modelValue="formData" :rules="rules" >
			 		<uni-forms-item label="账号" name="userno">
			 			<uni-easyinput type="text" prefixIcon="auth" v-model="formData.userno" placeholder="请输入账号"/>
			 		</uni-forms-item>
			 		<uni-forms-item label="昵称" name="nickname">
			 			<uni-easyinput type="text" prefixIcon="person" v-model="formData.nickname" placeholder="请输入昵称"/>
			 		</uni-forms-item>
					<uni-forms-item label="密码" name="paypwd">
						<uni-easyinput type="password" prefixIcon="locked" v-model="formData.paypwd" placeholder="请输入密码"/>
					</uni-forms-item>
			 		
			 	</uni-forms>
			 	 
			 	<view class="form-btn">
					<button class="cal-btn" @click="close">取消</button>
					<button class="sub-btn" @click="submit">确认创建</button>
				</view>
			 </view>
		 </uni-popup>
		<uni-popup ref="limitPopup" type="center" background-color="#fff">
			 			 <view class="form">
			 				 <view class="form-title">单项额度限制</view>
			 				 <view class="form-sub-title">所属账号：{{limitForm.userno}}</view>
			 				<uni-forms ref="limitForm" :modelValue="limitForm" :rules="limitFormRules" >
			 					<uni-forms-item label="宝斗最大限额" name="maxDou">
			 						<uni-easyinput type="number" v-model="limitForm.maxDou" placeholder="请输入宝斗最大限额"/>
			 					</uni-forms-item>
								<uni-forms-item label="牛牛平投最大限额" name="maxNiuP">
									<uni-easyinput type="number" v-model="limitForm.maxNiuP" placeholder="请输入牛牛平投最大限额"/>
								</uni-forms-item>
								<uni-forms-item label="牛牛倍投最大限额" name="maxNiuB">
									<uni-easyinput type="number" v-model="limitForm.maxNiuB" placeholder="请输入牛牛倍投最大限额"/>
								</uni-forms-item>
								<uni-forms-item>
									<view style="color: red;width: 100%;text-align: center;">* 0 代表不限制</view>
								</uni-forms-item>
			 				</uni-forms>
			 				 
			 				<view class="form-btn">
			 					<button class="cal-btn" size="mini" @click="closeLimitPopup">取消</button>
			 					<button class="sub-btn"  size="mini" @click="submitLimit" :loading="editloding" :disabled="editloding">确认</button>
			 				</view>
			 			 </view>
			 </uni-popup>
		
	</view>
</template>

<script>
	import md5 from '@/utils/md5.js'
	export default {
		data() {
			return {
				editloding:false,
				formData:{
					userno :'',
					nickname:'',
					paypwd:''
				},
				rules: {
					userno: {
						rules: [
							{required: true,errorMessage: '请输入账号'}
						]
					},
					 nickname: {
					 	rules: [
					 		{required: true,errorMessage: '请输入昵称'}
					 	]
					 },
					 paypwd: {
					 	rules: [
					 		{required: true,errorMessage: '请输入密码'}
					 	]
					 },
				},
				userno:'',
				curClevel:'',
				parentno:'',
				tabs:[
					{clevel:1,name:'分公司'},
					{clevel:2,name:'股东'},
					{clevel:3,name:'总代理'},
					{clevel:4,name:'代理'},
					{clevel:99,name:'会员'}
				],
				sexs:[
					{value:"0",text:'正常'},
					{value:"1",text:'冻结'},
					{value:"2",text:'停用'}
				],
				tabIndex:0,
				records:[],
				search:{
					orgtype:'',
					userno:'',
					clevel:'',
					sex:'',
					pageIdx:0,
					pageSize:10
				},
				totalPage:1,
				totalCount:0,
				loadMore:false,
				refresherTriggered:false,
				clevelName:'',
				scoreForm:{
					type:1,
					userId:'',
					userName:'',
					money:''
				},
				scoreFormRules: {
					 money: {
					 	rules: [
					 		{required: true,errorMessage: '请输入分数'}
					 	]
					 }
					 
				},
				pwdForm:{
					userId:'',
					userName:'',
					paypwd:'',
					paypwd2:''
				},
				pwdFormRules: {
					 paypwd: {
					 	rules: [
					 		{required: true,errorMessage: '请输入密码'}
					 	]
					 },
					 paypwd2: {
					 	rules: [
					 		{required: true,errorMessage: '请输入确认密码'},
							{validateFunction:(rule,value,data,callback)=>{
								if (value != this.pwdForm.paypwd) {
									callback('两次密码输入不一致')
								}
									return true
								}
							}
					 	]
					 }
				},
				nameForm:{
					userId:'',
					userName:''
				},
				nameFormRules: {
					 userName: {
					 	rules: [
					 		{required: true,errorMessage: '请输入昵称'}
					 	]
					 }
				},
				limitForm:{
					userno: "",
					minDou: 0,
					maxDou: 0,
					minNiuP: 0,
					minNiuB: 0,
					maxNiuP: 0,
					maxNiuB: 0
				},
				limitFormRules: {
					 minDou: {
					 	rules: [
					 		{required: true,errorMessage: '请输入宝斗最小限额'}
					 	]
					 },
					 maxDou: {
					 	rules: [
					 		{required: true,errorMessage: '请输入宝斗最大限额'}
					 	]
					 },
					 minNiuP: {
					 	rules: [
					 		{required: true,errorMessage: '请输入牛牛平投最小限额'}
					 	]
					 },
					 minNiuB: {
					 	rules: [
					 		{required: true,errorMessage: '请输入牛牛倍投最小限额'}
					 	]
					 },
					 maxNiuP: {
					 	rules: [
					 		{required: true,errorMessage: '请输入牛牛平投最大限额'}
					 	]
					 },
					 maxNiuB: {
					 	rules: [
					 		{required: true,errorMessage: '请输入牛牛倍投最大限额'}
					 	]
					 }
					 
				},
			}
		},
		onLoad(option) {
			this.userno = option.userno
			this.curClevel = option.clevel
			this.parentno = option.parentno
			if(this.curClevel < 4){
				this.tabIndex = parseInt(this.curClevel) + 1
			}else{
				this.tabIndex = 99
			}
			this.loadData()
		},
		methods: {
			changeMenu(item){
				item.menuMore =!item.menuMore
			},
			changeSex(){
				this.getRefresherrefresh()
			},
			changeStatus(userno,status){
				let para = {
					userno:userno,
					status:status + ''
				}
				this.$http.post('/User/ChangeStatus',para,(res=>{
					if(res.iCode ==0){
						 uni.showToast({
						 	title:'操作成功',
						 	icon:'success',
							duration:3000
						 })
						 setTimeout(this.getRefresherrefresh,1000)
					}else{
						uni.showToast({
							title:res.sMsg,
							icon:'error'
						})
					}
				}))
			},
			showSub(item){
				let path = './accountSub?userno='+item.userno+'&orgtype='+item.orgtype+'&clevel='+ this.tabIndex+'&parentno='+item.parentno
				uni.navigateTo({
					url:path
				})
			},
			showReport(item){
				let path = './report?userno='+item.userno+'&orgtype='+item.orgtype+'&clevel='+ this.tabIndex
				uni.navigateTo({
					url:path
				})
			},
			radioChange(value){
				this.scoreForm.type= value
			},
			closeScorePopup(){
				this.$refs.scorePopup.close()
				this.scoreForm.userId = ''
				this.scoreForm.userName = ''
				this.scoreForm.type = 1
				this.scoreForm.money = ''
			},
			editBalance(item){
				this.scoreForm.userId = item.userno
				this.scoreForm.userName = item.nickname
				this.$refs.scorePopup.open()
			},
			closePwdPopup(){
				this.$refs.pwdPopup.close()
				this.pwdForm.userId = ''
				this.pwdForm.userName = ''
				this.pwdForm.paypwd = ''
				this.pwdForm.paypwd2 = ''
			},
			openPwdPopup(item){
				this.pwdForm.userId = item.userno
				this.pwdForm.userName = item.nickname
				this.$refs.pwdPopup.open()
			},
			submitPwd(){
				this.$refs.pwdForm.validate().then(res=>{
					let para = {
						userno:this.pwdForm.userId,
						newPAYPWD: md5(this.pwdForm.userId + this.pwdForm.paypwd)
					}
					 let url = '/User/ResetPwd'
					this.$http.post(url,para,(res=>{
						if(res.iCode ==0){
							 uni.showToast({
							 	title:'操作成功',
							 	icon:'success',
								duration:3000
							 })
							 this.closePwdPopup()
							 // setTimeout(this.getRefresherrefresh,1000)
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
			closeNamePopup(){
				this.$refs.namePopup.close()
				this.nameForm.userId = ''
				this.nameForm.userName = ''
			},
			openNamePopup(item){
				this.nameForm.userId = item.userno
				this.nameForm.userName = item.nickname
				this.$refs.namePopup.open()
			},
			submitName(){
				this.$refs.nameForm.validate().then(res=>{
					let para = {
						userno:this.nameForm.userId,
						nickName: this.nameForm.userName
					}
					 let url = '/User/MdiNickName'
					this.$http.post(url,para,(res=>{
						if(res.iCode ==0){
							 uni.showToast({
							 	title:'操作成功',
							 	icon:'success',
								duration:3000
							 })
							 this.closeNamePopup()
							  setTimeout(this.getRefresherrefresh,1000)
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
			closeLimitPopup(){
				this.$refs.limitPopup.close()
				this.limitForm.userno = ''
				this.limitForm.minDou = 0
				this.limitForm.maxDou = 0
				this.limitForm.minNiuP = 0
				this.limitForm.minNiuB = 0
				this.limitForm.maxNiuP = 0
				this.limitForm.maxNiuB = 0
			},
			openLimitPopup(item){
				this.limitForm.userno = item.userno
				let url = '/User/QueryUserLimit'
				this.$http.post(url,{userno:item.userno},(res=>{
					if(res.iCode ==0 && res.rData.userno != null){
						this.limitForm = res.rData
					}
					this.$refs.limitPopup.open()
				}))
				
			},
			submitLimit(){
				this.$refs.limitForm.validate().then(res=>{
					this.editloding = true
					let para = Object.assign({},this.limitForm)
					 let url = '/User/SetUserLimit'
					this.$http.post(url,para,(res=>{
						this.editloding = false
						if(res.iCode ==0){
							 uni.showToast({
							 	title:'操作成功',
							 	icon:'success',
								duration:3000
							 })
							 this.closeLimitPopup()
							  setTimeout(this.getRefresherrefresh,1000)
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
			submitScore(){
				this.$refs.scoreForm.validate().then(res=>{
					let para = {
						userId:this.scoreForm.userId,
						userName: this.scoreForm.userName
					}
					 let url = ''
					 if(this.scoreForm.type==1){
						 url = '/Account/WebInMoney'
						 para.inMoney = this.scoreForm.money
					 }else{
						 url = '/Account/WebOutMoney'
						  para.outMoney = this.scoreForm.money
					 }
					this.$http.post(url,para,(res=>{
						if(res.iCode ==0){
							 uni.showToast({
							 	title:'操作成功',
							 	icon:'success',
								duration:3000
							 })
							 this.closeScorePopup()
							 setTimeout(this.getRefresherrefresh,1000)
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
			findData(item){
				this.tabIndex = item.clevel
				this.search.pageIdx = 0
				this.totalPage = 1
				this.totalCount = 0
				this.records = []
				this.loadData()
			},
			scrolltolower() {
				if (this.records.length >= this.totalCount) return
				if(!this.loadMore){
					this.loadMore = true
					this.loadData()
				}
			},
			//下拉刷新
			getRefresherrefresh(){
				// this.refresherTriggered = true
				this.search.pageIdx = 0
				this.totalPage = 1
				this.totalCount = 0
				this.records = []
				this.loadData()
			},
			loadData(){
				this.search.userno = this.userno
				if(this.tabIndex == 99){
					this.search.orgtype = 2
					this.search.clevel = null
				}else{
					this.search.clevel = this.tabIndex
					this.search.orgtype = 1
				}
				this.$http.post("/Query/SubFirmList",this.search,res => {
					let datas = res.rData || []
					datas.forEach(item=>{
						item.menuMore = false
					})
					this.records = [...this.records,...datas]
					this.totalCount = res.iCount;
					if (this.search.pageIdx >= this.totalCount) {
						this.search.pageIdx = this.totalCount + 1;
					} else {
						this.search.pageIdx = this.search.pageIdx + this.search.pageSize
					}
					this.refresherTriggered = false
					this.loadMore = false
				})
			},
			getStatus(clevel){
				if(clevel == 99){
					if(this.curClevel < 4){
						return '直属会员'
					}else{
						return '普通会员'
					}
				}else{
					let item = this.tabs.find(item=>item.clevel==clevel) || {}
					return item.name
				}
			},
			getSexLabel(val){
				if(val){
					let sex = this.sexs.find(temp=>val==temp.value) || {}
					return sex.text
				}
			},
			open() {
				this.$refs.popup.open()
			},
			close() {
				this.$refs.popup.close()
			},
			submit(){
				this.$refs.form.validate().then(res=>{
					const para = Object.assign({},this.formData)
					para.tjno = this.userno
					if(this.tabIndex == 99){
						para.orgtype = 2
					}else{
						para.orgtype = 1
					}
					para.paypwd = md5(this.formData.userno + this.formData.paypwd)
					this.$http.post('/User/AddAccount',para,(res=>{
						if(res.iCode ==0){
							this.formData.userno = ''
							this.formData.nickname = ''
							this.formData.paypwd = ''
							 uni.showToast({
							 	title:'添加成功',
							 	icon:'success',
								duration:3000
							 })
							 this.getRefresherrefresh()
							 this.close()
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
				const pages = getCurrentPages()
				if(pages.length > 1){
					uni.navigateBack({
						delta:1
					})
				}else{
					history.back()
				}
			}
		}
	}
</script>

<style scoped lang="scss">
.account{
	width: 750upx;
	height: 100vh;
	background-color: #fff;
	.row{
		padding-left: 40upx;
		padding-right: 40upx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		.add-btn{
			width: 250upx;
			background-color: rgb(40,148,255);
			color: #fff;
			margin-top: 20upx;
		}
		.text{
			margin-top: 40upx;
		}
		.search-status{
			width:300upx;
			margin-top: 20upx;
		}
	}
	.tab-bar{
		display: flex;
		width: 670upx;
		padding-left: 40upx;
		padding-right: 40upx;
		.tab-item{
			width:150upx;
			height:80upx;
			line-height: 80upx;
			text-align: center;
			border-bottom:5upx solid #eeeeee;
		}
		.active{
			border-bottom: 5upx solid rgb(40,148,255);
		}
	}
	.record-list{
		background-color: #fff;
		.record-item{
			padding:20upx 20upx 10upx 20upx;
			// border-bottom:5upx solid #eeeeee;
			.user-info{
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom:10upx;
				.link{
					color: rgb(40,148,255);
					cursor: pointer;
				}
				.left{
					width:110upx;
					image{
						width: 100upx;
						height: 100upx;
					}
				}
				.right{
					width: 600upx;
					.row-item{
						display: flex;
						justify-content: space-between;
						align-items: center;
						margin-bottom: 10upx;
						padding-right: 20upx;
						.sex0{
							color: green;
						}
						.sex1{
							color: orange;
						}
						.sex2{
							color: red;
						}
					}
				}
			}
			.report-row{
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-top:10upx;
				margin-bottom:10upx;
				font-size:26upx;
				padding: 5upx 20upx 0upx 20upx;
				.loss{
					display: inline-block;
					min-width: 100upx;
				}
			}
			.btn-row{
				width:100%;
				display: flex;
				justify-content: space-between;
				align-items: center;
				.btn-item{
					width: 50%;
					max-width: 320upx;
					text-align: center;
					border: 1px solid #eeeeee;
					padding:15upx 20upx;
					color:rgb(40,148,255);
					cursor: pointer;
				}
				.btn-more{
					position: relative;
				}
				.drop-menu{
					position: absolute;
					top: 60upx;
					right:10upx;
					border: #bfbfbf 1px solid;
					width: 240upx;
					background-color: #ffffff;
					box-shadow: 0px 0px 3px 1px #f2f2f2;
					z-index: 999;
					.drop-item{
						line-height: 60upx;
					}
				}
			}
		}
		.load-more{
			width: 100%;
			line-height: 80upx;
			color: #bfbfbf;
			text-align: center;
		}
	}
	.form{
		width:620upx;
		 padding: 40upx;
		 .form-title{
			 font-size: 16px;
			 font-weight:600;
			 text-align: center;
			 width:100%;
			 margin-bottom:10px;
		 }
		 .form-sub-title{
			 font-size: 14px;
			 text-align: center;
			 width:100%;
			 margin-bottom:10px;
		 }
		 .radio-box{
			 text-align: center;
			 width:100%;
			 margin-top: 20upx;
		 }
		 .form-btn{
			width: 100%;
			display: flex;
			justify-content: space-around;
		 }
		 .sub-btn{
			 width: 35%;
			 background-color: rgb(40,148,255);
			 color: #fff;
			 border-radius: 20upx;
		 }
		 .cal-btn{
			 width: 35%;
			 background-color: #fff;
			 color: #000;
			 border-radius: 20upx;
		 }
	}
}
</style>
