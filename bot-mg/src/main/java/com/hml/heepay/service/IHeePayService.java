package com.hml.heepay.service;

import java.util.Map;

import com.hml.mall.entity.order.PayOrder;

/**
 * 支付服务
 * @author Administrator
 *
 */
public interface IHeePayService {

	void pay(Map<String,String> dataMap)throws Exception;
	
	void auth(String str)throws Exception;
	
	Map<String,String> queryStatus(PayOrder order)throws Exception;
}
