import axios from '../../axios'

/*
 * 分享业绩统计
 */

// 计算
export const count = (data) => {
  return axios({
    url: '/orgTotal/count',
    method: 'post',
    data
  })
}
// 删除
export const delOrg = (data) => {
  return axios({
    url: '/orgTotal/delete',
    method: 'post',
    data
  })
}
// 办理
export const handle = (data) => {
  return axios({
    url: '/orgTotal/handle',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/orgTotal/findPage',
    method: 'post',
    data
  })
}
