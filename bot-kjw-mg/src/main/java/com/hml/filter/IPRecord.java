package com.hml.filter;

import lombok.Data;

@Data
public class IPRecord{

	private String ipAddr;
	
	private int num;
	
	private long preTime;
	
	private long curTime;
	
	private String uri;
	
	private long firstTime;
}
