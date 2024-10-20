package com.hml.backcore.utils;
/*
 * 账户资金服务
 */

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.HttpClientUtils;

public class ZbUtils {
	
	public static JSONObject inMoney(String userno,String userName,BigDecimal money)throws Exception{
		JSONObject  obj = new JSONObject();
		obj.put("userId",userno);
		obj.put("userName",userName);
		obj.put("inMoney",money);
		String url = BackCoreConfig.URL + BackCoreConfig.IN_MONEY;
		String res = HttpClientUtils.doPost(url, obj.toJSONString());
		JSONObject retObj = (JSONObject)JSONObject.parse(res);
		if(!"0".equals(retObj.get("iCode").toString())) {
			throw new Exception(retObj.getString("sMsg"));
		}
		return retObj;
	}
	public static JSONObject outMoney(String userno,String userName,BigDecimal money)throws Exception{
		JSONObject  obj = new JSONObject();
		obj.put("userId",userno);
		obj.put("userName",userName);
		obj.put("outMoney",money);
		String url = BackCoreConfig.URL + BackCoreConfig.OUT_MONEY;
		String res = HttpClientUtils.doPost(url, obj.toJSONString());
		JSONObject retObj = (JSONObject)JSONObject.parse(res);
		if(!"0".equals(retObj.get("iCode").toString())) {
			throw new Exception(retObj.getString("sMsg"));
		}
		return retObj;
	}
	/**
	 * 账本金额手动调整
	 * @param userno  登陆编号
	 * @param acctno 账本编号
	 * @param subno 科目编号
	 * @param money 变动金额
	 * @param contno 订单号
	 * @param memo 备注
	 * @return
	 * @throws Exception
	 */
	public static JSONObject changeMoney(String userno,String actno,String subno,String money,String contno,String memo,String msubno)throws Exception{
		JSONObject  obj = new JSONObject();
		obj.put("USERNO",userno);
		obj.put("DETYPE","4");
		obj.put("ACCTNO",actno);
		obj.put("SUBJECTNO",subno);
		obj.put("SONNO",msubno);
		obj.put("CHMONEY",money);
		obj.put("CONTNO",contno);
		obj.put("PAYTYPE","A");
		obj.put("PAYCHAN","");
		obj.put("MEMO",memo);
		String url = BackCoreConfig.URL + BackCoreConfig.MONEY_CHANGE;
		String res = HttpClientUtils.doPost(url, obj.toJSONString());
		JSONObject retObj = (JSONObject)JSONObject.parse(res);
		if(!"0".equals(retObj.get("ERRNO").toString())) {
			throw new Exception(retObj.getString("MSG"));
		}
		return retObj;
	}
	/**
	 * 账本金额手动调整
	 * @param userno  登陆编号
	 * @param acctno 账本编号
	 * @param subno 科目编号
	 * @param money 变动金额
	 * @param contno 订单号
	 * @param memo 备注
	 * @return
	 * @throws Exception
	 */
	public static JSONObject changeMoney(String userno,String actno,String subno,String money,String contno,String memo)throws Exception{
		JSONObject  obj = new JSONObject();
		obj.put("USERNO",userno);
		obj.put("DETYPE","4");
		obj.put("ACCTNO",actno);
		obj.put("SUBJECTNO",subno);
		obj.put("CHMONEY",money);
		obj.put("CONTNO",contno);
		obj.put("PAYTYPE","A");
		obj.put("PAYCHAN","");
		obj.put("MEMO",memo);
		String url = BackCoreConfig.URL + BackCoreConfig.MONEY_CHANGE;
		String res = HttpClientUtils.doPost(url, obj.toJSONString());
		JSONObject retObj = (JSONObject)JSONObject.parse(res);
		if(!"0".equals(retObj.get("ERRNO").toString())) {
			throw new Exception(retObj.getString("MSG"));
		}
		return retObj;
	}
	/**
	 * 陪玩订单
	 * @param userno
	 * @param dev
	 * @param water
	 * @param type 1 完成 2 作废
	 * @return
	 * @throws Exception
	 */
	public static JSONObject confirmPlayOrder(String userno,String water,String type)throws Exception{
		JSONObject  obj = new JSONObject();
		obj.put("USERNO",userno);
		obj.put("DETYPE","4");
		obj.put("WATERNO",water);
		obj.put("OPTYPE",type);
		String url = BackCoreConfig.URL + BackCoreConfig.PLAY_ORDER;
		String res = HttpClientUtils.doPost(url, obj.toJSONString());
		JSONObject retObj = (JSONObject)JSONObject.parse(res);
		if(!"0".equals(retObj.get("ERRNO").toString())) {
			throw new Exception(retObj.getString("MSG"));
		}
		return retObj;
	}
	
	/**
	 * 赛事活动审核
	 * @param userno
	 * @param dev
	 * @param water
	 * @return
	 * @throws Exception
	 */
	public static JSONObject activtyAudit(Long actno)throws Exception{
		JSONObject  obj = new JSONObject();
		obj.put("ACTNO",String.valueOf(actno));
		obj.put("DETYPE","4");
		obj.put("USERNO",SecurityUtils.getUsername());
		String url = BackCoreConfig.URL + BackCoreConfig.ACTIVITY_AUDIT;
		String res = HttpClientUtils.doPost(url, obj.toJSONString());
		JSONObject retObj = (JSONObject)JSONObject.parse(res);
		if(!"0".equals(retObj.get("ERRNO").toString())) {
			throw new Exception(retObj.getString("MSG"));
		}
		return retObj;
	}
	/**
	 * 后端通信请求
	 * @param url
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static JSONObject post(String url,Object obj)throws Exception{
		url = BackCoreConfig.URL + url;
		String res = HttpClientUtils.doPost(url, JSONObject.toJSONString(obj));
		JSONObject retObj = (JSONObject)JSONObject.parse(res);
		if(!"0".equals(retObj.get("ERRNO").toString())) {
			throw new Exception(retObj.getString("MSG"));
		}
		return retObj;
	}
}
