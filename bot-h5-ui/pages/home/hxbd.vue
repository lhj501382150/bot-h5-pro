<template>
	<view class="qmbd-game">
		<uni-nav-bar  title="四方通宝" background-color="rgb(250,81,81)" color="#fff" left-icon="back" :border="false" right-text="规则" @clickLeft="goBack" @clickRight="openRule"></uni-nav-bar>
		<view class="tab-bar">
			<view class="tab-item" :class="tabIndex==0 ? 'active':''">{{modeName}}</view>
			<view class="tab-item" :class="tabIndex==1 ? 'active':''" @click="goRecord">下注记录</view>
		</view>
		<view class="row">
			<view class="draw-time red">{{result.ISSUE-1}}期</view>
			<view class="draw-num">
				<view class="tips">{{result.RESULT}}</view>
				<view class="tips">{{getStatus(result.RESULT)}}</view>
			</view>
		</view>
		<view class="row">
			<view class="num-item" v-for="(item,index) in result.data" :key="index" :class="'color'+item">
				{{item}}
			</view>
		</view>
		<view class="row">
			<view class="draw-time red">{{result.ISSUE}}期</view>
			<view class="draw-time">封盘：<text style="color: red;">{{formatTime(fptime)}}</text></view>
			<view class="draw-time">开奖：<text style="color: blue;">{{formatTime(kjtime)}}</text></view>
		</view>
		<view class="row qing">
			<view class="draw-money">现金额度：{{user.enable}}</view>
			<view class="draw-money">未结金额：<text style="color: blue;">{{user.freeze}}</text></view>
			<view class="draw-money">今日输赢：<text style="color: red;">{{user.curLoss}}</text></view>
		</view>
		<view class="game-box">
			<view class="g-b-1">
				<view class="game-box-1" :class="items[0].check ? 'active':''" @click="chooseItem(items[0])">
					<view>{{items[0].name}} </view>
					<view class="rate">{{items[0].rate}}</view>
				</view>
				<view class="game-box-2" :class="items[1].check ? 'active':''" @click="chooseItem(items[1])">
					<view>{{items[1].name}} </view>
					<view class="rate">{{items[1].rate}} / {{items[1].rate1}}</view>
				</view>
				<view class="game-box-2" :class="items[2].check ? 'active':''" @click="chooseItem(items[2])">
					<view>{{items[2].name}} </view>
					<view class="rate">{{items[2].rate}} / {{items[1].rate1}}</view>
				</view>
				<view class="game-box-1" :class="items[3].check ? 'active':''" @click="chooseItem(items[3])">
					<view>{{items[3].name}} </view>
					<view class="rate">{{items[3].rate}}</view>
				</view>
			</view>
			<view class="g-b-2">
				<view class="game-box-3" :class="items[4].check ? 'active':''" @click="chooseItem(items[4])">
					<view>{{items[4].name}}</view>
					<view class="rate">{{items[4].rate}} / {{items[4].rate1}}</view>
				</view>
				<view class="game-box-4">
					<view class="game-box-4-1" :class="items[5].check ? 'active':''" @click="chooseItem(items[5])">
						<view class="text">
							<view>{{items[5].name}}</view>
							<view class="rate">{{items[5].rate}}  /{{items[5].rate1}}</view>
						</view>
					</view>
					<view class="game-box-4-2" :class="items[6].check ? 'active':''" @click="chooseItem(items[6])">
						<view class="text">
							<view>{{items[6].name}}</view>
							<view class="rate">{{items[6].rate}}  / {{items[6].rate1}}</view>
						</view>
					</view>
				</view>
				<view class="game-box-5">
					<view class="game-box-5-1" :class="items[7].check ? 'active':''" @click="chooseItem(items[7])">
						<view class="text">
							<view>{{items[7].name}}</view>
							<view class="rate">{{items[7].rate}}</view>
						</view>
					</view>
					<view class="game-box-5-2" :class="items[8].check ? 'active':''" @click="chooseItem(items[8])">
						<view class="text">
							<view>{{items[8].name}}</view>
							<view class="rate">{{items[8].rate}} / {{items[8].rate1}}</view>
						</view>
					</view>
				</view>
				<view class="game-box-3" :class="items[9].check ? 'active':''" @click="chooseItem(items[9])">
					<view>{{items[9].name}}</view>
					<view class="rate">{{items[9].rate}} / {{items[9].rate1}}</view>
				</view>
			</view>
			<view class="g-b-2">
				<view class="game-box-3" :class="items[10].check ? 'active':''" @click="chooseItem(items[10])">
					<view>{{items[10].name}}</view>
					<view class="rate">{{items[10].rate}} / {{items[10].rate1}}</view>
				</view>
				<view class="game-box-5">
					<view class="game-box-5-1" :class="items[11].check ? 'active':''" @click="chooseItem(items[11])">
						<view class="text">
							<view>{{items[11].name}}</view>
							<view class="rate">{{items[11].rate}}</view>
						</view>
					</view>
					<view class="game-box-5-2" :class="items[12].check ? 'active':''" @click="chooseItem(items[12])">
						<view class="text">
							<view>{{items[12].name}}</view>
							<view class="rate">{{items[12].rate}} / {{items[12].rate1}}</view>
						</view>
					</view>
				</view>
				<view class="game-box-4">
					<view class="game-box-4-1" :class="items[13].check ? 'active':''" @click="chooseItem(items[13])">
						<view class="text">
							<view>{{items[13].name}}</view>
							<view class="rate">{{items[13].rate}}</view>
						</view>
					</view>
					<view class="game-box-4-2" :class="items[14].check ? 'active':''" @click="chooseItem(items[14])">
						<view class="text">
							<view>{{items[14].name}}</view>
							<view class="rate">{{items[14].rate}}</view>
						</view>
					</view>
				</view>
				<view class="game-box-3" :class="items[15].check ? 'active':''" @click="chooseItem(items[15])">
					<view>{{items[15].name}}</view>
					<view class="rate">{{items[15].rate}} / {{items[15].rate1}}</view>
				</view>
			</view>
			<view class="g-b-1">
				<view class="game-box-1" :class="items[16].check ? 'active':''" @click="chooseItem(items[16])">
					<view>{{items[16].name}}</view>
					<view class="rate">{{items[16].rate}}</view>
				</view>
				<view class="game-box-2" :class="items[17].check ? 'active':''" @click="chooseItem(items[17])">
					<view>{{items[17].name}}</view>
					<view class="rate">{{items[17].rate}} / {{items[17].rate1}}</view>
				</view>
				<view class="game-box-2" :class="items[18].check ? 'active':''" @click="chooseItem(items[18])">
					<view>{{items[18].name}}</view>
					<view class="rate">{{items[18].rate}} / {{items[18].rate1}}</view>
				</view>
				<view class="game-box-1" :class="items[19].check ? 'active':''" @click="chooseItem(items[19])">
					<view>{{items[19].name}}</view>
					<view class="rate">{{items[19].rate}}</view>
				</view>
			</view>
			<view class="g-b-6">
				<view class="g-b-6-v">
					<view class="g-b-6-1" :class="items[20].check ? 'active':''" @click="chooseItem(items[20])">
						<view class="text">
							<view>{{items[20].name}}</view>
							<view class="rate">{{items[20].rate}}</view>
						</view>
					</view>
					<view class="g-b-6-2" :class="items[21].check ? 'active':''" @click="chooseItem(items[21])">
						<view class="text">
							<view>{{items[21].name}}</view>
							<view class="rate">{{items[21].rate}}</view>
						</view>
					</view>
					<view class="g-b-6-3" :class="items[22].check ? 'active':''" @click="chooseItem(items[22])">
						<view class="text">
							<view>{{items[22].name}}</view>
							<view class="rate">{{items[22].rate}}</view>
						</view>
					</view>
					<view class="g-b-6-4" :class="items[23].check ? 'active':''" @click="chooseItem(items[23])">
						<view class="text">
							<view>{{items[23].name}}</view>
							<view class="rate">{{items[23].rate}}</view>
						</view>
					</view>
				</view>
			</view>
			<view class="g-b-7" :class="'route'+result.RESULT"></view>
		</view>
		<view class="form">
			<uni-forms ref="form" :modelValue="formData" :rules="rules">
				<uni-forms-item  name="money">
					<uni-easyinput type="number" v-model="formData.money" placeholder="请输入投注金额" />
				</uni-forms-item>
			</uni-forms>
			<button :class="isStop?'btn1':'btn'" @click="submit">{{isStop?'封盘中':'确认下注'}}</button>
		</view>
		<view class="result">
			<view class="result-item" :class="'color'+item.no" v-for="(item,index) in results" :key="index">
				{{item.status}}
			</view>
		</view>
		
		<uni-popup ref="errPopup" type="dialog">
			<uni-popup-dialog mode="base" :duration="2000" :before-close="true"  @close="refresh"  @confirm="refresh">
			{{errMsg}}
			</uni-popup-dialog>
		</uni-popup>
		<uni-popup ref="popup" type="dialog">
			<uni-popup-dialog mode="base" :duration="2000" :before-close="true" @close="close" @confirm="confirm">
			共选择了{{formData.selected.length}}注，共计{{formData.money * formData.selected.length}}元
			</uni-popup-dialog>
		</uni-popup>
		<uni-popup ref="rulePopup" :mask-click="false" background-color="#fff" borderRadius="10upx 10upx 0upx 0upx">
			<view class="rule-content">
				<view>宝斗规则</view>
				<view>玩法：1入、2龙、3出、4虎</view>
				<view>串、角1:1/同、念1:2/古1:3</view>
				<view>念角下注要分开（没有明宝）</view>
				<!-- <view>中奖平台抽水4%</view> -->
				<view>开奖结果以1-2-3-4的先后排名为准，最靠近第一名的为最终开奖结果。</view>
				<view>例如：7-6-10-9-8-2-5-4-3-1</view>
				<view>开奖结果为（2龙）</view>
				
				<button @click="closeRule" class="pop-btn">关闭</button>
			</view>
			
		</uni-popup>
		<NoticeDialog :isShow="showNotice"></NoticeDialog>
	</view>
