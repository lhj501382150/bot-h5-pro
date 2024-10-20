package com.hml.mall.entity.trade;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2022-07-19
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware_firm_rule")
public class WareFirmRule implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("SNAME")
    private String sname;

    @TableField("MEM")
    private String mem;

    @TableField("OWAREINFO")
    private String owareinfo;

    /**
    * 权益商品
    */
    @TableField("WAREINFO")
    private String wareinfo;

    /**
    * 权益明细
    */
    @TableField("GIVEINFO")
    private String giveinfo;
    /**
     * 1持有资产2推荐人数
     */
    @TableField("RTYPE")
    private String rtype;
    /**
     * 触发时间
     */
    @TableField("TRITIME")
    private String tritime;
    /**
     * 是否开启Y开启N未开启
     */
    @TableField("ISTRI")
    private String istri;


}
