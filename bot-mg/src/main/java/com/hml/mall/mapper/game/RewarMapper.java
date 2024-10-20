package com.hml.mall.mapper.game;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.game.Rewar;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-06-09
 */
public interface RewarMapper extends BaseMapper<Rewar> {
	@Select("select t.* from (select t1.*,t2.actname from tb_gm_rewar t1 left join tb_gm_activity t2 on t1.actno = t2.actno) t ${ew.customSqlSegment} order by actno desc ")
	Page<Rewar> findPage(Page<Rewar> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
}
