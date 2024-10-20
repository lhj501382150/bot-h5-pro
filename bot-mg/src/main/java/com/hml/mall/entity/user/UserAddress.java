package com.hml.mall.entity.user;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-11
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_address")
public class UserAddress implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 收货地址编号
    */
    @TableId(value = "RECVNO", type = IdType.AUTO)
    private Integer recvno;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 收货人名称
    */
    @TableField("RECVNAME")
    private String recvname;

    /**
    * 手机号
    */
    @TableField("TELNO")
    private String telno;

    /**
    * 省市县编号
    */
    @TableField("AREAID")
    private Integer areaid;

    /**
    * 省
    */
    @TableField("PROVINCE")
    private String province;

    /**
    * 市
    */
    @TableField("CITY")
    private String city;

    /**
    * 县
    */
    @TableField("COUNTY")
    private String county;

    /**
    * 乡镇（街道）
    */
    @TableField("STREET")
    private String street;

    /**
    * 是否默认
    */
    @TableField("ISDEFAULT")
    private String isdefault;


}
