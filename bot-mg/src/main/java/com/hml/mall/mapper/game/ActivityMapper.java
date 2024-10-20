package com.hml.mall.mapper.game;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.game.Activity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-06-09
 */
public interface ActivityMapper extends BaseMapper<Activity> {
	
	@Select("select t.* from (select t1.*,t2.username,t3.cname as c1name,t4.cname as c2name,t5.warename "
			+ ",t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5  "
			+ " from tb_gm_activity t1 " + 
			" left join tb_hy_user_relation t2 on t1.userno = t2.userno" + 
			" left join tb_jy_wareclass t3 on t1.c1cno = t3.cno" + 
			" left join tb_jy_wareclass t4 on t1.c2cno = t4.cno" + 
			" left join tb_jy_ware t5 on t1.wareno = t5.wareno) t ${ew.customSqlSegment} order by actno desc ")
	Page<Activity> findPage(Page<Activity> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t.* from ( "
			+" select t1.*,t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5 from tb_gm_activity t1 "
			+" left join tb_hy_user_relation t2 on t1.userno = t2.userno "
			+ " ) t ${ew.customSqlSegment} ")
	List<Activity> findList(@Param(Constants.WRAPPER)Wrapper wrapper);
}
