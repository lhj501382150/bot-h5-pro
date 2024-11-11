import axios from '../../axios'

/*
 * 数据服务
 */
// 折线查询
export const findLine = (data) => {
  return axios({
    url: '/apis/BlockChain/GetTradeNum',
    method: 'post',
    data
  })
}
// 开户注册
export const regist = (data) => {
  return axios({
    url: '/blockapis/601',
    method: 'post',
    data
  })
}

export const login = (data) => {
  return axios({
    url: '/blockapis/602',
    method: 'post',
    data
  })
}
// 查询余额
export const findBalance = (data) => {
  return axios({
    url: '/blockapis/603',
    method: 'post',
    data
  })
}
// 绑定合约
export const bindCont = (data) => {
  return axios({
    url: '/blockapis/604',
    method: 'post',
    data
  })
}
// 查询合约符号
export const contSymbol = (data) => {
  return axios({
    url: '/blockapis/605',
    method: 'post',
    data
  })
}
// 查询合约余额
export const contBalance = (data) => {
  return axios({
    url: '/blockapis/606',
    method: 'post',
    data
  })
}
export const token607 = (data) => {
  return axios({
    url: '/blockapis/607',
    method: 'post',
    data
  })
}
export const token608 = (data) => {
  return axios({
    url: '/blockapis/608',
    method: 'post',
    data
  })
}

export const block = (data) => {
  return axios({
    url: '/apis/BlockChain/GetTradeBlock',
    method: 'post',
    data
  })
}

export const trans = (data) => {
  return axios({
    url: '/apis/BlockChain/GetTradeTrans',
    method: 'post',
    data
  })
}

export const account = (data) => {
  return axios({
    url: '/blockapis/612',
    method: 'post',
    data
  })
}
export const accountTrans = (data) => {
  return axios({
    url: '/blockapis/609',
    method: 'post',
    data
  })
}
