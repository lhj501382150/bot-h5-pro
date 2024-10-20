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
* @since 2021-04-05
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_sys_para")
public class Para implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 配置编号
    */
    @TableId(value = "SYSID", type = IdType.AUTO)
    private Integer sysid;

    /**
    * 配置名称
    */
    @TableField("SYSNAME")
    private String sysname;

    /**
    * 配置类型
    */
    @TableField("SYSTYPE")
    private String systype;
    /**
     * 配置名称
     */
     @TableField("groupname")
     private String groupname;

    /**
    * 配置值
    */
    @TableField("SVAL")
    private String sval;

    /**
    * 描述
    */
    @TableField("MEMO")
    private String memo;

    /**
    * 是否有效(Y 有效 N 无效)
    */
    @TableField("ISVALID")
    private String isvalid;


}
