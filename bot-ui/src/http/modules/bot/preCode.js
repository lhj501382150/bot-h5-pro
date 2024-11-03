import axios from '../../axios'

/*
 * 账户类型
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/preCode/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/preCode/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/preCode/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/preCode/findPage',
    method: 'post',
    data
  })
}
// 分页查询
export const findList = (data) => {
  return axios({
    url: '/preCode/list',
    method: 'post',
    data
  })
}
