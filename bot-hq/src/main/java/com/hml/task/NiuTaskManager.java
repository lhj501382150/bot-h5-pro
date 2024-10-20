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
import com.hml.websocket.server.WebSocketNiuServerApp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NiuTaskManager {

	@Autowired
	private RedisUtils redisUtils;
	
	@Async
	@Scheduled(fixedRate = 1000)
	public void syncStatus() {
		 try {
			 Object obj = redisUtils.lGetAndPop(RedisKey.CURRENT_STATUS_N);
			 if(StringUtils.isBlank(obj)) {
				 return;
			 }
			 log.info("【Redis-Niu信息】:{}",obj);
			 
			 RespBean resp = JSONObject.parseObject(obj.toString(), RespBean.class);
			 int step = resp.getIStatus();
			 int dataId = Integer.parseInt(resp.getDataId());
			 int maxId = NiuDrawInfo.ID + 2;
			 int minId = NiuDrawInfo.ID - 2;
			 if(dataId < minId || dataId > maxId) {
				 log.info("历史信息当前期数：{}--{}" ,NiuDrawInfo.ID  ,resp.getDataId());
				 Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_N);
					
				 log.info("【result_his_2】:{}",res);
				 if(res != null) {
					RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
					String key = getResultKey();
					redisUtils.hset(key,bean.getDataId(), res);
					redisUtils.expire(key, 60 * 60 * 24);
				 }
				 return;
			 }

			 if(Flow.START_ROB.getStep() == step) {
				 log.info("【START_ROB_2】：{}",step);
				 NiuDrawInfo.FLOW = Flow.START_ROB;
				 if(BotConfig.ENABLE) start();
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", NiuDrawInfo.DRAW_ISSUE);
					 json.put("CODE", NiuDrawInfo.PRE_DRAW_CODE);
					 json.put("RESULT", NiuDrawInfo.RESULT);
					 json.put("TIME", NiuDrawInfo.DRAW_TIME);
					 json.put("ID", NiuDrawInfo.ID);
					 WebSocketNiuServerApp.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.CONFIRM_ROB.getStep() == step) {
				 log.info("【CONFIRM_ROB_2】：{}",step);
				 NiuDrawInfo.FLOW = Flow.CONFIRM_ROB;
			 }else if(Flow.DOWN_ORDER.getStep() == step) {
				 log.info("【DOWN_ORDER_2】：{}",step);
				 NiuDrawInfo.FLOW = Flow.DOWN_ORDER;
				 if(BotConfig.ENABLE) drawOrder(resp);
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", NiuDrawInfo.DRAW_ISSUE);
					 json.put("CODE", NiuDrawInfo.PRE_DRAW_CODE);
					 json.put("RESULT", NiuDrawInfo.RESULT);
					 json.put("TIME", NiuDrawInfo.DRAW_TIME);
					 json.put("ID", NiuDrawInfo.ID);
					 WebSocketNiuServerApp.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.STOP_ORDER.getStep() == step) {
				 log.info("【STOP_ORDER_2】：{}",step);
				 NiuDrawInfo.FLOW = Flow.STOP_ORDER;
				 stopOrder();
				 if(WebSocketConfig.ENABLE && SysTaskManager.IS_AUTH) {
					 WebSocketNiuServerApp.sendInfo(Flow.STOP_ORDER.getStep(),"");
				 }
			 }else if(Flow.OVER.getStep() == step) {
				 log.info("【OVER_2】：{}",step);
				 NiuDrawInfo.FLOW = Flow.OVER;
				 overResult();
			 }else if(Flow.TIPS.getStep() == step) {
				 log.info("【TIME TIPS_2】：{}",step);
				 if(BotConfig.ENABLE)timeTips();
			 }
			 
			 
		} catch (Exception e) {
			log.error("抢庄异常： " + e.getMessage());
			e.printStackTrace();
		}
	}	
	
	private void timeTips() throws TelegramApiException {
		 
	}
	private void start() throws TelegramApiException {
		 
	}
 
	private void drawOrder(RespBean res) throws TelegramApiException {
 
	}
	private void stopOrder() throws TelegramApiException {
//		 查询是否有庄
		boolean flag = true;
		int index = 0;
		while(flag  && index < 5) {
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_N);
			  log.info("【QUERY_ORDER_2】:{}",res);
			  if(res != null) {
				  RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				  int dataId = Integer.parseInt(bean.getDataId());
				  int maxId = NiuDrawInfo.ID + 2;
				  int minId = NiuDrawInfo.ID - 2;
				 if(dataId < minId || dataId > maxId) {
					 log.info("历史信息当前期数：{}--{}" ,NiuDrawInfo.ID ,bean.getDataId());
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
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_N);
			
			log.info("【result_2】:{}",res);
			if(res != null) {
				RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				String key = getResultKey();
				redisUtils.hset(key,bean.getDataId(), res);
				redisUtils.expire(key, 60 * 60 * 24);
				log.info("存入结果:{}-{}",bean.getDataId(),res);
				 
				if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					NiuDrawInfo.RESULT = String.valueOf(bean.getSReust());
//					WebSocketNiuServerApp.sendInfo(Flow.OVER.getStep(),res.toString());
				 }
			    flag = false;
			}else {
				index++;
			}
		}
		  
	}
	
	 
	
	private String getResultKey() {
		String date = DateTimeUtils.getCurrentDate("yyyyMMdd");
		String key = RedisKey.ORDER_RESULT_N + ":" + date;
		return key;
	}
}
