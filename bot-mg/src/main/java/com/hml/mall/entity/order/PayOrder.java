package com.hml.mall.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hml.mall.entity.user.UserAddress;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-07-10
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_zj_payorder")
public class PayOrder implements Serializable {

private static final long serialVersionUID=1L;

	/**
	 * 主键
	 */
	 @TableId(value = "WARTNO", type = IdType.AUTO)
	 private Long wartno;
	
	 /**
	 * 结算日期
	 */
	 @TableField("FDATE")
	 private String fdate;
	
	 /**
	 * 用户编号
	 */
	 @TableField("USERNO")
	 private String userno;
	
	 /**
	 * 账本编号
	 */
	 @TableField("ACCTNO")
	 private String acctno;
	
	 /**
	 * 变动资金
	 */
	 @TableField("CHMONEY")
	 private BigDecimal chmoney;
	
	 /**
	 * 业务类型
	 */
	 @TableField("BUSTYPE")
	 private String bustype;
	
	 /**
	 * 业务单编号
	 */
	 @TableField("CONTNO")
	 private String contno;
	
	 @TableField("PAYTYPE")
	 private String paytype;
	
	 /**
	 * 状态
	 */
	 @TableField("PAYSTATUS")
	 private String paystatus;
	
	 @TableField("OPPUSERNO")
	 private String oppuserno;
	
	 @TableField("PAYCHAN")
	 private String paychan;
	
	 @TableField("RECVMSG")
	 private String recvmsg;
	
	 @TableField("CTIME")
	 private String ctime;
	
	 /**
	 * 备注
	 */
	 @TableField("MEMO")
	 private String memo;
	
	 /**
	 * 扩展信息
	 */
	 @TableField("EXTEN")
	 private String exten;
	
	 @TableField("SUBNO")
	 private String subno;
	
	 @TableField("IP")
	 private String ip;
	
	 @TableField("OPENID")
	 private String openid;
	
	 @TableField("DEVNO")
	 private String devno;
	
	 /**
	  * 同步账本
	  */
	  @TableField("SYNC")
	  private String sync;
	  
	  @TableField(exist=false)
	  private String username;
	  
	  @TableField(exist=false)
	  private String acctname;
	  
	  @TableField(exist=false)
	  private String subname;
}
