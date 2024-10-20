package com.hml.mall.entity.money;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("TB_ZJ_USERMONEY")
public class Usermoney implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 结算日期
    */
    @TableField("FDATE")
    private String fdate;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 账户编号
    */
    @TableField("ACCTNO")
    private String acctno;

    /**
    * 期初
    */
    @TableField("START")
    private BigDecimal start;

    /**
    * 入金
    */
    @TableField("INMONEY")
    private BigDecimal inmoney;

    /**
    * 出金
    */
    @TableField("OUTMONEY")
    private BigDecimal outmoney;
    /**
     * 入金
     */
     @TableField("`IN`")
     private BigDecimal in;

     /**
     * 出金
     */
     @TableField("`OUT`")
     private BigDecimal out;
    /**
    * 收入
    */
    @TableField("INCOME")
    private BigDecimal income;

    /**
    * 支出
    */
    @TableField("PAYMENT")
    private BigDecimal payment;

    /**
    * 期末
    */
    @TableField("BALANCE")
    private BigDecimal balance;

    /**
    * 冻结
    */
    @TableField("FREEZE")
    private BigDecimal freeze;

    /**
    * 盈亏
    */
    @TableField("LOSE")
    private BigDecimal lose;

    /**
    * 可用
    */
    @TableField("ENABLE")
    private BigDecimal enable;

    /**
    * 权益
    */
    @TableField("ALLQUITY")
    private BigDecimal allquity;

    /**
    * 限出
    */
    @TableField("LIMTOUT")
    private BigDecimal limtout;

    /**
    * 可出
    */
    @TableField("CANOUT")
    private BigDecimal canout;

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String nickname;
    
    @TableField(exist = false)
    private String acctname;
    
}
