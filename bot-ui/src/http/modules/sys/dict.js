import axios from '../../axios'

/*
 * 数据字典
 */
// 查询
export const findList = (data) => {
  return axios({
    url: '/dict/list',
    method: 'post',
    data:{dictno:data}
  })
}
