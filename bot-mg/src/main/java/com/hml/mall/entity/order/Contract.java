package com.hml.mall.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hml.mall.entity.user.UserAddress;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-18
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_contract")
public class Contract implements Serializable {

private static final long serialVersionUID=1L;

	@TableId("CONTNO")
    private String contno;
	
	 @TableField("FDATE")
	 private String fdate;


    /**
    * 模式 0 普通 1现货2竞价3预售
    */
    @TableField("MODE")
    private Integer mode;

    @TableField("USERNO")
    private String userno;

    @TableField("WARENO")
    private String wareno;

    @TableField("OPENFLAT")
    private String openflat;

    @TableField("BUYORSAL")
    private String buyorsal;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("FLATPRICE")
    private BigDecimal flatprice;

    @TableField("FLATNUM")
    private Integer flatnum;

    @TableField("NUM")
    private Integer num;

    @TableField("SETNUM")
    private Integer setnum;

    @TableField("TRADECOMM")
    private BigDecimal tradecomm;

    @TableField("BAILMONEY")
    private BigDecimal bailmoney;
    
    @TableField("CPRIGHT")
    private BigDecimal cpright;
    
    @TableField("TRANSCOMM")
    private BigDecimal transcomm;

    @TableField("LOSS")
    private BigDecimal loss;

    @TableField("STATUS")
    private String status;

    @TableField("CTIME")
    private String ctime;

    /**
    * yyyy-MM-dd HH:mm:ss
    */
    @TableField("ORDTIME")
    private String ordtime;

    @TableField("ORDTYPE")
    private String ordtype;

    @TableField("LOGINNO")
    private String loginno;

    @TableField("ORDERNO")
    private String orderno;

    /**
    * IP+MAC 移动端设备号
    */
    @TableField("ORDDEV")
    private String orddev;
    
    @TableField("ACCTNO")
	 private String acctno;
    
    @TableField("ARTID")
    private String artid;

    @TableField(exist = false)
    private UserAddress address;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String warename;
    
    @TableField(exist = false)
    private String uno1;
    
    @TableField(exist = false)
    private String uno2;
    
    @TableField(exist = false)
    private String tjno;
    
    @TableField(exist = false)
    private String sendstatus;
    
    @TableField(exist = false)
    private String invoicestatus;
    
    @TableField(exist = false)
    private String acctname;

}
