package com.hml.mall.entity.bot;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tb_jy_chat_user")
public class ChatUser implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 群号
    */
	@TableField("CHATID")
    private String chatid;

    /**
    * 群名称
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
