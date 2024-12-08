package com.hml.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hml.bean.DataSource;
import com.hml.config.BotConfig;
import com.hml.redis.RedisHqKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HqTaskManager {

	private static Map<String,DataSource> dataMap = new HashMap<String, DataSource>();
	
	public static DataSource getDraw(String type,RedisUtils redisUtils) {
		 DataSource item = dataMap.get(type);
		 if(item == null && redisUtils != null) {
			 Object obj = redisUtils.get(RedisHqKey.MODE_HQ_RESULT + type);
			 if(!StringUtils.isBlank(obj)) {
				 item = JSONObject.parseObject(obj.toString(), DataSource.class);
			 }
		 }
		 return item;
	}
	public static DataSource getDraw(String type) {
		 DataSource item = dataMap.get(type);
		 return item;
	}
	
	/*
	 * 获取数据源
	 */
	public static DataSource getDataSource(String mode,String key,JSONObject json) throws Exception {
		DataSource item = new DataSource();
		item.setId(json.getLong("dataId"));
		item.setBotId(BotConfig.CHAT_ID);
		item.setMode(mode);
		item.setDrawIssue(json.getLong("drawIssue"));
		item.setHash(json.getString("preDrawHash"));
		item.setSResult(json.getString("preDrawCode"));
//		item.setCode(result);
		item.setNextTime(json.getString("drawTime"));
		item.setSTime(json.getString("preDrawTime"));
		
		dataMap.put(key, item);
		return item;
	}
	
	public static DataSource copyDataSource(DataSource source,String mode,String key) throws Exception {
		DataSource item = new DataSource();
		item.setId(source.getId());
		item.setBotId(BotConfig.CHAT_ID);
		item.setMode(mode);
		item.setDrawIssue(source.getDrawIssue());
		
		 
		item.setHash(source.getHash());
		item.setSResult(source.getSResult());
		item.setNextTime(source.getNextTime());
		item.setSTime(source.getSTime());
		
		dataMap.put(key, item);
		return item;
	}
	   
}
