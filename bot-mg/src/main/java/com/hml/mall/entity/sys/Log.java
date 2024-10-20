package com.hml.mall.entity.sys;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("tb_sys_log")
public class Log implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 主键
    */
    @TableId(value = "LOG_ID", type = IdType.AUTO)
    private Long logId;

    /**
    * 用户名
    */
    @TableField("OPTUSER")
    private String optuser;

    /**
    * 操作
    */
    @TableField("OPERATION")
    private String operation;

    /**
    * 请求方法
    */
    @TableField("METHOD")
    private String method;

    /**
    * 请求参数
    */
    @TableField("PARAMS")
    private String params;

    /**
    * 执行时长(毫秒)
    */
    @TableField("TIME")
    private Long time;

    /**
    * IP地址
    */
    @TableField("IP")
    private String ip;

    /**
    * 最后更新时间
    */
    @TableField("LAST_UPDATE")
    private LocalDateTime lastUpdate;


}
