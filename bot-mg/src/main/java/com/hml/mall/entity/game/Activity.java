package com.hml.mall.entity.game;

import java.io.Serializable;

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
@TableName("tb_gm_activity")
public class Activity implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "ACTNO", type = IdType.AUTO)
    private Long actno;

    @TableField("USERNO")
    private String userno;

    @TableField("ACTNAME")
    private String actname;

    /**
    * 商品分类一级
    */
    @TableField("C1CNO")
    private String c1cno;

    /**
    * 商品分类二级
    */
    @TableField("C2CNO")
    private String c2cno;

    /**
    * 1微信区2QQ区
    */
    @TableField("TERTYPE")
    private Integer tertype;

    /**
    * 1单排2双排3战队赛4和平精英四排
    */
    @TableField("FMTTYPE")
    private Integer fmttype;

    /**
    * 商品编号，和分类不关联
    */
    @TableField("WARENO")
    private String wareno;

    /**
    * yyyy-mm-dd hh:mm:ss
    */
    @TableField("SIGDTME")
    private String sigdtme;

    /**
    * yyyy-mm-dd hh:mm:ss
    */
    @TableField("PREDTME")
    private String predtme;

    /**
    * yyyy-mm-dd hh:mm:ss
    */
    @TableField("SDTME")
    private String sdtme;

    @TableField("ROOMLINK")
    private String roomlink;

    /**
    * 1按人头2按排名
    */
    @TableField("REWTYPE")
    private Integer rewtype;

    @TableField("RULE")
    private String rule;

    @TableField("SEQ")
    private Integer seq;

    /**
    * 0不免1免
    */
    @TableField("ISDICKET")
    private Integer isdicket;

    /**
    * 0正常1结束
    */
    @TableField("ASTATUS")
    private Integer astatus;

    @TableField("PICPATH")
    private String picpath;

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String c1name;
    
    @TableField(exist = false)
    private String c2name;
    
    @TableField(exist = false)
    private String warename;

}
