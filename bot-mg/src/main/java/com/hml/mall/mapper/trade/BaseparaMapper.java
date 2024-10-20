package com.hml.mall.mapper.trade;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hml.mall.entity.trade.Basepara;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-17
 */
public interface BaseparaMapper extends BaseMapper<Basepara> {

	@Select("select t.* from (select t1.*,t2.paraval,t2.wareno from tb_jy_basepara t1 left join tb_jy_ware_para t2 on t1.parano =t2.parano) t ${ew.customSqlSegment}")
	List<Basepara> findBasePara(@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t.* from (select t1.*,t2.paraval,t2.wareno,t2.userno from tb_jy_basepara t1 left join tb_jy_ware_firm_para t2 on t1.parano =t2.parano) t ${ew.customSqlSegment}")
	List<Basepara> findBaseFirmPara(@Param(Constants.WRAPPER)Wrapper wrapper);
}
