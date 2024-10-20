package com.hml.mall.mapper.spread;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.spread.Active;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-07-31
 */
public interface ActiveMapper extends BaseMapper<Active> {
	
	@Select("select t.* from (select t1.*,t2.warename from tb_jl_active t1 left join tb_jy_ware t2 on t1.wareno = t2.wareno) t ${ew.customSqlSegment} order by actid desc")
	Page<Active> findPage(Page<Active> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
}
