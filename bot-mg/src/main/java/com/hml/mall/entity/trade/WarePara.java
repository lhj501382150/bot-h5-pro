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
@TableName("tb_jy_ware_para")
public class WarePara implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 商品编号
    */
	@TableId("WARENO")  
    private String wareno;

    /**
    * 参数编号
    */
    @TableField("PARANO")
    private String parano;

    /**
    * 参数名称
    */
    @TableField("PARAVAL")
    private String paraval;


}
