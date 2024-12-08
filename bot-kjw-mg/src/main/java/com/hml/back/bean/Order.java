package com.hml.back.bean;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Order {

	private String dataId;
	private String userId;                       //用户号      
	private String userName;                    //昵称        
	private BigDecimal integRal;                  //积分      
	private Integer winLose;                 //胜负 0胜1负   
	private BigDecimal winInteg;                  //      盈利积分 
	private BigDecimal fRate;                     //   赔率
	private BigDecimal dBef;                     //   赔率
	private BigDecimal dEnd;                     //   赔率
	private String RulId;                    //昵称        
	private String seq;
}
