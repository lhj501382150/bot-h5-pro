package com.hml.mall.entity.user;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-11-19
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_cardinfo")
public class UserCardinfo implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
    * 1加油卡2银行卡3支付宝
    */
    @TableField("CTYPE")
    private String ctype;

    @TableField("USERNO")
    private String userno;

    /**
    * 1中石化2中石油3中海油
    */
    @TableField("SERTYPE")
    private String sertype;

    /**
    * 加油卡/银行卡/支付宝号
    */
    @TableField("CARDNO")
    private String cardno;

    /**
    * 银行名称
    */
    @TableField("BANKNAME")
    private String bankname;

    /**
    * 开户行
    */
    @TableField("OPENBANK")
    private String openbank;

    /**
    * 0身份证1统一社会信用代码
    */
    @TableField("CARDTYPE")
    private String cardtype;

    /**
    * 证件号
    */
    @TableField("IDNO")
    private String idno;

    /**
    * 姓名
    */
    @TableField("SNAME")
    private String sname;

    /**
    * 手机号
    */
    @TableField("TELNO")
    private String telno;

    /**
    * 开户支行
    */
    @TableField("SUBBANK")
    private String subbank;

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String alias;
    
    @TableField("SIGSTATUS")
    private String sigstatus;
    
    @TableField("PROVINCE")
    private String province;
    
    @TableField("CITY")
    private String city;
    
    @TableField("BANKNO")
    private int bankno;

    @TableField("ISAUTH")
    private String isauth;
}
