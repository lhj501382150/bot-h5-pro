import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/userDigitartRule/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/userDigitartRule/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/userDigitartRule/delete',
    method: 'post',
    data
  })
}

// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/userDigitartRule/findPage',
    method: 'post',
    data
  })
}
//  查询所有
export const findWare = (data) => {
  return axios({
    url: '/userDigitartRule/warelist',
    method: 'post',
    data
  })
}

