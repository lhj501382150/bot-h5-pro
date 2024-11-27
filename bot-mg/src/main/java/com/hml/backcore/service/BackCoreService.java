package com.hml.backcore.service;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hml.backcore.config.BackCoreConfig;
import com.hml.utils.HttpClientUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("backCoreService")
public class BackCoreService {
	
	 
	public String addGroupId(JSONObject data){
		String result = "ok";
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.ADD_GROUPID, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	  
	public String addManaData(JSONObject data){
		String result = "ok";
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.ADD_MANA_DATA, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	public String downRob(JSONObject data){
		String result = "ok";
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.DOWN_ROB, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	public JSONArray getOrderDraw(JSONObject data){
		JSONArray arr = new JSONArray();
		try {
			String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.GET_ORDER_RESULT, data.toJSONString(),null);
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
			arr = json.getJSONArray("rData");
		} catch (Exception e) {
			log.error("请求下单异常：",e);
		}
		return arr;
	}
}
