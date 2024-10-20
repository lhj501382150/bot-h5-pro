package com.hml.mall.entity.notice;

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
* @since 2021-09-06
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_base_forum_recv")
public class ForumRecv implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 流水
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
    * 主题编号
    */
    @TableField("TITID")
    private Long titid;

    /**
    * 客户账号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 时间
    */
    @TableField("STIME")
    private String stime;

    /**
    * 序号
    */
    @TableField("SEQNO")
    private Integer seqno;

    /**
    * 回复内容
    */
    @TableField("CONTENT")
    private String content;

    /**
    * 回复图片
    */
    @TableField("PICPATH")
    private String picpath;

    /**
    * 是否私信Y私信N非私信
    */
    @TableField("ISPRIVATE")
    private String isprivate;

    /**
    * 回帖编号
    */
    @TableField("RID")
    private Long rid;
    
    @TableField("RUSERNO")
    private String ruserno;

    @TableField(exist = false)
    private String username;
}
