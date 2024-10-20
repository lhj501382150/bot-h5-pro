package com.hml.mall.mapper.game;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.game.Userlist;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-06-09
 */
public interface UserlistMapper extends BaseMapper<Userlist> {

	@Select("select t.* from (select t1.*,t2.actname,t3.username,t4.gameno "
			+ ",t5.uno1,t5.uno2,t5.uno3,t5.uno4,t5.uno5  "
			+ " from tb_gm_userlist t1 "
			+ " left join tb_gm_activity t2 on t1.actno = t2.actno "
			+ " left join tb_hy_user t3 on t1.userno =t3.userno "
			+ " left join tb_hy_user_relation t5 on t2.userno = t5.userno"  
			+ " left join tb_gm_userinfo t4 on t1.USERNO=t4.USERNO and t2.C1CNO = t4.C1CNO and t2.TERTYPE = t4.TERTYPE "
			+ ") t ${ew.customSqlSegment}")
	Page<Userlist> findPage(Page<Userlist> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
