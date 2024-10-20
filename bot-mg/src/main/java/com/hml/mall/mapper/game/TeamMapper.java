package com.hml.mall.mapper.game;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.game.Team;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-06-28
 */
public interface TeamMapper extends BaseMapper<Team> {
	@Select("select t.* from ( "
			+" SELECT t1.*,COALESCE(t2.MONEY,0) AS MONEY,t3.USERNAME,t4.ACTNAME "
			+ " ,t5.uno1,t5.uno2,t5.uno3,t5.uno4,t5.uno5,t5.uno6,t5.uno7,t5.uno8,t5.uno9,t5.uno10,t5.uno11,t5.uno12,t5.uno13,t5.uno14 "
			+ " FROM tb_gm_team t1 "
			+" left join tb_gm_userlist t2 on t1.actno = t2.ACTNO and t1.userno = t2.USERNO "
			+" left join tb_hy_user t3 on t1.USERNO = t3.USERNO "
			+" left join tb_gm_activity t4 on t1.ACTNO = t4.ACTNO "
			+" left join TB_HY_USER_RELATION t5 on t1.USERNO = t5.USERNO "
			+" ) t ${ew.customSqlSegment} order by actno desc , userno asc ")
	Page<Team> findPage(Page<Team> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
