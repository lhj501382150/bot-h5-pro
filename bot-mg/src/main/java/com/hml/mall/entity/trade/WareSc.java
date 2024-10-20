package com.hml.mall.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-17
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware_sc")
public class WareSc implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 商品编号
    */
	@TableId("WARENO")    
    private String wareno;

    /**
    * 分类编号
    */
    @TableField("CNO")
    private String cno;

    /**
    * 商品标题（搜索）
    */
    @TableField("TITLE")
    private String title;

    /**
    * 规格
    */
    @TableField("SPEC")
    private String spec;

    /**
    * 推广标签(0 普通 1特价2热卖)
    */
    @TableField("FLAG")
    private Integer flag;

    /**
    * 数量
    */
    @TableField("NUM")
    private Integer num;

    /**
    * 销售数量
    */
    @TableField("SALNUM")
    private Integer salnum;

    /**
    * 原价
    */
    @TableField("PRICE")
    private BigDecimal price;

    /**
    * 折扣价
    */
    @TableField("SALPRICE")
    private BigDecimal salprice;

    /**
    * 积分报价（0 不可用积分）
    */
    @TableField("INTEPRICE")
    private BigDecimal inteprice;
    
    /**
     * 赠送积分数量
     */
    @TableField("INTEREW")
    private BigDecimal interew;

    /**
    * 积分购买数量(最大用积分购买数量)
    */
    @TableField("INTENUM")
    private Integer intenum;

    /**
    * 重量
    */
    @TableField("WEIGHT")
    private BigDecimal weight;

    /**
    * 运费
    */
    @TableField("TRANSCOMM")
    private BigDecimal transcomm;

    /**
    * 物流公司
    */
    @TableField("TRANSCOMP")
    private String transcomp;

    /**
     * 状态 状态0:初始1上架2下架
     */
     @TableField("WSTATUS")
     private Integer wstatus;

}
