package com.hml.mall.entity.trade;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2022-05-13
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware_sc_handorder")
public class WareScHandorder implements Serializable {

private static final long serialVersionUID=1L;

	@TableId(value = "HID", type = IdType.AUTO)
    private Long hid;

    @TableField("ARTID")
    private Long artid;

    @TableField("WARENO")
    private String wareno;

    @TableField("SUSERNO")
    private String suserno;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("NUM")
    private Integer num;

    @TableField("BS")
    private String bs;

    @TableField("EXT")
    private String ext;

    /**
    * 0申请1审核2驳回
    */
    @TableField("REQSTATUS")
    private String reqstatus;

    /**
    * 0未挂牌1已挂牌2挂牌失败
    */
    @TableField("HSTATUS")
    private String hstatus;

    @TableField("MEM")
    private String mem;


    @TableField(exist = false)
    private String susername;//委托人
    
    @TableField(exist = false)
    private String warename;//商品名称
}
