package com.hml.mall.mapper.reward;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.reward.OrgHandle;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */
public interface OrgHandleMapper extends BaseMapper<OrgHandle> {

	@Select("select t.* from (select t1.*,t2.username,t2.nickname from tb_jl_org_handle t1 left join TB_HY_USER t2 on t1.ORGNO = t2.USERNO) t ${ew.customSqlSegment}")
	Page<OrgHandle> findPage(Page<OrgHandle> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select sum(consumn) as consumn,sum(bonus) as bonus,sum(comms) as comms,sum(commbonus) as commbonus from (select t1.*,t2.username from tb_jl_org_handle t1 left join TB_HY_USER t2 on t1.ORGNO = t2.USERNO) t ${ew.customSqlSegment}")
	OrgHandle selectSum(@Param(Constants.WRAPPER)Wrapper wrapper);
}
