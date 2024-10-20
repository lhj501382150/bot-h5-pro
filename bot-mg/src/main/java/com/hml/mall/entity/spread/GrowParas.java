package com.hml.mall.entity.spread;

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
* @since 2021-09-06
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jl_grow_paras")
public class GrowParas implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 级别
    */
	@TableId("LEVENO")
    private Integer leveno;

    /**
    * 名称
    */
    @TableField("LNAME")
    private String lname;

    /**
    * 最小值
    */
    @TableField("MINNUM")
    private Integer minnum;

    /**
    * 最大值
    */
    @TableField("MAXNUM")
    private Integer maxnum;

    /**
    * 特权描述
    */
    @TableField("MEM")
    private String mem;

    /**
    * 特权编号例如1,2,3,4
    */
    @TableField("REWNUM")
    private String rewnum;

    /**
    * 等级标签图片
    */
    @TableField("PICPATH")
    private String picpath;


}
