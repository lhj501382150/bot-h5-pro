package com.hml.mall.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hml.mall.util.JwtTokenUtils;

@Component
public class TokenTask {

	@Scheduled(cron ="0 0/5 * * * *")
	public void getSendData(){
		JwtTokenUtils.clearToken();
	}
}
