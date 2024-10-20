import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
  return axios({
    url: '/wareApply/save',
    method: 'post',
    data
  })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/wareApply/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
  return axios({
    url: '/wareApply/delete',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/wareApply/findPage',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/wareApply/list',
    method: 'post',
    data
  })
}

//  查询所有
export const getByNo = (data) => {
  return axios({
    url: '/wareApply/getById',
    method: 'post',
    data
  })
}
//  查询属性
export const findAttrlist = (data) => {
  return axios({
    url: '/wareApply/attr',
    method: 'post',
    data
  })
}

// 查询待审核列表
export const findAuditPage = (data) => {
  return axios({
    url: '/wareApply/findAuditPage',
    method: 'post',
    data
  })
}

// 查询审核列表
export const findListPage = (data) => {
  return axios({
    url: '/wareApply/findListPage',
    method: 'post',
    data
  })
}

// 提交申请
export const apply = (data) => {
  return axios({
    url: '/wareApply/apply',
    method: 'post',
    data
  })
}

// 审核
export const audit = (data) => {
  return axios({
    url: '/wareApply/audit',
    method: 'post',
    data
  })
}

// 驳回
export const refuse = (data) => {
  return axios({
    url: '/wareApply/refuse',
    method: 'post',
    data
  })
}
