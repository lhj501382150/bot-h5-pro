package com.hml.mall.entity.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-11
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user")
public class User implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 客户编号
    */
	@TableId("USERNO")
    private String userno;

    /**
    * 客户名称
    */
    @TableField("USERNAME")
    private String username;
    /**
     * 客户名称
     */
    @TableField("NICKNAME")
    private String nickname;

    /**
    * 法定代表人
    */
    @TableField(value = "REPRESENTNAME",fill = FieldFill.UPDATE)
    private String representname;

    /**
    * 1 机构 2 普通用户
    */
    @TableField("ORGTYPE")
    private Integer orgtype;

    /**
    * 是否允许邀请 （0 不允许 1 允许）
    */
    @TableField("USERTYPE")
    private Integer usertype;

    /**
    * 证件类型(0身份证1统一社会信用代码)
    */
    @TableField("CARDTYPE")
    private Integer cardtype;

    /**
    * 证件号
    */
    @TableField("IDENTIFICATION")
    private String identification;

    /**
    * 工商注册号
    */
    @TableField(value = "REGISTRATIONNO",fill = FieldFill.UPDATE)
    private String registrationno;

    /**
    * 纳税人识别号
    */
    @TableField(value = "TAXPAYERNO",fill = FieldFill.UPDATE)
    private String taxpayerno;

    /**
    * 组织机构代码
    */
    @TableField(value = "ORGANIZATION",fill = FieldFill.UPDATE)
    private String organization;

    /**
    * 注册地址
    */
    @TableField(value = "REGADDRESS",fill = FieldFill.UPDATE)
    private String regaddress;

    /**
    * 注册电话
    */
    @TableField(value = "REGTELNO",fill = FieldFill.UPDATE)
    private String regtelno;

    /**
    * 开户银行
    */
    @TableField("OPENBANK")
    private String openbank;

    /**
    * 银行账户
    */
    @TableField("BANKNO")
    private String bankno;

    /**
    * 企业联系人
    */
    @TableField(value = "LINKNAME",fill = FieldFill.UPDATE)
    private String linkname;

    /**
    * 联系人手机号
    */
    @TableField(value = "TELNO",fill = FieldFill.UPDATE)
    private String telno;

    /**
    * 联系人QQ号
    */
    @TableField(value = "QQNO",fill = FieldFill.UPDATE)
    private String qqno;

    /**
    * 联系人邮箱地址
    */
    @TableField(value = "EMAILADDR",fill = FieldFill.UPDATE)
    private String emailaddr;

    /**
    * 个人为头像地址
    */
    @TableField(value = "LOGOADDR",fill = FieldFill.UPDATE)
    private String logoaddr;

    /**
    * Y启用N不启用
    */
    @TableField("ISVALID")
    private String isvalid;

    @TableField("SEX")
    private String sex;
    
    @TableField("OPENID")
    private String openid;
    /**
    * 注册日期
    */
    @TableField("REGDATE")
    private String regdate;

    
    @TableField("PAYPWD")
    private String paypwd;

    /**
    * 最后更新时间
    */
    @TableField("LAST_UPDATE")
    private LocalDateTime lastUpdate;
    
    @TableField(exist = false)
    private Integer clevel; //当前等级
    @TableField(exist = false)
    private String parentno;  //所属机构
    @TableField(exist = false)
    private String tjusername;   //推荐人
    @TableField(exist = false)
    private String tjnickname;   //推荐人
    
    @TableField(exist = false)
    private String realstatus;   //实名状态 0未申请1未认证2已认证3已驳回
    
    @TableField(exist = false)
    private Integer roleId;
    
    @TableField(exist = false)
    private BigDecimal enable;   //
    
    @TableField(exist = false)
    private BigDecimal freeze;   //
    
    @TableField(exist = false)
    private BigDecimal balance;   //
    
    @TableField(exist = false)
    private BigDecimal limtout;   //
    

}
