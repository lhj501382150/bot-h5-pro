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
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HqTaskManager {

	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private BackCoreService backCoreService;
	
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
	//模式编号  0、普通 1、牛牛  2、普通1分 3、普通3分 4、普通5分  5、牛牛1分   6、牛牛3分  7、牛牛5分
//	@Scheduled(cron="10 0/1 * * * ?")
//	public void gethxn1() {//5、牛牛1分
//		try {
//			String key = "HXNN1";
//			dataMap.remove(key);
//			
//			DataSource item = getDataSource("5", key, 1);
//			JSONObject req = (JSONObject)JSONObject.toJSON(item);
//			backCoreService.addData(req);
//			dataMap.put(key, item);
//			
//			backCoreService.addHqData(item);
//		} catch (Exception e) {
//			log.error("生成行情数据异常：",e);
//		}
//	}
	
//	@Scheduled(cron="20 0/3 * * * ?")
//	public void gethxn3() {
//		try {
//			String key = "HXNN3";
//			dataMap.remove(key);
//			
//			DataSource item = getDataSource("6", key, 3);
//			JSONObject req = (JSONObject)JSONObject.toJSON(item);
//			backCoreService.addData(req);
//			dataMap.put(key, item);
//			
//			backCoreService.addHqData(item);
//		} catch (Exception e) {
//			log.error("生成行情数据异常：",e);
//		}
//	}
	
//	@Scheduled(cron="30 0/5 * * * ?")
//	public void gethxn5() {
//		try {
//			String key = "HXNN5";
//			dataMap.remove(key);
//			
//			DataSource item = getDataSource("7", key, 5);
//			JSONObject req = (JSONObject)JSONObject.toJSON(item);
//			backCoreService.addData(req);
//			dataMap.put(key, item);
//			
//			backCoreService.addHqData(item);
//		} catch (Exception e) {
//			log.error("生成行情数据异常：",e);
//		}
//	}
	
	@Scheduled(cron="3 0/1 * * * ?")
	public void gethxd1() {//5、牛牛1分
		try {
			String key = "HXBD1";
			dataMap.remove(key);
			
			DataSource item = getDataSource("2", key, 1);
			backCoreService.addHqData(item);
			
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
			key = "HXNN1";
			dataMap.remove(key);
			item = copyDataSource(item,"5", key, 1);
			req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			log.info("HQ-MODE2:{}",item);
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(cron="40 0/3 * * * ?")
	public void gethbd3() {
		try {
			String key = "HXBD3";
			dataMap.remove(key);
			
			DataSource item = getDataSource("3", key, 3);
			backCoreService.addHqData(item);
			
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
			key = "HXNN3";
			dataMap.remove(key);
			
			item = copyDataSource(item,"6", key, 3);
			req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(cron="40 4/5 * * * ?")
	public void gethxd5() {
		try {
			String key = "HXBD5";
			dataMap.remove(key);
			
			DataSource item = getDataSource("4", key, 5);
			backCoreService.addHqData(item);
			
			JSONObject req = (JSONObject)JSONObject.toJSON(item);
			backCoreService.addData(req);
			dataMap.put(key, item);
			
			key = "HXNN5";
			dataMap.remove(key);
			
			item = copyDataSource(item,"7", key, 5);
			req = (JSONObject)JSONObject.toJSON(item);
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
		long issue = Long.valueOf(val);
		redisUtils.set(key, String.valueOf(issue + 1),60 * 60 * 24);
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
		item.setNextTime(DateTimeUtils.addMinute(minutes));
		item.setSTime(DateTimeUtils.getDateTime());
		
		try {
			redisUtils.set(RedisHqKey.MODE_HQ_RESULT + key, JSONObject.toJSONString(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	
	private DataSource copyDataSource(DataSource source,String mode,String key,int minutes) throws Exception {
		DataSource item = new DataSource();
		item.setId(source.getId());
		item.setBotId(BotConfig.CHAT_ID);
		item.setMode(mode);
		item.setDrawIssue(source.getDrawIssue());
		
		 
		item.setHash(source.getHash());
		item.setSResult(source.getSResult());
		item.setNextTime(source.getNextTime());
		item.setSTime(source.getSTime());
		
		try {
			redisUtils.set(RedisHqKey.MODE_HQ_RESULT + key, JSONObject.toJSONString(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
}
