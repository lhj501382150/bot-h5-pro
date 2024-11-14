package com.hml.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BackCoreConfig {
	/*
	 * 后台访问路径
	 */
	public static String URL = "";
	
	public static String IN_MONEY = "/Account/InMoney";
	public static String OUT_MONEY = "/Account/OutMoney";
	public static String QUERY_MONEY = "/Account/QueryMoney";
	public static String ADD_DATA = "/Data/AddData";
	public static String ADD_GROUPID = "/Data/AddGroupId";
	public static String GET_DRAW = "/Data/GetData";
	public static String QUERY_DATA = "/Data/GetData";
	public static String ROB_ORDER = "/Order/RobOrder";
	public static String DOWN_ROB = "/Order/DownRob";
	public static String ORDER = "/Order/Order";
	public static String QUERY_ORDRE = "/Order/OrderQuery";
	public static String ADD_ACCOUNT = "/Account/AddAccount";
	public static String ADD_HQ = "/BlockChain/AddData";


	@Value("${backcore.sendUrl}")
	public void setUrl(String url) {
		URL = url;
	}
}
