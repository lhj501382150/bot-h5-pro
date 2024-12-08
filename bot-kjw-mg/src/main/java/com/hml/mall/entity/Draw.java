package com.hml.mall.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2024-12-05
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_kj_draw")
public class Draw implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 流水号
    */
	@TableField("DATA_ID")
    private Long dataId;

    /**
    * 类型
    */
    @TableField("MODE")
    private String mode;

    /**
    * 日期
    */
    @TableField("PRE_DRAW_DATE")
    private String preDrawDate;

    /**
    * 上期开奖时间
    */
    @TableField("PRE_DRAW_TIME")
    private String preDrawTime;

    /**
    * 开奖时间
    */
    @TableField("DRAW_TIME")
    private String drawTime;

    /**
    * 上期期数
    */
    @TableField("PRE_DRAW_ISSUE")
    private String preDrawIssue;

    /**
    * 当前期数
    */
    @TableField("DRAW_ISSUE")
    private String drawIssue;

    /**
    * 上期开奖结果
    */
    @TableField("PRE_DRAW_CODE")
    private String preDrawCode;

    /**
    * 上期开奖结果
    */
    @TableField("PRE_DRAW_HASH")
    private String preDrawHash;
    
    @TableField("VIDEO_URL")
    private String videoUrl;
    
    @TableField("TITLE")
    private String title;

    @TableField("MEMO")
    private String memo;

    /**
    * 最后更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private Long leftTime;
    
    @TableField(exist = false)
    private String modeName;

    /**
    * 间隔时间
    */
    @TableField(exist = false)
    private Integer time;

    /**
    * 类型
    */
    @TableField(exist = false)
    private String type;
    
    @TableField(exist = false)
    private String modeUrl;
}
