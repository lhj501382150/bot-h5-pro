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
@TableName("tb_jy_ware_attr")
public class WareAttr implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 商品编号
    */
	@TableField("WARENO")    
	private String wareno;

    /**
    * 分类编号
    */
    @TableField("CNO")
    private String cno;

    @TableField("ATTR_KEY")
    private String attrKey;

    @TableField("ATTR_NAME")
    private String attrName;

    @TableField("ATTR_VAL")
    private String attrVal;


}
