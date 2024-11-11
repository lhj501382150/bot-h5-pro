import { Message } from 'element-ui';

export function copy(text){
  let target =  document.createElement('div');
      target.id = 'tempTarget';
      target.style.opacity = '0';
      target.innerText = text;
      document.body.appendChild(target);

      try {
          let range = document.createRange();
          range.selectNode(target);
          window.getSelection().removeAllRanges();
          window.getSelection().addRange(range);
          document.execCommand('copy');
          window.getSelection().removeAllRanges();
          target.parentElement.removeChild(target);
          Message({
            type:'success',
            message:'复制成功'
          })
      } catch (e) {
        console.log(e)
          console.log('复制失败')
      }
}

export function getArr(data){
  let arr = []
  Object.getOwnPropertyNames(data).forEach(function(key){
     arr.push(data[key])
  })
  return arr
}
function get_thousand_num(num) {
  return num.toString().replace(/\d+/, function (n) { // 先提取整数部分
      return n.replace(/(\d)(?=(\d{3})+$)/g, function ($1) { // 对整数部分添加分隔符
          return $1 + ",";
      });
  });
}
export function divid(num){
  if(num != 0){
    if(num.length <= 18){
      for(var i=0;i< 18 - num.length;i++){
        num = '0' + num;
      }
      num = '0.' + num;
    }else{
      num = num.toString().substring(0,num.length -18) + "." + num.substring(18);
    }
    num = num.toString()
    var z = 0;
    for(var i = num.length -1 ; i > 0;i--){
      if(num[i] == 0) z++;
      else if(num[i] == '.') z++;
      else break;
    }
    if(z > 0){
      num = num.substring(0,num.length - z)
    }
    num = get_thousand_num(num);
  }
  return num;
}

function toDecimal2(val,len) {
  var f = parseFloat(val);
  if (isNaN(f)) {
      return false;
  }
  var f = val/Math.pow(10,len);
  var s = f.toString();
  var rs = s.indexOf('.');
  if (rs < 0) {
      rs = s.length;
      s += '.';
  }
  while (s.length <= rs + len) {
      s += '0';
  }
  return s;
}
