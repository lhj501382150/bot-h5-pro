package com.hml.mall.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("tb_jy_his_order")
public class HisOrder implements Serializable {

private static final long serialVersionUID=1L;

	@TableId("ORDERNO")
    private String orderno;
	
	@TableField("FDATE")
    private String fdate;

    @TableField("USERNO")
    private String userno;
    
    @TableField("CONTNO")
    private String contno;

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

    @TableField("NUM")
    private Integer num;

    @TableField("CONTNUM")
    private Integer contnum;

    @TableField("BAILMONEY")
    private BigDecimal bailmoney;
    @TableField("COMM")
    private BigDecimal comm;
    @TableField("CPRIGHT")
    private BigDecimal cpright;
    @TableField("TRANSCOMM")
    private BigDecimal transcomm;

    @TableField("LOSS")
    private BigDecimal loss;

    @TableField("STATUS")
    private String status;

    /**
    * yyyy-MM-dd HH:mm:ss
    */
    @TableField("ORDTIME")
    private String ordtime;

//    @TableField("ORDTYPE")
//    private String ordtype;

//    @TableField("LOGINNO")
//    private String loginno;

    /**
    * IP+MAC 移动端设备号
    */
//    @TableField("ORDDEV")
//    private String orddev;

    /**
    * yyyy-MM-dd HH:mm:ss
    */
    @TableField("CANTIME")
    private String cantime;

//    @TableField("CLOGINNO")
//    private String cloginno;

    /**
    * IP+MAC 移动端设备号
    */
//    @TableField("CANDEV")
//    private String candev;
    
    @TableField("ARTID")
    private String artid;
    
    @TableField("MODE")
    private String mode;

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String nickname;
    
    @TableField(exist = false)
    private String warename;
    @TableField(exist = false)
    private String tjusername;
    
    @TableField(exist = false)
    private String tjnickname;
    
}
