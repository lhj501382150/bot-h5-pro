import axios from '../../axios'

/*
 * 分享奖励参数设置
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/growParas/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/growParas/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/growParas/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/growParas/findPage',
    method: 'post',
    data
  })
}

// 分页查询
export const findList = (data) => {
  return axios({
    url: '/growParas/list',
    method: 'post',
    data
  })
}
