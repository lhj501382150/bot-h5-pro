package com.hml.mall.entity.game;

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
* @since 2021-06-28
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_team")
public class Team implements Serializable {

private static final long serialVersionUID=1L;
    @TableField("ACTNO")
    private String actno;
    
    @TableField("USERNO")
    private String userno;

    @TableField("MEMNO")
    private String memno;
    
    @TableField("ISTM")
    private String istm;
    
    @TableField("BMMONEY")
    private String bmmoney;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String actname;
    
    @TableField(exist = false)
    private BigDecimal money;


}
