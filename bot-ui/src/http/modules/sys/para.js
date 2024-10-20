import axios from '../../axios'

// 保存
export const save = (data) => {
  return axios({
    url: '/para/save',
    method: 'post',
    data
  })
}
// 保存
export const edit = (data) => {
  return axios({
    url: '/para/edit',
    method: 'post',
    data
  })
}
// 删除
export const del = (data) => {
  return axios({
    url: '/para/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/para/findPage',
    method: 'post',
    data
  })
}
