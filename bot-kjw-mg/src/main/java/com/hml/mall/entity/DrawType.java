package com.hml.mall.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2024-12-05
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_kj_draw_type")
public class DrawType implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 类型
    */
	@TableId("MODE")
    private String mode;

    /**
    * 名称
    */
    @TableField("MODE_NAME")
    private String modeName;

    /**
    * 间隔时间
    */
    @TableField("TIME")
    private Integer time;

    /**
    * 访问地址
    */
    @TableField("MODE_URL")
    private String modeUrl;

    /**
    * 类型
    */
    @TableField("TYPE")
    private String type;
    
    @TableField("SEQ")
    private Integer seq;


}
