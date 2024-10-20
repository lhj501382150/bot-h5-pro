import axios from '../../axios'

//前端图片管理
// 保存
export const save = (data) => {
  return axios({
    url: '/imageList/save',
    method: 'post',
    data
  })
}
// 保存
export const edit = (data) => {
  return axios({
    url: '/imageList/edit',
    method: 'post',
    data
  })
}
// 删除
export const del = (data) => {
  return axios({
    url: '/imageList/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/imageList/findPage',
    method: 'post',
    data
  })
}
