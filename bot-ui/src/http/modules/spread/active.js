import axios from '../../axios'

/*
 * 会议嘉宾管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/active/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/active/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/active/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/active/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const list = (data) => {
  return axios({
    url: '/active/list',
    method: 'post',
    data
  })
}
