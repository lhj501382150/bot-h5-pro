package com.hml.mall.mapper.order;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.order.PayOrder;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-07-10
 */
public interface PayOrderMapper extends BaseMapper<PayOrder> {

	@Select("select t.* from (select t1.*,t2.username,t2.CLEVEL as clevel,t2.PARENTNO as parentno,t2.TJNO as tjno,t3.acctname,t4.subname,"
			+ "t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14 "
			+ " from tb_zj_payorder t1 left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+ " left join tb_zj_account t3 on t1.acctno = t3.acctno "
			+ " left join tb_zj_subject t4 on t1.subno = t4.sunno "
			+ ") t ${ew.customSqlSegment} order by wartno desc ")
	Page<PayOrder> findPageByUser(Page<PayOrder> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select sum(CHMONEY) as CHMONEY  "
			+ "from (select t1.*,t2.username,t2.CLEVEL as clevel,t2.PARENTNO as parentno,t2.TJNO as tjno,t3.acctname,t4.subname,"
			+ "t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14 "
			+ " from tb_zj_payorder t1 left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+ " left join tb_zj_account t3 on t1.acctno = t3.acctno "
			+ " left join tb_zj_subject t4 on t1.subno = t4.sunno "
			+ " ) t ${ew.customSqlSegment}")
	PayOrder selectSum(@Param(Constants.WRAPPER)Wrapper wrapper);
}
