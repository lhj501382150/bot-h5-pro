package com.hml.mall.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 
* @author hml
* @since 2021-07-17
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_feedback")
public class UserFeedback implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 流水号
    */
	@TableId(value = "FID", type = IdType.AUTO)
     private Long fid;

    /**
    * 请求方
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 接受方
    */
    @TableField("RECVNO")
    private String RECVNO;

    /**
    * 时间
    */
    @TableField("STIME")
    private String stime;

    /**
    * 类型1功能异常2闪退卡顿3建议优化4其他
    */
    @TableField("STYPE")
    private Integer stype;

    /**
    * 回内容
    */
    @TableField("MESSAGE")
    private String message;

    /**
    * 图片
    */
    @TableField("PICPATH")
    private String picpath;

    /**
    * 处理人
    */
    @TableField("OPTUSER")
    private String optuser;

    /**
    * 处理时间
    */
    @TableField("OPTTIME")
    private String opttime;

    /**
    * 处理状态（0未处理 1 已处理）
    */
    @TableField("STATUS")
    private Integer status;

    /**
    * 反馈结果
    */
    @TableField("RESULT")
    private String result;

    /**
    * 备注
    */
    @TableField("MEM")
    private String mem;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String recvname;
    
    
    @TableField(exist = false)
    private Integer num;
    


}
