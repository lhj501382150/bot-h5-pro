package com.hml.mall.mapper.user;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.user.UserLandinfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-12-27
 */
public interface UserLandinfoMapper extends BaseMapper<UserLandinfo> {

	@Select("select t.* from (select t1.*,t2.username  from tb_hy_user_landinfo t1 "
			+ "left join tb_hy_user t2 on t1.userno = t2.userno "
			+ ") t ${ew.customSqlSegment} order by artid desc")
	Page<UserLandinfo> findPage(Page<UserLandinfo> page,@Param(Constants.WRAPPER) Wrapper wrapper);
}
