package com.hml.mall.entity.mt;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2022-05-14
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_mt_meeting")
public class Meeting implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "MNO", type = IdType.AUTO)
    private Long mno;

    /**
    * 名称
    */
    @TableField("MNAME")
    private String mname;

    /**
    * 日期
    */
    @TableField("FDATE")
    private String fdate;

    /**
    * 开始时间
    */
    @TableField("STIME")
    private String stime;

    /**
    * 结束时间
    */
    @TableField("ETIME")
    private String etime;

    /**
    * 主持人
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 展示资源
    */
    @TableField("FPATH")
    private String fpath;

    /**
    * 0未开始1已开始2已结束
    */
    @TableField("MSTATUS")
    private String mstatus;

    /**
    * 当前展示资源编号
    */
    @TableField("CURNUM")
    private String curnum;

    @TableField("LASTUPTIME")
    private LocalDateTime lastuptime;

    
    @TableField(exist = false)
    private String username;

}
