// 格式化数字 没三位加 ，
export const formatNum =  (val)=>{
	val = val.toString();
	var arrNum=val.split('').reverse().join('').replace(/(\d{3})/g,'$1,').split('').reverse().join('');
	// var arrLength=arrNum.length;
	// if(arrLength%4==0){
	// 	 arrNum=arrNum.substring(1);
	// }
	return arrNum
}
export const formatDate = (timestamp,type)=>{
	 const date = new Date(timestamp);
    const year = date.getFullYear();
      const month = ("0" + (date.getMonth() + 1)).slice(-2);
      const day = ("0" + date.getDate()).slice(-2);
      const hours = ("0" + date.getHours()).slice(-2);
      const minutes = ("0" + date.getMinutes()).slice(-2);
      const seconds = ("0" + date.getSeconds()).slice(-2);
		if(type==1){//mm-dd HH:mm
			return `${month}-${day} ${hours}:${minutes}`;
		}else if(type==2){//mm-dd HH:mm
			return `${year}-${month}-${day}`;
		}else if(type==3){//mm-dd HH:mm
			return `${hours}${minutes}${seconds}`;
		}else{
			return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
		}
}

export const divide = (num) =>{
	if(num > 0){
		num = (num / 100).toFixed(2)
		let str = formatNum(num)
		if (str.charAt(0) === ',') {
		  str = str.slice(1);
		}
		return str
	}else{
		return 0;
	}
}

export const divide100 = (num) =>{
	if(num != 0){
		return num = (num / 100).toFixed(2)
	}else{
		return 0;
	}
}

export const getSecond = (time) =>{
	var currentDate = Date.parse(new Date());
	var specifiedDate = Date.parse(new Date(time.replace(/-/g,'/'))); // 假设这是你要计算的指定时间
	var timeDifferenceInMilliseconds = specifiedDate - currentDate;
	// 转换为秒数
	var timeDifferenceInSeconds = Math.floor(timeDifferenceInMilliseconds / 1000);
	if(timeDifferenceInSeconds < 0 ){
		timeDifferenceInSeconds = 0
	}
	return timeDifferenceInSeconds;
}
export const getCurTime = ()=>{
	 const date = new Date();
      const hours = ("0" + date.getHours()).slice(-2);
      const minutes = ("0" + date.getMinutes()).slice(-2);
      const seconds = ("0" + date.getSeconds()).slice(-2);
	  return `${hours}${minutes}${seconds}`;
}
export const getWeekStartEndDates = (offset = 0) => {
    const now = new Date();
    // 调整日期到周日（0代表周日，6代表周六，这里根据实际需要调整，大多数情况下周一是0）
    const dayOfWeek = (now.getDay() + 6) % 7; // 这里假设周日作为一周开始
    // 计算本周一的日期
    const mondayThisWeek = new Date(now.setDate(now.getDate() - dayOfWeek));
    // 计算上周一的日期
    const mondayLastWeek = new Date(new Date().setDate(mondayThisWeek.getDate() - 1));
    
    
    // 根据偏移量计算目标周的周一和周日
    const mondayTargetWeek = new Date(mondayThisWeek.setDate(mondayThisWeek.getDate() + offset * 7));
    const temp = new Date(mondayTargetWeek.getTime());
    const sundayTargetWeek = new Date(temp.setDate(temp.getDate() + 7));
    
    // 返回格式化后的日期（可根据需要调整格式）
    return {
        startOfWeek: formatDate(mondayTargetWeek,2), // 或使用其他格式化方法
        endOfWeek: formatDate(sundayTargetWeek,2)
    };
}

export const getIp = () =>{
	let url = window.location.href
	const regex = /^(?:https?:\/\/)?(?:[^@\n]+@)?(?:([^:\/\n?#]+)(?::(\d+))?)?(?:[\/?#]|$)/;
	const matches = url.match(regex);
	  
	  
	let ip = 'localhost'
	if (matches && matches.length >= 3) {
	  ip = `${matches[1]}:${matches[2] || ''}`;
	} 
	return `ws://${ip}/socket/`
}