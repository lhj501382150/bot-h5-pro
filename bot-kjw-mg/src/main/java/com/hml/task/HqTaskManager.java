package com.hml.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hml.mall.entity.Draw;
import com.hml.mall.entity.DrawType;
import com.hml.mall.service.IDrawService;
import com.hml.mall.service.IDrawTypeService;
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
	private IDrawService drawService;
	
	@Autowired
	private IDrawTypeService drawTypeService;
	
	//模式编号  B1 B2 B3
	@Scheduled(cron="1 0/1 * * * ?")
	public void gethxd1() {//5、牛牛1分
		try {
			String key = "HXBD1";
			String mode = "B1";
			Draw item = getDataSource(mode, key);
			drawService.save(item);
			log.info("HQ-MODE2:{}",item);
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(cron="40 0/3 * * * ?")
	public void gethbd3() {
		try {
			String key = "HXBD3";
			String mode = "B2";
			Draw item = getDataSource(mode, key);
			drawService.save(item);
		} catch (Exception e) {
			log.error("生成行情数据异常：",e);
		}
	}
	
	@Scheduled(cron="40 4/5 * * * ?")
	public void gethxd5() {
		try {
			String key = "HXBD5";
			String mode = "B3";
			Draw item = getDataSource(mode, key);
			drawService.save(item);
			
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
	private String getDrawIssue(String key) {
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
		return val;
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
	private Draw getDataSource(String mode,String key) throws Exception {
		DrawType type = drawTypeService.getById(mode);
		int minutes = type.getTime();
		String redisKey = RedisHqKey.PRE_DRAW_ITEM + mode;
		
		Object obj = redisUtils.get(redisKey);
		Draw preDraw = null;
		if(obj == null) {
			preDraw = drawService.getLast(mode);
		}else {
			preDraw = JSONObject.parseObject(obj.toString(),Draw.class);
		}
		String result = getRedisResult(key);
		String hashStr = RandomStringGenerator.getHashStr();
		if("".equals(result)){
			result = RandomStringGenerator.getResult(hashStr);
		}else {
			hashStr = RandomStringGenerator.replaceNum(hashStr, result);
		}
		
		Draw item = new Draw();
		item.setDataId(getDrawId(key));
		item.setMode(mode);
		item.setDrawIssue(getDrawIssue(key));
		item.setDrawTime(DateTimeUtils.addMinute(minutes));
		
		
		if(preDraw != null) {
			item.setPreDrawIssue(preDraw.getDrawIssue());
			item.setPreDrawTime(preDraw.getDrawTime());
			item.setPreDrawDate(preDraw.getDrawTime().substring(0,10));
		}
		
		item.setPreDrawCode(result);
		item.setPreDrawHash(hashStr);
		
		try {
			redisUtils.set(redisKey, JSONObject.toJSONString(item));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}
	
	 
}
