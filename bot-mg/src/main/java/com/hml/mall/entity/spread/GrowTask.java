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
* @since 2021-09-06
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jl_grow_task")
public class GrowTask implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
    * 成长任务名称
    */
    @TableField("LNAME")
    private String lname;

    /**
    * 任务说明
    */
    @TableField("LTITLE")
    private String ltitle;

    /**
    * 奖励成长值
    */
    @TableField("REWNUM")
    private Integer rewnum;

    /**
    * 说明
    */
    @TableField("MEM")
    private String mem;


}
