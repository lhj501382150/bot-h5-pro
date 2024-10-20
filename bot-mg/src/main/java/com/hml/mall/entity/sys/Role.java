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
* @since 2021-04-05
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_sys_role")
public class Role implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 角色编号
    */
    @TableId(value = "ROLE_ID", type = IdType.AUTO)
    private Integer roleId;

    /**
    * 角色名称
    */
    @TableField("ROLE_NAME")
    private String roleName;

    /**
    * 备注
    */
    @TableField("REMARK")
    private String remark;
    
    @TableField("OWNER")
    private String owner;


}
