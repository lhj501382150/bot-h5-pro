import axios from '../../axios'

/*
 * 短信管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/forumRecv/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/forumRecv/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/forumRecv/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/forumRecv/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const findList = (data) => {
  return axios({
    url: '/forumRecv/list',
    method: 'post',
    data
  })
}
