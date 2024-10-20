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
@TableName("tb_base_forum")
public class Forum implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 流水
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
    * 板块
    */
    @TableField("BLOCKNO")
    private String blockno;

    /**
    * 发帖账号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 时间
    */
    @TableField("STIME")
    private String stime;

    /**
    * 排序序号
    */
    @TableField("SEQNO")
    private Integer seqno;

    /**
    * 标题
    */
    @TableField("TITLE")
    private String title;

    /**
    * 内容
    */
    @TableField("CONTENT")
    private String content;

    /**
    * 图片路径
    */
    @TableField("PICPATH")
    private String picpath;

    /**
    * 总回帖数
    */
    @TableField("RECVNUM")
    private Integer recvnum;

    /**
    * 总关注
    */
    @TableField("FOLLNUM")
    private Integer follnum;

    /**
    * 总点赞数
    */
    @TableField("GIVENUM")
    private Integer givenum;

    @TableField(exist = false)
    private String username;

}
