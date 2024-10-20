package com.hml.mall.entity.reward;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tb_jl_share_total")
public class ShareTotal implements Serializable {

private static final long serialVersionUID=1L;

	@TableField("PDATE")
     private String pdate;

    @TableField("USERNO")
    private String userno;

    @TableField("PNUM")
    private Integer pnum;

    @TableField("RATE")
    private BigDecimal rate;

    @TableField("CONSUMN")
    private BigDecimal consumn;

    @TableField("BONUS")
    private BigDecimal bonus;
    
    @TableField("STATUS")
    private Integer status;
    
    @TableField(exist = false)
    private String username;


}
