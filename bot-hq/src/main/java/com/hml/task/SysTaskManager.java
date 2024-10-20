package com.hml.task;

import java.nio.charset.Charset;
import java.security.PrivateKey;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hml.config.BotConfig;
import com.hml.utils.DateTimeUtils;
import com.hml.utils.SM2Utils;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SysTaskManager {

	public static boolean IS_AUTH = true;
	
	@Scheduled(cron="0 0 7 * * *")
	public void checkSysAuth() {
		IS_AUTH = checkAuth();
		log.info("当前系统状态：{}",IS_AUTH);
	}
	
	protected boolean checkAuth() {
		String priPath = BotConfig.FILE_PATH + "/private_key.pem";
		String listenPath = BotConfig.FILE_PATH + "/listen.lic";
		String expireDate = FileUtil.readString(listenPath,Charset.forName("UTF-8"));
		expireDate = expireDate.replace("\n", "").trim();
		PrivateKey privateKey = SM2Utils.importPrivateKey(priPath);
		String decrypt = SM2Utils.decrypt(expireDate, privateKey);
		String curDate = DateTimeUtils.getCurrentDate("yyyyMMdd");
		log.info(curDate+"---"+ decrypt+ "=====" + curDate.compareTo(decrypt));
		if(curDate.compareTo(decrypt) > 0) {
			return false;
		}else {
			return true;
		}
	}
}
