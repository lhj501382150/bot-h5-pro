package com.hml.mall.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2021-07-10
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_sys_dict")
public class Dict implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 字典编号
    */
	@TableField("DICTNO")
    private String dictno;

    /**
    * 字段值
    */
    @TableField("SVAL")
    private String sval;

    /**
    * 显示内容
    */
    @TableField("SSHOW")
    private String sshow;

    /**
    * 描述
    */
    @TableField("DICTINFO")
    private String dictinfo;

    /**
    * 表名
    */
    @TableField("TABNAME")
    private String tabname;

    /**
    * 字段名
    */
    @TableField("FIELDNAME")
    private String fieldname;


    /**
     * 排序
     */
     @TableField("SEQ")
     private String seq;
}
