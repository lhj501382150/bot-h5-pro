package com.hml.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hml.mall.entity.Draw;
import com.hml.mall.service.IDrawService;
import com.hml.utils.HttpClientUtils;
import com.hml.utils.SpringUtil;
import com.hml.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewMaKaoDataThread extends Thread {

	private boolean flag = true;
	
	private boolean isFirst = true;
	
	private IDrawService drawService;
	
	private String MODE = "C2";
	
	private Long DATA_ID = 0l;
	
	private Draw draw;
		
	public NewMaKaoDataThread() {
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
				String url ="https://cc.a6tk46.com/gallerynew/h5/index/lastLotteryRecord?lotteryType=5";
				String ret = HttpClientUtils.doGet(url,isFirst);
				JSONObject json = JSONObject.parseObject(ret);
				if("10000".equals(json.getString("code"))) {
					  JSONObject data = (JSONObject)json.get("data");
					  if(DATA_ID == 0) {
						  draw = drawService.getLast(MODE);
						  if(draw != null) {
							  DATA_ID = draw.getDataId();
						  }
					  }
					  isFirst = DATA_ID - data.getInteger("intPeriod") != 0;
					  if(isFirst) {
						  draw = new Draw();
						  draw.setDataId(data.getLong("intPeriod"));
						  draw.setPreDrawCode(StringUtils.getStr(data.getJSONArray("originalDataList")));
						  draw.setPreDrawDate(data.getString("lotteryTime"));
						  draw.setPreDrawTime(data.getString("lotteryTime"));
						  draw.setPreDrawIssue(data.getString("period"));
						  draw.setDrawIssue(data.getString("nextIntLotteryNumber"));
						  draw.setDrawTime(data.getString("nextLotteryTime"));
						  draw.setVideoUrl(data.getString("videoUrlForH5"));
						  draw.setTitle(data.getString("title"));
						  draw.setMemo(data.getString("numberList"));
						  draw.setMode(MODE);
						  
						  drawService.save(draw);
						  
						  DATA_ID = draw.getDataId();
					  }
					  long sleepTime = 1000 * 60;
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
