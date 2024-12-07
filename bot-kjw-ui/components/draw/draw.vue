<template>
	<view class="container">
		 <view class="header">
			 <view class="position">
				 <view class="positm" v-for="(item,index) in positms" :key="index" :class="'num' + parseInt(item)"></view>
			 </view>
			 <view class="currentdraw">
					当前期：<text style="color:#00fff8;font-weight: bold;font-size:1.1em;" class="currentdrawid">{{dataIssue}}</text>
			 </view>
		 </view>
		 <view class="scenary page1">
			 <view class="scenaryitm">
				 <image src="../../static/images/scenery.jpg" mode="aspectFill" class="scenaryitm-img"></image>
			 </view>
		 </view>
		 <view class="road page1">
			 <view class="roadani">
				 <view class="roaditm">
					 <image src="../../static/images/road.jpg" mode="aspectFill" class="road-img"></image>
				 </view>
			 </view>
			 <view class="roadstart">
				 <image src="../../static/images/finisher.png" class="roadstart-img"></image>
			 </view>
			 <view v-for="(item,index) in cars" :key="index" :class="'car' + parseInt(item)">
				 <image :src="'../../static/images/car'+parseInt(item)+'.png'" mode="aspectFill" class="car-img"></image>
			 </view>
			 <view class="trafficlight">
				 <view class="countdownnum number">{{leftSecond}}</view>
				 <view class="countdownnum2 number">{{leftMills}}</view>
				 <view class="redlight" v-if="countTime <= 3000"></view>
				 <view class="yellowlight" v-if="countTime <= 2000"></view>
				 <view class="greenlight" v-if="countTime === 0"></view>
			 </view>
		 </view>
		 <view class="resultpage page2">
			<view class="resultitm result1"></view>
			<view class="resultitm result2"></view>
			<view class="resultitm result3"></view>
			
			<view v-for="(item,index) in positms" :key="index" v-if="index < 3" :class="'resultitm resultcar'+(index+1)">
				<image :src="'../../static/images/winner'+item+'.png'" mode="aspectFill"></image>
			</view>
		 </view>
	</view>
</template>

<script>
	import  {shuffleArray} from '@/utils/util.js'
	export default {
		props:{
			items:{
				type:Array,
				default:()=>[]
			},
			dataIssue:{
				type:String,
				default:'8552'
			},
			startTime:''
		},
		data() {
			return {
				positms:[1,2,3,4,5,6,7,8,9,10],
				cars:[1,2,3,4,5,6,7,8,9,10],
				leftSecond:'03:00',
				leftMills:'52',
				countTime:3000
			}
		},
		mounted() {
			// setInterval(()=>{
			// 	this.startGame()
			// },300)
		},
		methods: {
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
