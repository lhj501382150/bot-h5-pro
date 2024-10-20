package com.hml.mall.entity.money;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-14
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TB_ZJ_USERMONEY_CHANGE")
public class HisUsermoneyChange implements Serializable {

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
    * 变动前金额
    */
    @TableField("BEFMONEY")
    private BigDecimal befmoney;

    /**
    * 变动后金额
    */
    @TableField("AFTMONEY")
    private BigDecimal aftmoney;

    /**
    * 科目编号
    */
    @TableField("SUBNO")
    private String subno;

    /**
    * 备注
    */
    @TableField("MEMO")
    private String memo;
    
    @TableField("CTIME")
    private String ctime;

    /**
    * 扩展信息
    */
    @TableField("EXTEN")
    private String exten;
    
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String acctname;
    
    @TableField(exist = false)
    private String subname;

}
