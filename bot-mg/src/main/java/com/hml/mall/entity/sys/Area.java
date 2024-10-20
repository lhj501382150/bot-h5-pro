package com.hml.mall.entity.sys;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-05
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TB_SYS_AREA")
public class Area implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 编号
    */
	@TableId("CODE")
     private Integer code;

    /**
    * 名称
    */
    @TableField("NAME")
    private String name;

    /**
    * 级别 1 省 2 市 3 县
    */
    @TableField("LEVEL")
    private Integer level;

    /**
    * 上级编号
    */
    @TableField("PARENTID")
    private Integer parentid;
    
    @TableField(exist = false)
    private List<Area> children;


}
