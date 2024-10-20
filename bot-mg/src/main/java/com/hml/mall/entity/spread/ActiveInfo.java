package com.hml.mall.entity.spread;

import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tb_jl_active_info")
public class ActiveInfo implements Serializable {

private static final long serialVersionUID=1L;

	@TableField("ACTID")
    private Long actid;

    @TableField("USERNO")
    private String userno;

    /**
    * 资产编号(同步反馈）
    */
    @TableField("ARTID")
    private String artid;

    /**
    * Y同步N不同步
    */
    @TableField("SYCN")
    private String sycn;
    
    @TableField("NUM")
    private Integer num;
    
    @TableField(exist = false)
    private String sname;

    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String warename;

    @TableField(exist = false)
    private Map<String,Object> params;


}
