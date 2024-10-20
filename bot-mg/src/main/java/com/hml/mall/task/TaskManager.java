package com.hml.mall.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hml.mall.iface.order.IOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskManager {
	
	@Autowired
	private IOrderService orderService;

	@Scheduled(cron ="0 50 6 * * ? ")
	public void initAddUser(){
		 try {
			 orderService.syncHisData();
		} catch (Exception e) {
			log.error("备份数据异常：{}",e);
		}
	}
}
