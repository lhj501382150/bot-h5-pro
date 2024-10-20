import axios from '../../axios'

/*
 * 商品在线管控模块
 */

// 修改
export const edit = (data) => {
  return axios({
    url: '/wareMem/edit',
    method: 'post',
    data
  })
}
// 分页查询
export const findPage = (data) => {
  return axios({
    url: '/wareMem/findPage',
    method: 'post',
    data
  })
}
