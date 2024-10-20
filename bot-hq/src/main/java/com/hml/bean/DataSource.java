package com.hml.bean;

import java.io.Serializable;

import lombok.Data;

/**
* 
* @author hml
* @since 2023-12-14
@
*/
@Data
public class DataSource implements Serializable {
	private static final long serialVersionUID=1L;
	
    private Long id;
    private Long botId;
    private Long drawIssue;
    private String sResult;
    private String sTime;
    private String mode;
    private String hash;
}
