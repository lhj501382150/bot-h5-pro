package com.hml.sms.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsProperties {

	public static String SEND_URL;
	
	public static String SECRET_ID;
	
	public static String SECRET_KEY;
	
	public static String SIGNATURE;
	
	public static Map<Integer,String> idsMap = new HashMap<>();
	/*
	 * 获取业务ID
	 */
	public static String getBusinessId(Integer type) {
		String id = idsMap.get(type);
		if(id == null) id = "";
		return id;
	}

	@Value("${sms.sendUrl}")
	public void setSendUrl(String sendUrl) {
		SEND_URL = sendUrl;
	}

	 
	@Value("${sms.secretId}")
	public void setSecretId(String secretId) {
		SECRET_ID = secretId;
	}

	@Value("${sms.secretKey}")
	public void setSecretKey(String secretKey) {
		SECRET_KEY = secretKey;
	}
	
	@Value("${sms.signature}")
	public void setSignature(String signature) {
		SIGNATURE = signature;
	}
}
