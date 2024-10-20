package com.hml.mall.entity.game;

import com.baomidou.mybatisplus.annotation.TableName;
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
* @since 2023-01-06
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_gm_horse_digitart")
public class HorseDigitart implements Serializable {

private static final long serialVersionUID=1L;
	
	@TableId("HORSENO")
    private String horseno;

    @TableField("USERNO")
    private String userno;

    /**
    * 类型:T天属性、M马天属性、X行天属性、K空天属性
    */
    @TableField("HTYPE")
    private String htype;

    /**
    * 血统：UTR、SSR、SR、R、N
    */
    @TableField("ANCESTRY")
    private String ancestry;

    /**
    * 马种：阿拉伯马(AL)、汗血马(HX)、蒙古马(MG)、纯血马(CX)、混血马(HUNX)
    */
    @TableField("HBREED")
    private String hbreed;

    /**
    * 体质：热血马(RX)、温血马(WX)、冷血马(LX)
    */
    @TableField("CONS")
    private String cons;

    /**
    * 性别：母马(M)、公马(G)、骟马(S)
    */
    @TableField("SEX")
    private String sex;

    /**
    * 颜色：赛马红(R)、碧水绿(G)、沙金黄(Y)、皓月白(W)、水晶银(S)、孔雀蓝(B)、丁香紫(P)、可可棕(C)
    */
    @TableField("COLOR")
    private String color;

    /**
    * 配饰：马厩(k1)(J)、能量水(k2)(W)、草料(k3)(G)、翅膀(k4)(WI)、勋章(k5)(X)、马蹄衔铁(k6)(IR)、马鞍(k7)(A)、骑手(波仔)(k8)(B)、医师(哈妹)(k9)(H)、驯兽师(机器人)(k10)(M)
    */
    @TableField("ORNAM")
    private String ornam;

    @TableField(exist = false)
    private String username;

}
