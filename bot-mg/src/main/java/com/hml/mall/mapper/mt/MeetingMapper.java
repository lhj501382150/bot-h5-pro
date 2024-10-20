package com.hml.mall.mapper.mt;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.mt.Meeting;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-05-14
 */
public interface MeetingMapper extends BaseMapper<Meeting> {

	@Select("select t.* from ("
			+ " select t1.*,t2.username "
			+ " from tb_mt_meeting t1 "
			+ " left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+ ") t ${ew.customSqlSegment} order by mno desc ")
	Page<Meeting> findPage(Page<Meeting> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
