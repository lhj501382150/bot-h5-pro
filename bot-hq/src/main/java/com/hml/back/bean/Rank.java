package com.hml.back.bean;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class Rank {

	private BigDecimal sumInteg;
	
	private String sCurTime;
	
	private List<IntegSeq> userList;
}
