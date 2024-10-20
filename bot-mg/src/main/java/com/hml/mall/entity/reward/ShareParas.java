package com.hml.mall.entity.reward;

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
* @since 2021-05-26
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jl_share_paras")
public class ShareParas implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("MINNUM")
    private Integer minnum;

    @TableField("MAXNUM")
    private Integer maxnum;

    /**
    * 百分比
    */
    @TableField("RATE")
    private BigDecimal rate;


}
