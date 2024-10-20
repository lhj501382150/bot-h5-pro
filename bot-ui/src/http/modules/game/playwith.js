import axios from '../../axios'

/*
 * 陪玩管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/playwith/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/playwith/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/playwith/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/playwith/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const findList = (data) => {
  return axios({
    url: '/playwith/list',
    method: 'post',
    data
  })
}
