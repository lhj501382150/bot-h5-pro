package com.hml.back.bean;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RespMoney {

	private String userId;
	private String userName;
	private BigDecimal blance;
	private BigDecimal freeze;
	private BigDecimal enable;
}
