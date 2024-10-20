import axios from '../../axios'

/*
 * 排行榜管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/ranklist/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/ranklist/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/ranklist/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/ranklist/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const findList = (data) => {
  return axios({
    url: '/ranklist/list',
    method: 'post',
    data
  })
}
