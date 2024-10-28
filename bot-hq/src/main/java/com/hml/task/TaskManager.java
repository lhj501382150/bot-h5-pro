package com.hml.task;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.bean.Order;
import com.hml.back.bean.RespBean;
import com.hml.back.service.BackCoreService;
import com.hml.bean.CommandTextParser;
import com.hml.bot.BaseBot;
import com.hml.command.BossCommand;
import com.hml.command.ErrorCommand;
import com.hml.config.BotConfig;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.SM2Utils;
import com.hml.utils.StringUtils;
import com.hml.websocket.config.WebSocketConfig;
import com.hml.websocket.server.WebSocketServerApp;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskManager {

	@Autowired
	private BaseBot baseBot;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private BossCommand bossCommand;
	
	@Autowired
	private ErrorCommand errorCommand;
	
	private String mode = "0";
	
	@Async
	@Scheduled(fixedRate = 1000)
	public void syncStatusSys() {
		try {
			Object obj = redisUtils.get(RedisKey.SYSTEM_STATUS);
			 if(StringUtils.isBlank(obj)) {
				 return;
			 }
			 String status= obj.toString();
			 int step = Integer.parseInt(status);
			 if(WebSocketConfig.ENABLE && SysTaskManager.IS_AUTH) {
				 WebSocketServerApp.sendInfo(step, "Update");
			 }
			 redisUtils.del(RedisKey.SYSTEM_STATUS);
		} catch (Exception e) {
			 log.error("发送结果图异常：{}",e);
		}
	}
	
	  
	@Async
	@Scheduled(fixedRate = 1000)
	public void syncStatus() {
		 try {
			 Object obj = redisUtils.lGetAndPop(RedisKey.CURRENT_STATUS_MODE + 1);
			 if(StringUtils.isBlank(obj)) {
				 return;
			 }
			 log.info("【Redis信息】:{}",obj);
			 
			 RespBean resp = JSONObject.parseObject(obj.toString(), RespBean.class);
			 int step = resp.getIStatus();
			 int dataId = Integer.parseInt(resp.getDataId());
			 int maxId = DrawInfo.ID + 2;
			 int minId = DrawInfo.ID - 2;
			 if(dataId < minId || dataId > maxId) {
				 log.info("历史信息当前期数：{}--{}" ,DrawInfo.ID  ,resp.getDataId());
				 Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + mode);
					
				 log.info("【result】:{}",res);
				 if(res != null && BotConfig.ENABLE) {
					RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
					String key = getResultKey();
					redisUtils.hset(key,bean.getDataId(), res);
					redisUtils.expire(key, 60 * 60 * 24);
				 }
				 return;
			 }
 
			 if(Flow.START_ROB.getStep() == step) {
				 log.info("【START_ROB】：{}",step);
				 DrawInfo.FLOW = Flow.START_ROB;
				 if(BotConfig.ENABLE) start();
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", DrawInfo.DRAW_ISSUE);
					 json.put("CODE", DrawInfo.PRE_DRAW_CODE);
					 json.put("RESULT", DrawInfo.RESULT);
					 json.put("TIME", DrawInfo.DRAW_TIME);
					 json.put("ID", DrawInfo.ID);
					 WebSocketServerApp.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.CONFIRM_ROB.getStep() == step) {
				 log.info("【CONFIRM_ROB】：{}",step);
				 DrawInfo.FLOW = Flow.CONFIRM_ROB;
//				 confirm(resp);
			 }else if(Flow.DOWN_ORDER.getStep() == step) {
				 log.info("【DOWN_ORDER】：{}",step);
				 DrawInfo.FLOW = Flow.DOWN_ORDER;
				 if(BotConfig.ENABLE) drawOrder(resp);
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", DrawInfo.DRAW_ISSUE);
					 json.put("CODE", DrawInfo.PRE_DRAW_CODE);
					 json.put("RESULT", DrawInfo.RESULT);
					 json.put("TIME", DrawInfo.DRAW_TIME);
					 json.put("ID", DrawInfo.ID);
					 WebSocketServerApp.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.STOP_ORDER.getStep() == step) {
				 log.info("【STOP_ORDER】：{}",step);
				 DrawInfo.FLOW = Flow.STOP_ORDER;
				 stopOrder();
				 if(WebSocketConfig.ENABLE && SysTaskManager.IS_AUTH) {
					 WebSocketServerApp.sendInfo(Flow.STOP_ORDER.getStep(),"");
				 }
			 }else if(Flow.OVER.getStep() == step) {
				 log.info("【OVER】：{}",step);
				 DrawInfo.FLOW = Flow.OVER;
				 overResult();
			 }else if(Flow.TIPS.getStep() == step) {
				 log.info("【TIME TIPS】：{}",step);
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
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE+mode);
			  log.info("【QUERY_ORDER】:{}",res);
			  if(res != null) {
				  RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				  int dataId = Integer.parseInt(bean.getDataId());
				  int maxId = DrawInfo.ID + 2;
				  int minId = DrawInfo.ID - 2;
				 if(dataId < minId || dataId > maxId) {
					 log.info("历史信息当前期数：{}--{}" ,DrawInfo.ID ,bean.getDataId());
					 return;
				 }
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
			
			log.info("【result】:{}",res);
			if(res != null) {
				RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				String key = getResultKey();
				redisUtils.hset(key,bean.getDataId(), res);
				redisUtils.expire(key, 60 * 60 * 24);
				log.info("存入结果:{}-{}",bean.getDataId(),res);
				if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					DrawInfo.RESULT = String.valueOf(bean.getIWinNo());
					WebSocketServerApp.sendInfo(Flow.OVER.getStep(),res.toString());
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
