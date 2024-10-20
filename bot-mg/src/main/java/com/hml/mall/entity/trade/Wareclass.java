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
@TableName("tb_jy_wareclass")
public class Wareclass implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 分类编号
    */
	@TableId(value="cno")
    private String cno;

    /**
    * 分类名称
    */
    @TableField("CNAME")
    private String cname;

    /**
    * 当前级别
    */
    @TableField("CLEVEL")
    private Integer clevel;

    /**
    * 上级级别
    */
    @TableField("PARENTNO")
    private String parentno;
    
    /**
     * 系统类型
     * 01商城 02游戏
     */
    @TableField("SYSBLKNO")
    private String sysblkno;

    /**
    * 显示顺序
    */
    @TableField("SEQ")
    private Integer seq;

    /**
    * 分类图标路径
    */
    @TableField("FILEPATH")
    private String filepath;
    
    @TableField("MEM")
    private String mem;
    /**
     * 1级分类编号
     */
    @TableField("CNO1")
    private String cno1;

    /**
    * 2级分类编号
    */
    @TableField("CNO2")
    private String cno2;

    /**
    * 3级分类编号
    */
    @TableField("CNO3")
    private String cno3;

    /**
    * 4级分类编号
    */
    @TableField("CNO4")
    private String cno4;

    /**
    * 5级分类编号
    */
    @TableField("CNO5")
    private String cno5;

    /**
    * 6级分类编号
    */
    @TableField("CNO6")
    private String cno6;

    /**
    * 7级分类编号
    */
    @TableField("CNO7")
    private String cno7;

    /**
    * 8级分类编号
    */
    @TableField("CNO8")
    private String cno8;

    /**
    * 9级分类编号
    */
    @TableField("CNO9")
    private String cno9;

    /**
    * 10级分类编号
    */
    @TableField("CNO10")
    private String cno10;

    /**
    * 11级分类编号
    */
    @TableField("CNO11")
    private String cno11;

    /**
    * 12级分类编号
    */
    @TableField("CNO12")
    private String cno12;

    /**
    * 13级分类编号
    */
    @TableField("CNO13")
    private String cno13;

    /**
    * 14级分类编号
    */
    @TableField("CNO14")
    private String cno14;


    @TableField(exist = false)
    private List<WareclassAttrcol> attrcols;
}
