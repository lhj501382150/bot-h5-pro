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
    const dateMat = new Date(datetime);
    const year = dateMat.getFullYear();
    const month = dateMat.getMonth() + 1;
    const day = dateMat.getDate();
    const hh = dateMat.getHours();
    const mm = dateMat.getMinutes();
    const ss = dateMat.getSeconds();
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
export function getCurrentTime(){
  var dateMat = new Date();
  const year = dateMat.getFullYear();
  const month = dateMat.getMonth() + 1;
  const day = dateMat.getDate();
  const hh = dateMat.getHours();
  const mm = dateMat.getMinutes();
  const ss = dateMat.getSeconds();
  const timeFormat = year + '-' + month + '-' + day + " " + hh + ':' + mm + ':' + ss;
  return timeFormat;
}

export function subTime(time){
  var t1 = new Date(time).getTime();
  var t2 = new Date().getTime();
  var t = parseInt((t2 - t1) /1000)
  return t
}
