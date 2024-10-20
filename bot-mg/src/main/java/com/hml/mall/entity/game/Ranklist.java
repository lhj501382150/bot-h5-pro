package com.hml.mall.entity.game;

import java.math.BigDecimal;
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
* @since 2021-06-28
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_ranklist")
public class Ranklist implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 1钻石榜、2金豆榜
    */
	@TableField("LTYPE")
    private String ltype;

    /**
    * 1月2周3日
    */
    @TableField("CYCTYPE")
    private String cyctype;

    @TableField("NICKNAME")
    private String nickname;

    @TableField("AWARD")
    private BigDecimal award;

    @TableField("SEQ")
    private Integer seq;


}
