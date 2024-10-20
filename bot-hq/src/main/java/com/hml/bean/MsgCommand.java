package com.hml.bean;

public enum MsgCommand {
	QZ("抢庄"),XZ("下庄"),XZHU("下注");
	
	private String value;
	MsgCommand (String val) {
		value =val;
	}
	public String getValue() {
		return value;
	}
}
