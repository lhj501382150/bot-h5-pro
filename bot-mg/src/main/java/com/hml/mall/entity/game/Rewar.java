package com.hml.mall.entity.game;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-06-09
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_rewar")
public class Rewar implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("ACTNO")
    private Long actno;

    /**
    * 1按人头2按排名
    */
    @TableField("REWTYPE")
    private Integer rewtype;

    /**
    * 0--1第一名1-3第二、三名
    */
    @TableField("MINNO")
    private Integer minno;

    @TableField("MANNO")
    private Integer manno;

    @TableField("REWMONEY")
    private BigDecimal rewmoney;
    
    @TableField(exist = false)
    private String actname;


}
