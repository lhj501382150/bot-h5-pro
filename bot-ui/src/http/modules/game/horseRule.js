import axios from '../../axios'

/*
 * 用户管理收货地址模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/horseRule/save',
    method: 'post',
    data
  })
}
// 保存
export const saveLike = (data) => {
  return axios({
    url: '/horseRule/saveLike',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/horseRule/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/horseRule/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/horseRule/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const findList = (data) => {
  return axios({
    url: '/horseRule/list',
    method: 'post',
    data
  })
}
export const dictTree = (data) => {
  return axios({
    url: '/horseRule/dictTree',
    method: 'post',
    data
  })
}
