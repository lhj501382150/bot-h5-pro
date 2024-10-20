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
@TableName("tb_sys_user_role")
public class UserRole implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 主键
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
    * 用户编号
    */
    @TableField("LOGINNO")
    private String loginno;

    /**
    * 角色编号
    */
    @TableField("ROLE_ID")
    private Integer roleId;


}
