package com.hml.mall.entity.user;

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
* @since 2022-10-28
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_givbox")
public class UserGivbox implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "RID", type = IdType.AUTO)
    private Long rid;

    /**
    * 人编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 空投盲盒数量
    */
    @TableField("NUM")
    private Integer num;

    /**
    * 使用数量
    */
    @TableField("UNUM")
    private Integer unum;


}
