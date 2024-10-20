import axios from '../../axios'

/*
 * 成交订单管理模块
 */
// 修改
export const edit = (data) => {
  return axios({
    url: '/contract/edit',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/contract/findPage',
    method: 'post',
    data
  })
}

// 获取发货信息
export const getSend = (data) => {
  return axios({
    url: '/contractInfo/getSend',
    method: 'post',
    data
  })
}

// 修改发货信息
export const editSend = (data) => {
  return axios({
    url: '/contractInfo/editSend',
    method: 'post',
    data
  })
}

// 修改
export const getInvoic = (data) => {
  return axios({
    url: '/contractInfo/getInvoic',
    method: 'post',
    data
  })
}

// 修改
export const editInvoic = (data) => {
  return axios({
    url: '/contractInfo/editInvoic',
    method: 'post',
    data
  })
}
