package com.hml.mall.mapper.game;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.game.HorseDigitart;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2023-01-06
 */
public interface HorseDigitartMapper extends BaseMapper<HorseDigitart> {

	@Select("select t.* from (select t1.*,t2.username  "
			+ " from tb_gm_horse_digitart t1 " + 
			" left join tb_hy_user_relation t2 on t1.userno = t2.userno" + 
			" ) t ${ew.customSqlSegment} order by userno,horseno")
	Page<HorseDigitart> findPage(Page<HorseDigitart> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
