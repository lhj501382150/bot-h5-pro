package com.hml.mall.mapper.reward;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.reward.ShareTotal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */
public interface ShareTotalMapper extends BaseMapper<ShareTotal> {

	@Select("select t.* from (select t1.*,t2.username from tb_jl_share_total t1 left join TB_HY_USER t2 on t1.USERNO = t2.USERNO) t ${ew.customSqlSegment}")
	Page<ShareTotal> findPage(Page<ShareTotal> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select sum(consumn) as consumn,sum(bonus) as bonus from (select t1.*,t2.username from tb_jl_share_total t1 left join TB_HY_USER t2 on t1.USERNO = t2.USERNO) t ${ew.customSqlSegment}")
	ShareTotal selectSum(@Param(Constants.WRAPPER)Wrapper wrapper);
	
	void saveTotal(@Param("fdate")String fdate);
	
	void updateTotal(@Param("fdate")String fdate);
	
	void saveHandle(@Param("fdate")String fdate);
}
