package com.hml.mall.entity.spread;

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
* @since 2022-07-31
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jl_active")
public class Active implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ACTID", type = IdType.AUTO)
    private Long actid;
    /*
     * '1拉新推荐人数空投2购买资产空投3持有资产空投4资产送盲盒5导入送盲盒6导入空投7消费排行'
     */
    @TableField("ACTYPE")
    private String actype;
    
    @TableField("SNAME")
    private String sname;
    
    @TableField("WARENO")
    private String wareno;
    
    @TableField("NUM")
    private Integer num;
    
    @TableField("MAXNUM")
    private Integer maxnum;
    
    @TableField("TRITIME")
    private String tritime;
    
    /*
     * Y开启N未开启
     */
    @TableField("ISTRI")
    private String istri;

    @TableField("MEM")
    private String mem;

    @TableField(exist = false)
    private String warename;
    
    
}
