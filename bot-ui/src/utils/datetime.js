/**
 * 时间日期相关操作
 */


/**
 * 时间格式化
 * 将 2018-09-23T11:54:16.000+0000 格式化成 2018/09/23 11:54:16
 * @param datetime 国际化日期格式
 */
export function format (datetime) {
  return formatWithSeperator(datetime, "/", ":");
}

/**
 * 时间格式化
 * 将 2018-09-23T11:54:16.000+0000 格式化成类似 2018/09/23 11:54:16
 * 可以指定日期和时间分隔符
 * @param datetime 国际化日期格式
 */
export function formatWithSeperator (datetime, dateSeprator, timeSeprator) {
  if (datetime != null) {
    var dateMat = new Date(datetime);
    var year = dateMat.getFullYear();
    var month = dateMat.getMonth() + 1;
    if (month >= 1 && month <= 9) {
      month = "0" + month;
    }
    var day = dateMat.getDate();
    if (day >= 1 && day <= 9) {
      day = "0" + day;
    }
    var hh = dateMat.getHours();
    if (hh >= 1 && hh <= 9) {
      hh = "0" + hh;
    }
    var mm = dateMat.getMinutes();
    if (mm <= 9) {
      mm = "0" + mm;
    }
    var ss = dateMat.getSeconds();
    if (ss <= 10) {
      ss = "0" + ss;
    }
    const timeFormat = year + dateSeprator + month + dateSeprator + day + " " + hh + timeSeprator + mm + timeSeprator + ss;
    return timeFormat;
  }
}
export function getCurrentDate(){
    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
      month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
      strDate = "0" + strDate;
    }
    var currentdate = year +'-'+ month  +'-'+ strDate ;
    return currentdate;
}
export function getCurrentMonth(){
  var date = new Date();
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  var currentdate = year +'-'+ month ;
  return currentdate;
}


export function getDate(datetime){
  var date= new Date(Date.parse(datetime.replace(/-/g,  "/")));
  return date;
}

export function subTime(str1,str2,str){
  var date = getDate(str);
  var date1 = getDate(str1);
  var date2 = getDate(str2);
  var datetime = date.getTime() + (date1.getTime() - date2.getTime())
  return formatWithSeperator(datetime,"-",":");
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

export const getCurTime = ()=>{
  const date = new Date();
     const hours = ("0" + date.getHours()).slice(-2);
     const minutes = ("0" + date.getMinutes()).slice(-2);
     const seconds = ("0" + date.getSeconds()).slice(-2);
   return `${hours}${minutes}${seconds}`;
}