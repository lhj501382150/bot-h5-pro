package com.hml.backcore.req;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
/**
 * 充值生成订单
 * @author Administrator
 *
 */
@Data
public class REQ530 {

	@JSONField(name="LOGINNO")
	private String LOGINNO  ; //登录编号  4管理传交易商号
	@JSONField(name="TOKEN")
	private String TOKEN    ;//认证TOKEN     
	@JSONField(name="DETYPE")
	private String DETYPE   ;//终端类型    1PC2安卓3苹果4管理  
	@JSONField(name="DEVICENO")
	private String DEVICENO ;//设备号        
	@JSONField(name="IP")
	private String IP       ;//IP地址        
	@JSONField(name="USERNO")
	private String USERNO   ;//登录编号      
	@JSONField(name="WARTNO")
	private String WARTNO      ;//          
	@JSONField(name="PAYSTATUS")
	private String PAYSTATUS    ;//          
	@JSONField(name="MEMO")
	private String MEMO    ;//     
	 
}
