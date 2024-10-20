import axios from '../../axios'

/*
 * 省市县模块
 */
// 查询
export const findList = (data) => {
  return axios({
    url: '/area/list',
    method: 'post',
    data
  })
}
