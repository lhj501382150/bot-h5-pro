package com.hml.mall.entity.sys;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-05
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TB_SYS_LOGIN")
public class Login implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 登陆账号
    */
	@TableId("LOGINNO")
    private String loginno;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 登陆名称
    */
    @TableField("LOGINNAME")
    private String loginname;

    /**
    * 昵称
    */
    @TableField("NICKNAME")
    private String nickname;

    /**
    * 登陆密码
    */
    @TableField("LOGINPWD")
    private String loginpwd;

    /**
    * 手机号
    */
    @TableField("TELNO")
    private String telno;

    /**
    * QQNO
    */
    @TableField("QQNO")
    private String qqno;

    /**
    * 微信号
    */
    @TableField("WEIXINNO")
    private String weixinno;

    /**
    * 邮箱地址
    */
    @TableField("EMAILADDR")
    private String emailaddr;

    /**
    * 是否启用（Y 启用 N 停用）
    */
    @TableField("ISVALID")
    private String isvalid;
    /**
     * 账号类型（M 管理员 C 客户）
     */
    @TableField("TYPE")
    private Integer type;
    
    @TableField("TOKEN")
    private String token;
    
    @TableField("LOGTIME")
    private String logtime;
    
    @TableField("FAILNUM")
    private Integer failnum;
    
    @TableField("IPADDR")
    private String ipaddr;
    
    @TableField("DEVICENO")
    private String deviceno;
    
    @TableField("INOROUT")
    private Integer inorout;
    
    @TableField(exist = false)
    private Integer clevel;

    @TableField(exist = false)
    private UserRole userRole;

    @TableField(exist = false)
    private String roleName;
}
