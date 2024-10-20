import axios from '../../axios'

/*
 * 土地资产管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/userLandinfo/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/userLandinfo/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/userLandinfo/delete',
    method: 'post',
    data
  })
}
// 发行
export const issue = (data) => {
  return axios({
    url: '/userLandinfo/issue',
    method: 'post',
    data
  })
}
// 终止
export const termination = (data) => {
  return axios({
    url: '/userLandinfo/termination',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/userLandinfo/findPage',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/userLandinfo/list',
    method: 'post',
    data
  })
}

//  查询所有
export const findAttrList = (data) => {
  return axios({
    url: '/userLandinfo/attrlist',
    method: 'post',
    data
  })
}
