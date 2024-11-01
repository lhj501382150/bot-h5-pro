package com.hml.task;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.bean.RespBean;
import com.hml.bean.CommandTextParser;
import com.hml.bean.DataSource;
import com.hml.bot.BaseBot;
import com.hml.command.BossCommand;
import com.hml.command.ErrorCommand;
import com.hml.config.BotConfig;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.StringUtils;
import com.hml.websocket.config.WebSocketConfig;
import com.hml.websocket.server.WebSocketServerNn1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NnTaskManager1 {

	@Autowired
	private BaseBot baseBot;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private BossCommand bossCommand;
	
	@Autowired
	private ErrorCommand errorCommand;
	
	private String MODE = "5";//bd 1 fencai
	private String MODE_KEY = "HXNN1";
	
	@Async
	@Scheduled(fixedRate = 1000)
	public void syncStatus() {
		 try {
			 Object obj = redisUtils.lGetAndPop(RedisKey.CURRENT_STATUS_MODE + MODE);
			 if(StringUtils.isBlank(obj)) {
				 return;
			 }
			 log.info("【Redis-{}信息】:{}",MODE,obj);
			 
			 RespBean resp = JSONObject.parseObject(obj.toString(), RespBean.class);
			 int step = resp.getIStatus();
			 int dataId = Integer.parseInt(resp.getDataId());
			 DataSource draw = HqTaskManager.getDraw(MODE_KEY);
			 if(draw == null || draw.getId() == null) {
				 return;
			 }
			 long maxId = draw.getId() + 2;
			 long minId = draw.getId() - 2;
			 if(dataId < minId || dataId > maxId) {
				 log.info("历史信息当前期数：{}--{}" ,dataId  ,resp.getDataId());
				 Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + MODE);
					
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
				 if(BotConfig.ENABLE) start();
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", draw.getDrawIssue());
					 json.put("CODE", draw.getSResult());
					 json.put("RESULT", draw.getResult());
					 json.put("TIME", draw.getNextTime());
					 json.put("ID", draw.getId());
					 json.put("HASH", draw.getHash());
					 WebSocketServerNn1.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.CONFIRM_ROB.getStep() == step) {
				 log.info("【CONFIRM_ROB】：{}",step);
			 }else if(Flow.DOWN_ORDER.getStep() == step) {
				 log.info("【DOWN_ORDER】：{}",step);
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", draw.getDrawIssue());
					 json.put("CODE", draw.getSResult());
					 json.put("RESULT", draw.getResult());
					 json.put("TIME", draw.getNextTime());
					 json.put("ID", draw.getId());
					 json.put("HASH", draw.getHash());
					 WebSocketServerNn1.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.STOP_ORDER.getStep() == step) {
				 log.info("【STOP_ORDER】：{}",step);
				 stopOrder(draw);
				 if(WebSocketConfig.ENABLE && SysTaskManager.IS_AUTH) {
					 WebSocketServerNn1.sendInfo(Flow.STOP_ORDER.getStep(),"");
				 }
			 }else if(Flow.OVER.getStep() == step) {
				 log.info("【OVER-{}】：{}",MODE,step);
				 overResult(draw);
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
		baseBot.execute(bossCommand.sendMessage(BotConfig.CHAT_ID, "下注倒计时:30秒"));
	}
	private void start() throws TelegramApiException {
		baseBot.execute(bossCommand.sendPhoto(BotConfig.CHAT_ID, DrawInfo.MIN_MONEY, null));
	}
	private void stopOrder(DataSource draw) throws TelegramApiException {
//		 查询是否有庄
		boolean flag = true;
		int index = 0;
		while(flag  && index < 5) {
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + MODE);
			  log.info("【QUERY_ORDER-{}】:{}",MODE,res);
			  if(res != null) {
				  RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				  int dataId = Integer.parseInt(bean.getDataId());
				  if(dataId != draw.getId()) {
						 log.info("历史信息当前期数-MODE3：{}--{}" ,draw.getId() ,bean.getDataId());
						 continue;
					 }
				 flag = false;
			  }else {
				  index++;
			  }
		}
		  
	}
	private void overResult(DataSource draw) throws TelegramApiException, IOException {
//		 查询是否有庄
		boolean flag = true;
		int index = 0;
		while(flag && index < 5) {
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + MODE);
			
			log.info("【result-{}】:{}",MODE,res);
			if(res != null) {
				RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				String key = getResultKey();
				redisUtils.hset(key,bean.getDataId(), res);
				redisUtils.expire(key, 60 * 60 * 24);
				log.info("存入结果-{}:{}-{}",MODE,bean.getDataId(),res);
				int dataId = Integer.parseInt(bean.getDataId());
				if(dataId != draw.getId()) {
					 continue;
				 }
				if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					draw.setResult(String.valueOf(bean.getSReust()));
//					WebSocketServerNn1.sendInfo(Flow.OVER.getStep(),res.toString());
				 }
			    flag = false;
			}else {
				index++;
			}
		}
		  
	}
	
	private void sendTable(RespBean bean,String what,boolean flag) throws TelegramApiException {
		  
	}
	 
	 
	
	private String getResultKey() {
		String date = DateTimeUtils.getCurrentDate("yyyyMMdd");
		String key = RedisKey.ORDER_RESULT_MODE + MODE + ":" + date;
		 
		return key;
	}
}
