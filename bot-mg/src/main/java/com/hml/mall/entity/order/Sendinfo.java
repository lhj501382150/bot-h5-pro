package com.hml.mall.entity.order;

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
* @since 2021-04-27
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_jy_sendinfo")
public class Sendinfo implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 发货编号
    */
	@TableId("CONTNO")
   private String contno;

    /**
    * 物流公司
    */
    @TableField("TRANSCOMP")
    private String transcomp;

    /**
    * 快递单号
    */
    @TableField("TRANSNO")
    private String transno;

    /**
    * 发货时间
    */
    @TableField("STIME")
    private String stime;

    /**
    * 状态0 未发货1 已发货2 已收货
    */
    @TableField("STATUS")
    private Integer status;

    @TableField("RECVNAME")
    private String recvname;

    @TableField("TELNO")
    private String telno;

    @TableField("RECVADDR")
    private String recvaddr;


}
