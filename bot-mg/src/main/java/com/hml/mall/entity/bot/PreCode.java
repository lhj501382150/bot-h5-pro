package com.hml.mall.entity.bot;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2024-11-03
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_data_pre_code")
public class PreCode implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 流水号
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("DATA_ID")
    private Long dataId;
    
    @TableField("MODE")
    private Integer mode;
    
    @TableField("RKEY")
    private String rkey;


    /**
    * 期数
    */
    @TableField("DRAW_ISSUE")
    private String drawIssue;

    /**
    * 结果
    */
    @TableField("CODE")
    private String code;

    /**
    * 商户号
    */
    @TableField("MEMO")
    private String memo;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("UPDATE_USER")
    private String updateUser;

    /**
    * 最后更新时间
    */
    @JsonFormat(locale = "zh",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
    * 最后更新时间
    */
    @JsonFormat(locale = "zh",pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;


}
