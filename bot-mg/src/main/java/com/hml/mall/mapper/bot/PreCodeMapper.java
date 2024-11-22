package com.hml.mall.mapper.bot;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hml.mall.entity.bot.PreCode;
import com.hml.mall.entity.order.Order;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2024-11-03
 */
public interface PreCodeMapper extends BaseMapper<PreCode> {

	@Select("select ARTID,SUM(BAILMONEY) AS BAILMONEY "
			+ " from tb_jy_order where mode =#{mode} and BUYORSAL = 'B' and CONTNUM =#{contnum} "
			+ " group by ARTID ")
	List<Order> findDraw(@Param("mode")String mode,@Param("contnum")String contnum);
	
	@Select("select ARTID,CPRIGHT,SUM(BAILMONEY) AS BAILMONEY "
			+ " from tb_jy_order where mode =#{mode} and BUYORSAL = 'B' and CONTNUM =#{contnum} "
			+ " group by ARTID,CPRIGHT order by ARTID asc ")
	List<Order> findDrawByNiu(@Param("mode")String mode,@Param("contnum")String contnum);
}
