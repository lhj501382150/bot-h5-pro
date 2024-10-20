package com.hml.mall.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.user.UserDigitartApply;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-05-25
 */
public interface UserDigitartApplyMapper extends BaseMapper<UserDigitartApply> {

	@Select("select t.* from (select t1.*,t2.username,t3.warename  from tb_hy_user_digitart_apply t1 "
			+ "left join tb_hy_user t2 on t1.userno = t2.userno "
			+ "left join tb_jy_ware t3 on t1.wareno = t3.wareno "
			+ ") t ${ew.customSqlSegment} order by artid desc")
	Page<UserDigitartApply> findPage(Page<UserDigitartApply> page,@Param(Constants.WRAPPER) Wrapper wrapper);
}
