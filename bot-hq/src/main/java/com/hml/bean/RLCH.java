package com.hml.bean;

public enum RLCH {

	RU("入",1),LONG("龙",2),CHU("出",3),HU("虎",4);
	String text = "";
	Integer num;
	RLCH(String text,Integer num){
		this.text = text;
		this.num = num;
	}
	
	public String getText() {
		return text;
	}
	public Integer getNum() {
		return num;
	}
}
