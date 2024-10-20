package com.hml.backcore.req;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 数字资产发行 转赠 终止
 * @author Administrator
 *
 */
@Data
public class REQ518 {

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
	
	@JSONField(name="OPTYPE")
	private String OPTYPE   ;//1发行2转增3终止4冻结
	@JSONField(name="ARTID")
	private String ARTID      ;//资产编号          
	@JSONField(name="OPUSERNO")
	private String OPUSERNO    ;//转增账号          
	      
	@JSONField(name="NUM")
	private String NUM    ;//数量         
	@JSONField(name="MEM")
	private String MEM    ;//备注         
	
}
