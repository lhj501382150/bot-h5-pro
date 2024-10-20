package com.hml.mall.entity.user;

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
* @since 2024-09-19
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_limit")
public class UserLimit implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 用户编号
    */
	@TableId("USERNO")
    private String userno;

    @TableField("MIN_DOU")
    private Integer minDou;

    @TableField("MAX_DOU")
    private Integer maxDou;

    @TableField("MIN_NIU_P")
    private Integer minNiuP;

    @TableField("MAX_NIU_P")
    private Integer maxNiuP;

    @TableField("MIN_NIU_B")
    private Integer minNiuB;

    @TableField("MAX_NIU_B")
    private Integer maxNiuB;
    
    
    @TableField(exist = false)
    private Integer orgtype;
    
    @TableField(exist = false)
    private Integer clevel;


}
