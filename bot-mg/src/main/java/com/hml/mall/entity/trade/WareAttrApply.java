package com.hml.mall.entity.trade;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2021-05-18
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware_attr_apply")
public class WareAttrApply implements Serializable {

private static final long serialVersionUID=1L;

	@TableField("WATERID") 
     private Long waterid;

    /**
    * 商品编号
    */
    @TableField("WARENO")
    private String wareno;

    @TableField("ATTR_KEY")
    private String attrKey;

    /**
    * 分类编号
    */
    @TableField("CNO")
    private String cno;

    @TableField("ATTR_NAME")
    private String attrName;

    @TableField("ATTR_VAL")
    private String attrVal;


}
