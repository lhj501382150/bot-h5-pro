<template>
	<view class="container">
		<view class="preloader" v-if="countTime < 0"></view>
		<view v-else>
			<view class="header">
						 <view class="position">
							 <view class="positm" v-for="(item,index) in positms" :key="index" :class="'num' + parseInt(item)"></view>
						 </view>
						 <view class="currentdraw">
								当前期：<text style="color:#00fff8;font-weight: bold;font-size:1.1em;" class="currentdrawid">{{drawIssue}}</text>
						 </view>
			</view>
			<view class="scenary page1" v-if="isStart">
						 <view class="scenaryitm" :style="{left:initLeft}">
							 <image src="../../static/images/scenery.jpg" mode="aspectFill" class="scenaryitm-img"></image>
						 </view>
			</view>
			<view class="road page1" v-if="isStart">
						 <view class="roadani">
							 <view class="roaditm" :style="{left:initLeft}">
								 <image src="../../static/images/road.jpg" mode="aspectFill" class="road-img"></image>
							 </view>
						 </view>
						 <view class="roadstart">
							 <image src="../../static/images/finisher.png" class="roadstart-img"></image>
						 </view>
						 <view class="roadend" v-show="isEnd">
							 <image src="../../static/images/finisher.png" class="roadstart-img"></image>
						 </view>
						 <view v-for="(item,index) in cars" :key="index" :class="'car' + parseInt(item)" :style="{left:carsLeft[index]+'upx'}">
							 <image :src="'../../static/images/car'+parseInt(item)+'.png'" mode="aspectFill" :class="'car-img car-img'+parseInt(item)"></image>
							 <image src="../../static/images/wheel.gif" :class="'wheel'+parseInt(item)+'a'"></image>
							 <image src="../../static/images/wheel.gif" :class="'wheel'+parseInt(item)+'b'"></image>
							 <image src="../../static/images/wind.png" class="wind" v-show="carsWind[index]"></image>
							 <image src="../../static/images/flame.png" class="flame" v-show="carsWind[index]"></image>
						 </view>
						 <view class="trafficlight" v-if="countTime >= 0 " :class="countTime == 0 ?'trafficlight-out':''">
							 <view class="countdownnum number">{{countdownnum}}</view>
							 <view class="countdownnum2 number">{{countdownnum2}}</view>
							 <view class="redlight" v-if="countTime < 3"></view>
							 <view class="yellowlight" v-if="countTime < 2"></view>
							 <view class="greenlight" v-if="countTime < 1"></view>
						 </view>
			</view>
			<view class="resultpage page2" v-show="!isStart">
				<view class="resultitm result1"></view>
				<view class="resultitm result2"></view>
				<view class="resultitm result3"></view>
				<view v-for="(item,index) in positms" :key="index" v-if="index < 3" :class="'resultitm resultcar'+(index+1)">
					<image :src="'../../static/images/winner'+item+'.png'" mode="aspectFill" class="resultcar-img"></image>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import  {shuffleArray} from '@/utils/util.js'
	import {TweenMax,Power1,Linear} from 'gsap'
	export default {
		props:{
			mode:{
				type:String,
				default:''
			}
		},
		data() {
			return {
				dataIssue:'',
				drawIssue:'',
				positms:[1,2,3,4,5,6,7,8,9,10],
				cars:[1,2,3,4,5,6,7,8,9,10],
				carsWind:[false,false,false,false,false,false,false,false,false,false],
				carsPosition:[0,0,0,0,0,0,0,0,0,0],
				carsLeft:[670,665,658,650,645,640,630,625,615,610],
				carpositionoffset : [43,39,35,31, 27,23,20,15,9,0],
				countdownnum:'',
				countdownnum2:'',
				countdowninv:'',
				countdowninv2:'52',
				animation1:'',
				animation2:'',
				countTime:60,
				isStart:true,
				initLeft:'-750upx',
				roadstartTween:'',
				roaditmTween:'',
				scenaryitmTween:'',
				carTweens:[],
				isEnd:false, //是否到达终点
				timed:''
			}
		},
		mounted() {
			this.positms = []
			if(this.mode){
				uni.setStorageSync('DRAWW_MODE',this.mode)
				this.loadResult()
			}
		},
		destroyed(){
			console.log('destroyed---------------')
			if(this.timed){
				clearTimeout(this.timed)
			}
			this.stopanimation()
		},
		methods: {
			changeStart(){
				setTimeout(()=>{
					this.isStart = true
					this.loadResult()
					this.stopCars()
				},3000)
			},
			async getResult(){
				this.isEnd = false
				console.log('----------------------',this.mode)
				let mode = uni.getStorageSync('DRAWW_MODE')
				if(this.mode != mode) {
					if(this.timed){
						clearTimeout(this.timed)
					}
					this.stopanimation()
					return
				}
				let res = await this.$http.post('/draw/getResult',{mode:this.mode})
				res = res.data
				if(res.data.dataId != this.dataIssue){
					this.drawIssue = res.data.drawIssue
					this.isEnd = true
					this.$emit('updateDraw',res.data)
					this.finishgame(res.data.preDrawCode)
					this.$emit('updateRun',false)//通知结束
					if(this.timed){
						clearTimeout(this.timed)
					}
				}else{
					this.timed = setTimeout(()=>{
						this.getResult()
					},1000)
				}
			},
			loadResult(){
				this.isEnd = false
				this.$http.post('/draw/getResult',{mode:this.mode},(res)=>{
					if(this.dataIssue != res.data.dataId){
						this.dataIssue = res.data.dataId
						this.drawIssue = res.data.drawIssue
						this.positms = res.data.preDrawCode.split(',').map(item=>parseInt(item))
						this.countTime = res.data.leftTime - 15
						if(this.countTime < 0){
							this.getResult()
						}else{
							this.startcountdown(this.countTime)
						}
					}
				})
			},
			finishgame(finalresult) {
				var resultarry = finalresult.split(",");
				//停止移动
				this.stopanimation()
				
				this.positms = resultarry.map(item=>parseInt(item))
				this.positms.forEach((item,index)=>{
					var randomdelay = Math.floor((Math.random() * 2) + 1);
					randomdelay = randomdelay / 10;
					var caroffset = parseInt(item) - 1;
					var winningpos =  this.carpositionoffset[caroffset] + 60 + (index * 10);
					var anipos = winningpos + "px";
					this.activateflame(winningpos, item);
					let tween = TweenMax.to('.car' + item, 1, {
						left: anipos,
						delay: randomdelay
					});
					this.carTweens.push(tween)
				})
				setTimeout(()=>{
					this.isStart = false
					this.changeStart()
				},3000)
			},
			startanimation(){
				this.roadstartTween = TweenMax.to('.roadstart', 1, {
					left: "750px",
					ease: Power1.easeIn
				});
				// this.roadstartTween.kill()
				this.roaditmTween = TweenMax.to('.roaditm', 0.4, {
					left: "-120px",
					repeat: -1,
					ease: Linear.easeNone
				});
				this.scenaryitmTween = TweenMax.to('.scenaryitm', 20, {
					left: "0",
					repeat: -1,
					ease: Linear.easeNone
				});
				this.animation1 = setInterval(()=> {
					this.animate();
				}, 3000);
				this.animation2 = setInterval(()=> {
					this.startGame()
				}, 300);
				this.animate()
			},
			convertTime(num) {
				var shownum = String(num);
				var countnum = shownum.length;
				if(countnum == 1) {
					shownum = "0" + shownum;
					return shownum;
				} else {
					return shownum;
				}
			},
			countdown(sec) {
				var minute = sec / 60;
				minute = Math.floor(minute);
				if(minute == 0) {
					var showminute = "00";
				} else {
					var showminute = String(minute);
					showminute = this.convertTime(showminute);
				}
			
				var second = sec - (minute * 60);
				var showsec = this.convertTime(second);
			
				if(sec < 0) {
					this.countdownnum = '00:00'
				} else {
					this.countdownnum = showminute + ":" + showsec
				}
			},
			startcountdown(timer) {
				 this.isStart = true
				 this.stopanimation();
				 this.carsLeft = [670,665,658,650,645,640,630,625,615,610]
			
				this.countdowninv = setInterval(()=> {
					timer -= 1;
					this.countTime = timer
					this.countdown(timer);
					if(timer == 0) {
						this.$emit('updateRun',true)//通知结束
						this.startanimation()
						setTimeout(()=>{
							this.getResult()
						},1000)
						clearInterval(this.countdowninv);
						clearInterval(this.countdowninv2);
					}
				}, 1000);
			
				var timer2 = 99;
				this.countdowninv2 = setInterval(()=> {
					this.countdownnum2 = this.convertTime(timer2)
					if(timer2 == 0) {
						timer2 = 99;
					}
					timer2 -= 1;
				}, 10);
				this.countdown(timer);
			},
			stopanimation() {
				this.initLeft = '-750upx'
				this.carsWind = []
				for(var i=0;i<10;i++){
					this.carsWind.push(false)
				}
				this.carsPosition = [0,0,0,0,0,0,0,0,0,0]
				if(this.roadstartTween){
					this.roadstartTween.kill()
				}
				if(this.roaditmTween){
					this.roaditmTween.kill()
				}
				if(this.scenaryitmTween){
					this.scenaryitmTween.kill()
				}
				this.stopCars()
				clearInterval(this.animation1);
				clearInterval(this.animation2);
				clearInterval(this.countdowninv);
				clearInterval(this.countdowninv2);
			},
			animateOne(carNum){
				var random = Math.floor((Math.random() * 40) + 30);
				random = random / 10;
				
				var randoma = Math.floor((Math.random() * 10) + 1);
				randoma = randoma / 10;
				
				var randomb = Math.floor((Math.random() * 150) + 100);
				this.activateflame(randomb, carNum);
				randomb = randomb + "px";
				
				let tween = TweenMax.to('.car' + carNum, random, {
					left: randomb,
					delay: randoma
				});
				this.carTweens.push(tween)
			},
			stopCars(){
				this.carTweens.forEach(item=>{
					if(item){
						item.kill()
					}
				})
				this.carTweens = []
			},
			animate() {
				 this.cars.forEach(item=>{
					 this.animateOne(item)
				 })
			
			},
			activateflame(pos,carNum){//控制wind样式
				 let index = carNum - 1
				 let pos1 = this.carsPosition[index]
				 if(pos > pos1){
					this.carsWind.splice(index,1)
					this.carsWind.splice(index,0,true)
				 }else{
					 this.carsWind.splice(index,1)
					 this.carsWind.splice(index,0,false)
				 }
				 this.carsPosition[index] = pos
			},
			startGame(){//洗牌算法
				let arr = shuffleArray(this.positms)
				this.positms = []
				for(var i =0 ;i<arr.length;i++){
					this.positms.push(arr[i])
				}
			}
		}
	}
</script>

<style>

</style>
