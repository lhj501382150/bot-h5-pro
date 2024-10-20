package com.hml.heepay.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hml.heepay.bean.DataHelper;
import com.hml.heepay.bean.HeepayXMLReturn;
import com.hml.heepay.bean.ThreeDES;
import com.hml.heepay.config.HeePayConfig;
import com.hml.heepay.config.HeePayMethod;
import com.hml.heepay.service.IHeePayService;
import com.hml.mall.entity.order.PayOrder;
import com.hml.mall.util.PasswordEncoder;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.Des;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.IdWorker;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service("heePayService")
@Slf4j
public class HeePayServiceImpl implements IHeePayService {

	 
	
	@Autowired
	private HeePayConfig config;
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void auth(String detail_data) throws Exception {
		Map<String,String> para = new HashMap<String, String>();
		para.put("version","1");
		para.put("agent_id",config.getAgent_id());
		para.put("detail_data", detail_data);
		String str = getAuthSignData(para);
		log.info("【鉴权】--请求参数为:{}",str);
		
		String sign = new PasswordEncoder("").encode(str).toLowerCase();
		para.put("sign", sign);
		log.info("【3DES】加密数据为：{}",detail_data);
//		 detail_data = Des.Encrypt3Des(detail_data, config.getDeskey(),"ToHex16");
		 detail_data = ThreeDES.encryptMode(config.getJqdeskey(),detail_data);
		 log.info("【3DES】加密后结果为：" + detail_data);
		 para.put("detail_data", detail_data);
		
//		String ret = HttpClientUtils.doPostStr(HeePayMethod.JQ_URL, para,"UTF-8");
//		 https://www.heepay.com/API/Merchant/BankCeritfy.aspx?version=1&agent_id=1664502&detail_data=0C587A6308554643E9652C7150B2E0C9098AC14DC809E218846703F68BC752AC283ED8A4F10F4236C840344784003F19884F0B5A554B1B6530F7FAD2786183004FE093CB41EC0B95BE6B64C3C7306DF87C68316FAB2662DC591F5BD150A26243&sign=d9***4f
		 String url = HeePayMethod.JQ_URL + "?version=1&agent_id="+para.get("agent_id")
		 			+ "&detail_data=" + detail_data 
		 			+ "&sign=" + sign;
		 String ret = HttpClientUtils.doGet(url);
		log.info("【鉴权】--返回结果为：{}",ret);
		JSONObject res = JSONObject.parseObject(ret);
		if(!"0000".equals(res.getString("ret_code"))){
			throw new Exception("鉴权失败：" + res.getString("ret_msg"));
		}
		if(!StringUtils.isBlank(res.getString("detail_data"))){
			throw new Exception("鉴权失败：" + res.getString("detail_data"));
		}
	}
	  
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void pay(Map<String,String> dataMap)throws Exception{
		 
		Map<String,String> para = new HashMap<String, String>();
		para.put("version","3");
		para.put("agent_id",config.getAgent_id());
		para.put("batch_no",IdWorker.getNextId());
		para.put("notify_url", config.getNotifyUrl());
		para.put("ext_param1", IdWorker.getNextId());
		para.put("sign_type", "MD5");
		para.putAll(dataMap);
		
		String str = getSignData(para);
		log.info("发送短信--请求参数为:{}",str);
		
		String sign = new PasswordEncoder("").encode(str);
		para.put("sign", sign);
		String detail_data = para.get("detail_data");
//		detail_data = new String(detail_data.getBytes("ISO8859-1"),"GBK");
		log.info("【3DES】加密数据为：{}",detail_data);
		 detail_data = Des.Encrypt3Des(detail_data, config.getDeskey(),"ToHex16");
		 log.info("【3DES】加密后结果为：" + detail_data);
		 para.put("detail_data", detail_data);
		
		String ret = HttpClientUtils.doPostStr(config.getUrl() + HeePayMethod.PAY_URL, para,"GBK");
		log.info("发送短信--返回结果为：{}",ret);
		HeepayXMLReturn payheepReturn=DataHelper.GetRetuenXmlContent(ret);
		//是否成功.
		  boolean isSucess="0000".equals(payheepReturn.get_retcode());
		  if(!isSucess)
		  {			 
			  String msg = payheepReturn.get_retmsg();
			  msg = new String(msg.getBytes(),"UTF-8");
			  System.out.println(msg+"--------------");
			  throw new Exception("提交支付数据失败：" + msg);
		  }
		}
	 
		@Override
		public Map<String,String> queryStatus(PayOrder order) throws Exception {
			Map<String,String> para = new HashMap<String, String>();
			para.put("version","1");
			para.put("agent_id",config.getAgent_id());
			para.put("agent_bill_id",formatWater(order));
			para.put("agent_bill_time", DateTimeUtils.getCurrentDate("yyyyMMddHHmmss"));
			para.put("return_mode", "1");
			
			String sign = getQuerySign(para);
			log.info("【签名字符串为：】:{}",sign);
			sign = new PasswordEncoder("").encode(sign);
			para.put("sign", sign);
			
			String ret = HttpClientUtils.doPostStr(config.getQueryUrl(), para,"GBK");
			
			log.info("【查询返回结果为】:{}",ret);
			String[] paras = ret.split("\\|");
			Map<String,String> result = new HashMap<>();
			for(String p: paras){
				String[] t = p.split("=");
				if(t.length == 2){
					result.put(t[0], t[1]);
				}
			}
			return result;
		}
 
 
		private String getSignData(Map<String,String> para)throws Exception{
			StringBuffer buff = new StringBuffer();
			buff.append("agent_id=").append(para.get("agent_id")).append("&");
			buff.append("batch_amt=").append(para.get("batch_amt")).append("&");
			buff.append("batch_no=").append(para.get("batch_no")).append("&");
			buff.append("batch_num=").append(para.get("batch_num")).append("&");
			buff.append("detail_data=").append(para.get("detail_data")).append("&");
			buff.append("ext_param1=").append(para.get("ext_param1")).append("&");
			buff.append("key=").append(config.getMd5key()).append("&");
			buff.append("notify_url=").append(para.get("notify_url")).append("&");
			buff.append("version=3");
			return buff.toString().toLowerCase();
		}
		private String getAuthSignData(Map<String,String> para)throws Exception{
			StringBuffer buff = new StringBuffer();
			buff.append("agent_id=").append(para.get("agent_id")).append("&");
			buff.append("detail_data=").append(para.get("detail_data")).append("&");
			buff.append("key=").append(config.getJqmd5key()).append("&");
			buff.append("version=1");
			return buff.toString().toLowerCase();
		}
		/*
		 * sign=MD5(version=1&agent_id=1664502&agent_bill_id=2012220300056004&agent_bill_time=20201228143244&return_mode=1&key=651**************12)
		 */
		public String getQuerySign(Map<String,String> para)throws Exception{
			StringBuffer buff = new StringBuffer();
			buff.append("version=").append(para.get("version")).append("&");
			buff.append("agent_id=").append(para.get("agent_id")).append("&");
			buff.append("agent_bill_id=").append(para.get("agent_bill_id")).append("&");
			buff.append("agent_bill_time=").append(para.get("agent_bill_time")).append("&");
			buff.append("return_mode=").append(para.get("return_mode")).append("&");
			buff.append("key=").append(config.getQuerykey());
			
			String str = buff.toString();
			return str;
		}
		
		public String formatWater(PayOrder order) {
			String fdate = order.getFdate();
			fdate = fdate.replaceAll("-", "");
			return fdate + String.format("%08d", order.getWartno());
		}
}