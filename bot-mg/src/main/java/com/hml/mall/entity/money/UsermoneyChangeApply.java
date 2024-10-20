package com.hml.mall.entity.money;

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
@TableName("tb_zj_usermoney_change_apply")
public class UsermoneyChangeApply implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 主键
    */
    @TableId(value = "WARTNO", type = IdType.AUTO)
    private Long wartno;
    /**
    * 用户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 账本编号
    */
    @TableField("ACCTNO")
    private String acctno;

    /**
    * 变动资金
    */
    @TableField("CHMONEY")
    private BigDecimal chmoney;

    /**
    * 科目编号
    */
    @TableField("SUBNO")
    private String subno;

    /**
    * 备注
    */
    @TableField("MEMO")
    private String memo;

    /**
    * 扩展信息
    */
    @TableField("EXTEN")
    private String exten;

    @TableField("STATUS")
    private Integer status;

    @TableField("CHKUSER")
    private String chkuser;

    @TableField("CHKTIME")
    private String chktime;

    @TableField("CHKIP")
    private String chkip;

    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String acctname;
    
    @TableField(exist = false)
    private String subname;
}
