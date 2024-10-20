package com.hml.mall.entity.trade;

import java.math.BigDecimal;
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
@TableName("tb_jy_ware_xhys")
public class WareXhys implements Serializable {

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
    * 显示顺序
    */
    @TableField("SEQ")
    private Integer seq;

    /**
    * 市场编号
    */
    @TableField("MNO")
    private String mno;

    /**
    * 板块编号
    */
    @TableField("BNO")
    private String bno;

    /**
    * 交易系数
    */
    @TableField("POINT")
    private BigDecimal point;

    /**
    * 报价单位名称
    */
    @TableField("UNIT")
    private String unit;

    /**
    * 涨跌幅类型(1 定量 2比例)
    */
    @TableField("UWFLAG")
    private Integer uwflag;

    /**
    * 涨跌幅参数
    */
    @TableField("UWPARA")
    private BigDecimal uwpara;

    /**
    * 最高限价
    */
    @TableField("HIGHTPRICE")
    private BigDecimal hightprice;

    /**
    * 最低限价
    */
    @TableField("LOWPRICE")
    private BigDecimal lowprice;


}
