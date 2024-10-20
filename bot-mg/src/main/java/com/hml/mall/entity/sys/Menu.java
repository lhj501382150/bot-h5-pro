package com.hml.mall.entity.sys;

import java.io.Serializable;
import java.util.List;

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
@TableName("tb_sys_menu")
public class Menu implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 主键
    */
	@TableId("MENU_ID")	
    private String menuId;

    /**
    * 菜单名称
    */
    @TableField("MENU_NAME")
    private String menuName;

    /**
    * 父菜单ID，一级菜单为0
    */
    @TableField("PARENT_ID")
    private String parentId;

    /**
    * 菜单URL
    */
    @TableField("MENU_URL")
    private String menuUrl;

    /**
    * 授权
    */
    @TableField("PERMS")
    private String perms;

    /**
    * 类型   0：目录   1：菜单   2：按钮
    */
    @TableField("TYPE")
    private Integer type;

    /**
    * 菜单图标
    */
    @TableField("ICON")
    private String icon;

    /**
    * 排序
    */
    @TableField("ORDER_NO")
    private Integer orderNo;

 // 非数据库字段
    @TableField(exist = false)
    private String parentName;
    // 非数据库字段
    @TableField(exist = false)
    private Integer level;
    // 非数据库字段
    @TableField(exist = false)
    private List<Menu> children;

}
