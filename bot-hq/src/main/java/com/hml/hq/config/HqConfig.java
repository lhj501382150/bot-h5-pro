package com.hml.hq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HqConfig {

	/*
	 * 行情访问路径
	 */
	public static String URL = "";
	
	@Value("${hxhq.url}")
	public void setUrl(String url) {
		URL = url;
	}
	
	public static String GET_DATA = "/draw/getResult";
}
