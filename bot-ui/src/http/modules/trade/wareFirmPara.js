import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/wareFirmPara/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/wareFirmPara/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/wareFirmPara/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/wareFirmPara/findPage',
    method: 'post',
    data
  })
}

// 查询参数列表
export const findParalist = (data) => {
  return axios({
    url: '/wareFirmPara/para',
    method: 'post',
    data
  })
}
