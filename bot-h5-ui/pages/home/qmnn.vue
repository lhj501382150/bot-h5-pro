z<template>
	<view class="qmbd-game">
		<uni-nav-bar  title="四方通宝" background-color="rgb(250,81,81)" color="#fff" left-icon="back" :border="false" right-text="规则" @clickLeft="goBack" @clickRight="openRule"></uni-nav-bar>
		<view class="tab-bar">
			<view class="tab-item" :class="tabIndex==0 ? 'active':''">牛牛</view>
			<view class="tab-item" :class="tabIndex==1 ? 'active':''" @click="goRecord">下注记录</view>
		</view>
		<view class="row">
			<view class="draw-time red">{{result.ISSUE-1}}期</view>
			<view class="draw-num">
				<view class="tips">庄：{{result.zj.res}}</view>
			</view>
			<view class="link-btn" @click="goUrl">{{link.title}}</view>
		</view>
		<view class="row">
			<view class="draw-num" v-for="(item,index) in result.arr" :key="index" v-if="index>0">
				 <text>{{item.title}}:</text>{{item.res}}
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
		<view class="game-box-view">
			<view class="game-box">
				<view class="game-item bg1" v-for="(item,index) in items" :key="index" :class="item.check ? 'active':''" @click="chooseItem(item)" v-if="index < 5">
					 <view class="text">{{item.name}}</view>
					 <view class="rate">{{item.rate}}</view>
				</view>
			</view>
			 <view class="game-box">
				<view class="game-item bg2" v-for="(item,index) in items" :key="index" :class="item.check ? 'active':''" @click="chooseItem(item)" v-if="index >= 5">
						 <view class="text">{{item.name}}</view>
						 <view class="rate">{{item.rate}}</view>
				</view>
			</view>
			 
			
		</view>
		<view class="rate-box">
			<view class="rate-item-box" v-for="(item,index) in nums" :key="index" @click="chooseMoney(item)">
				 {{item}}
			</view>
		</view>
		<view class="form">
			<uni-forms ref="form" :modelValue="formData" :rules="rules">
				<uni-forms-item  name="money" required>
					<uni-easyinput type="number" v-model="formData.money" placeholder="请输入投注金额" />
				</uni-forms-item>
			</uni-forms>
			<button :class="isStop?'btn1':'btn'" @click="submit">{{isStop?'封盘中':'确认下注'}}</button>
		</view>
		<view class="result">
			<view class="result-header">
				<view class="col col0">庄</view>
				<view class="col">闲一</view>
				<view class="col">闲二</view>
				<view class="col">闲三</view>
				<view class="col">闲四</view>
				<view class="col">闲五</view>
			</view>
			<view class="result-content">
				<view class="result-row" v-for="(item,index) in results" :key="index">
					<view class="col" :class="i==0?'col0':'col1'" v-for="(temp,i) in item.bNos" :key="i">
						{{temp.status}}
					</view>
				</view>
			</view>
			<!-- -->
		</view>
		
		<uni-popup ref="errPopup" type="dialog">
			<uni-popup-dialog mode="base" :duration="2000" :before-close="true"  @close="refresh"  @confirm="refresh">
			{{errMsg}}
			</uni-popup-dialog>
		</uni-popup>
		<uni-popup ref="popup" type="dialog">
			<uni-popup-dialog mode="base" :duration="2000" :before-close="true" @close="close" @confirm="confirm">
					<view class="tip-content">
						<view v-for="(item,index) in formData.selected">
								<text v-if="item.rate1==1">平投</text>
								<text v-if="item.rate1==2">倍投</text>
								{{item.name}}:{{formData.money}}
							</view>
							共选择了{{formData.selected.length}}注
					</view>
			</uni-popup-dialog>
		</uni-popup>
		<uni-popup ref="rulePopup" :mask-click="false" background-color="#fff" borderRadius="10upx 10upx 0upx 0upx">
			<view class="rule-content">
				<view>牛牛规则与玩法</view>
				<view>【规则】</view>
				<view>庄牌为第1-5球五个数字</view>
				<view>闲1 牌为 ：第2球-第6球这五个数字</view>
				<view>闲2 牌为 ：第3球-第7球这五个数字</view>
				<view>闲3 牌为 ：第4球-第8球这五个数字</view>
				<view>闲4 牌为 ：第5球-第9球这五个数字</view>
				<view>闲5 牌为 ：第6球-第10球这五个数字</view>
				<view>闲家的牌大于庄家，则赢。反之则输</view>
				
				<view class="rule-tip">【玩法】</view>
				<view>平投：</view>
				<view>庄闲有牛:点数一样时，则按五个数字从大到小对比，优先大者为赢</view>
				<view>庄闲无牛:与上一致，按五个数字从大到小对比，优先大者为赢</view>
				<view>倍投：</view>
				<view>庄闲同是牛三或牛三以上点数时，按五个数字从大到小对比，优先大者为赢</view>
				<view>庄闲同是牛二或牛二以下点数时，则庄赢，无需比大小</view>
				<view class="rule-tip">【闲家单局下注限额】</view>
				<view>平投:金额10分-10000分，封顶50000分。</view>
				<view>倍投:金额10分-10000分，封顶10000分</view>
				<view class="rule-tip">中奖则平台抽取5%，最终一切解释权归本公司所有，同意着才可进行游戏</view>
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
					title:'138极速赛车',
					src:'https://m.228168d.com/html/jisusaiche/index.html'
				},
				result:{
					arr:[],
					zj:{}
				},
				fptime:0,
				kjtime:0,
				items:[
					{index:'1',name:'闲一',rate:'2-6球 平投',rate1:1,check:false},
					{index:'2',name:'闲二',rate:'3-7球 平投',rate1:1,check:false},
					{index:'3',name:'闲三',rate:'4-8球 平投',rate1:1,check:false},
					{index:'4',name:'闲四',rate:'5-9球 平投',rate1:1,check:false},
					{index:'5',name:'闲五',rate:'6-10球 平投',rate1:1,check:false},
					{index:'1',name:'闲一',rate:'2-6球 倍投',rate1:2,check:false},
					{index:'2',name:'闲二',rate:'3-7球 倍投',rate1:2,check:false},
					{index:'3',name:'闲三',rate:'4-8球 倍投',rate1:2,check:false},
					{index:'4',name:'闲四',rate:'5-9球 倍投',rate1:2,check:false},
					{index:'5',name:'闲五',rate:'6-10球 倍投',rate1:2,check:false}
				],
				types:[
					{text:'平投',value:'1'},
					{text:'倍投',value:'2'}
				],
				rates:[
					{name:'平投倍率',datas:[{name:'',rate:1.95}]},
					{name:'倍投倍率',datas:[
						{name:'牛一',rate:1.95},{name:'牛二',rate:2.9},{name:'牛三',rate:3.85},{name:'牛四',rate:4.8},{name:'牛五',rate:5.75},
						{name:'牛六',rate:6.7},{name:'牛七',rate:7.65},{name:'牛八',rate:8.6},{name:'牛九',rate:9.55},{name:'牛牛',rate:10.5}
					]}
				],
				formData:{
					money:'',
					type:'1',
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
					{val:0,name:'无牛'},
					{val:1,name:'牛一'},
					{val:2,name:'牛二'},
					{val:3,name:'牛三'},
					{val:4,name:'牛四'},
					{val:5,name:'牛五'},
					{val:6,name:'牛六'},
					{val:7,name:'牛七'},
					{val:8,name:'牛八'},
					{val:9,name:'牛九'},
					{val:10,name:'牛牛'}
				],
				orgtype:'',
				errMsg:'',
				curStatus:false,
				showNotice:false,
				tempShowNotice:false,
				collapse:'',
				nums:[10,20,50,100,200,500,1000,2000,5000,10000]
			}
		},
		onLoad() {
			this.getUserBalance()
			this.loadData()
			this.getShowNoticePara()
			this.connectSocketInit()
			let user = JSON.parse(uni.getStorageSync('userinfo'))
			this.orgtype = user.orgtype
		},
		beforeDestroy() {
			this.closeSocket()
		},
		methods: {
			chooseMoney(item){
				this.formData.money = item
			},
			radioChange(value){
				this.formData.type= value
			},
			getShowNoticePara(){
				let para = {
					sysid:11
				}
				try{
					this.$http.post('/Query/SysPara',para,res=>{
					  if(res.iCode==0){
						  this.tempShowNotice =  res.rData.sval =='Y'
					  }
					})
				}catch(e){
					this.tempShowNotice = false
				}
			},
			loadData(){
				let para = {
					pageIdx:0,
					pageSize:20,
					mode:"1",
					userno :uni.getStorageSync('userno')
				}
				this.$http.post("/Query/ReustList",para,res => {
					this.results = []
					let datas = res.rData || []
					datas.forEach(item=>{
						let bNos = item.bNno.split(',') || []
						let sresult = item.sresult.split(',') || []
						bNos = bNos.map((item,index)=>{
							return {
								name:this.getTitle(index),
								status:this.getStatus(item),
								nums:this.getNums(sresult,index)
							}
						})
						let temp = {
							issue:item.issue,
							bNos:bNos
						}
						this.results.push(temp)
					})
				})
			},
			getNums(sresult,index){
				let arr = []
				for(var i = 0;i<5;i++){
					arr[i] = sresult[index+i]
				}
				return arr
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
				let url = webSocketUrl + 'niu/' + userno+"/" + pwd;
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
								this.parseResult()
								this.leftTime = getSecond(this.result.TIME)
								this.fillTime()
								this.getUserBalance()
								this.loadData()
							}else if(data.status == 4){
								this.isStop = true
								this.fptime = 0
								this.kjtime = 15
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
			parseResult(){
				this.result.arr = []
				this.result.zj = {}
				try{
					// let result = '2#6#3#1#4#10#8#7#5#9*2#6#3#1#4-6,6#3#1#4#10-4,3#1#4#10#8-0,1#4#10#8#7-0,4#10#8#7#5-4,10#8#7#5#9-9'
					let result = this.result.RESULT
					if(result){
						let datas = result.split("*") || []
						if(datas.length == 2){
							datas = datas[1].split(',') || []
							datas = datas.map((item,index) =>{
								let arr = item.split('-');
								let  t = {
									index:index,
									title: this.getTitle(index),
									res:this.getStatus(arr[1]),
									code:arr[0].split('#')
								}
								return t
							})
							this.result.zj = datas[0]
							this.result.arr = datas
						}
					}
				}catch(e){
					console.error(e)
				}
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
				this.fptime = this.leftTime - 15 < 0 ? 0 :this.leftTime - 15
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
						ruId:item.index + '-' +this.formData.money +'-' + item.rate1
					}
					return para
				})
				let para = {
					botId:botId,
					dataId:this.result.ID,
					userId:uni.getStorageSync('userno'),
					userName:uni.getStorageSync("nickname"),
					orders:orders,
					mode:1
				}
				
				this.$http.post('/Order/NOrder',para,(res=>{
					if(res.iCode ==0){
						this.formData.money = ''
						this.formData.type='1'
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
							duration:2000
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
			getTitle(index){
				let titles = [{val:0,name:'庄'},{val:1,name:'闲一'}
							  ,{val:2,name:'闲二'},{val:3,name:'闲三'}
							  ,{val:4,name:'闲四'},{val:5,name:'闲五'}]
				const item = titles.find(item=> item.val==index) || {}
				return item.name
			},
			chooseItem(item){
				item.check = !item.check
			},
			goRecord(){
				uni.setStorageSync('record_mode',1)
				uni.switchTab({
					url:'/pages/record/record'
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
		height: 50upx;
		padding: 20upx;
		border-bottom: 2upx solid #e2e2e2;
		margin:0 auto;
		.draw-time{
			width: 33%;
			text-align: center;
		}
		.draw-num{
			display: flex;
			align-items: center;
			justify-content: flex-end;
			.tips{
				width: 120upx;
				height: 40upx;
				line-height: 40upx;
				border-radius: 10upx;
				background-color: rgb(250,81,81);
				color: #fff;
				text-align: center;
				font-size: 26upx;
				margin-right: 20upx;
			}
			text{
				color:#999;
				padding-right: 2upx;
				padding-left:6upx;
			}
		}
		.link-btn{
			width: 250upx;
			height: 60upx;
			line-height: 60upx;
			text-align: center;
			background-color: rgb(250,41,41);
			color: #fff;
			margin-right: 20upx;
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
	.game-box-view{
		width: 724upx;
		background-image: url('../../static/images/home/niu-bg.jpg');
		background-size: 100% 100%;
		background-color: #eee;
		margin:15upx auto;
		padding: 15upx;
	}
	.game-box{
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		position: relative;
		.game-item{
			width: 32%;
			height: 120upx;
			margin-top: 30upx;
			margin-bottom: 30upx;
			border-radius: 20upx;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			font-size: 36upx;
			color: #ffffff;
			font-weight: 600;
			letter-spacing: 10upx;
			opacity: 0.8;
			.rate{
				font-size: 14px;
			}
		}
		.bg1{
			background-image: linear-gradient(to bottom,#8f91fa,#5500ff);
		}
		.bg2{
			background-image: linear-gradient(to bottom,#00dada,#00a3a3);
		}
		.active{
			// background-image: linear-gradient(to bottom,#91fa8b,#13ac02);
			background-image: linear-gradient(to bottom, #f2f7f2, #dfecde);
			color: #000000;
		}
	}
	.rate-box{
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		padding: 0upx 20upx;
		.rate-item-box{
			 width:130upx;
			 line-height: 60upx;
			 background-color: #7B68EE;
			 color: #fff;
			 border-radius: 10upx;
			 margin-bottom: 20upx;
			 text-align: center;
		}
		.rate-item-box:hover{
			background-color: #e2e2e2;
			color: #000;
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
		::v-deep .checklist-text{
			font-size: 48upx!important;
			font-weight: 600;
		}
		::v-deep .radio__inner{
			    width: 20px!important;
			    height: 20px!important;
			    // border: 2px solid #000000!important;
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
		display:flex;
		height:380upx;
		.col{
			width:100upx;
			height:60upx;
			line-height: 60upx;
			text-align: center;
		}
		.col0{
			color:red;
		}
		.col1{
			color:#000!important;
		}
		.result-header{
			display:flex;
			flex-direction: column;
		}
		.result-content{
			padding:0upx;
			margin:0upx;
			overflow-x: auto;
			display: flex;
			flex-wrap: nowrap;
			.result-row{
				display:flex;
				flex-direction: column;
				margin:0upx;
			}
		}
	}
	.rule-content{
		width: 600upx;
		padding: 40upx;
		.rule-tip{
			margin-top:30upx;
		}
		.pop-btn{
			margin-top: 100upx;
			width: 200upx;
			background-color: #f35458;
			color: #fff;
		}
	}
	
}
</style>
