package com.hml.mall.entity.trade;

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
* @since 2021-04-17
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_wareclass_attrcol")
public class WareclassAttrcol implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 分类编号
    */
	@TableId(value="CNO")
    private String cno;

    /**
    * 属性名称1
    */
    @TableField("ATTR_KEY")
    private String attrKey;

    /**
    * 属性名称2
    */
    @TableField("ATTR_NAME")
    private String attrName;

    /**
    * 属性名称3
    */
    @TableField("SEQ")
    private Integer seq;

     
}
