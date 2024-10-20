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
@TableName("TB_ZJ_SUBJECT")
public class Subject implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 科目编号
    */
	@TableId("SUNNO")
    private String sunno;

    /**
    * 科目名称
    */
    @TableField("SUBNAME")
    private String subname;

    /**
    * 出入标记(A 入 B 出)
    */
    @TableField("INOROUT")
    private String inorout;

    /**
    * A:INMONEY B:OUTMONEY C:IN D:OUT E:FREEZE
    */
    @TableField("CALCFIELD")
    private String calcfield;

    /**
    * 备注
    */
    @TableField("MEMO")
    private String memo;


}
