package com.hml.mall.mapper.money;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.money.UsermoneyChangeApply;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-07-11
 */
public interface UsermoneyChangeApplyMapper extends BaseMapper<UsermoneyChangeApply> {

	@Select("select t.* from (select t1.*,t2.username,t2.nickname,t3.acctname,t4.subname"
			+ " from tb_zj_usermoney_change_apply t1 "
			+ " left join TB_HY_USER t2 on t1.USERNO = t2.USERNO "
			+ " left join tb_zj_account t3 on t1.acctno = t3.acctno "
			+ " left join tb_zj_subject t4 on t1.subno = t4.sunno "
			+ ") t ${ew.customSqlSegment} order by wartno desc ")
	Page<UsermoneyChangeApply> findPageByUser(Page<UsermoneyChangeApply> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
