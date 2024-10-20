package com.hml.heepay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/*
 * 支付配置
 */
@Configuration
@Data
public class HeePayConfig {

	@Value("${heepay.url}")
	private String url;
	
	@Value("${heepay.agent_id}")
	private String agent_id;
	
	@Value("${heepay.notifyUrl}")
	private String notifyUrl;
	
	@Value("${heepay.md5key}")
	private String md5key;
	
	@Value("${heepay.deskey}")
	private String deskey;
	
	@Value("${heepay.queryUrl}")
	private String queryUrl;
	
	@Value("${heepay.jqmd5key}")
	private String jqmd5key;
	
	@Value("${heepay.jqdeskey}")
	private String jqdeskey;
	
	@Value("${heepay.querykey}")
	private String querykey;
}
