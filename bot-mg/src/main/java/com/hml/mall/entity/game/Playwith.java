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
* @since 2021-06-28
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_playwith")
public class Playwith implements Serializable {

private static final long serialVersionUID=1L;

	@TableField("ACTNO")
	private String actno;

	@TableField("USERNO")
    private String userno;
	/*
	 * 1关注2点赞
	 */
	@TableField("PTYPE")
	private String ptype;

    /**
    * 百分比
    */
    @TableField("COMMRATE")
    private BigDecimal commrate;
    /**
     * 奖励积分
     */
    @TableField("JLMONEY")
    private BigDecimal jlmoney;

    /**
    * 0申请1已审核2已驳回3已撤销
    */
    @TableField("CHECKSTATUS")
    private String checkstatus;

    @TableField("CHECKMSG")
    private String checkmsg;
    
    @TableField("OPUSER")
    private String opuser;
    
    @TableField("OPADDR")
    private String opaddr;
    
    @TableField("OPTIME")
    private String optime;

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String actname;

}
