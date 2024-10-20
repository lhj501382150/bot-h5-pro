package com.hml.mall.entity.money;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-14
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TB_ZJ_ACCOUNT")
public class Account implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 账户编号
    */
	@TableId("ACCTNO")
    private String acctno;

    /**
    * 账户名称
    */
    @TableField("ACCTNAME")
    private String acctname;

    /**
    * 说明信息
    */
    @TableField("MEMO")
    private String memo;


}
