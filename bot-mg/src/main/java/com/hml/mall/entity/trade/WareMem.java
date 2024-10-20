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
* @since 2022-07-27
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware_mem")
public class WareMem implements Serializable {

private static final long serialVersionUID=1L;

	@TableId("WARENO")
    private String wareno;

    /**
    * Y挂起N正常
    */
    @TableField("HSTATUS")
    private String hstatus;

    /**
    * 商品数量
    */
    @TableField("NUM")
    private String num;

    /**
    * 销售数量
    */
    @TableField("SALNUM")
    private String salnum;

    @TableField(exist=false)
    private String warename;

}
