package com.hml.thread;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.hml.mall.entity.Draw;
import com.hml.mall.service.IDrawService;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JndDataThread extends Thread {

	private boolean flag = true;
	
	private boolean isFirst = true;
	
	private IDrawService drawService;
	
	private String MODE = "A3";
	
	private Long DATA_ID = 0l;
	
	private Draw draw;
		
	public JndDataThread() {
		drawService = (IDrawService)SpringUtil.getBean("drawService");
		if(drawService == null) {
			throw new RuntimeException("系统启动异常");
		}
	}
	
	@Override
	public void run() {
		while(flag) {
			try {
				drawService = (IDrawService)SpringUtil.getBean("drawService");
				String url ="https://super.pc28660.com/nextdraw/JND28";
				String ret = HttpClientUtils.doGet(url,isFirst);
				JSONObject json = JSONObject.parseObject(ret);
				if("1".equals(json.getString("code"))) {
					  JSONObject data = (JSONObject)json.get("data");
					  if(DATA_ID == 0) {
						  draw = drawService.getLast(MODE);
						  if(draw != null) {
							  DATA_ID = draw.getDataId();
						  }
					  }
					  JSONObject now = data.getJSONObject("now");
					  JSONObject next = data.getJSONObject("next");
					  
					  isFirst = DATA_ID - now.getInteger("expect") != 0;
					  if(isFirst) {
						  draw = new Draw();
						  draw.setDataId(now.getLong("expect"));
						  draw.setPreDrawCode(now.getString("opencode"));
						  String timeStamp = now.getString("opentime") + "000";
						  String date = formatStr(timeStamp);
						  draw.setPreDrawDate(date.substring(0,10));
						  draw.setPreDrawTime(date);
						  draw.setPreDrawIssue(now.getString("expect"));
						  draw.setDrawIssue(next.getString("drawIssue"));
						  long time = (System.currentTimeMillis() / 1000 + next.getLong("countdown")) ;
						  draw.setDrawTime(formatStr(String.valueOf(time)+"000"));
						  draw.setMode(MODE);
						  
						  drawService.save(draw);
						  
						  DATA_ID = draw.getDataId();
					  }
					  long sleepTime = next.getLong("countdown") + 2;
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
	public static String formatStr(String str) {
		long timestamp = Long.parseLong(str);
        // 转换为Instant
        Instant instant = Instant.ofEpochMilli(timestamp);
 
        // 转换为LocalDateTime
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
 
        // 打印
        System.out.println("LocalDateTime: " + dateTime.toString());
 
        // 如果需要其他格式，可以使用DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        System.out.println("Formatted DateTime: " + formattedDateTime);
        return formattedDateTime;
	}
}
