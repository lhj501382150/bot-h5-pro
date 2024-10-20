package com.hml.backcore.req;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 委托挂牌
 * @author Administrator
 *
 */
@Data
public class REQ514 {

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
	private String WARENO   ;//商品代码
	@JSONField(name="PRICE")
	private String PRICE      ;//价格        
	@JSONField(name="NUM")
	private String NUM    ;//数量          
	@JSONField(name="BS")
	private String BS    ;//S卖-->挂出 B买--->摘牌购买          
	@JSONField(name="CONTNO")
	private String CONTNO    ;//卖出填订单号，买入填挂单号     
	@JSONField(name="MODE")
	private String MODE    ;//（1或空）竞价 2商城      
	@JSONField(name="EXT")
	private String EXT    ;//扩展信息     
	@JSONField(name="PAYPWD")
	private String PAYPWD    ;//支付密码      
	@JSONField(name="ARTID")
	private String ARTID    ;//资产编号 
	      
	
}
