package com.hml.mall.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-09-30
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware_jj_handorder")
public class WareJjHandorder implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 竞价申请单号
    */
    @TableId(value = "HID", type = IdType.AUTO)
    private Long hid;

    /**
    * 资产编号
    */
    @TableField("ARTID")
    private Long artid;

    /**
    * 商品编号
    */
    @TableField("WARENO")
    private String wareno;

    /**
    * 委托拍卖人
    */
    @TableField("SUSERNO")
    private String suserno;

    /**
    * 竞价开始时间
    */
    @TableField("STIME")
    private String stime;

    /**
    * 竞价倒计时开始时间
    */
    @TableField("STEPTIME")
    private String steptime;

    /**
    * 倒计时时长
    */
    @TableField("SETSTEP")
    private Integer setstep;

    @TableField("SPRICE")
    private BigDecimal sprice;

    @TableField("STEPPRICE")
    private BigDecimal stepprice;

    /**
    * 1未开始2竞价中3倒计时4已成交5已流标
    */
    @TableField("STATUS")
    private String status;

    /**
    * 当前竞得人
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 出价次数
    */
    @TableField("NUM")
    private Integer num;

    /**
    * 当前价
    */
    @TableField("CPRICE")
    private BigDecimal cprice;

    /**
    * 成交价
    */
    @TableField("CONTPRICE")
    private BigDecimal contprice;

    /**
    * 倒计时时长剩余时间
    */
    @TableField("STEP")
    private Integer step;

    /**
    * 委托拍卖申请状态0申请1审核2驳回
    */
    @TableField("REQSTATUS")
    private String reqstatus;

    /**
    * 驳回原因
    */
    @TableField("MEM")
    private String mem;

    
    @TableField(exist = false)
    private String username;//竞得人
    
    @TableField(exist = false)
    private String susername;//委托人
    
    @TableField(exist = false)
    private String warename;//商品名称
    
    

}
