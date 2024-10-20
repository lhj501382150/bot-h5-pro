package com.hml.mall.mapper.order;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.order.Contract;
import com.hml.mall.entity.order.Order;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-18
 */
public interface ContractMapper extends BaseMapper<Contract> {
	@Select("select t.* from (select A.*,B.username,C.warename,T.acctname from tb_jy_contract A "
			+ " left join tb_hy_user B on A.userno = B.userno "
			+ " left join tb_jy_ware C on A.wareno = C.wareno "
			+ " left join tb_zj_account T on A.acctno = T.acctno "
			+ ") t ${ew.customSqlSegment} order by CONTNO desc ")
	Page<Contract> findPage(Page<Contract> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t.* from (select t1.*,t2.username,t3.warename,t4.STATUS as sendstatus,t5.STATUS as invoicestatus,t6.acctname, "
			+ " t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14,t2.parentno,t2.tjno  "
			+ " from tb_jy_contract t1 left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+ " left join TB_JY_SENDINFO t4 on t1.contno =t4.contno "
			+ " left join TB_JY_INVOIC t5 on t1.contno =t5.contno "
			+ " left join tb_zj_account t6 on t1.acctno = t6.acctno "
			+ " left join tb_jy_ware t3 on t1.wareno = t3.wareno) t ${ew.customSqlSegment} order by fdate desc,contno desc ")
	Page<Contract> findPageByUser(Page<Contract> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select sum(flatnum) as flatnum,sum(num) as num,sum(setnum) as setnum,sum(tradecomm) as tradecomm,sum(bailmoney) as bailmoney, "
			+ "sum(loss) as loss,sum(cpright) as cpright,sum(transcomm) as transcomm  "
			+ "from (select t1.*,t2.username,t3.warename,t4.STATUS as sendstatus,t5.STATUS as invoicestatus, "
			+ " t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14,t2.parentno,t2.tjno  "
			+ " from tb_jy_contract t1 left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+ " left join TB_JY_SENDINFO t4 on t1.contno =t4.contno "
			+ " left join TB_JY_INVOIC t5 on t1.contno =t5.contno "
			+ " left join tb_zj_account t6 on t1.acctno = t6.acctno "
			+ " left join tb_jy_ware t3 on t1.wareno = t3.wareno) t ${ew.customSqlSegment}")
	Contract selectSum(@Param(Constants.WRAPPER)Wrapper wrapper);
}
