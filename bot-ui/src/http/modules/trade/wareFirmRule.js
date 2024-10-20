import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/wareFirmRule/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/wareFirmRule/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/wareFirmRule/delete',
    method: 'post',
    data
  })
}

// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/wareFirmRule/findPage',
    method: 'post',
    data
  })
}
//  查询所有
export const findWare = (data) => {
  return axios({
    url: '/wareFirmRule/warelist',
    method: 'post',
    data
  })
}

