/*
 * @(#) Response.java 2020-11-03
 *
 * Copyright 2020 NetEase.com, Inc. All rights reserved.
 */

package com.hml.sms.bean;

import java.util.List;

/**
 * 短信发送操作的响应信息
 * 
 * @author huangchao04
 * @version 2020-11-03
 */
public class SendResponse {
    private int resultCode;
    private String resultDesc;
    private List<Data> resultLists;

      

    public int getResultCode() {
		return resultCode;
	}



	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}



	@Override
	public String toString() {
		return "SendResponse [resultCode=" + resultCode + ", resultDesc=" + resultDesc + ", resultLists=" + resultLists
				+ "]";
	}



	public String getResultDesc() {
		return resultDesc;
	}



	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}



	public List<Data> getResultLists() {
		return resultLists;
	}



	public void setResultLists(List<Data> resultLists) {
		this.resultLists = resultLists;
	}



	public static class Data {
    	
    	private String mobile;
        private int resultCode;
        private String resultDesc;
        private String messageId;
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public int getResultCode() {
			return resultCode;
		}
		public void setResultCode(int resultCode) {
			this.resultCode = resultCode;
		}
		public String getResultDesc() {
			return resultDesc;
		}
		public void setResultDesc(String resultDesc) {
			this.resultDesc = resultDesc;
		}
		public String getMessageId() {
			return messageId;
		}
		public void setMessageId(String messageId) {
			this.messageId = messageId;
		}
		@Override
		public String toString() {
			return "Data [mobile=" + mobile + ", resultCode=" + resultCode + ", resultDesc=" + resultDesc
					+ ", messageId=" + messageId + "]";
		}

         
    }
}
