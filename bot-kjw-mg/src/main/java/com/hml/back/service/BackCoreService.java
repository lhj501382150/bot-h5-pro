package com.hml.back.service;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.config.BackCoreConfig;
import com.hml.mall.entity.Draw;
import com.hml.utils.HttpClientUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("backCoreService")
public class BackCoreService{
	
	public String addHqData(Draw data){
		String result = "ok";
		try {
			JSONObject req = new JSONObject();
			req.put("sysTime", data.getPreDrawTime());
			req.put("hashData", "0x" + data.getPreDrawHash());
			String ret = HttpClientUtils.doPost(BackCoreConfig.URL + BackCoreConfig.ADD_HQ, req.toJSONString());
			JSONObject json = JSONObject.parseObject(ret);
			if(!"0".equals(json.getString("iCode"))){
				throw new Exception(json.getString("sMsg"));
			}
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	} 
}
