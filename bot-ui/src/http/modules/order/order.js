import axios from '../../axios'

/*
 * 订单模块
 */
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/order/findPage',
    method: 'post',
    data
  })
}

export const edit = (data) => {
  return axios({
    url: '/order/edit',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/order/list',
    method: 'post',
    data
  })
}
//  查询所有
export const findCount = (data) => {
  return axios({
    url: '/order/findCount',
    method: 'post',
    data
  })
}
//  查询所有
export const findFYCount = (data) => {
  return axios({
    url: '/order/findFYCount',
    method: 'post',
    data
  })
}

export const findLevelCount = (data) => {
  return axios({
    url: '/order/findLevelCount',
    method: 'post',
    data
  })
}

