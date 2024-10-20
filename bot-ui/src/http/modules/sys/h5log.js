import axios from '../../axios'

/*
 * 操作日志模块
 */

// 删除
export const batchDelete = (data) => {
    return axios({
        url: '/h5Log/delete',
        method: 'post',
        data
    })
}
// 分页查询
export const findPage = (data) => {
    return axios({
        url: '/h5Log/findPage',
        method: 'post',
        data
    })
}
