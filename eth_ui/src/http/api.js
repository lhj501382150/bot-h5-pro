/*
 * 接口统一集成模块
 */
/*import * as login from './modules/login'
import * as user from './modules/user'
import * as role from './modules/role'
import * as menu from './modules/menu'
import * as log from './modules/log'
import * as loginlog from './modules/loginlog'



// 默认全部导出
export default {
    login,
    user,
    role,
    menu,
    log,
    loginlog


}*/
/**
 * './modules/'：要读取的文件夹
 * true：是否读取子目录，不读取设置false
 * /\.js$/ 匹配.js类型的文件
 * files：获取的api集合
 */
const files = require.context('./modules/', true, /\.js$/);

let apiMaps = {};
files.keys().forEach(key => {
  let name =key.substring(key.lastIndexOf("/")+1).replace(".js","");
  //let name = key.replace("./","").replace(".js","");
  let api = {}
  api[name] = files(key)
  apiMaps = Object.assign(apiMaps,api);
  //modules[key.replace(/(\.\/|\.js)/g, '')] = files(key).default
});
export default apiMaps;
