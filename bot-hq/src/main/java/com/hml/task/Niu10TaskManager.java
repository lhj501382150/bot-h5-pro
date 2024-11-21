package com.hml.task;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.bean.RespBean;
import com.hml.config.BotConfig;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.StringUtils;
import com.hml.websocket.config.WebSocketConfig;
import com.hml.websocket.server.WebSocketServerNn10;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Niu10TaskManager {

	@Autowired
	private RedisUtils redisUtils;
	
	private String mode = "8";
	
	@Async
	@Scheduled(fixedRate = 1000)
	public void syncStatus() {
		 try {
			 Object obj = redisUtils.lGetAndPop(RedisKey.CURRENT_STATUS_MODE + mode);
			 if(StringUtils.isBlank(obj)) {
				 return;
			 }
			 log.info("【Redis-Niu信息】:{}",obj);
			 
			 RespBean resp = JSONObject.parseObject(obj.toString(), RespBean.class);
			 int step = resp.getIStatus();
			 int dataId = Integer.parseInt(resp.getDataId());
			 int maxId = DrawInfo.ID + 2;
			 int minId = DrawInfo.ID - 2;
			 if(dataId < minId || dataId > maxId) {
				 log.info("历史信息当前期数：{}--{}" ,DrawInfo.ID  ,resp.getDataId());
				 Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + mode);
					
				 log.info("【result_his_8】:{}",res);
				 if(res != null) {
					RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
					String key = getResultKey();
					redisUtils.hset(key,bean.getDataId(), res);
					redisUtils.expire(key, 60 * 60 * 24);
				 }
				 return;
			 }

			 if(Flow.START_ROB.getStep() == step) {
				 log.info("【START_ROB_8】：{}",step);
				 DrawInfo.FLOW = Flow.START_ROB;
				 if(BotConfig.ENABLE) start();
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", DrawInfo.DRAW_ISSUE);
					 json.put("CODE",DrawInfo.PRE_DRAW_CODE);
					 json.put("RESULT", DrawInfo.RESULT_NIU);
					 json.put("TIME", DrawInfo.DRAW_TIME);
					 json.put("ID", DrawInfo.ID);
					 WebSocketServerNn10.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.CONFIRM_ROB.getStep() == step) {
				 log.info("【CONFIRM_ROB_8】：{}",step);
			 }else if(Flow.DOWN_ORDER.getStep() == step) {
				 log.info("【DOWN_ORDER_8】：{}",step);
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", DrawInfo.DRAW_ISSUE);
					 json.put("CODE", DrawInfo.PRE_DRAW_CODE);
					 json.put("RESULT", DrawInfo.RESULT_NIU);
					 json.put("TIME", DrawInfo.DRAW_TIME);
					 json.put("ID", DrawInfo.ID);
					 WebSocketServerNn10.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.STOP_ORDER.getStep() == step) {
				 log.info("【STOP_ORDER_8】：{}",step);
				 stopOrder();
				 if(WebSocketConfig.ENABLE && SysTaskManager.IS_AUTH) {
					 WebSocketServerNn10.sendInfo(Flow.STOP_ORDER.getStep(),"");
				 }
			 }else if(Flow.OVER.getStep() == step) {
				 log.info("【OVER_8】：{}",step);
				 overResult();
			 }else if(Flow.TIPS.getStep() == step) {
				 log.info("【TIME TIPS_8】：{}",step);
			 }
			 
			 
		} catch (Exception e) {
			log.error("抢庄异常： " + e.getMessage());
			e.printStackTrace();
		}
	}	
	
	private void start() throws TelegramApiException {
		 
	}
	private void stopOrder() throws TelegramApiException {
//		 查询是否有庄
		boolean flag = true;
		int index = 0;
		while(flag  && index < 5) {
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + mode);
			  log.info("【QUERY_ORDER_8】:{}",res);
			  if(res != null) {
				  RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				  int dataId = Integer.parseInt(bean.getDataId());
				  int maxId = DrawInfo.ID + 2;
				  int minId = DrawInfo.ID - 2;
				 if(dataId < minId || dataId > maxId) {
					 log.info("历史信息当前期数：{}--{}" ,DrawInfo.ID ,bean.getDataId());
					 return;
				 }
				  flag = false;
			  }else {
				  index++;
			  }
		}
		  
	}
	private void overResult() throws TelegramApiException, IOException {
//		 查询是否有庄
		boolean flag = true;
		int index = 0;
		while(flag && index < 5) {
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + mode);
			
			log.info("【result_2】:{}",res);
			if(res != null) {
				RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				String key = getResultKey();
				redisUtils.hset(key,bean.getDataId(), res);
				redisUtils.expire(key, 60 * 60 * 24);
				log.info("存入结果:{}-{}",bean.getDataId(),res);
				 
				if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					DrawInfo.RESULT_NIU = String.valueOf(bean.getSReust());
				 }
			    flag = false;
			}else {
				index++;
			}
		}
		  
	}
	
	 
	
	private String getResultKey() {
		String date = DateTimeUtils.getCurrentDate("yyyyMMdd");
		String key = RedisKey.ORDER_RESULT_MODE + mode + ":" + date;
		return key;
	}
}
