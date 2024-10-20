import axios from '../../axios'

/*
 * 用户管理收货地址模块
 */

// 审核
export const audit = (data) => {
  return axios({
    url: '/userChange/audit',
    method: 'post',
    data
  })
}
// 驳回
export const reject = (data) => {
  return axios({
    url: '/userChange/reject',
    method: 'post',
    data
  })
}

// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/userChange/findPage',
    method: 'post',
    data
  })
}

