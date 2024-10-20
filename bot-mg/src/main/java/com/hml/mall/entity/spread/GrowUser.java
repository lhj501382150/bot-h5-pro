package com.hml.mall.entity.spread;

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
* @since 2021-09-06
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jl_grow_user")
public class GrowUser implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "WATERNO", type = IdType.AUTO)
    private Long waterno;

    /**
    * 日期
    */
    @TableField("FDATE")
    private String fdate;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 任务编号
    */
    @TableField("ID")
    private Long id;

    /**
    * 状态0未完成1已完成
    */
    @TableField("GSTATUS")
    private String gstatus;

    /**
    * 成长值
    */
    @TableField("REWNUM")
    private Integer rewnum;

    /**
    * 时间
    */
    @TableField("CTIME")
    private String ctime;

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String lname;
}
