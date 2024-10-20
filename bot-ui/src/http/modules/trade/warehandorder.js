import axios from '../../axios'

/*
 * 拍卖申请管理
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/wareHandorder/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/wareHandorder/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/wareHandorder/delete',
    method: 'post',
    data
  })
}
// 审核
export const audit = (data) => {
  return axios({
    url: '/wareHandorder/audit',
    method: 'post',
    data
  })
}
// 驳回
export const refuse = (data) => {
  return axios({
    url: '/wareHandorder/refuse',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/wareHandorder/findPage',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/wareHandorder/list',
    method: 'post',
    data
  })
}

//  查询所有
export const findAttrList = (data) => {
  return axios({
    url: '/wareHandorder/attrlist',
    method: 'post',
    data
  })
}
