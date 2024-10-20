import axios from '../../axios'

/*
 * 用户管理收货地址模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/usermoneyChange/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/usermoneyChange/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/usermoneyChange/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/usermoneyChange/findPage',
    method: 'post',
    data
  })
}
export const findScorePage = (data) => {
  return axios({
    url: '/usermoneyChange/findScorePage',
    method: 'post',
    data
  })
}
