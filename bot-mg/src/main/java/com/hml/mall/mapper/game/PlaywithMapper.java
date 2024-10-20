package com.hml.mall.mapper.game;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.game.Playwith;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-06-28
 */
public interface PlaywithMapper extends BaseMapper<Playwith> {
	@Select("select t.* from ("
			+" SELECT t1.*,t3.USERNAME,t4.ACTNAME "
			+ " ,t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5,t2.uno6,t2.uno7,t2.uno8,t2.uno9,t2.uno10,t2.uno11,t2.uno12,t2.uno13,t2.uno14 "
			+" FROM tb_gm_playwith t1 "
			+" left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+" left join tb_hy_user t3 on t1.USERNO = t3.USERNO "
			+" left join tb_gm_activity t4 on t1.ACTNO = t4.ACTNO "
			+" ) t ${ew.customSqlSegment} order by actno desc,userno asc ")
	Page<Playwith> findPage(Page<Playwith> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
