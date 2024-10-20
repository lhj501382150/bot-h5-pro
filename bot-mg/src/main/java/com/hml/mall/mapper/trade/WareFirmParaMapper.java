package com.hml.mall.mapper.trade;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.trade.Ware;
import com.hml.mall.entity.trade.WareFirmPara;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-07-18
 */
public interface WareFirmParaMapper extends BaseMapper<WareFirmPara> {

	@Select("select t.* from ("
			+ " select A.*,B.username,c.warename from ("
			+ "	select userno,wareno "
			+ "	,max(case when PARANO = '1001' then PARAVAL else '' end) as 'P1001' "
			+ "	,max(case when PARANO = '1002' then PARAVAL else '' end) as 'P1002' "
			+ "	,max(case when PARANO = '1003' then PARAVAL else '' end) as 'P1003' "
			+ "	from tb_jy_ware_firm_para  "
			+ "	group by userno,wareno "
			+ " ) A "
			+ " LEFT JOIN tb_hy_user B ON A.userno = B.userno " 
			+ " LEFT JOIN tb_jy_ware C ON A.wareno = C.wareno " 
			+ " ) t ${ew.customSqlSegment} order by userno,wareno ")
	Page<WareFirmPara> findPage(Page<WareFirmPara> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
}
