package com.hml.mall.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-05-18
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware_apply")
public class WareApply implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 流水号
    */
    @TableId(value = "WATERID", type = IdType.AUTO)
    private Long waterid;

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

    /**
    * 商品名称
    */
    @TableField("WARENAME")
    private String warename;

    /**
    * 显示顺序
    */
    @TableField("SEQ")
    private Integer seq;

    /**
    * 报价单位名称
    */
    @TableField("UNIT")
    private String unit;
    
    @TableField("FILEPATH")
    private String filepath;
    
    @TableField("PICPATH")
    private String picpath;
    
    @TableField("VIDEOPATH")
    private String videopath;
    /**
    * A 商城  B  现货预售
    */
    @TableField("MODULE")
    private String module;
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

    @TableField("USERNO")
    private String userno;

    /**
    * 0 待提交 1 待审核 2 已审核 3 已驳回
    */
    @TableField("STATUS")
    private Integer status;

    @TableField("CHECKDATE")
    private String checkdate;
    /**
    * 备注
    */
    @TableField("MEMO")
    private String memo;

    @TableField("LAST_UPDATE")
    private LocalDateTime lastUpdate;
    
    @TableField(exist = false)
    private String cname;
    
    @TableField(exist = false)
    private List<WareAttrApply> wareAttrs;
}
