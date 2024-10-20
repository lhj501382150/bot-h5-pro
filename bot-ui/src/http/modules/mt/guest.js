import axios from '../../axios'

/*
 * 会议嘉宾管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/guest/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/guest/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/guest/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/guest/findPage',
    method: 'post',
    data
  })
}
