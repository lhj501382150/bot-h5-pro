package com.hml.mall.mapper.game;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.game.HorseRule;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2023-01-06
 */
public interface HorseRuleMapper extends BaseMapper<HorseRule> {

	@Select("select t.* from (select t1.*,t2.actname from tb_gm_horse_rule t1 left join tb_gm_activity t2 on t1.actno = t2.actno) t ${ew.customSqlSegment} order by actno desc ")
	Page<HorseRule> findPage(Page<HorseRule> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
