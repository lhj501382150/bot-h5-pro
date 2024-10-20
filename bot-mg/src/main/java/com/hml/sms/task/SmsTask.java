package com.hml.sms.task;
/**
 * 短信发送定时器
 * @author Administrator
 *
 */

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.mall.entity.notice.Mess;
import com.hml.mall.iface.notice.IMessService;
import com.hml.sms.utils.SmsUtils;

@Component
public class SmsTask {
	
	private Logger log = Logger.getLogger(SmsTask.class);
	
	@Autowired
	private IMessService messService;
	
	private Deque<Mess> queue = new LinkedList<Mess>();

//	@Scheduled(cron ="0/1 * * * * *")
//	public void getSendData(){
//		
//		QueryWrapper<Mess> qw = new QueryWrapper<Mess>();
//		qw.eq("SENDFLAG", 0);
//		List<Mess> list = messService.list(qw);
//		if(list.size() > 0 ) {
//			log.info("查询发送短信数据: " + list.size());
//			for(Mess m : list) {
//				queue.offer(m);
//			}
//		}
//	}
//	@Scheduled(cron ="1/1 * * * * *")
//	public void sendData(){
//		if(queue.size() > 0) {
//			log.info("开始发送短信......");
//			Mess m = queue.poll();
//			while(m !=null) {
//				try {
//					m = SmsUtils.sendMess(m);
//					messService.updateById(m);
//				} catch (Exception e) {
//					log.error("发送短信异常：" + e.getMessage());
//					e.printStackTrace();
//				}
//				m = queue.poll();
//			}
//			log.info("发送短信结束......");
//		}
//	}
}
