import axios from '../../axios'

/*
 * 用户管理收货地址模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/outmoney/save',
    method: 'post',
    data
  })
}
// 修改
export const audit = (data) => {
  return axios({
    url: '/outmoney/audit',
    method: 'post',
    data
  })
}
// 删除
export const reject = (data) => {
  return axios({
    url: '/outmoney/reject',
    method: 'post',
    data
  })
}
// 批量支付
export const batchPay = (data) => {
  return axios({
    url: '/outmoney/batchPay',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/outmoney/findPage',
    method: 'post',
    data
  })
}

export const findExport = (data) => {
  return axios({
    url: '/outmoney/findExport',
    method: 'post',
    data
  })
}


export const findList = (data) => {
  return axios({
    url: '/outmoney/list',
    method: 'post',
    data
  })
}
