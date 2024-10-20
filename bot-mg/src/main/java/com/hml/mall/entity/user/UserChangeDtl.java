package com.hml.mall.entity.user;

import com.alibaba.fastjson.JSONObject;
import com.hml.utils.StringUtils;

public class UserChangeDtl {

	private String USERNO;
//	0普通1青铜 2白银 3黄金
	private String VIPLEV;
//	0未申请1未认证2已认证3已驳回
	private String REALSTATUS;
	private String REALFILE1;
	private String REALFILE2;
	private String REALFILE3;
	private String REALFILE4;
	private String REALFILE5;
	
	public UserChangeDtl(String userno,String str) {
		System.out.println("----------" + str);
		JSONObject obj = null;
		try {
			obj = JSONObject.parseObject(str);
		} catch (Exception e) {
			obj = new JSONObject();
		}
		this.USERNO = userno;
		this.VIPLEV = fv(obj, "VIPLEV");
		this.REALSTATUS = fv(obj, "REALSTATUS");
		this.REALFILE1 = fv(obj, "REALFILE1");
		this.REALFILE2 = fv(obj, "REALFILE2");
		this.REALFILE3 = fv(obj, "REALFILE3");
		this.REALFILE4 = fv(obj, "REALFILE4");
		this.REALFILE5 = fv(obj, "REALFILE5");
	}
	
	private String fv(JSONObject obj,String key) {
		String val = "";
		if(!StringUtils.isBlank(obj.get(key))) {
			val = obj.getString(key);
		}else if(!StringUtils.isBlank(obj.get(key.toLowerCase()))) {
			val = obj.getString(key.toLowerCase());
		}
		return val;
	}
	public String getUSERNO() {
		return USERNO;
	}

	public void setUSERNO(String uSERNO) {
		USERNO = uSERNO;
	}

	public String getVIPLEV() {
		return VIPLEV;
	}

	public void setVIPLEV(String vIPLEV) {
		VIPLEV = vIPLEV;
	}

	public String getREALSTATUS() {
		return REALSTATUS;
	}

	public void setREALSTATUS(String rEALSTATUS) {
		REALSTATUS = rEALSTATUS;
	}

	public String getREALFILE1() {
		return REALFILE1;
	}

	public void setREALFILE1(String rEALFILE1) {
		REALFILE1 = rEALFILE1;
	}

	public String getREALFILE2() {
		return REALFILE2;
	}

	public void setREALFILE2(String rEALFILE2) {
		REALFILE2 = rEALFILE2;
	}

	public String getREALFILE3() {
		return REALFILE3;
	}

	public void setREALFILE3(String rEALFILE3) {
		REALFILE3 = rEALFILE3;
	}

	public String getREALFILE4() {
		return REALFILE4;
	}

	public void setREALFILE4(String rEALFILE4) {
		REALFILE4 = rEALFILE4;
	}

	public String getREALFILE5() {
		return REALFILE5;
	}

	public void setREALFILE5(String rEALFILE5) {
		REALFILE5 = rEALFILE5;
	}
 
	
}
