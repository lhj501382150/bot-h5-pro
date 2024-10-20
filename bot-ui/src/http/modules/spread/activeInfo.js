import axios from '../../axios'

/*
 * 会议嘉宾管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/activeInfo/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/activeInfo/edit',
    method: 'post',
    data
  })
}
// 空投
export const sync = (data) => {
  return axios({
    url: '/activeInfo/sync',
    method: 'post',
    data
  })
}
// 删除
export const del = (data) => {
  return axios({
    url: '/activeInfo/batchDel',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/activeInfo/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/activeInfo/findPage',
    method: 'post',
    data
  })
}

export const findUser = (data) => {
  return axios({
    url: '/activeInfo/findUser',
    method: 'post',
    data
  })
}
