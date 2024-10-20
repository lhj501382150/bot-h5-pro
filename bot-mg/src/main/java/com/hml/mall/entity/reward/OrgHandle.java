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
@TableName("tb_jl_org_handle")
public class OrgHandle implements Serializable {

private static final long serialVersionUID=1L;

	@TableId(value="WATERNO",type=IdType.ASSIGN_ID)
	private String waterno;
	 /**
    * 开始时间
    */
    @TableField("BTIME")
    private String btime;
    /**
    * 结束时间
    */
    @TableField("ETIME")
    private String etime;
	@TableField("PDATE")
    private String pdate;

    @TableField("ORGNO")
    private String orgno;
    @TableField("USERNO")
    private String userno;
    @TableField("TOTALNO")
    private String totalno;
    @TableField("CONSUMN")
    private BigDecimal consumn;
    
    /**
     * 级别
     */
     @TableField("CLEVEL")
     private Integer clevel;

    @TableField("BONUS")
    private BigDecimal bonus;

    @TableField("COMMS")
    private BigDecimal comms;

    @TableField("COMMBONUS")
    private BigDecimal commbonus;

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
    
    @TableField(exist = false)
    private String nickname;
}
