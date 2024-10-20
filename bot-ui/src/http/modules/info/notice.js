import axios from '../../axios'

/*
 * 公告管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/notice/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/notice/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/notice/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/notice/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const findList = (data) => {
  return axios({
    url: '/notice/list',
    method: 'post',
    data
  })
}

export const getContent = (data) => {
  return axios({
    url: '/notice/getContent',
    method: 'post',
    data
  })
}
