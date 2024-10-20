package com.hml.mall.entity.sys;

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
* @since 2022-01-21
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_sys_image_list")
public class ImageList implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 编号
    */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
    * 类型1功能图片2形象图片
    */
    @TableField("IMGTYPE")
    private String imgtype;

    /**
    * 次序
    */
    @TableField("SEQ")
    private Integer seq;

    /**
    * 图片路径
    */
    @TableField("IMGPATH")
    private String imgpath;

    /**
    * 1时填功能路径信息
    */
    @TableField("MEM")
    private String mem;
    
    /**
     * 功能路径
     */
     @TableField("PATH")
     private String path;


}
