package com.hml.mall.entity.spread;

import java.io.Serializable;
import java.math.BigDecimal;

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
@TableName("tb_jl_draw_paras")
public class DrawParas implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 等级
    */
	@TableId("LEVENO")
   private Integer leveno;

    /**
    * 等级名称
    */
    @TableField("LEVNAME")
    private String levname;

    /**
    * 奖励物品
    */
    @TableField("REWINFO")
    private String rewinfo;

    /**
    * 中奖比例(%)
    */
    @TableField("RATIO")
    private BigDecimal ratio;

    /**
    * 奖品个数
    */
    @TableField("NUM")
    private Integer num;

    /**
    * 抽中个数
    */
    @TableField("OUTNUM")
    private Integer outnum;

    /**
    * 0谢谢参与1虚拟值2实物3盲盒4卡牌
    */
    @TableField("RTYPE")
    private Integer rtype;

    /**
    * 奖品图片
    */
    @TableField("PICPATH")
    private String picpath;


}
