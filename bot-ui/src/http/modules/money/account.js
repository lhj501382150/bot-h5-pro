import axios from '../../axios'

/*
 * 账户类型
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/account/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/account/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/account/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/account/findPage',
    method: 'post',
    data
  })
}
// 分页查询
export const findList = (data) => {
  return axios({
    url: '/account/list',
    method: 'post',
    data
  })
}
