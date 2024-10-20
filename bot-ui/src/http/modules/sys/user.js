import axios from '../../axios'

/*
 * 用户管理模块
 */

// 保存
export const save = (data) => {
    return axios({
        url: '/loginUser/save',
        method: 'post',
        data
    })
}
// 修改
export const edit = (data) => {
  return axios({
    url: '/loginUser/edit',
    method: 'post',
    data
  })
}
// 删除
export const batchDelete = (data) => {
    return axios({
        url: '/loginUser/delete',
        method: 'post',
        data
    })
}
// 分页查询
export const findPage = (data) => {
    return axios({
        url: '/loginUser/findPage',
        method: 'post',
        data
    })
}
// 导出Excel用户信息
export const exportUserExcelFile = (data) => {
    return axios({
        url: '/loginUser/exportUserExcelFile',
        method: 'post',
        data
    })
}
// 查找用户的菜单权限标识集合
export const findPermissions = (params) => {
    return axios({
        url: '/loginUser/findPermissions',
        method: 'get',
        params
    })
}
// 根据用户名查找
export const findByName = (params) => {
    return axios({
        url: '/loginUser/findByName',
        method: 'get',
        params
    })
}
// 更新用户密码
export const updatePassword = (params) => {
    return axios({
        url: '/loginUser/updatePassword',
        method: 'post',
        params
    })
}
// 更新用户密码
export const initPassword = (params) => {
    return axios({
        url: '/loginUser/initPassword',
        method: 'post',
        params
    })
}
