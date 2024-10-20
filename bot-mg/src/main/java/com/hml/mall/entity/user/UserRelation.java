package com.hml.mall.entity.user;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-11
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_relation")
public class UserRelation implements Serializable {

private static final long serialVersionUID=1L;

	@TableId("USERNO")
    private String userno;

    @TableField("USERNAME")
    private String username;

    @TableField("CLEVEL")
    private Integer clevel;

    @TableField("PARENTNO")
    private String parentno;

    @TableField("TJNO")
    private String tjno;

    @TableField("UNO1")
    private String uno1;

    @TableField("UNO2")
    private String uno2;

    @TableField("UNO3")
    private String uno3;

    @TableField("UNO4")
    private String uno4;

    @TableField("UNO5")
    private String uno5;

    @TableField("UNO6")
    private String uno6;

    @TableField("UNO7")
    private String uno7;

    @TableField("UNO8")
    private String uno8;

    @TableField("UNO9")
    private String uno9;

    @TableField("UNO10")
    private String uno10;

    @TableField("UNO11")
    private String uno11;

    @TableField("UNO12")
    private String uno12;

    @TableField("UNO13")
    private String uno13;

    @TableField("UNO14")
    private String uno14;


}
