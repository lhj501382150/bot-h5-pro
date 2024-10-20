package com.hml.mall.entity.sys;

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
* @since 2024-06-25
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_sys_h5_log")
public class H5Log implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "LogNo", type = IdType.AUTO)
    private Long logNo;

    /**
    * 操作账号
    */
    @TableField("Account")
    private String account;

    /**
    * 请求路径
    */
    @TableField("RequestUrl")
    private String requestUrl;

    /**
    * 方式
    */
    @TableField("HttpType")
    private String httpType;

    /**
    * 请求数据
    */
    @TableField("Body")
    private String body;

    /**
    * 反馈数据
    */
    @TableField("ResponseBody")
    private String responseBody;

    /**
    * 时间
    */
    @TableField("ElapsedTime")
    private String elapsedTime;

    /**
    * 开始时间
    */
    @TableField("StartTime")
    private String startTime;

    /**
    * 结束时间
    */
    @TableField("EndTime")
    private String endTime;

}
