package com.hml.mall.mapper.spread;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.spread.DrawNum;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-09-06
 */
public interface DrawNumMapper extends BaseMapper<DrawNum> {

	@Select(" select t.* from ("
			+ " select t1.*,t2.username from tb_jl_draw_num t1 "
			+ " left join tb_hy_user t2 on t1.userno = t2.userno "
			+ ") t ${ew.customSqlSegment}   ")
	Page<DrawNum> findPage(Page<DrawNum> page,@Param(Constants.WRAPPER) Wrapper wrapper);
}
