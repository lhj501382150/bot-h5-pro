package com.hml.mall.entity.order;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-27
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_invoic")
public class Invoic implements Serializable {

private static final long serialVersionUID=1L;

	@TableId("CONTNO")
    private String contno;

    @TableField("USERNO")
    private String userno;

    /**
    * 1、个人2单位
    */
    @TableField("TYPE")
    private String type;

    @TableField("INVOICTITLE")
    private String invoictitle;

    @TableField("DUTYNO")
    private String dutyno;

    @TableField("REGADDR")
    private String regaddr;

    @TableField("REGTELNO")
    private String regtelno;

    @TableField("OPENBANK")
    private String openbank;

    @TableField("BANKNO")
    private String bankno;

    /**
    * 0未开1已开
    */
    @TableField("STATUS")
    private String status;


}
