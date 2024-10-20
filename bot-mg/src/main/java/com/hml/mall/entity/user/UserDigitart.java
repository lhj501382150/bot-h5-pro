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
* @since 2021-09-22
@
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_hy_user_digitart")
public class UserDigitart implements Serializable {

private static final long serialVersionUID=1L;

    /**
    * 资产编号
    */
	@TableId(value = "ARTID", type = IdType.AUTO)
    private Long artid;

	 /**
    * 原始资产编号
    */
    @TableField("ORGARTID")
    private String orgartid;
    /**
     * 原始发行方
     */
     @TableField("ORGUSERNO")
     private String orguserno;
    /**
    * 商品编号
    */
    @TableField("WARENO")
    private String wareno;

    /**
    * 客户编号
    */
    @TableField("USERNO")
    private String userno;

    /**
    * 卡牌名称
    */
    @TableField("CARDNAME")
    private String cardname;

    /**
    * 卡牌编号
    */
    @TableField("CARDNO")
    private String cardno;

    /**
    * 性别0男1女
    */
    @TableField("CARDSEX")
    private String cardsex;

    /**
    * 部族
    */
    @TableField("CARDRACE")
    private String cardrace;

    /**
    * 状态 1发行2持有3终止4冻结
    */
    @TableField("CARDSTATUS")
    private String cardstatus;

    /**
    * 资产校验码
    */
    @TableField("CHKHASH")
    private String chkhash;

    /**
    * 存储路径
    */
    @TableField("SPATH")
    private String spath;

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
    
    /*
     * 1唯一2传奇3传说4史诗5罕见6稀有7常见8普通
     */
    @TableField("CARDGRADE")
    private String cardgrade;
    
    /*
     * A01：图片 A02：3D模型 A03：声音
     */
    @TableField("ARTTYPE")
    private String arttype;
    
    @TableField("NUM")
    private int num;
    
    /**
     * 链操作状态‘0’未上链 ‘1’上链中‘2’已上链‘3’上链失败
     */
     @TableField("UPSTATUS")
     private String upstatus;
     
     /**
      * 业务分类
      * 1销售2抽奖奖励3实名奖励
      */
     @TableField("BUSTYPE")
     private String bustype;

     /**
     * 上链反馈
     */
     @TableField("ERRMSG")
     private String errmsg;
    
    @TableField(exist = false)
    private String username;
    
    @TableField(exist = false)
    private String orgusername;

    @TableField(exist = false)
    private String warename;

    @TableField(exist = false)
    private String cardpre;
    
    @TableField(exist = false)
    private int cardlength;
    
    public UserDigitart clone(){
    	UserDigitart item = new UserDigitart();
    	item.setArtid(this.getArtid());
    	item.setOrgartid(this.getOrgartid());
    	item.setUserno(this.getUserno());
    	item.setOrguserno(this.getOrguserno());
    	item.setWareno(this.getWareno());
    	item.setCardno(this.getCardno());
    	item.setCardname(this.getCardname());
    	item.setCardsex(this.getCardsex());
    	item.setCardrace(this.getCardrace());
    	item.setCardgrade(this.getCardgrade());
    	item.setCardstatus(this.getCardstatus());
    	item.setChkhash(this.getChkhash());
    	item.setSpath(this.getSpath());
    	item.setMem(this.getMem());
    	item.setBlocknum(this.getBlocknum());
    	item.setDate(this.getDate());
    	item.setTxhash(this.getTxhash());
    	item.setPerhash(this.getPerhash());
    	item.setUpstatus(this.getUpstatus());
    	item.setNum(this.getNum());
    	item.setArttype(this.getArttype());
    	item.setErrmsg(this.getErrmsg());
    	item.setBustype(this.getBustype());
    	
    	return item;
    }
}
