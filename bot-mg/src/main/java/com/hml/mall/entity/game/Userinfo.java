package com.hml.mall.entity.game;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2021-06-28
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_userinfo")
public class Userinfo implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "UGNO", type = IdType.AUTO)
    private Long ugno;

    @TableField("USERNO")
    private String userno;

    @TableField("C1CNO")
    private String c1cno;

    @TableField("GAMENO")
    private String gameno;
    
    @TableField("TERTYPE")
    private String tertype;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String cname;


}
