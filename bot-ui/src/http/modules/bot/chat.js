import axios from '../../axios'

/*
 * 机器人模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/chat/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/chat/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/chat/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/chat/findPage',
    method: 'post',
    data
  })
}
export const list = (data) => {
  return axios({
    url: '/chat/list',
    method: 'post',
    data
  })
}
export const pause = (data) => {
  return axios({
    url: '/chat/pause',
    method: 'post',
    data
  })
}
export const restart = (data) => {
  return axios({
    url: '/chat/restart',
    method: 'post',
    data
  })
}
export const down = (data) => {
  return axios({
    url: '/chat/down',
    method: 'post',
    data
  })
}

