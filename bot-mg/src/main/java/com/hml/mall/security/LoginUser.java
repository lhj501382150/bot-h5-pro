package com.hml.mall.security;
/**
 * 登陆用户信息
 * @author Administrator
 *
 */

import lombok.Data;

@Data
public class LoginUser {

	private String loginno;
	
	private String loginname;
	
	private Integer type;  // 0 管理员 1 普通用户 2 客户
	
	private String userno;
	
	private String username;
	
	private Integer orgtype; // 1 机构 2 客户
	
	private Integer clevel;
	
	private String parentno;
	
	private String tjrno;
	
//	获取查询当前级别数据
	public String getQueryNo() {
		if(clevel!=null) {
			return "uno"+ clevel;
		}else {
			return null;
		}
	}
}
