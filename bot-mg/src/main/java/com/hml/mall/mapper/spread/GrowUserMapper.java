package com.hml.mall.mapper.spread;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.spread.GrowUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-09-06
 */
public interface GrowUserMapper extends BaseMapper<GrowUser> {

	@Select(" select t.* from ("
			+ " select t1.*,t2.username,t3.lname from tb_jl_grow_user t1 "
			+ " left join tb_hy_user t2 on t1.USERNO = t2.userno "
			+ " left join tb_jl_grow_task t3 on t1.id = t3.id "
			+ ") t ${ew.customSqlSegment} order by waterno desc  ")
	Page<GrowUser> findPage(Page<GrowUser> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
