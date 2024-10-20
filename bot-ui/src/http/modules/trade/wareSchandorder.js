import axios from '../../axios'

/*
 * 拍卖申请管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/wareScHandorder/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/wareScHandorder/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/wareScHandorder/delete',
    method: 'post',
    data
  })
}
// 审核
export const audit = (data) => {
  return axios({
    url: '/wareScHandorder/audit',
    method: 'post',
    data
  })
}
// 驳回
export const refuse = (data) => {
  return axios({
    url: '/wareScHandorder/refuse',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/wareScHandorder/findPage',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/wareScHandorder/list',
    method: 'post',
    data
  })
}

//  查询所有
export const findAttrList = (data) => {
  return axios({
    url: '/wareScHandorder/attrlist',
    method: 'post',
    data
  })
}
