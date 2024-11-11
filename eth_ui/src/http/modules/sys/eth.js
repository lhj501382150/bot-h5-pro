import axios from '../../axios'

// 返回客户端持有地址列表
export const accounts = () => {
  let id = '1001';
  return post('eth_accounts',[],id)
}
//返回指定地址发生的交易数量
export const getTransactionCount = (addr) =>{
  let id = '1002';
  let para = [
    addr,'latest'
  ]
  return post('eth_getTransactionCount',para,id)
}
//返回指定块发生的交易数量 块哈希
export const getBlockTransactionCountByHash = (addr) =>{
  let id = '1003';
  let para = [
    addr
  ]
  return post('eth_getBlockTransactionCountByHash',para,id)
}
//返回指定块发生的交易数量 块编号
export const getBlockTransactionCountByNumber = (addr) =>{
  let id = '1004';
  let para = [
    addr
  ]
  return post('eth_getBlockTransactionCountByNumber',para,id)
}

//获取指定地址处的代码
export const getCode = (addr,blockNum) =>{
  let id = '1005';
  let para = [
    addr,blockNum
  ]
  return post('eth_getCode',para,id)
}

// 返回具有指定哈希的块
/*
number: QUANTITY - 块编号，挂起块为null
hash: DATA, 32 Bytes - 块哈希，挂起块为null
parentHash: DATA, 32 Bytes - 父块的哈希
nonce: DATA, 8 Bytes - 生成的pow哈希，挂起块为null
sha3Uncles: DATA, 32 Bytes - 块中叔伯数据的SHA3哈希
logsBloom: DATA, 256 Bytes - 快日志的bloom过滤器，挂起块为null
transactionsRoot: DATA, 32 Bytes - 块中的交易树根节点
stateRoot: DATA, 32 Bytes - 块最终状态树的根节点
receiptsRoot: DATA, 32 Bytes - 块交易收据树的根节点
miner: DATA, 20 Bytes - 挖矿奖励的接收账户
difficulty: QUANTITY - 块难度，整数
totalDifficulty: QUANTITY - 截止到本块的链上总难度
extraData: DATA - 块额外数据
size: QUANTITY - 本块字节数
gasLimit: QUANTITY - 本块允许的最大gas用量
gasUsed: QUANTITY - 本块中所有交易使用的总gas用量
timestamp: QUANTITY - 块时间戳
transactions: Array - 交易对象数组，或32字节长的交易哈希数组
uncles: Array - 叔伯哈希数组
     "number": "0x1b4", // 436
    "hash": "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331",
    "parentHash": "0x9646252be9520f6e71339a8df9c55e4d7619deeb018d2a3f2d21fc165dde5eb5",
    "nonce": "0xe04d296d2460cfb8472af2c5fd05b5a214109c25688d3704aed5484f9a7792f2",
    "sha3Uncles": "0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347",
    "logsBloom": "0xe670ec64341771606e55d6b4ca35a1a6b75ee3d5145a99d05921026d1527331",
    "transactionsRoot": "0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421",
    "stateRoot": "0xd5855eb08b3387c0af375e9cdb6acfc05eb8f519e419b874b6ff2ffda7ed1dff",
    "miner": "0x4e65fda2159562a496f9f3522f89122a3088497a",
    "difficulty": "0x027f07", // 163591
    "totalDifficulty":  "0x027f07", // 163591
    "extraData": "0x0000000000000000000000000000000000000000000000000000000000000000",
    "size":  "0x027f07", // 163591
    "gasLimit": "0x9f759", // 653145
    "gasUsed": "0x9f759", // 653145
    "timestamp": "0x54e34e8e" // 1424182926
    "transactions": [{...},{ ... }]
    "uncles": ["0x1606e5...", "0xd5145a9..."]
*/
export const getBlockByHash = (addr) =>{
  let id = '1006';
  let para = [
    addr,true
  ]
  return post('eth_getBlockByHash',para,id)
}

