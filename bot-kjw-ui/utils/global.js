/**
 * 全局常量、方法封装模块
 * 通过原型挂载到Vue属性
 * 通过 this.Global 调用
 */

 // 后台管理系统服务器地址
export const baseUrl = '/kjw'
export const AUTH_TOKEN = "Authorization"

export default {
    baseUrl,
	AUTH_TOKEN
}