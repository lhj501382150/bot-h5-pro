package com.hml.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.hml.mall.entity.Draw;
import com.hml.mall.service.IDrawService;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetDataThread extends Thread {

	private boolean flag = true;
	
	private boolean isFirst = true;
	
	private IDrawService drawService;
	
	private String MODE = "A1";
	
	private Long DATA_ID = 0l;
	
	private Draw draw;
		
	public GetDataThread() {
		drawService = (IDrawService)SpringUtil.getBean("drawService");
		if(drawService == null) {
			throw new RuntimeException("系统启动异常");
		}
	}
	
	@Override
	public void run() {
		while(flag) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				drawService = (IDrawService)SpringUtil.getBean("drawService");
				String url ="https://api.api168168.com/pks/getPksDoubleCount.do?date=&lotCode=10012";
				String ret = HttpClientUtils.doGet(url,isFirst);
				JSONObject json = JSONObject.parseObject(ret);
				if("0".equals(json.getString("errorCode"))) {
					  JSONObject result = (JSONObject)json.get("result");
					  JSONObject data = (JSONObject)result.get("data");
					  if(DATA_ID == 0) {
						  draw = drawService.getLast(MODE);
						  if(draw != null) {
							  DATA_ID = draw.getDataId();
						  }
					  }
					  isFirst = DATA_ID - data.getInteger("id") != 0;
					  if(isFirst) {
						  draw = new Draw();
						  draw.setDataId(data.getLong("id"));
						  draw.setPreDrawCode(data.getString("preDrawCode"));
						  draw.setPreDrawDate(data.getString("preDrawDate"));
						  draw.setPreDrawTime(data.getString("preDrawTime"));
						  draw.setPreDrawIssue(data.getString("preDrawIssue"));
						  draw.setDrawIssue(data.getString("drawIssue"));
						  draw.setDrawTime(data.getString("drawTime"));
						  draw.setMode(MODE);
						  
						  drawService.save(draw);
						  
						  DATA_ID = draw.getDataId();
					  }
					  Date edate = sdf.parse(draw.getDrawTime());
					  long endTime = edate.getTime();
					  long currentTimeMillis = System.currentTimeMillis();
					  long seconds = (endTime - currentTimeMillis) / 1000;
					  long sleepTime = 500l;
					  if(seconds > 1 * 60) {
						  sleepTime = 1 * 60 * 1000 ;
					  }else if(seconds > 1 * 15 && seconds <=  60) {
						  sleepTime = 15 * 1000 ;
					  }else if(seconds > 0 && seconds <= 15) {
						  sleepTime = 5 * 1000 ;
					  }else if(seconds > -10 && seconds <= 0) {
						  sleepTime = 3000 ;
					  }else if(seconds > -18 && seconds <= -10) {
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
