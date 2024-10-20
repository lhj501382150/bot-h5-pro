import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/userDigitart/save',
    method: 'post',
    data
  })
}
export const batchSave = (data) => {
  return axios({
    url: '/userDigitart/batchSave',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/userDigitart/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/userDigitart/delete',
    method: 'post',
    data
  })
}
// 发行
export const issue = (data) => {
  return axios({
    url: '/userDigitart/issue',
    method: 'post',
    data
  })
}
//批量发行
export const batchIssue = (data) => {
  return axios({
    url: '/userDigitart/batchIssue',
    method: 'post',
    data
  })
}
// 终止
export const termination = (data) => {
  return axios({
    url: '/userDigitart/termination',
    method: 'post',
    data
  })
}
export const lock = (data) => {
  return axios({
    url: '/userDigitart/lock',
    method: 'post',
    data
  })
}
export const unlock = (data) => {
  return axios({
    url: '/userDigitart/unlock',
    method: 'post',
    data
  })
}
// 手工上链
export const update = (data) => {
  return axios({
    url: '/userDigitart/update',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/userDigitart/findPage',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/userDigitart/list',
    method: 'post',
    data
  })
}

//  查询所有
export const findAttrList = (data) => {
  return axios({
    url: '/userDigitart/attrlist',
    method: 'post',
    data
  })
}
// 赠送
export const send = (data) => {
  return axios({
    url: '/userDigitart/send',
    method: 'post',
    data
  })
}
