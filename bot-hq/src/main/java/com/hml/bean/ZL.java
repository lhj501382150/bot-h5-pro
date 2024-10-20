package com.hml.bean;

public enum ZL {

	JIAO("角",2,1f),CHUAN("串",2,1f),GU("古",1,3f),
	MING("明",3,0.33f),TONG("同",2,2f,0f),ZHENG("正念",2,2f,0f),WEI("尾",2,1.5f,0.5f);
	String text = "";
	Integer num;
	Float rate1;
	Float rate2;
	ZL(String text,Integer num,Float rate){
		this.text = text;
		this.num = num;
		this.rate1 = rate;
	}
	ZL(String text,Integer num,Float rate1,Float rate2){
		this.text = text;
		this.num = num;
		this.rate1 = rate1;
		this.rate2 = rate2;
	}
	
	public String getText() {
		return text;
	}
	
	public Float getRate() {
		return rate1;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Float getRate2() {
		return rate2;
	}
	public void setRate2(Float rate2) {
		this.rate2 = rate2;
	}
}
