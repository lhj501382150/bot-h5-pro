import axios from '../../axios'

/*
 * 订单模块
 */
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/hisOrder/findPage',
    method: 'post',
    data
  })
}


//  查询所有
export const findList = (data) => {
  return axios({
    url: '/hisOrder/list',
    method: 'post',
    data
  })
}
//  查询所有
export const findCount = (data) => {
  return axios({
    url: '/hisOrder/findCount',
    method: 'post',
    data
  })
}
//  查询所有
export const findFYCount = (data) => {
  return axios({
    url: '/hisOrder/findFYCount',
    method: 'post',
    data
  })
}

export const findLevelCount = (data) => {
  return axios({
    url: '/hisOrder/findLevelCount',
    method: 'post',
    data
  })
}

export const findScorePage = (data) => {
  return axios({
    url: '/hisOrder/findScorePage',
    method: 'post',
    data
  })
}

export const delData = (data) => {
  return axios({
    url: '/hisOrder/delete',
    method: 'post',
    data
  })
}

