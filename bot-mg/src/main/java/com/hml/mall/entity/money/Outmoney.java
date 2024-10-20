package com.hml.mall.entity.money;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2021-11-19
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_zj_outmoney")
public class Outmoney implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 主键
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("FDATE")
    private String fdate;

    @TableField("USERNO")
    private String userno;

    @TableField("CHMONEY")
    private BigDecimal chmoney;
    
    @TableField("OUTMONEY")
    private BigDecimal outmoney;
    
    @TableField("TCOMM")
    private BigDecimal tcomm;

    @TableField("PAYCHAN")
    private String paychan;

    @TableField("RECCUSTNO")
    private String reccustno;
    
    @TableField("OPENBANK")
    private String openbank;

    @TableField("CONTNO")
    private String contno;

    @TableField("MEMO")
    private String memo;

    @TableField("RECVMSG")
    private String recvmsg;

    @TableField("CTIME")
    private String ctime;

    @TableField("CHKTIME")
    private String chktime;

    @TableField("CHKUSERNO")
    private String chkuserno;

    @TableField("CHKIP")
    private String chkip;

    @TableField("PAYSTATUS")
    private String paystatus;

    @TableField("CHKSTATUS")
    private String chkstatus;

    @TableField("REQADDR")
    private String reqaddr;

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private Long cardId;
    
    @TableField(exist = false)
    private String BANKNO;
    
    @TableField(exist = false)
    private String BANKNAME;
    
    @TableField(exist = false)
    private String CARDNO;
    
    @TableField(exist = false)
    private String SNAME;
    
    @TableField(exist = false)
    private String PROVINCE;
    
    @TableField(exist = false)
    private String CITY;
    
    @TableField(exist = false)
    private String SUBBANK;
    
    /**
     * 证件号
     */
     @TableField(exist = false)
     private String IDNO;

}
