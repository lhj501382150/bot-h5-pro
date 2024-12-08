package com.hml.thread;

import com.alibaba.fastjson.JSONObject;
import com.hml.back.service.BackCoreService;
import com.hml.bean.DataSource;
import com.hml.config.BotConfig;
import com.hml.hq.config.HqConfig;
import com.hml.redis.RedisKey;
import com.hml.redis.RedisUtils;
import com.hml.task.HqTaskManager;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Hash3Thread extends Thread {

	private boolean flag = true;
	
	private boolean isFirst = true;
	
	private String MODE = "B2";
	private String KEY_1 = "HXBD3";
	private String KEY_2 = "HXNN3";
	private String MODE_1 = "3";
	private String MODE_2 = "6";
	@Override
	public void run() {
		while(flag) {
			RedisUtils redisUtils = SpringUtil.getBean(RedisUtils.class);
			try {
				BackCoreService backCoreService = SpringUtil.getBean(BackCoreService.class);
				String url = HqConfig.URL + HqConfig.GET_DATA;
				JSONObject json = new JSONObject();
				json.put("mode", MODE);
				String ret = HttpClientUtils.doPost(url,json.toJSONString());
				json = JSONObject.parseObject(ret);
				if("200".equals(json.getString("code"))) {
					  JSONObject data = (JSONObject)json.get("data");
					  DataSource draw = HqTaskManager.getDraw(KEY_1);
					  if(draw != null) {
						  isFirst = draw.getId() - data.getInteger("dataId") != 0;
					  }
					  if(isFirst) {
						  draw = HqTaskManager.getDataSource(MODE_1, KEY_1, data);
						  HqTaskManager.copyDataSource(draw, MODE_2, KEY_2);
						  boolean flag = true;
						  try {
							 String val = String.valueOf(draw.getId());
							 String key = RedisKey.DRAW_RESULT + MODE_1 + ":" + val;
							 flag = redisUtils.setnx(key, val, 60 * 8);
						 }catch (Exception e) {
							log.error("存入开奖结果异常:{}",e);
						 }
						  if(flag) {
							  JSONObject req = new JSONObject();
							  req.put("id", data.getInteger("dataId"));
							  req.put("botId", BotConfig.CHAT_ID);
							  req.put("drawIssue", data.getLong("preDrawIssue"));
							  req.put("sTime", data.getString("preDrawTime"));
							  req.put("sResult", data.getString("preDrawCode"));
							  req.put("mode", MODE_1);
							  backCoreService.addData(req);
							  
							  req.put("mode", MODE_2);
							  backCoreService.addData(req);
						  }
					  }
					  long seconds = data.getLongValue("leftTime");
					  long sleepTime = (seconds + 1) * 1000;
					  if(seconds <= 0) {
						  sleepTime = 500l;
					  }
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
