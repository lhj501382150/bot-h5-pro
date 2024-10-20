import axios from '../../axios'

/*
 * 交易员模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/trader/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/trader/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/trader/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/trader/findPage',
    method: 'post',
    data
  })
}

