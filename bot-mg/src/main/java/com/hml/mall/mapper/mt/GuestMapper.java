package com.hml.mall.mapper.mt;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.mt.Guest;
import com.hml.mall.entity.mt.Meeting;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-05-14
 */
public interface GuestMapper extends BaseMapper<Guest> {
	@Select("select t.* from ("
			+ " select t1.*,t2.username,t3.mname from tb_mt_guest t1"
			+ " left join  tb_mt_meeting t3 on t1.mno = t3.mno "
			+ " left join TB_HY_USER_RELATION t2 on t1.USERNO = t2.USERNO "
			+ ") t ${ew.customSqlSegment} order by mno desc ")
	Page<Guest> findPage(Page<Guest> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
