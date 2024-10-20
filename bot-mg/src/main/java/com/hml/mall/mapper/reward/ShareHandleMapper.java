package com.hml.mall.mapper.reward;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.reward.ShareHandle;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */
public interface ShareHandleMapper extends BaseMapper<ShareHandle> {

	@Select("select t.* from (select t1.*,t2.username from tb_jl_share_handle t1 left join TB_HY_USER t2 on t1.USERNO = t2.USERNO) t ${ew.customSqlSegment}")
	Page<ShareHandle> findPage(Page<ShareHandle> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select sum(consumn) as consumn,sum(bonus) as bonus from (select t1.*,t2.username from tb_jl_share_handle t1 left join TB_HY_USER t2 on t1.USERNO = t2.USERNO) t ${ew.customSqlSegment}")
	ShareHandle selectSum(@Param(Constants.WRAPPER)Wrapper wrapper);
	
	void updateHandle(ShareHandle handle);
}
