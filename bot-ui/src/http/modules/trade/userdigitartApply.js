import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/userDigitartApply/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/userDigitartApply/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/userDigitartApply/delete',
    method: 'post',
    data
  })
}
// 审核
export const audit = (data) => {
  return axios({
    url: '/userDigitartApply/audit',
    method: 'post',
    data
  })
}
// 驳回
export const reject = (data) => {
  return axios({
    url: '/userDigitartApply/reject',
    method: 'post',
    data
  })
}

// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/userDigitartApply/findPage',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/userDigitartApply/list',
    method: 'post',
    data
  })
}
