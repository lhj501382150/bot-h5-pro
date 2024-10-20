import http from './apiRequest'
import {baseUrl,AUTH_TOKEN} from '../utils/global'

http.setBaseUrl(baseUrl)
http.beforeRequestFilter = function(res){
	const requestTime = uni.getStorageSync('request_time')
	const curTime = new Date().getTime()
	if(requestTime > 0 && curTime - requestTime > 1000 * 60  * 60){
		uni.removeStorageSync("userinfo")
		uni.removeStorageSync("Token")
		uni.removeStorageSync("userno")
		uni.removeStorageSync("mask")
	}
	http.header[AUTH_TOKEN] = '';
	if(res.url.indexOf("/login") < 0){
		if (uni.getStorageSync('Token')) {
		    http.header[AUTH_TOKEN] = 'Bearer ' + uni.getStorageSync('Token');
		}
	} 
	return res;
}
http.beforeResponseFilter = function (res) {
	uni.setStorageSync('request_time',new Date().getTime())
	if(res.statusCode==200){
		res = res.data
		if(res.iCode==401){
			uni.removeStorageSync("userinfo")
			uni.removeStorageSync("Token")
			uni.removeStorageSync("userno")
			uni.removeStorageSync("mask")
			uni.navigateTo({
				url:'/pages/index/index'
			})
		}else if(res.iCode != 0 && res.iCode != -100){
			uni.showToast({
				 icon: 'none',
				 title: res.sMsg,
				 duration:5000
			})
		}
		return res;
	}else if(res.statusCode==401){
		uni.removeStorageSync("userinfo")
		uni.removeStorageSync("Token")
		uni.removeStorageSync("userno")
		uni.removeStorageSync("mask")
		 uni.navigateTo({
		 	url:'/pages/index/index'
		 })
	}else{
		uni.showToast({
			 icon: 'none',
			 title: res.errMsg,
		})
		return res;
	}
   
}
 
let req = {
    'http': http
}
export default req