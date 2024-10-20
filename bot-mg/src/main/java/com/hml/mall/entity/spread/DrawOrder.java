package com.hml.mall.entity.spread;

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
@TableName("tb_jl_draw_order")
public class DrawOrder implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 编号
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 等级
    */
    @TableField("LEVENO")
    private Integer leveno;

    /**
    * 奖励物品
    */
    @TableField("REWINFO")
    private String rewinfo;

    /**
    * 状态1待发货2待收货3已完成
    */
    @TableField("STATUS")
    private Integer status;

    /**
    * 时间
    */
    @TableField("CTIME")
    private String ctime;

    /**
    * 下单终端标识
    */
    @TableField("ORDDEV")
    private String orddev;

    /**
    * IP地址
    */
    @TableField("CHKIP")
    private String chkip;

    /**
    * 0谢谢参与1虚拟值2实物3盲盒4卡牌
    */
    @TableField("RTYPE")
    private Integer rtype;
    
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String levname;

}
