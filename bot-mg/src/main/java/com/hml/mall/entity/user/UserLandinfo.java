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
* @since 2021-12-27
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_landinfo")
public class UserLandinfo implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 资产编号
    */
    @TableId(value = "ARTID", type = IdType.AUTO)
    private Long artid;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 地块坐标X
    */
    @TableField("COORDX")
    private String coordx;

    /**
    * 地块坐标Y
    */
    @TableField("COORDY")
    private String coordy;

    /**
    * 地块类型1道路2广场3普通
    */
    @TableField("ATYPE")
    private String atype;

    /**
    * 面积
    */
    @TableField("AREA")
    private String area;

    /**
    * 状态 1发行2持有3终止4冻结
    */
    @TableField("ASTATUS")
    private String astatus;

    /**
    * 客户设置颜色
    */
    @TableField("COLOR")
    private String color;

    /**
    * 时间
    */
    @TableField("DATE")
    private String date;

    /**
    * 属性
    */
    @TableField("MEM")
    private String mem;

    /**
    * 所在区块
    */
    @TableField("BLOCKNUM")
    private String blocknum;

    /**
    * 区块HASH
    */
    @TableField("TXHASH")
    private String txhash;

    /**
    * 上一区块HASH
    */
    @TableField("PERHASH")
    private String perhash;

    @TableField(exist = false)
    private String username;

    public void setAtypeVal(String type){
    	String val = type;
    	if("".equals(type) || type == null){
    		type = "3";
    	}
    	type = type.trim();
    	if("道路".equals(type)){
    		type = "1";
    	}else if(type.indexOf("广场") > -1 || "哈勃天文中心".equals(type)){
    		type = "2";
    		this.mem = val + this.mem;
    	}else{
    		type = "3";
    	}
    	this.atype = type;
    }
}
