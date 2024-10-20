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
* @since 2021-06-09
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_userlist")
public class Userlist implements Serializable {

private static final long serialVersionUID=1L;

	@TableField("ACTNO")
    private Long actno;

    @TableField("USERNO")
    private String userno;

    @TableField("MONEY")
    private BigDecimal money;

    /**
    * 0胜1负
    */
    @TableField("VORF")
    private Integer vorf;

    @TableField("SEQ")
    private Integer seq;

    @TableField("REWMN")
    private BigDecimal rewmn;
    
    @TableField(exist = false)
    private String actname;
    
    @TableField(exist = false)
    private String username;

    @TableField(exist = false)
    private String gameno;


}
