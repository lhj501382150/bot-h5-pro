package com.hml.mall.entity.trade;

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
* @since 2022-07-18
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_digitart_rule")
public class UserDigitartRule implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("SNAME")
    private String sname;

    @TableField("MEM")
    private String mem;

    @TableField("NWARENO")
    private String nwareno;

    @TableField("OWAREINFO")
    private String owareinfo;
    
    @TableField(exist = false)
    private String warename;

}
