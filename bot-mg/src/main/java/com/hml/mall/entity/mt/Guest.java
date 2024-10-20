package com.hml.mall.entity.mt;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2022-05-14
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_mt_guest")
public class Guest implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 会议编号
    */
	@TableField("MNO")
    private Long mno;

    /**
    * 嘉宾
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 发言次序
    */
    @TableField("SEQ")
    private Integer seq;

    /**
    * 嘉宾展示资源
    */
    @TableField("FPATH")
    private String fpath;

    /**
    * 当前资源展示编号
    */
    @TableField("CURNUM")
    private String curnum;

    /**
    * 0禁言1发言
    */
    @TableField("AUTH")
    private String auth;

    /**
    * 0未开始1已开始2已结束
    */
    @TableField("MSTATUS")
    private String mstatus;

    @TableField("LASTUPTIME")
    private LocalDateTime lastuptime;


    @TableField(exist = false)
    private String mname;
    
    @TableField(exist = false)
    private String username;
}