</template>

<script>
	import { getSecond,getCurTime } from '../../utils/util'
	import {botId, webSocketUrl} from '@/static/config/config.js'
	import NoticeDialog from '@/components/notice-dialog.vue'
	import md5 from '@/utils/md5.js'
	export default {
		components:{
			NoticeDialog
		},
		data() {
			return {
				user:{},
				tabIndex:0,
				link:{
					title:'澳洲10在线直播',
					src:'https://m.228168d.com/html/aozxy10/index.html'
				},
				result:{},
				fptime:0,
				kjtime:0,
				items:[
					{index:'1',name:'虎入角',rate:1.96,rate1:0,check:false},
					{index:'2',name:'入正念虎',rate:2.92,rate1:1,check:false},
					{index:'3',name:'入正念龙',rate:2.92,rate1:1,check:false},
					{index:'4',name:'龙入角',rate:1.96,rate1:0,check:false},
					{index:'5',name:'虎正念入',rate:2.92,rate1:1,check:false},
					{index:'6',name:'虎同',rate:2.92,rate1:1,check:false},
					{index:'7',name:'入同',rate:2.92,rate1:1,check:false},
					{index:'8',name:'入串',rate:1.96,rate1:0,check:false},
					{index:'9',name:'龙同',rate:2.92,rate1:1,check:false},
					{index:'10',name:'龙正念入',rate:2.92,rate1:1,check:false},
					{index:'11',name:'虎正念出',rate:2.92,rate1:1,check:false},
					{index:'12',name:'虎串',rate:1.96,rate1:0,check:false},
					{index:'13',name:'出同',rate:2.92,rate1:1,check:false},
					{index:'14',name:'出串',rate:1.96,rate1:0,check:false},
					{index:'15',name:'龙串',rate:1.96,rate1:0,check:false},
					{index:'16',name:'龙正念出',rate:2.92,rate1:1,check:false},
					{index:'17',name:'虎出角',rate:1.96,rate1:0,check:false},
					{index:'18',name:'出正念虎',rate:2.92,rate1:1,check:false},
					{index:'19',name:'出正念龙',rate:2.92,rate1:1,check:false},
					{index:'20',name:'龙出角',rate:1.96,rate1:0,check:false},
					{index:'21',name:'入古',rate:3.88,rate1:0,check:false},
					{index:'22',name:'龙古',rate:3.88,rate1:0,check:false},
					{index:'23',name:'出古',rate:3.88,rate1:0,check:false},
					{index:'24',name:'虎古',rate:3.88,rate1:0,check:false}
				],
				formData:{
					money:'',
					selected:[]
				},
				rules: {
					 money: {
					 	rules: [
					 		{required: true,errorMessage: '请输入人下注金额'}
					 	]
					 }
				},
				results:[],
				isStop:false,
				socketTask: null,
				// 确保websocket是打开状态
				is_open_socket: false,
				leftTime:0,
				timer:'',
				statusList:[
					{val:1,name:'入'},
					{val:2,name:'龙'},
					{val:3,name:'出'},
					{val:4,name:'虎'}
				],
				orgtype:'',
				errMsg:'',
				curStatus:false,
				showNotice:false,
				tempShowNotice:false,
				modeName:'',
				mode:'',
				modeTime:5
			}
		},
		onLoad() {
			this.modeName = uni.getStorageSync('xz_name')
			this.mode = uni.getStorageSync('xz_mode')
			this.modeTime = uni.getStorageSync('xz_time')
			 
			this.getShowNoticePara()
			this.connectSocketInit()
			let user = JSON.parse(uni.getStorageSync('userinfo'))
			this.orgtype = user.orgtype
		},
		beforeDestroy() {
			this.closeSocket()
		},
		methods: {
			async getShowNoticePara(){
				let para = {
					sysid:11
				}
				try{
					let res = await this.$http.post('/Query/SysPara',para)
					res = res.data
					if(res.iCode==0){
						this.tempShowNotice =  res.rData.sval =='Y'
					}
				}catch(e){
					console.error(e)
					this.tempShowNotice = false
				}
			},
			loadData(){
				this.results = []
				let para = {
					pageIdx:0,
					pageSize:50,
					mode:this.mode,
					userno :uni.getStorageSync('userno')
				}
				this.$http.post("/Query/ReustList",para,res => {
					let datas = res.rData || []
					datas.forEach(item=>{
						let temp ={
							no:item.bNo,
							status: this.getStatus(item.bNo)
						}
						this.results.push(temp)
					})
				})
			},
			getUserBalance(){
				let userno = uni.getStorageSync('userno')
				let para = {
					userNo : userno
				}
				this.$http.post('/Account/GetBalance',para,res=>{
					 this.user = res.rData || {}
				})
			},
			// 进入这个页面的时候创建websocket连接【整个页面随时使用】
			connectSocketInit() {
				let userno = uni.getStorageSync('userno')
				if(!userno){
					uni.navigateTo({
						url:'/pages/login/login'
					})
				}
				let pwd = md5(userno+userno)
				let url = webSocketUrl + 'bd'+this.modeTime+'/' + userno+"/" + pwd;
				this.socketTask = uni.connectSocket({
					url: url ,
					success(data) {
						console.log("websocket连接成功");
					},
				});
				// 消息的发送和接收必须在正常连接打开中,才能发送或接收【否则会失败】
				this.socketTask.onOpen((res) => {
					console.log("WebSocket连接正常打开中...！");
					this.is_open_socket = true;
					// 注：只有连接正常打开中 ，才能正常成功发送消息
					this.socketTask.send({
						data: "uni-app发送一条消息",
						async success() {
							// console.log("消息发送成功");
						},
					});
					// 注：只有连接正常打开中 ，才能正常收到消息
					this.socketTask.onMessage((res) => {
						console.log("收到服务器内容：" + res.data);
						try{
							const data = JSON.parse(res.data);
							if(data.status==1){
								clearTimeout(this.timer)
								this.getCurStatus()
								// console.log(this.curStatus)
								if(this.curStatus){
									this.isStop = false
								}else{
									this.isStop = true
								}
								this.showNotice = this.tempShowNotice
								this.result = JSON.parse(data.data)
								
								this.result.data = this.result.CODE.split(',') || []
								this.leftTime = getSecond(this.result.TIME) + 15
								this.fillTime()
								this.getUserBalance()
								this.loadData()
							}else if(data.status == 4){
								this.isStop = true
								this.fptime = 0
								if(this.modeTime == 5){
									this.kjtime = 30
								}else if(this.modeTime == 3){
									this.kjtime = 20
								}else{
									this.kjtime = 15
								}
								
								this.getShowNoticePara()
							}
						}catch(e){
							console.log('消息处理异常：' + e)
						}
						
					});
				})
				// 这里仅是事件监听【如果socket关闭了会执行】
				this.socketTask.onClose(() => {
					console.log("已经被关闭了")
				})
			},
			getCurStatus(){
				let curTime = parseInt(getCurTime())
				if(curTime >=55800 && curTime <= 65900){
					this.curStatus = false
				}else{
					this.curStatus = true
				}
			},
			startCount(){
				if(this.fptime > 0){
					this.fptime--
				}else{
					this.isStop= true
				}
				if(this.kjtime > 0){
					this.kjtime--
				}
				this.timer = setTimeout(this.startCount,1000)
			},
			fillTime(){
				let time = 30
				if(this.modeTime == 3){
					time = 20
				}else if (this.modeTime == 1){
					time = 15
				}
				this.fptime = this.leftTime - time < 0 ? 0 :this.leftTime - time
				this.kjtime = this.leftTime
				this.startCount()
			},
			formatTime(time){
				if(time > 60){
					const min = parseInt(time / 60) 
					let sec = time % 60 
					if(sec < 10) sec = '0' + sec
					return min +  ':' + sec
				}else{
					if(time < 10){
						return '00:0' + time
					}else{
						return '00:' + time
					}
					
				}
			},
			// 关闭websocket【离开这个页面的时候执行关闭】
			closeSocket() {
				this.socketTask.close({
					success(res) {
						this.is_open_socket = false;
						console.log("关闭成功", res)
					},
					fail(err) {
						console.log("关闭失败", err)
					}
				})
			},
			openRule(){
				this.$refs.rulePopup.open()
			},
			closeRule(){
				this.$refs.rulePopup.close()
			},
			close(){
				this.$refs.popup.close()
			},
			refresh(){
				this.$refs.errPopup.close()
				window.location.reload()
			},
			confirm() {
				this.close()
				let orders = this.formData.selected.map(item=>{
					let para = {
						integRal:this.formData.money,
						ruId:item.name
					}
					return para
				})
				let para = {
					botId:botId,
					dataId:this.result.ID,
					mode:this.mode,
					userId:uni.getStorageSync('userno'),
					userName:uni.getStorageSync("nickname"),
					orders:orders
				}
				
				this.$http.post('/Order/Order',para,(res=>{
					if(res.iCode ==0){
						this.formData.money = ''
						this.formData.selected = []
						this.items.forEach(item=> item.check=false)
						this.getUserBalance()
						uni.showToast({
							title:'下注成功',
							icon:'success'
						})
					}else if(res.iCode == -100){
						 this.errMsg = res.sMsg
						 this.$refs.errPopup.open()
					}
				}))
				
			},
			submit(){
				if(this.isStop) return;
				if(this.orgtype==1){
					uni.showToast({
						title:'代理不能下注',
						icon:'error',
						duration:2000
					})
					return
				}
				if(this.formData.money <= 0){
					uni.showToast({
						title:'输入金额不正确',
						icon:'error',
						duration:2000
					})
					return
				}
				this.$refs.form.validate().then(res=>{
					let arr = this.items.filter(item=>item.check==true) || []
					if(arr.length == 0){
						uni.showToast({
							title:'请选择下注类型',
							icon:'error',
							duration:3000
						})
						return
					}
					this.formData.selected = arr
					this.$refs.popup.open()
				}).catch(err =>{
					console.log( err);
				})
			},
			getStatus(status){
				const item = this.statusList.find(item=> item.val==status) || {}
				return item.name
			},
			chooseItem(item){
				item.check = !item.check
			},
			goRecord(){
				uni.setStorageSync('record_mode',this.mode)
				uni.navigateTo({
					url:'/pages/user/order'
				})
			},
			goUrl(){
				window.open(this.link.src,'_blank')
			},
			goBack(){
				uni.switchTab({
					url:'/pages/home/home'
				})
			}
		}
	}
