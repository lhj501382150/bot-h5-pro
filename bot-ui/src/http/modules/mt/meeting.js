import axios from '../../axios'

/*
 * 会议管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/meeting/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/meeting/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/meeting/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/meeting/findPage',
    method: 'post',
    data
  })
}
// 分页查询
export const findList = (data) => {
  return axios({
    url: '/meeting/list',
    method: 'post',
    data
  })
}
