package com.hml.mall.entity.sys;

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
* @since 2021-04-13
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_sys_login_log")
public class LoginLog implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "WATERID", type = IdType.AUTO)
    private Long waterid;

    @TableField("LOGINNO")
    private String loginno;

    @TableField("USERNO")
    private String userno;

    /**
    * 1pc2安卓3苹果
    */
    @TableField("DETYPE")
    private String detype;

    @TableField("TOKEN")
    private String token;

    @TableField("LOGTIME")
    private String logtime;

    @TableField("IPADDR")
    private String ipaddr;

    @TableField("DEVICENO")
    private String deviceno;

    /**
    * 1成功2失败
    */
    @TableField("ISSUCCESS")
    private Integer issuccess;

    /**
    * 1登录2退出
    */
    @TableField("INOROUT")
    private Integer inorout;

    @TableField("MEMO")
    private String memo;

}
