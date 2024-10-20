package com.hml.config;

import java.security.PrivateKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hml.utils.SM2Utils;
import com.hml.utils.StringUtils;

@Component
public class BotConfig {
	
	public static String TOKEN = "";
	
	public static String USERNAME = "";
	
	public static String KF_CHANNEL = "";
	public static String CZ_CHANNEL = "";
	public static String TX_CHANNEL = "";
	public static String WF_CHANNEL = "";
	public static String KJ_CHANNEL = "";
	public static Boolean ENABLE = false;
	public static Long CHAT_ID = 0l;
	
	public static String FILE_PATH = "";
	public static String EXPIRE_DATE = "";
	
	@Value("${bot.token}")
	public void setToekn(String token) {
		TOKEN = token;
	}
	@Value("${bot.username}")
	public void setUsername(String username) {
		USERNAME = username;
	}
	@Value("${bot.kf_chanel}")
	public void setKf(String chanel) {
		KF_CHANNEL = chanel;
	}
	@Value("${bot.cz_chanel}")
	public void setcz(String chanel) {
		CZ_CHANNEL = chanel;
	}
	@Value("${bot.tx_chanel}")
	public void settx(String chanel) {
		TX_CHANNEL = chanel;
	}
	@Value("${bot.wf_chanel}")
	public void setwf(String chanel) {
		WF_CHANNEL = chanel;
	}
	@Value("${bot.kj_chanel}")
	public void setkj(String chanel) {
		KJ_CHANNEL = chanel;
	}
	
	@Value("${bot.chatId}")
	public void setChatId(Long chatId) {
		CHAT_ID = chatId;
	}
	@Value("${bot.filePath}")
	public void setFilePath(String filePath) {
		FILE_PATH = filePath;
	}
	@Value("${bot.status}")
	public void setEnable(String status) {
		ENABLE = "Y".equals(status);
	}
	
//	@Value("${bot.cert}")
//	public void setExpireDate(String cert) {
//		EXPIRE_DATE = cert;
//	}
}
