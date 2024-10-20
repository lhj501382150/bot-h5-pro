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
	public void syncRobDown() {
		try {
			Object obj = redisUtils.get(RedisKey.ROB_DOWN);
			 if(StringUtils.isBlank(obj)) {
				 return;
			 }
			 StringBuffer buff = new StringBuffer();
			buff.append("下庄成功！从下轮起，重新竞选庄家，请广大玩家踊跃参加庄家竞选。");
			SendPhoto photo = bossCommand.getSendPhoto(BotConfig.CHAT_ID, buff.toString(), "Markdown");
			try {
				if(BotConfig.ENABLE && SysTaskManager.IS_AUTH) {
					baseBot.execute(photo);
				}
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			redisUtils.del(RedisKey.ROB_DOWN);
		} catch (Exception e) {
			 log.error("发送结果图异常：{}",e);
		}
	}
	
	@Async
	@Scheduled(fixedRate = 1000)
	public void syncPic() {
		try {
			Object obj = redisUtils.lGetAndPop(RedisKey.ROB_SEND_PIC);
			 if(StringUtils.isBlank(obj)) {
				 return;
			 }
			 log.info("重新下发结果图：{}-{}",RedisKey.ROB_SEND_PIC,obj);
			 Object res = redisUtils.hget(getResultKey(), obj.toString());
			 log.info("下发结果：{}",res);
			 if(res != null && BotConfig.ENABLE  && SysTaskManager.IS_AUTH) {
				 RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				 sendTable(bean,"开奖成功!("+bean.getIWinNo() + CommandTextParser.getText(bean.getIWinNo()) +")\n本期期数： " + bean.getDrawIssue() ,true);
			 }
		} catch (Exception e) {
			 log.error("发送结果图异常：{}",e);
		}
	}
	
	@Async
	@Scheduled(fixedRate = 1000)
	public void syncStatus() {
		 try {
			 Object obj = redisUtils.lGetAndPop(RedisKey.CURRENT_STATUS);
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
				 Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY);
					
				 log.info("【result】:{}",res);
				 if(res != null && BotConfig.ENABLE) {
					RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
					String key = getResultKey();
					redisUtils.hset(key,bean.getDataId(), res);
					redisUtils.expire(key, 60 * 60 * 24);
				 }
				 return;
			 }
//			 if(!backCoreService.isBotEnable(String.valueOf(BotConfig.CHAT_ID))) {
//				 if(step == 4 || step == 5) {
//					 Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY);
//					 log.info("【result】:{}",res);
//				 }
//				return;
//			 }
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
		baseBot.execute(bossCommand.sendMessage(BotConfig.CHAT_ID, "下注倒计时:30秒"));
	}
	private void start() throws TelegramApiException {
		getMinMoney();
		getMaxMoney();
		baseBot.execute(bossCommand.sendPhoto(BotConfig.CHAT_ID, DrawInfo.MIN_MONEY, null));
	}
	private void confirm(RespBean res) throws TelegramApiException {
//		 查询是否有庄
		 if(StringUtils.isBlank(res.getUserId())) {//
			 errorCommand.sendError(baseBot, BotConfig.CHAT_ID, "本场未存在庄家，请等待下轮进行下注，如果10次输入错误，则采取禁言处理", null);
		 }else {
			 baseBot.execute(bossCommand.sendPhotoConfirm(BotConfig.CHAT_ID, res));
		 }
	}
	private void drawOrder(RespBean res) throws TelegramApiException {
//		 查询是否有庄
		if(StringUtils.isBlank(res.getUserId())) {//
			errorCommand.sendError(baseBot, BotConfig.CHAT_ID, "本场未存在庄家，请等待下轮进行下注，如果10次输入错误，则采取禁言处理", null);
		}else {
			getOrderMinMoney();
			getOrderMaxMoney();
			baseBot.execute(bossCommand.sendPhotoConfirm(BotConfig.CHAT_ID, res));
		}
	}
	private void stopOrder() throws TelegramApiException {
//		 查询是否有庄
		boolean flag = true;
		int index = 0;
		while(flag  && index < 5) {
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY);
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
				 if(BotConfig.ENABLE  && SysTaskManager.IS_AUTH) sendTable(bean,"停止下注！",false);
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
			Object res = redisUtils.lGetAndPop(RedisKey.ORDER_QUERY);
			
			log.info("【result】:{}",res);
			if(res != null) {
				RespBean bean = JSONObject.parseObject(res.toString(), RespBean.class);
				String key = getResultKey();
				redisUtils.hset(key,bean.getDataId(), res);
				redisUtils.expire(key, 60 * 60 * 24);
				log.info("存入结果:{}-{}",bean.getDataId(),res);
				if(BotConfig.ENABLE  && SysTaskManager.IS_AUTH) {
					sendTable(bean,"开奖成功!("+bean.getIWinNo() + CommandTextParser.getText(bean.getIWinNo()) +")\n本期期数： " + (DrawInfo.DRAW_ISSUE - 1) ,true);
				}
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
	
	private void sendTable(RespBean bean,String what,boolean flag) throws TelegramApiException {
		  List<Order> orderMx = bean.getOrderMx();
		  if(flag) {
			  baseBot.execute(bossCommand.sendOnlyPhotoResult(BotConfig.CHAT_ID,what,bean.getIWinNo()));
		  }else {
			  baseBot.execute(bossCommand.sendMessage(BotConfig.CHAT_ID,what));
		  }
		  baseBot.execute(bossCommand.sendPhotoResult(BotConfig.CHAT_ID, bean,flag));
		  if(flag) {
			  if(bean.getRankList() != null) {
				  baseBot.execute(bossCommand.sendOverResult(BotConfig.CHAT_ID, bean));
			  }
		  }
	}
	private Map<String,Object> getDataMap(RespBean bean,List<Order> orders,String title,String subTitle){
		Map<String,Object> dataMap = new HashMap<>();
		Map<String,Object> data = new HashMap<>();
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
		
		BigDecimal total = new BigDecimal(0);
		if(orders != null && orders.size() > 0) {
			
			for(Order order : orders) {
				Map<String,Object> item = new HashMap<String, Object>();
				item.put("userName",order.getUserName());
				item.put("fRate",order.getFRate().toString());
				item.put("integRal",order.getIntegRal());
				item.put("leftTRal",order.getWinInteg().add(order.getIntegRal()));
				item.put("winLose",order.getWinLose());
				item.put("winInteg",order.getWinInteg());
				item.put("RulId",order.getRulId());
				item.put("dBef",order.getDBef().toString());
				item.put("dEnd",order.getDEnd().toString());
				total = total.add(order.getIntegRal());
				item.put("seq",  order.getSeq());
				dataList.add(item);
			}
		}
		if(StringUtils.isBlank(bean.getUserName())) {
			data.put("count", dataList.size());
			data.put("total", total);
			data.put("drawNo", "");
			data.put("userName", "");
			data.put("winInteg", "");
			data.put("integRal", "");
			data.put("iLossNum", 0);
			data.put("iWinNum", 0);
		}else {
			data.put("count", dataList.size());
			data.put("total", total);
			data.put("drawNo", bean.getIWinNo());
			data.put("userName", bean.getUserName());
			data.put("winInteg", bean.getWinInteg());
			data.put("integRal", bean.getIntegRal());
			data.put("iLossNum", bean.getILossNum());
			data.put("iWinNum", bean.getIWinNum());
		}
		
		
		dataMap.put("title", title);
		dataMap.put("subTitle", subTitle);
		dataMap.put("data", data);
		dataMap.put("dataList", dataList);
		
		return dataMap;
	}
	private String getMinMoney() {
		Object obj = redisUtils.get(RedisKey.ROB_MIN_MONEY);
		if(!StringUtils.isBlank(obj)) {
			DrawInfo.MIN_MONEY = obj.toString();
		}
		return DrawInfo.MIN_MONEY;
	}
	private String getMaxMoney() {
		Object obj = redisUtils.get(RedisKey.ROB_MAX_MONEY);
		if(!StringUtils.isBlank(obj)) {
			DrawInfo.MAX_MONEY = obj.toString();
		}
		return DrawInfo.MAX_MONEY;
	}
	private String getOrderMinMoney() {
		Object obj = redisUtils.get(RedisKey.ORDER_MIN_MONEY);
		if(!StringUtils.isBlank(obj)) {
			DrawInfo.ORDER_MIN_MONEY = obj.toString();
		}
		return DrawInfo.ORDER_MIN_MONEY;
	}
	private String getOrderMaxMoney() {
		Object obj = redisUtils.get(RedisKey.ORDER_MAX_MONEY);
		if(!StringUtils.isBlank(obj)) {
			DrawInfo.ORDER_MAX_MONEY = obj.toString();
		}
		return DrawInfo.ORDER_MAX_MONEY;
	}
	
	private String getResultKey() {
		String date = DateTimeUtils.getCurrentDate("yyyyMMdd");
		String key = RedisKey.ORDER_RESULT + ":" + date;
		 
		return key;
	}
}
