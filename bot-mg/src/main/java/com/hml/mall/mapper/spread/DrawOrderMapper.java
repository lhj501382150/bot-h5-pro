package com.hml.mall.mapper.spread;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.spread.DrawOrder;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-09-06
 */
public interface DrawOrderMapper extends BaseMapper<DrawOrder> {

	@Select(" select t.* from ("
			+ " select t1.*,t2.username,t3.levname from tb_jl_draw_order t1 "
			+ " left join tb_hy_user t2 on t1.USERNO = t2.userno "
			+ " left join tb_jl_draw_paras t3 on t1.leveno = t3.leveno "
			+ ") t ${ew.customSqlSegment} order by id desc  ")
	Page<DrawOrder> findPage(Page<DrawOrder> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
