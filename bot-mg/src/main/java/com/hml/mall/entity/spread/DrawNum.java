package com.hml.mall.entity.spread;

import java.io.Serializable;

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
@TableName("tb_jl_draw_num")
public class DrawNum implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 客户编号
    */
	@TableId("USERNO")
    private String userno;

    /**
    * 抽奖次数
    */
    @TableField("NUM")
    private Integer num;

    @TableField(exist = false)
    private String username;
}
