package com.hml.mall.entity.trade;

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
* @since 2021-04-17
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware")
public class Ware implements Serializable {

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
    
    @TableField(exist = false)
    private String cname;
    
    @TableField(exist = false)
    private String sysblkno;

    @TableField(exist = false)
    private WareSc wareSc;
    
    @TableField(exist = false)
    private WareXhys wareXhys;
    
    @TableField(exist = false)
    private WareJj wareJj;
    
    @TableField(exist = false)
    private List<WarePara> wareParas;
    
    @TableField(exist = false)
    private List<WareAttr> wareAttrs;
    
    @TableField(exist = false)
    private String content;

}
