package com.hml.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.service.BackCoreService;
import com.hml.bean.DataSource;
import com.hml.config.BotConfig;
import com.hml.redis.RedisHqKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.RandomStringGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HqTaskManager {

	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private BackCoreService backCoreService;
	
	private static Map<String,DataSource> dataMap = new HashMap<String, DataSource>();
	
	public static DataSource getDraw(String type) {
		return dataMap.get(type);
	}
	
	@Scheduled(fixedRate = 60 * 1000)
	public void gethxn1() {
		try {
			String key = "HXNN1";
			dataMap.remove(key);
			DataSource item = new DataSource();
			item.setId(getDrawId(key));
			item.setBotId(BotConfig.CHAT_ID);
			item.setMode("1");
			item.setDrawIssue(getDrawIssue(key));
			
			String result = getResult(key);
			String hashStr = RandomStringGenerator.getHashStr();
			if("".equals(result)){
				result = RandomStringGenerator.getResult(hashStr);
			}else {
				hashStr = RandomStringGenerator.replaceNum(hashStr, result);
			}
			item.setHash(hashStr);
			item.setSResult(result);
			item.setSTime(DateTimeUtils.addMinute(1));
			
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			
			dataMap.put(key, item);
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(fixedRate = 3 * 60 * 1000)
	public void gethxn3() {
		
	}
	
	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void gethxn5() {
		
	}
	
	private long getDrawId(String key) {
		key = RedisHqKey.DRAW_ID + key;
		long id = redisUtils.incr(key, 1);
		return id;
	}
	
	private long getDrawIssue(String key) {
		String currentDate = DateTimeUtils.getCurrentDate("yyMMdd");
		key = RedisHqKey.DRAW_ISSUE + key + ":" + currentDate;
		Object obj = redisUtils.get(key);
		String val = "";
		if(obj == null) {
			val = currentDate + "0000";
		}else {
			val = obj.toString();
		}
		long issue = Long.valueOf(val) + 1;
		redisUtils.set(key, String.valueOf(issue),60 * 60 * 24);
		return issue;
	}
	
	private String getResult(String key) {
		String redisKey = RedisHqKey.DRAW_RESULT + key;
		Object obj = redisUtils.get(redisKey);
		String result = "";
		if(obj != null) {
			result = obj.toString();
			redisUtils.del(redisKey);
		}
		return result;
	}
}
