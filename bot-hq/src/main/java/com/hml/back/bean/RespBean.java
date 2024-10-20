package com.hml.back.bean;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class RespBean {
	private String botId;               //机器人ID                                  
	private String dataId;         //场次编号                                        
	private String userId;             //用户号                                     
	private String userName;          //昵称                                       
	private BigDecimal integRal;        //积分                                     
	private BigDecimal winInteg;        //盈利积分                                   
	private BigDecimal dComm;           //服务费                                    
	private Integer iSumNum;         //总数                                        
	private Integer iWinNum;         //盈利数                                       
	private Integer iLossNum;        //亏损数                                       
	private Integer iStatus;         //状态抢庄(1),抢庄结束(2),下注(3),下注结束(4),结算完成(5)     
	private Integer iWinNo;         //中奖号码                                      
	private BigDecimal dSumInteg;      //下注金额                                    
	private Integer iCount;      //下注金额                                    
	private BigDecimal dBef;      //下注金额                                    
	private BigDecimal dEnd;      //下注金额                                    
	private List<Order> orderMx;
	private Rank rankList;
	
	private String drawIssue;
	private String sReust;
}
