package com.hml.mall.mapper.trade;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.trade.UserDigitartRule;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-07-18
 */
public interface UserDigitartRuleMapper extends BaseMapper<UserDigitartRule> {

	@Select("select t.* from ("
			+ "select A.*,C.warename from tb_hy_user_digitart_rule A "
			+ " LEFT JOIN tb_jy_ware C ON A.nwareno = C.wareno " 
			+ " ) t ${ew.customSqlSegment} order by id ")
	Page<UserDigitartRule> findPage(Page<UserDigitartRule> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
