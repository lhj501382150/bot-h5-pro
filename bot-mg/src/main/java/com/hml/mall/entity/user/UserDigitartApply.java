package com.hml.mall.entity.user;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
* @since 2022-05-25
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_digitart_apply")
public class UserDigitartApply implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 资产编号
    */
    @TableId(value = "ARTID", type = IdType.AUTO)
    private Long artid;

    /**
    * 原始资产编号
    */
    @TableField("ORGARTID")
    private Long orgartid;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 卡牌名称
    */
    @TableField("CARDNAME")
    private String cardname;

    /**
    * 卡牌编号
    */
    @TableField("CARDNO")
    private String cardno;

    /**
    * 性别0男1女
    */
    @TableField("CARDSEX")
    private String cardsex;

    /**
    * 材质 金卡 银卡 铜卡 普通卡
    */
    @TableField("CARDRACE")
    private String cardrace;

    /**
    * 状态 1申请2已审核3已驳回
    */
    @TableField("REQSTATUS")
    private String reqstatus;

    /**
    * 1唯一2传奇3传说4史诗5罕见6稀有7常见8普通
    */
    @TableField("CARDGRADE")
    private String cardgrade;

    /**
    * 存储路径
    */
    @TableField("SPATH")
    private String spath;

    /**
    * 总份数
    */
    @TableField("NUM")
    private Integer num;

    /**
    * 服务费
    */
    @TableField("TCOMM")
    private BigDecimal tcomm;

    /**
    * 时间
    */
    @TableField("REQTIME")
    private String reqtime;

    /**
    * 属性
    */
    @TableField("MEM")
    private String mem;

    /**
    * 商品
    */
    @TableField("WARENO")
    private String wareno;

    /**
    * A01：图片 A02：3D模型 A03：声音
    */
    @TableField("ARTTYPE")
    private String arttype;

    /**
    * 上链失败原因
    */
    @TableField("ERRMSG")
    private String errmsg;

    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String warename;

}
