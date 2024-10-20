import axios from '../../axios'

/*
 * 机器人模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/bot/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/bot/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/bot/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/bot/findPage',
    method: 'post',
    data
  })
}

