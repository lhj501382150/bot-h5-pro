package com.hml.backcore.req;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 商品在线管控
 * @author Administrator
 *
 */
@Data
public class REQ517 {

	@JSONField(name="LOGINNO")
	private String LOGINNO  ; //登录编号  4管理传交易商号
	@JSONField(name="USERNO")
	private String USERNO    ;//用户编号  
	@JSONField(name="TOKEN")
	private String TOKEN    ;//认证TOKEN     
	@JSONField(name="DETYPE")
	private String DETYPE   ;//终端类型    1PC2安卓3苹果4管理  
	@JSONField(name="DEVICENO")
	private String DEVICENO ;//设备号        
	@JSONField(name="IP")
	private String IP       ;//IP地址    
	
	@JSONField(name="WARENO")
	private String WARENO      ;//资产编号        
	
	@JSONField(name="HSTATUS")
	private String HSTATUS    ;//Y挂起N正常  
	      
	@JSONField(name="NUM")
	private String NUM    ;//数量         
	
	@JSONField(name="SALNUM")
	private String SALNUM    ;//数量         
	
}
