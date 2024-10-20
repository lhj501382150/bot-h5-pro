import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/ware/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/ware/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/ware/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/ware/findPage',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/ware/list',
    method: 'post',
    data
  })
}

//  查询所有
export const getByNo = (data) => {
  return axios({
    url: '/ware/getById',
    method: 'post',
    data
  })
}
//  查询所有
export const findAttrlist = (data) => {
  return axios({
    url: '/ware/attr',
    method: 'post',
    data
  })
}
//  查询参数
export const findParalist = (data) => {
  return axios({
    url: '/ware/para',
    method: 'post',
    data
  })
}
