package com.hml.mall.mapper.user;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.user.UserDigitart;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-09-22
 */
public interface UserDigitartMapper extends BaseMapper<UserDigitart> {

	@Select("select t.* from (select t1.*,t2.username,t3.warename,t4.username as orgusername  from tb_hy_user_digitart t1 "
			+ "left join tb_hy_user t2 on t1.userno = t2.userno "
			+ "left join tb_hy_user t4 on t1.orguserno = t4.userno "
			+ "left join tb_jy_ware t3 on t1.wareno = t3.wareno "
			+ ") t ${ew.customSqlSegment} order by artid desc")
	Page<UserDigitart> findPage(Page<UserDigitart> page,@Param(Constants.WRAPPER) Wrapper wrapper);
	
	@Select("SELECT MAX(CARDNO) as CARDNO FROM tb_hy_user_digitart where length(CARDNO) = #{length} and cardno  like concat(#{pre},'%') ")
	Map<String,Object> getMaxNo(@Param("length")int length,@Param("pre")String pre);
}
