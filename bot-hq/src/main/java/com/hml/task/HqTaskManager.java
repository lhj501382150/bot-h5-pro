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
		 DataSource item = dataMap.get(type);
		 if(item == null) {
			 item =new DataSource();
		 }
		 return item;
	}
	//模式编号  0、普通 1、牛牛  2、普通1分 3、普通3分 4、普通5分  5、牛牛1分   6、牛牛3分  7、牛牛5分
	@Scheduled(fixedRate = 60 * 1000)
	public void gethxn1() {//5、牛牛1分
		try {
			String key = "HXNN1";
			dataMap.remove(key);
			
			DataSource item = getDataSource("5", key, 1);
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(fixedRate = 3 * 60 * 1000)
	public void gethxn3() {
		try {
			String key = "HXNN3";
			dataMap.remove(key);
			
			DataSource item = getDataSource("6", key, 3);
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void gethxn5() {
		try {
			String key = "HXNN5";
			dataMap.remove(key);
			
			DataSource item = getDataSource("7", key, 7);
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(fixedRate = 60 * 1000)
	public void gethxd1() {//5、牛牛1分
		try {
			String key = "HXBD1";
			dataMap.remove(key);
			
			DataSource item = getDataSource("2", key, 1);
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(fixedRate = 3 * 60 * 1000)
	public void gethbd3() {
		try {
			String key = "HXBD3";
			dataMap.remove(key);
			
			DataSource item = getDataSource("3", key, 3);
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void gethxd5() {
		try {
			String key = "HXBD5";
			dataMap.remove(key);
			
			DataSource item = getDataSource("4", key, 7);
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	private long getDrawId(String key) {
		key = RedisHqKey.DRAW_ID + key;
		long id = redisUtils.incr(key, 1);
		return id;
	}
	/*
	 * 获取开奖号
	 */
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
	/*
	 * 获取Redis数据
	 */
	private String getRedisResult(String key) {
		String redisKey = RedisHqKey.DRAW_RESULT + key;
		Object obj = redisUtils.get(redisKey);
		String result = "";
		if(obj != null) {
			result = obj.toString();
			redisUtils.del(redisKey);
		}
		return result;
	}
	/*
	 * 获取数据源
	 */
	private DataSource getDataSource(String mode,String key,int minutes) throws Exception {
		DataSource item = new DataSource();
		item.setId(getDrawId(key));
		item.setBotId(BotConfig.CHAT_ID);
		item.setMode(mode);
		item.setDrawIssue(getDrawIssue(key));
		
		String result = getRedisResult(key);
		String hashStr = RandomStringGenerator.getHashStr();
		if("".equals(result)){
			result = RandomStringGenerator.getResult(hashStr);
		}else {
			hashStr = RandomStringGenerator.replaceNum(hashStr, result);
		}
		item.setHash(hashStr);
		item.setSResult(result);
//		item.setCode(result);
		item.setSTime(DateTimeUtils.addMinute(minutes));
		
		return item;
	}
}