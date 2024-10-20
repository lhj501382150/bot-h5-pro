package com.hml.mall.entity.notice;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-05
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_sys_notice")
public class Notice implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 公告编号
    */
	@TableId("NOTICENO")
    private String noticeno;

    /**
    * 阅读对象客户级别
    */
    @TableField("USERLEVEL")
    private Integer userlevel;

    /**
    * 阅读对象客户编号
             | 分割，客户及下级才可阅读，为空所有客户
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 公告类型(1 公告 2规则，3风险告知)
    */
    @TableField("NTYPE")
    private Integer ntype;

    /**
    * 公告标题
    */
    @TableField("TITLE")
    private String title;

    /**
    * 公告内容
    */
    @TableField("CONTENT")
    private String content;
    
    /**
    * 排序
    */
    @TableField("SEQ")
    private Integer seq;

    /**
    * 是否有效(Y有效N无效)
    */
    @TableField("ISVALID")
    private String isvalid;


}
