package com.hml.backcore.config;

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
	public static String DOWN_ROB = "/Order/DownRob";
	public static String ADD_GROUPID = "/Data/AddGroupId";
	public static String CLEAR_MONEY = "/Account/ClearAll";
	public static String ALTER_BOT_INTEGE = "/Data/MdiBodIntege";
	public static String ALTER_PARA = "/Data/MdiSysPara";
	public static String ADD_MANA_DATA = "/Data/ManaAddData";
	public static String EDIT_ORDER_STATUS = "/Data/SetOrderStatus";
	public static String GET_ORDER_RESULT = "/Data/ManaGetHash";
	
	public static String ADD_ACCOUNT = "/Account/AddAccount";
//	账户手动金额调整
	public static String MONEY_CHANGE = "/trade510";
//	陪玩订单完成
	public static String PLAY_ORDER = "/trade523";
//	赛事结果审核
	public static String ACTIVITY_AUDIT = "/trade524";
//	商品在线管控
	public static String WARE_CONTROL = "/trade517";
//	数字资产 发行 转赠 终止
	public static String USER_DIGITART = "/trade518";
//	土地资产 发行 转赠 终止
	public static String USER_LANDINFO = "/trade518";
//	委托挂牌指令
	public static String TRADE514 = "/trade514";
//	支付回馈结果
	public static String TRADE530 = "/trade530";
//	数字资产区块链发行/转增/终止 1发行2转增3终止4冻结<br>5管理手工上链6自主创作（ARTID不为空为撤销）7空投奖励
	public static String TRADE518 = "/trade518";


	@Value("${backcore.sendUrl}")
	public void setUrl(String url) {
		URL = url;
	}
}
