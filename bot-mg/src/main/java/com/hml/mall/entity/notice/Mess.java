package com.hml.mall.entity.notice;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* 
* @author hml
* @since 2021-04-05
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_sys_mess")
public class Mess implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 短信编号
    */
	@TableId("MSGNO")
    private String msgno;

    /**
    * 客户级别
    */
    @TableField("USERLEVEL")
    private Integer userlevel;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;
    
    /**
     * 手机号
     */
     @TableField("MOBILENO")
     private String mobileno;

    /**
    * 1 验证码
            2 业务提醒
            3 风险提醒
    */
    @TableField("NTYPE")
    private Integer ntype;

    /**
    * 短信内容
    */
    @TableField("CONTENT")
    private String content;
    
    /**
     * 备注
     */
     @TableField("MEM")
     private String mem;
    /**
     * 0默认1已发送2发送失败
     */
     @TableField("SENDFLAG")
     private Integer sendflag;

     /**
     * 短信内容
     */
     @TableField("MSG")
     private String msg;
     /**
      * 短信内容
      */
      @TableField("TPLID")
      private String tplid;
      
      /**
       * 短信内容
       */
       @TableField("SENDTIME")
       private String sendtime;
    /**
     * 手机号
     */
    @TableField(exist = false)
    private String telno;


}
