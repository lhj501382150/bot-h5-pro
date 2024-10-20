import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/user/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/user/edit',
    method: 'post',
    data
  })
}
export const editPwd = (data) => {
  return axios({
    url: '/user/editPwd',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/user/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/user/findPage',
    method: 'post',
    data
  })
}
// 分页查询
export const findList = (data) => {
  return axios({
    url: '/user/list',
    method: 'post',
    data
  })
}
export const findOrgNum = (data) => {
  return axios({
    url: '/user/findOrgNum',
    method: 'post',
    data
  })
}
//查询层级信息
export const findParent = (data) => {
  return axios({
    url: '/user/findParent',
    method: 'post',
    data
  })
}
export const getById = (data) => {
  return axios({
    url: '/user/getById',
    method: 'post',
    data
  })
}
export const changeState = (data) => {
  return axios({
    url: '/user/editStatus',
    method: 'post',
    data
  })
}
export const addMoney = (data) => {
  return axios({
    url: '/user/addMoney',
    method: 'post',
    data
  })
}
export const subMoney = (data) => {
  return axios({
    url: '/user/subMoney',
    method: 'post',
    data
  })
}
export const clean = (data) => {
  return axios({
    url: '/user/clean',
    method: 'post',
    data
  })
}
