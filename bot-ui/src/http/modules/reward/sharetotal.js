import axios from '../../axios'

/*
 * 分享业绩统计
 */

// 计算
export const count = (data) => {
  return axios({
    url: '/shareTotal/count',
    method: 'post',
    data
  })
}
// 办理
export const handle = (data) => {
  return axios({
    url: '/shareTotal/handle',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/shareTotal/findPage',
    method: 'post',
    data
  })
}
