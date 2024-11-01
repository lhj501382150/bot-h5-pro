package com.hml.task;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.bean.RespBean;
import com.hml.bean.DataSource;
import com.hml.config.BotConfig;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.StringUtils;
import com.hml.websocket.config.WebSocketConfig;
import com.hml.websocket.server.WebSocketServerBd1;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BdTaskManager1 {

	
	@Autowired
	private RedisUtils redisUtils;
	
	
	private String MODE = "2";//bd 1 fencai
	private String MODE_KEY = "HXBD1";
	
	@Async
	@Scheduled(fixedRate = 1000)
	public void syncStatus() {
		 try {
			 Object obj = redisUtils.lGetAndPop(RedisKey.CURRENT_STATUS_MODE + MODE);
			 if(StringUtils.isBlank(obj)) {
				 return;
			 }
			 log.info("【Redis-MODE{}信息】:{}",MODE,obj);
			 DataSource draw = HqTaskManager.getDraw(MODE_KEY);
			 if(draw == null) {
				 return;
			 }
			 RespBean resp = JSONObject.parseObject(obj.toString(), RespBean.class);
			 int step = resp.getIStatus();
			 int dataId = Integer.parseInt(resp.getDataId());
			 long maxId = draw.getId() + 2;
			 long minId = draw.getId() - 2;
			 if(dataId < minId || dataId > maxId) {
				 log.info("历史信息当前期数：{}--{}" ,draw.getId()  ,resp.getDataId());
				 Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + MODE);
					
				 log.info("【result-MODE{}】:{}",MODE,res);
				 if(res != null && BotConfig.ENABLE) {
					RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
					String key = getResultKey();
					redisUtils.hset(key,bean.getDataId(), res);
					redisUtils.expire(key, 60 * 60 * 24);
				 }
				 return;
			 }
			 if(Flow.START_ROB.getStep() == step) {
				 log.info("【START_ROB-MODE2】：{}",step);
				 if(BotConfig.ENABLE) start();
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", draw.getDrawIssue());
					 json.put("CODE", draw.getSResult());
					 json.put("RESULT", draw.getResult());
					 json.put("TIME", draw.getNextTime());
					 json.put("ID", draw.getId());
					 json.put("HASH", draw.getHash());
					 WebSocketServerBd1.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.CONFIRM_ROB.getStep() == step) {
				 log.info("【CONFIRM_ROB-MODE2】：{}",step);
			 }else if(Flow.DOWN_ORDER.getStep() == step) {
				 log.info("【DOWN_ORDER-MODE2】：{}",step);
				 if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					 JSONObject json = new JSONObject();
					 json.put("ISSUE", draw.getDrawIssue());
					 json.put("CODE", draw.getSResult());
					 json.put("RESULT", draw.getResult());
					 json.put("TIME", draw.getNextTime());
					 json.put("ID", draw.getId());
					 json.put("HASH", draw.getHash());
					 WebSocketServerBd1.sendInfo(Flow.START_ROB.getStep(),json.toJSONString());
				 }
			 }else if(Flow.STOP_ORDER.getStep() == step) {
				 log.info("【STOP_ORDER-MODE{}】：{}",MODE,step);
				 stopOrder(draw);
				 if(WebSocketConfig.ENABLE && SysTaskManager.IS_AUTH) {
					 WebSocketServerBd1.sendInfo(Flow.STOP_ORDER.getStep(),"");
				 }
			 }else if(Flow.OVER.getStep() == step) {
				 log.info("【OVER--MODE{}】：{}",MODE,step);
				 overResult(draw);
			 }else if(Flow.TIPS.getStep() == step) {
				 log.info("【TIME TIPS-MODE{}】：{}",MODE,step);
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

 
	private void stopOrder(DataSource draw) throws TelegramApiException {
//		 查询是否有庄
		boolean flag = true;
		int index = 0;
		while(flag  && index < 5) {
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY_MODE + MODE);
			  log.info("【QUERY_ORDER-MODE{}】:{}",MODE,res);
			  if(res != null) {
				  RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				  int dataId = Integer.parseInt(bean.getDataId());
				 if(dataId != draw.getId()) {
					 log.info("历史信息当前期数-MODE2：{}--{}" ,draw.getId() ,bean.getDataId());
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
			
			log.info("【result-MODE{}】:{}",MODE,res);
			if(res != null) {
				RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				String key = getResultKey();
				redisUtils.hset(key,bean.getDataId(), res);
				redisUtils.expire(key, 60 * 60 * 24);
				log.info("存入结果-MODE{}:{}-{}",MODE,bean.getDataId(),res);
				if(WebSocketConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					draw.setResult(String.valueOf(bean.getIWinNo()));
				 }
			    flag = false;
			}else {
				index++;
			}
		}
		  
	}
	private String getResultKey() {
		String date = DateTimeUtils.getCurrentDate("yyyyMMdd");
		String key = RedisKey.ORDER_RESULT_MODE + MODE + ":" + date;
		 
		return key;
	}
}
