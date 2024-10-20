package com.hml.mall.entity.trade;

import java.io.Serializable;
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
* @since 2022-07-18
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_ware_firm_para")
public class WareFirmPara implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
    * 交易商号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 商品编号
    */
    @TableField("WARENO")
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

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String warename;
    
    @TableField(exist = false)
    private String p1001;
    
    @TableField(exist = false)
    private String p1002;
    
    @TableField(exist = false)
    private String p1003;
    
    @TableField(exist = false)
    private List<Basepara> items;
    
}
