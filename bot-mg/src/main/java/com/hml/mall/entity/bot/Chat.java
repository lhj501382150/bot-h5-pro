package com.hml.mall.entity.bot;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2023-12-08
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_chat")
public class Chat implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 群号
    */
	@TableId("CHATID")
    private String chatid;

    /**
    * 群名称
    */
    @TableField("CHATNAME")
    private String chatname;

    /**
    * 庄家ID
    */
    @TableField("USERNO")
    private String userno;
    
    @TableField("INTEGE")
    private Integer intege;

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

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String nickname;
    
    
}
