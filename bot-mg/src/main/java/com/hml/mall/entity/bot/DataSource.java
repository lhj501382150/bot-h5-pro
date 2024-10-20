package com.hml.mall.entity.bot;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2023-12-14
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_data_source")
public class DataSource implements Serializable {

private static final long serialVersionUID=1L;

	@TableId("DataId")
    private Integer DataId;

    @TableField("sTime")
    private String stime;

    @TableField("sresult")
    private String sresult;

    /**
    * 结果号
    */
    @TableField("bNo")
    private Integer bNo;
    
    @TableField("ISSUE")
    private String issue;
    
    @TableField("Mode")
    private Integer mode;
    
    @TableField("bNNo")
    private String bNNo;
    
    @TableField(exist = false)
    private String wareno;
    
    @TableField(exist = false)
    private String contnum;
}
