package com.hml.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.service.BackCoreService;
import com.hml.config.BotConfig;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.task.DrawInfo;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetDataThread extends Thread {

	private boolean flag = true;
	
	private boolean isFirst = true;
	@Override
	public void run() {
		while(flag) {
			
			RedisUtils redisUtils = SpringUtil.getBean(RedisUtils.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				BackCoreService backCoreService = SpringUtil.getBean(BackCoreService.class);
				String url ="https://api.api168168.com/pks/getPksDoubleCount.do?date=&lotCode=10012";
				String ret = HttpClientUtils.doGet(url,isFirst);
				JSONObject json = JSONObject.parseObject(ret);
				if("0".equals(json.getString("errorCode"))) {
					  JSONObject result = (JSONObject)json.get("result");
					  JSONObject data = (JSONObject)result.get("data");
					  isFirst = DrawInfo.ID - data.getInteger("id") != 0;
					  DrawInfo.ID = data.getInteger("id");
					  DrawInfo.BEFORE_DRAW_CODE = DrawInfo.PRE_DRAW_CODE;
					  DrawInfo.PRE_DRAW_CODE = data.getString("preDrawCode");
					  DrawInfo.PRE_DRAW_TIME = data.getString("preDrawTime");
					  DrawInfo.DRAW_TIME = data.getString("drawTime");
					  DrawInfo.DRAW_COUNT = data.getInteger("drawCount");
					  DrawInfo.DRAW_ISSUE = data.getInteger("drawIssue");
					  if(isFirst) {
						  boolean flag = true;
						  try {
							  String val = String.valueOf(DrawInfo.ID);
							 String key = RedisKey.DRAW_RESULT + val;
							 flag = redisUtils.setnx(key, val, 60 * 8);
						 } catch (Exception e) {
							log.error("存入开奖结果异常:{}",e);
						 }
						  if(flag) {
							  JSONObject req = new JSONObject();
							  req.put("id", DrawInfo.ID);
							  req.put("botId", BotConfig.CHAT_ID);
							  req.put("drawIssue", DrawInfo.DRAW_ISSUE - 1);
							  req.put("sTime", DrawInfo.PRE_DRAW_TIME);
							  req.put("sResult", DrawInfo.PRE_DRAW_CODE);
							  req.put("mode", "0");
							  backCoreService.addData(req);
						  }
						  
					  }
					  Date edate = sdf.parse(DrawInfo.DRAW_TIME);
					  long endTime = edate.getTime();
					  long currentTimeMillis = System.currentTimeMillis();
					  long seconds = (endTime - currentTimeMillis) / 1000;
					  long sleepTime = 500l;
					  if(seconds > 1 * 60) {
//						  DrawInfo.FLOW = Flow.CONFIRM_ROB;
						  sleepTime = 1 * 60 * 1000 ;
					  }else if(seconds > 1 * 15 && seconds <=  60) {
//						  DrawInfo.FLOW = Flow.DOWN_ORDER;
						  sleepTime = 15 * 1000 ;
					  }else if(seconds > 0 && seconds <= 15) {
//						  DrawInfo.FLOW = Flow.STOP_ORDER;
						  sleepTime = 5 * 1000 ;
					  }else if(seconds > -10 && seconds <= 0) {
//						  DrawInfo.FLOW = Flow.STOP_ORDER;
						  sleepTime = 3000 ;
					  }else if(seconds > -18 && seconds <= -10) {
//						  DrawInfo.FLOW = Flow.STOP_ORDER;
						  sleepTime = 1000 ;
					  }else {
						  sleepTime = 200;
					  }
					  log.info("等待查询:【{}】" ,sleepTime);
					  Thread.sleep(sleepTime);
				  }else {
					  throw new Exception("接收数据异常");
				  }
				
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(1 * 1000l);
				} catch (Exception e2) {
 
				}
			}
		}
	}
}
