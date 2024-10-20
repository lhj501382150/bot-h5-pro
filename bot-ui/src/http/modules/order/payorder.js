import axios from '../../axios'

/*
 * 成交订单管理模块
 */
// 修改
export const edit = (data) => {
  return axios({
    url: '/payOrder/edit',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/payOrder/findPage',
    method: 'post',
    data
  })
}

// 获取发货信息
export const getSend = (data) => {
  return axios({
    url: '/payOrder/getSend',
    method: 'post',
    data
  })
}

// 修改发货信息
export const editSend = (data) => {
  return axios({
    url: '/payOrder/editSend',
    method: 'post',
    data
  })
}

// 修改
export const getInvoic = (data) => {
  return axios({
    url: '/payOrder/getInvoic',
    method: 'post',
    data
  })
}

// 修改
export const editInvoic = (data) => {
  return axios({
    url: '/payOrder/editInvoic',
    method: 'post',
    data
  })
}
// 获取发货信息
export const getDtl = (data) => {
  return axios({
    url: '/payOrder/getDtl',
    method: 'post',
    data
  })
}

// 修改
export const balance = (data) => {
  return axios({
    url: '/payOrder/balance',
    method: 'post',
    data
  })
}
