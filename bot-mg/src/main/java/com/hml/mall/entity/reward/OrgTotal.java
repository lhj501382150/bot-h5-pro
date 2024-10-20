package com.hml.mall.entity.reward;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2023-12-21
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jl_org_total")
public class OrgTotal implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 流水号
    */
	@TableId(value="WATERNO",type=IdType.ID_WORKER_STR)
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

    /**
    * 日期
    */
    @TableField("PDATE")
    private String pdate;

    /**
    * 机构编号
    */
    @TableField("ORGNO")
    private String orgno;

    /**
    * 级别
    */
    @TableField("CLEVEL")
    private Integer clevel;

    /**
    * 消费合计
    */
    @TableField("CONSUMN")
    private BigDecimal consumn;

    /**
    * 消费金额比率(百分比）
    */
    @TableField("MRATE")
    private BigDecimal mrate;

    /**
    * 消费奖励金
    */
    @TableField("BONUS")
    private BigDecimal bonus;

    /**
    * 服务费合计
    */
    @TableField("COMMS")
    private BigDecimal comms;

    @TableField("USERNO1")
    private String userno1;

    /**
    * 服务费比率
    */
    @TableField("CRATE1")
    private BigDecimal crate1;

    /**
    * 服务费奖励
    */
    @TableField("COMMBONUS1")
    private BigDecimal commbonus1;

    @TableField("USERNO2")
    private String userno2;

    /**
    * 服务费比率
    */
    @TableField("CRATE2")
    private BigDecimal crate2;

    /**
    * 服务费奖励
    */
    @TableField("COMMBONUS2")
    private BigDecimal commbonus2;

    @TableField("USERNO3")
    private String userno3;

    /**
    * 服务费比率
    */
    @TableField("CRATE3")
    private BigDecimal crate3;

    /**
    * 服务费奖励
    */
    @TableField("COMMBONUS3")
    private BigDecimal commbonus3;

    /**
    * 0 未办理 1 已办理
    */
    @TableField("STATUS")
    private Integer status;

    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String nickname;

}
