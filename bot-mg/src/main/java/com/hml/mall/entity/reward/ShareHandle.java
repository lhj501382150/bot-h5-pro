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
@TableName("tb_jl_share_handle")
public class ShareHandle implements Serializable {

private static final long serialVersionUID=1L;

	@TableField("PDATE")
    private String pdate;

    @TableField("USERNO")
    private String userno;

    @TableField("CONSUMN")
    private BigDecimal consumn;

    @TableField("BONUS")
    private BigDecimal bonus;

    /**
    * 0待审核1已审核2审核失败
    */
    @TableField("PSTATUS")
    private String pstatus;

    @TableField("CHKUNO")
    private String chkuno;

    @TableField("CHKTIME")
    private String chktime;

    @TableField("CHKIP")
    private String chkip;

    @TableField("MEM")
    private String mem;

    @TableField(exist = false)
    private String username;

}