</script>

<style scoped lang="scss">
.qmbd-game{
	width: 750upx;
	min-height: 100vh;
	background-color: #fff;
	.tab-bar{
		width: 100%;
		height: 100upx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		.tab-item{
			width: 50%;
			height: 80upx;
			line-height: 80upx;
			text-align: center;
			border-bottom: 6upx solid #e2e2e2;
		}
		.active{
			color:rgb(250,81,81);
			border-bottom: 6upx solid rgb(250,81,81);
		}
	}
	.qing{
		background-color: #d7f0ff;
	}
	.row{
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 710upx;
		height: 80upx;
		padding: 20upx;
		border-bottom: 2upx solid #e2e2e2;
		margin:0 auto;
		.draw-time{
			width: 33%;
			text-align: center;
		}
		.draw-num{
			width: 27%;
			display: flex;
			align-items: center;
			justify-content: flex-end;
			.tips{
				width: 40upx;
				height: 40upx;
				line-height: 40upx;
				border-radius: 50%;
				background-color: rgb(250,81,81);
				color: #fff;
				text-align: center;
				font-size: 26upx;
				margin-right: 20upx;
			}
		}
		.link-btn{
			width: 250upx;
			height: 60upx;
			line-height: 60upx;
			background-color: rgb(250,41,41);
			color: #fff;
			margin-right: 20upx;
			padding-left: 30upx;
			padding-right: 30upx;
			border-radius: 20upx;
		}
		.red{
			color: rgb(250,81,81);
		}
		.num-item{
			width: 59upx;
			height: 59upx;
			margin-left: 10upx;
			margin-right: 6upx;
			border-radius: 50%;
			display: flex;
			justify-content: center;
			align-items: center;
			color: #fff;
		}
		.color01{
			background-color:#FFD700;
		}
		.color02{
			background-color:#00BFFF;
		}
		.color03{
			background-color:#8B4513;
		}
		.color04{
			background-color:#FFA500;
		}
		.color05{
			background-color:#48D1CC;
		}
		.color06{
			background-color:#7B68EE;
		}
		.color07{
			background-color:#778899;
		}
		.color08{
			background-color:#DC143C;
		}
		.color09{
			background-color:#FA8072;
		}
		.color10{
			background-color:#32CD32;
		}
	}
	.game-box{
		width: 724upx;
		height: 724upx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		position: relative;
		background-color: #eee;
		margin:15upx auto;
		.rate{
			font-size: 24upx;
		}
		.g-b-1{
			display: flex;
			justify-content: space-between;
			.game-box-1{
				background-color: #1785ed;
				width: 124upx;
				height: 124upx;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
			}
			.game-box-2{
				background-color: #07be5e;
				width: 234upx;
				height: 124upx;
				display: flex;
				align-items: center;
				flex-direction: column;
				justify-content: center;
			}
		}
		.g-b-2{
			display: flex;
			justify-content: space-between;
			.game-box-3{
				background-color: #07be5e;
				width: 124upx;
				height: 234upx;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
			}
			.game-box-4{
				height: 234upx;
				width: 234upx;
				display: flex;
				position: relative;
				overflow: hidden;
				.game-box-4-1{
					overflow: hidden;
					position: absolute;
					width: 150%;
					height: 100%;
					background-color: #f35458;
					transform: rotate(45deg) translate(-10%, 68.5%);
					display: flex;
					justify-content: center;
					align-items: center;
					.text{
						transform: rotate(-45deg);
						margin-top: -122upx;
						margin-left: -10upx;
					}
				}
				.game-box-4-2{
					overflow: hidden;
					position: absolute;
					width: 150%;
					height: 100%;
					background-color: #f35458;
					-webkit-transform: rotate(45deg) translate(-10%, -33%);
					transform: rotate(45deg) translate(-10%, -33%);
					display: flex;
					justify-content: center;
					align-items: center;
					.text{
						transform: rotate(-45deg);
						margin-top: 100upx;
						margin-left: -44upx;
					}
				}
			}
			.game-box-5{
				height: 234upx;
				width: 234upx;
				display: flex;
				position: relative;
				overflow: hidden;
				.game-box-5-1{
					overflow: hidden;
					position: absolute;
					width: 150%;
					height: 100%;
					background-color: #f35458;
					-webkit-transform: rotate(135deg) translate(10%, 68.5%);
					transform: rotate(135deg) translate(10%, 68.5%);
					display: flex;
					justify-content: center;
					align-items: center;
					.text{
						transform: rotate(-135deg);
						margin-top: -46px;
					}
				}
				.game-box-5-2{
					overflow: hidden;
					position: absolute;
					width: 150%;
					height: 100%;
					background-color: #f35458;
					-webkit-transform: rotate(135deg) translate(10%, -33%);
					transform: rotate(135deg) translate(10%, -33%);
					display: flex;
					justify-content: center;
					align-items: center;
					.text{
						transform: rotate(-135deg);
						margin-left: 0upx;
						margin-top: 100upx;
					}
					
				}
			}
		}
		.g-b-6{
			position: absolute;
			background-color: #eee;
			width: 248upx;
			height: 248upx;
			margin-left: 238upx;
			margin-top: 238upx;
			.g-b-6-v{
				position: relative;
				width: 248upx;
				height: 248upx;
				overflow: hidden;
				.g-b-6-1{
					overflow: hidden;
					position: absolute;
					width: 150%;
					height: 100%;
					background-color: #f99d3d;
					-webkit-transform: rotate(135deg) translate(-39%, 68%);
					transform: rotate(135deg) translate(-39%, 68%);
					display: flex;
					justify-content: center;
					align-items: center;
					.text{
						transform: rotate(-135deg);
						margin: -280upx -170upx -114upx 82upx;
					}
				}
				.g-b-6-2{
					overflow: hidden;
					position: absolute;
					width: 150%;
					height: 100%;
					background-color: #f99d3d;
					-webkit-transform: rotate(135deg) translate(-39%, -33%);
					transform: rotate(135deg) translate(-39%, -33%);
					display: flex;
					justify-content: center;
					align-items: center;
					.text{
						transform: rotate(-135deg);
						margin: 144upx -270upx 0upx 0upx;
					}
				}
				.g-b-6-3{
					overflow: hidden;
					position: absolute;
					width: 150%;
					height: 100%;
					background-color: #f99d3d;
					-webkit-transform: rotate(-135deg) translate(-38.5%, -67.5%);
					transform: rotate(-135deg) translate(-38.5%, -67.5%);
					display: flex;
					justify-content: center;
					align-items: center;
					.text{
						transform: rotate(135deg);
						margin: 150upx -260upx 0upx 0upx;
					}
				}
				.g-b-6-4{
					overflow: hidden;
					position: absolute;
					width: 150%;
					height: 100%;
					background-color: #f99d3d;
					-webkit-transform: rotate(-135deg) translate(62%, -67.5%);
					transform: rotate(-135deg) translate(62%, -67.5%);
					display: flex;
					justify-content: center;
					align-items: center;
					.text{
						transform: rotate(135deg);
						margin: 104upx 248upx -30upx 0upx;
					}
				}
			}
		}
		.g-b-7{
			position: absolute;
			width: 82upx;
			height: 82upx;
			background-color: #fff;
			margin-left: 320upx;
			margin-top: 320upx;
			display: flex;
			justify-content: space-between;
			background-size: 100%;
			background-image: url('../../static/images/home/qp.jpg');
		}
		.route1{
			transform: rotate(180deg);
		}
		.route2{
			transform: rotate(270deg);
		}
		.route3{
			transform: rotate(0deg);
		}
		.route4{
			transform: rotate(90deg);
		}
		.active{
			background-color: #fff!important;
		}
	}
	.form{
		width: 670upx;
		border-top: 10upx solid #e2e2e2;
		padding: 40upx;
		margin: 0 auto;
		::v-deep .uni-easyinput__content{
			height: 100upx;
			font-size: 30upx;
			border: 4upx solid #f35458!important;
		}
		.btn{
			background-color: #f35458;
			color: #fff;
		}
		.btn1{
			background-color: #5e6262;
			color: #fff;
		}
	}
	.result{
		display: flex;
		justify-content: flex-start;
		flex-wrap: wrap;
		padding: 20upx;
		border-top: 10upx solid #e2e2e2;
		.result-item{
			width: 60upx;
			height: 60upx;
			border-radius: 50%;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-bottom: 20upx;
			color: #fff;
			margin-left: 10upx;
		}
		.color1{
			background-color:#FFD700;
		}
		.color2{
			background-color:#00BFFF;
		}
		.color3{
			background-color:#32CD32;
		}
		.color4{
			background-color:#DC143C;
		}
	}
	.rule-content{
		width: 600upx;
		padding: 40upx;
		.pop-btn{
			margin-top: 100upx;
			width: 200upx;
			background-color: #f35458;
			color: #fff;
		}
	}
	
}
</style>
