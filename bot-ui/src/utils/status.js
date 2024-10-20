/*
*格式化数据状态
*/
export function userIsvalidate(row, column, cellValue, index){
  console.log("row, column, cellValue, index",row, column, cellValue, index)
  if(cellValue==='Y') {
    return '启用'
  } else {
    return "禁用"
  }
}

export function getStatus(cellValue,types){
  let t = types.filter(function(item){
    return item.sval == cellValue
  });
  let val = cellValue;
  if(t && t.length > 0) val = t[0].sshow
  return val
}
