import axios from '../../axios'

/*
 * 分享业绩办理
 */

// 审核
export const audit = (data) => {
  return axios({
    url: '/shareHandle/audit',
    method: 'post',
    data
  })
}

// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/shareHandle/findPage',
    method: 'post',
    data
  })
}
