package com.hml.mall.entity.game;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2021-07-11
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_play_order")
public class PlayOrder implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 流水号
    */
    @TableId(value = "WATERNO", type = IdType.AUTO)
    private Integer waterno;

    /**
    * 日期
    */
    @TableField("FDATE")
    private String fdate;

    /**
    * 陪玩账号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 服务费(钻/小时)
    */
    @TableField("COMM")
    private BigDecimal comm;

    /**
    * 平台服务费
    */
    @TableField("TCOMM")
    private BigDecimal tcomm;

    /**
    * 性别0男1女
    */
    @TableField("SEX")
    private String sex;

    /**
    * 年龄
    */
    @TableField("AGE")
    private Integer age;

    /**
    * 擅长游戏01王者荣耀02和平精英，多选
    */
    @TableField("GMTYPE")
    private String gmtype;

    /**
    * 时长
    */
    @TableField("STEP")
    private Integer step;

    /**
    * 标签
    */
    @TableField("TITLE")
    private String title;

    /**
    * 状态0未使用1已使用2已撤销
    */
    @TableField("GMSTATUS")
    private String gmstatus;

    /**
    * 接单账户
    */
    @TableField("REQUSERNO")
    private String requserno;

    /**
    * 接单时间
    */
    @TableField("REQTIME")
    private String reqtime;

    /**
    * 接单地址
    */
    @TableField("REQADDR")
    private String reqaddr;
    @TableField(exist = false)
    private String username;
    
    /*
     * 1 完成 2 作废
     */
    @TableField(exist = false)
    private String optType;

}
