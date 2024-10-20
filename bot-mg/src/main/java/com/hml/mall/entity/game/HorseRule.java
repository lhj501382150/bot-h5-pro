package com.hml.mall.entity.game;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2023-01-06
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_horse_rule")
public class HorseRule implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 游戏活动号
    */
	@TableField("ACTNO")
    private Long actno;

    /**
    * 规则编号如：HTYPE_T
    */
    @TableField("RULENO")
    private String ruleno;

    /**
    * 描述  如：T天属性
    */
    @TableField("MEM")
    private String mem;

    /**
    * 速度加成(%)如：5
    */
    @TableField("SPEED")
    private BigDecimal speed;

    /**
    * 随机最小加成(%)如：0.1
    */
    @TableField("RANDMIN")
    private BigDecimal randmin;

    /**
    * 随机最大加成(%)如：0.5
    */
    @TableField("RANDMAX")
    private BigDecimal randmax;

    @TableField(exist = false)
    private String actname;
    
    public HorseRule clone(Long actNo){
    	HorseRule item = new HorseRule();
    	item.setActno(actNo);
    	item.setRuleno(this.getRuleno());
    	item.setMem(this.getMem());
    	item.setSpeed(this.getSpeed());
    	item.setRandmax(this.getRandmax());
    	item.setRandmin(this.getRandmin());
    	return item;
    }
}
