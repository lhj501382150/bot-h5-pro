import axios from '../../axios'

/*
 * 订单模块
 */
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/playOrder/findPage',
    method: 'post',
    data
  })
}


//  订单完成
export const edit = (data) => {
  return axios({
    url: '/playOrder/edit',
    method: 'post',
    data
  })
}
//  订单作废
export const del = (data) => {
  return axios({
    url: '/playOrder/delete',
    method: 'post',
    data
  })
}

