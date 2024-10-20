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
* @since 2021-06-29
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_change")
public class UserChange implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "WNO", type = IdType.AUTO)
    private Long wno;

    @TableField("USERNO")
    private String userno;

//    1实名认证2VIP等级
    @TableField("CHTYPE")
    private String chtype;

    @TableField("CHCONTENT")
    private String chcontent;
//    0未申请1未审核2已审核3已驳回
    @TableField("CHECKSTATUS")
    private String checkstatus;

    @TableField("CHECKMSG")
    private String checkmsg;

    @TableField("CHKUSER")
    private String chkuser;

    @TableField("CHKTIME")
    private String chktime;

    @TableField("CHKIP")
    private String chkip;
    
    @TableField(exist = false)
    private String username;


}
