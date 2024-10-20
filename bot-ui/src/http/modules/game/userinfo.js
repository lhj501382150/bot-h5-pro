import axios from '../../axios'

/*
 * 游戏账号管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/userinfo/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/userinfo/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/userinfo/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/userinfo/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const findList = (data) => {
  return axios({
    url: '/userinfo/list',
    method: 'post',
    data
  })
}
