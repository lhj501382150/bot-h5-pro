package com.hml.mall.mapper.notice;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.notice.Mess;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */
public interface MessMapper extends BaseMapper<Mess> {

	@Select("select t.* from (select A.*,B.telno from tb_sys_mess A left join tb_hy_user B on A.userno = B.userno) t ${ew.customSqlSegment}")
	Page<Mess> findPage(Page<Mess> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t.* from (select A.*,B.telno from tb_sys_mess A left join tb_hy_user B on A.userno = B.userno) t ${ew.customSqlSegment}")
	List<Mess> findList(@Param(Constants.WRAPPER)Wrapper wrapper);
	
}
