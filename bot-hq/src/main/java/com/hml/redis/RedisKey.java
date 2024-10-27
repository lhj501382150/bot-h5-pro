package com.hml.redis;

import com.hml.config.BotConfig;

public class RedisKey {
	
	public static String DRAW_RESULT = "DRAW_RESULT:";
	
	public static String CURRENT_STATUS = "CURRENT_STATUS:" + BotConfig.CHAT_ID;
	public static String CURRENT_STATUS_N = "CURRENT_STATUS_N:" + BotConfig.CHAT_ID;
	public static String ORDER_QUERY_N = "ORDER_QUERY_N:" + BotConfig.CHAT_ID;
	public static String ORDER_RESULT_N = "ORDER_RESULT_N:" + BotConfig.CHAT_ID;
	
	public static String CURRENT_STATUS_MODE = "CURRENT_STATUS_MODE:";
	public static String ORDER_QUERY_MODE = "ORDER_QUERY_MODE:";
	public static String ORDER_RESULT_MODE = "ORDER_RESULT_MODE:";
	
	public static String ROB_MIN_MONEY = "ROB_MIN_MONEY";
	public static String ROB_MAX_MONEY = "ROB_MAX_MONEY";
	
	public static String ROB_STATUS = "ROB_STATUS:";
	
	public static String ORDER_MIN_MONEY = "ORDER_MIN_MONEY";
	public static String ORDER_MAX_MONEY = "ORDER_MAX_MONEY";
	
	public static String ORDER_QUERY = "ORDER_QUERY:" + BotConfig.CHAT_ID;
	
//	资金支付订单结果通知
	public static String START_ROB = "START_ROB:" + BotConfig.CHAT_ID;
	
	public static String CONFIRM_ROB = "CONFIRM_ROB:" + BotConfig.CHAT_ID;
	public static String DRAW_ORDER = "DRAW_ORDER:" + BotConfig.CHAT_ID;
	
	public static String STOP_ORDER = "ADD_ORDER:" + BotConfig.CHAT_ID;
	
	public static String NOTIFY_RESULT = "NOTIFY_RESULT:" + BotConfig.CHAT_ID;
	
	public static String ORDER_RESULT = "ORDER_RESULT:" + BotConfig.CHAT_ID;
	
	public static String ROB_SEND_PIC ="ROB_SEND_PIC:" + BotConfig.CHAT_ID;
	
	public static String CHAT_INVITE_LINK = "CHAT_INVITE_LINK:" + BotConfig.CHAT_ID;
	
	public static String ROB_DOWN = "ROB_DOWN:" + BotConfig.CHAT_ID;
	
	public static String USER_INVITE_AUTH = "USER_INVITE_AUTH";
	
	public static String SYSTEM_STATUS =  "SYSTEM:STATUS";
	
	public static String BLACK_IP_REQUEST = "BLACK_IP_REQUEST:";
	
	public static String BLACK_IP_LIST = "BLACK_IP_LIST";
}