// 返回具有指定哈希的块
export const getBlockByNumber = (addr) =>{
  let id = '1007';
  let para = [
    addr,true
  ]
  return post('eth_getBlockByNumber',para,id)
}
// 返回指定哈希对应的交易
/*
hash: DATA, 32字节 - 交易哈希
nonce: QUANTITY - 本次交易之前发送方已经生成的交易数量
blockHash: DATA, 32字节 - 交易所在块的哈希，对于挂起块，该值为null
blockNumber: QUANTITY - 交易所在块的编号，对于挂起块，该值为null
transactionIndex: QUANTITY - 交易在块中的索引位置，挂起块该值为null
from: DATA, 20字节 - 交易发送方地址
to: DATA, 20字节 - 交易接收方地址，对于合约创建交易，该值为null
value: QUANTITY - 发送的以太数量，单位：wei
gasPrice: QUANTITY - 发送方提供的gas价格，单位：wei
gas: QUANTITY - 发送方提供的gas可用量
input: DATA - 随交易发送的数据
  "hash":"0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b",
    "nonce":"0x",
    "blockHash": "0xbeab0aa2411b7ab17f30a99d3cb9c6ef2fc5426d6ad6fd9e2a26a6aed1d1055b",
    "blockNumber": "0x15df", // 5599
    "transactionIndex":  "0x1", // 1
    "from":"0x407d73d8a49eeb85d32cf465507dd71d507100c1",
    "to":"0x85h43d8a49eeb85d32cf465507dd71d507100c1",
    "value":"0x7f110", // 520464
    "gas": "0x7f110", // 520464
    "gasPrice":"0x09184e72a000",
    "input":"0x603880600c6000396000f300603880600c6000396000f3603880600c6000396000f360",
*/
export const getTransactionByHash = (addr) =>{
  let id = '1008';
  let para = [
    addr
  ]
  return post('eth_getTransactionByHash',para,id)
}
//返回指定块内具有指定索引序号的交易
export const getTransactionByBlockHashAndIndex = (addr,index) =>{
  let id = '1009';
  let para = [
    addr,index
  ]
  return post('eth_getTransactionByBlockHashAndIndex',para,id)
}
//返回指定块内具有指定索引序号的交易
export const getTransactionByBlockNumberAndIndex = (addr,index) =>{
  let id = '1010';
  let para = [
    addr,index
  ]
  return post('eth_getTransactionByBlockNumberAndIndex',para,id)
}

//返回指定交易的收据，使用哈希指定交易
/*
transactionHash: DATA, 32字节 - 交易哈希
transactionIndex: QUANTITY - 交易在块内的索引序号
blockHash: DATA, 32字节 - 交易所在块的哈希
blockNumber: QUANTITY - 交易所在块的编号
from: DATA, 20字节 - 交易发送方地址
to: DATA, 20字节 - 交易接收方地址，对于合约创建交易该值为null
cumulativeGasUsed: QUANTITY - 交易所在块消耗的gas总量
gasUsed: QUANTITY - 该次交易消耗的gas用量
contractAddress: DATA, 20字节 - 对于合约创建交易，该值为新创建的合约地址，否则为null
logs: Array - 本次交易生成的日志对象数组
logsBloom: DATA, 256字节 - bloom过滤器，轻客户端用来快速提取相关日志
       transactionHash: '0xb903239f8543d04b5dc1ba6579132b143087c68db1b2168786408fcbce568238',
     transactionIndex:  '0x1', // 1
     blockNumber: '0xb', // 11
     blockHash: '0xc6ef2fc5426d6ad6fd9e2a26abeab0aa2411b7ab17f30a99d3cb96aed1d1055b',
     cumulativeGasUsed: '0x33bc', // 13244
     gasUsed: '0x4dc', // 1244
     contractAddress: '0xb60e8dd61c5d32be8058bb8eb970870f07233155', // or null, if none was created
     logs: [{
         // logs as returned by getFilterLogs, etc.
     }, ...],
     logsBloom: "0x00...0", // 256 byte bloom filter
     status: '0x1'
*/
export const getTransactionReceipt = (addr) =>{
  let id = '1011';
  let para = [
    addr
  ]
  return post('eth_getTransactionReceipt',para,id)
}

//查询最新块编号
export const blockNumber = () => {
  let id = '1012';
  return post('eth_blockNumber',[],id)
}

// 获取客户端版本
export const web3_clientVersion = (data) => {
  let id = '3001';
  return post('web3_clientVersion',data,id)
}
//查询连接节点数量
export const net_peerCount = () => {
  let id = '2001';
  return post('net_peerCount',[],id)
}

//监听网络连接状态
export const net_listening = () => {
  let id = '2002';
  return post('net_listening',[],id)
}

/*
 * eth API接口管理
 */
export const post = (method,para,id) =>{
  if(!para){
    para = []
  }
  let data = {
    "jsonrpc":"2.0",
    "method":method,
    "params":para,
    "id":id
  }
  return axios({
    url: '/eth',
    method: 'post',
    data
})
}



