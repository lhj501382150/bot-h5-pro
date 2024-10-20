import axios from '../../axios'

/*
 * 用户管理收货地址模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/userCard/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/userCard/edit',
    method: 'post',
    data
  })
}
// 鉴权
export const auth = (data) => {
  return axios({
    url: '/userCard/auth',
    method: 'post',
    data
  })
}
// 驳回签约
export const reject = (data) => {
  return axios({
    url: '/userCard/reject',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/userCard/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/userCard/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const findList = (data) => {
  return axios({
    url: '/userCard/list',
    method: 'post',
    data
  })
}

