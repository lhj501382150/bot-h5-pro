package com.hml.mall.entity.trade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tb_jy_basepara")
public class Basepara implements Serializable {

private static final long serialVersionUID=1L;

    @TableField("PARATYPE")
    private String paratype;

    @TableField("PARANO")
    private String parano;

    @TableField("PARANAME")
    private String paraname;

    /**
    * 0 整数 1字符串2 浮点数 3列表值
    */
    @TableField("VALTYPE")
    private Integer valtype;

    /**
    * A=1,B=2|
    */
    @TableField("LIST_DATA")
    private String listData;

    /**
    * 是否启用(Y启用，N不启用)
    */
    @TableField("ISVALID")
    private String isvalid;
    
    @TableField(exist = false)
    private String paraval;
    
    @TableField(exist = false)  
    private String wareno;
    
    @TableField(exist = false)  
    private String userno;
    
    @TableField(exist = false)  
    private List<Map<String,String>> items = new ArrayList();


}
