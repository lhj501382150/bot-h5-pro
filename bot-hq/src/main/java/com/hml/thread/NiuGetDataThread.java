package com.hml.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.service.BackCoreService;
import com.hml.config.BotConfig;
import com.hml.redis.Redis2Utils;
import com.hml.redis.RedisKey;
import com.hml.task.NiuDrawInfo;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NiuGetDataThread extends Thread {

	private boolean flag = true;
	
	private boolean isFirst = true;
	@Override
	public void run() {
		while(flag) {
			
			Redis2Utils redis2Utils = SpringUtil.getBean(Redis2Utils.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				BackCoreService backCoreService = SpringUtil.getBean(BackCoreService.class);
				String url ="https://api.api168168.com/pks/getPksDoubleCount.do?date=&lotCode=10037";
				String ret = HttpClientUtils.doGet(url,isFirst);
				JSONObject json = JSONObject.parseObject(ret);
				if("0".equals(json.getString("errorCode"))) {
					  JSONObject result = (JSONObject)json.get("result");
					  JSONObject data = (JSONObject)result.get("data");
					  isFirst = NiuDrawInfo.ID - data.getInteger("id") != 0;
					  NiuDrawInfo.ID = data.getInteger("id");
					  NiuDrawInfo.BEFORE_DRAW_CODE = NiuDrawInfo.PRE_DRAW_CODE;
					  NiuDrawInfo.PRE_DRAW_CODE = data.getString("preDrawCode");
					  NiuDrawInfo.PRE_DRAW_TIME = data.getString("preDrawTime");
					  NiuDrawInfo.DRAW_TIME = data.getString("drawTime");
					  NiuDrawInfo.DRAW_COUNT = data.getInteger("drawCount");
					  NiuDrawInfo.DRAW_ISSUE = data.getInteger("drawIssue");
					  if(isFirst) {
						  boolean flag = true;
						  try {
							 String val = String.valueOf(NiuDrawInfo.ID);
							 String key = RedisKey.DRAW_RESULT + val;
							 flag = redis2Utils.setnx(key, val, 60 * 2);
						 } catch (Exception e) {
							log.error("存入开奖结果异常:{}",e);
						 }
						  if(flag) {
							  JSONObject req = new JSONObject();
							  req.put("id", NiuDrawInfo.ID);
							  req.put("botId", BotConfig.CHAT_ID);
							  req.put("drawIssue", NiuDrawInfo.DRAW_ISSUE - 1);
							  req.put("sTime", NiuDrawInfo.PRE_DRAW_TIME);
							  req.put("sResult", NiuDrawInfo.PRE_DRAW_CODE);
							  req.put("mode","1");
							  backCoreService.addData(req);
						  }
						  
					  }
					  Date edate = sdf.parse(NiuDrawInfo.DRAW_TIME);
					  long endTime = edate.getTime();
					  long currentTimeMillis = System.currentTimeMillis();
					  long seconds = (endTime - currentTimeMillis) / 1000;
					  long sleepTime = 500l;
					  if(seconds > 1 * 15) {
						  sleepTime = 15 * 1000 ;
					  }else if(seconds > 5 && seconds <= 15) {
						  sleepTime = 5 * 1000 ;
					  }else if(seconds > 0 && seconds <= 5) {
						  sleepTime = 1 * 1000 ;
//					  }else if(seconds > -10 && seconds <= 0) {
//						  sleepTime = 500 ;
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
