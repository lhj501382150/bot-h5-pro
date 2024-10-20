package com.hml.mall.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.user.UserChange;
import com.hml.mall.entity.user.UserChangeDtl;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-06-29
 */
public interface UserChangeMapper extends BaseMapper<UserChange> {

	@Select("select t.* from (select t1.*,t2.username from tb_hy_user_change t1 left join tb_hy_user t2 on t1.userno = t2.userno) t ${ew.customSqlSegment} order by wno desc")
	Page<UserChange> findPage(Page<UserChange> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Update("UPDATE TB_HY_USER SET VIPLEV = #{VIPLEV}  WHERE USERNO = #{USERNO}")
	int updateUserVip(UserChangeDtl dtl)throws Exception;
	
	@Update("UPDATE TB_HY_USER SET REALSTATUS = #{REALSTATUS},REALFILE1 = #{REALFILE1},REALFILE2 = #{REALFILE2}"
			+",REALFILE3 = #{REALFILE3},REALFILE4 = #{REALFILE4},REALFILE5 = #{REALFILE5}"
			+ "  WHERE USERNO = #{USERNO}")
	int updateUserReal(UserChangeDtl dtl)throws Exception;
}
