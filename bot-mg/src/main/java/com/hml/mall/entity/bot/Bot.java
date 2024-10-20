package com.hml.mall.entity.bot;

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
* @since 2023-12-08
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_bot")
public class Bot implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 群号
    */
    @TableId(value = "BOTID", type = IdType.AUTO)
    private Long botid;

    /**
    * 机器人TOKEN
    */
    @TableField("TOKEN")
    private String token;

    /**
    * 机器人名称
    */
    @TableField("BOTNAME")
    private String botname;

    /**
    * 客服链接
    */
    @TableField("KFURL")
    private String kfurl;

    /**
    * 玩法说明
    */
    @TableField("PLAYURL")
    private String playurl;

    /**
    * 庄家ID
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 当前状态 Y 开启 N 停止
    */
    @TableField("STATUS")
    private String status;

    /**
    * 最后更新时间
    */
    @TableField("LAST_UPDATE")
    private LocalDateTime lastUpdate;


}
