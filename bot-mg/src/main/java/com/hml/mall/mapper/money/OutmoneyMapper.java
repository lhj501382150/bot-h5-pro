package com.hml.mall.mapper.money;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.money.Outmoney;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-11-19
 */
public interface OutmoneyMapper extends BaseMapper<Outmoney> {
	@Select("select t.* from (select t1.*,t2.username,t2.telno as regtelno from tb_zj_outmoney t1 left join tb_hy_user t2 on t1.userno = t2.userno) t ${ew.customSqlSegment} order by id desc")
	Page<Outmoney> findPage(Page<Outmoney> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select sum(chmoney) as chmoney,sum(outmoney) as outmoney,sum(tcomm) as tcomm from "
			+ "(select t1.*,t2.username from tb_zj_outmoney t1 left join tb_hy_user t2 on t1.userno = t2.userno) t ${ew.customSqlSegment}")
	Outmoney findSum(@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("SELECT T1.*,T2.BANKNO,T2.BANKNAME,T2.CARDNO,T2.SNAME,T2.PROVINCE,T2.CITY,T2.SUBBANK,T2.IDNO FROM tb_zj_outmoney T1 " 
		 + " LEFT JOIN tb_hy_user_cardinfo T2 ON T1.USERNO = T2.USERNO where t1.id in (#{ids})")
	List<Outmoney> findPayMoney(@Param("ids")String ids);
	
	@Select("select t.* from ("
			 + " SELECT T1.*,T2.BANKNO,T2.BANKNAME,T2.CARDNO,T2.SNAME,T2.PROVINCE,T2.CITY,T2.SUBBANK,T2.IDNO FROM tb_zj_outmoney T1 " 
			 + " LEFT JOIN tb_hy_user_cardinfo T2 ON T1.USERNO = T2.USERNO " 
			+ ") t ${ew.customSqlSegment} order by id desc")
	Page<Outmoney> findExportPage(Page<Outmoney> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	
	void updatePayStatus(Long id);
}
