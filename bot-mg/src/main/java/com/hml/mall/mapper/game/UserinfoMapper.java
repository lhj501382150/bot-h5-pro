package com.hml.mall.mapper.game;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.game.Userinfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-06-28
 */
public interface UserinfoMapper extends BaseMapper<Userinfo> {
	@Select("select t.* from (select t1.*,t2.username,t3.cname "
			+" ,t2.uno1,t2.uno2,t2.uno3,t2.uno4,t2.uno5 "
			+ " from tb_gm_userinfo t1 " + 
			" left join tb_hy_user_relation t2 on t1.userno = t2.userno" + 
			" left join tb_jy_wareclass t3 on t1.c1cno = t3.cno" + 
			" ) t ${ew.customSqlSegment}")
	Page<Userinfo> findPage(Page<Userinfo> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
