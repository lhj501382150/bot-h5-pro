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
@TableName("tb_jl_org_paras")
public class OrgParas implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 编号
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

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
    * 百分比消费金额比率
    */
    @TableField("MRATE")
    private BigDecimal mrate;

    /**
    * 百分比服务费比率
    */
    @TableField("CRATE")
    private BigDecimal crate;
    
    @TableField(exist = false)
    private String username;
}
