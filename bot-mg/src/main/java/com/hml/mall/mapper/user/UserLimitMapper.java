package com.hml.mall.mapper.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.user.UserLimit;
import com.hml.mall.entity.user.UserRelation;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2024-09-19
 */
public interface UserLimitMapper extends BaseMapper<UserLimit> {

	@Select("select t.* from ( "
			+ " select t1.*,t2.orgtype,t3.clevel from tb_hy_user_limit t1 "
			+ " left join tb_hy_user t2 on t1.userno = t2.userno "
			+ " left join tb_hy_user_relation t3 on t1.userno = t3.userno "
			+ ") t ${ew.customSqlSegment} ")
	Page<UserLimit> findPage(Page<UserLimit> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t.* from ( "
			+ " select t1.*,t2.orgtype from tb_hy_user_relation t1 "
			+ " left join tb_hy_user t2 on t1.userno = t2.userno "
			+ ") t ${ew.customSqlSegment} ")
	List<UserRelation> findUser(@Param(Constants.WRAPPER)Wrapper wrapper); 
}
