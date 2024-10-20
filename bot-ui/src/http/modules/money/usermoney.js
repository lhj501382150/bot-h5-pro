import axios from '../../axios'

/*
 * 用户管理收货地址模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/usermoney/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/usermoney/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/usermoney/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/usermoney/findPage',
    method: 'post',
    data
  })
}
// 分页查询
export const findMonthPage = (data) => {
  return axios({
    url: '/usermoney/findMonthPage',
    method: 'post',
    data
  })
}
// 查询明细汇总
export const findChange = (data) => {
  return axios({
    url: '/usermoney/findChange',
    method: 'post',
    data
  })
}
export const getOne = (data) => {
  return axios({
    url: '/usermoney/getById',
    method: 'post',
    data
  })
}
