package com.hml.mall.mapper.reward;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.reward.OrgParas;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-05-26
 */
public interface OrgParasMapper extends BaseMapper<OrgParas> {

	@Select("select t.* from (select t1.*,ifnull(t2.username,'默认') as username from tb_jl_org_paras t1 left join TB_HY_USER t2 on t1.ORGNO = t2.USERNO) t ${ew.customSqlSegment}")
	Page<OrgParas> findPage(Page<OrgParas> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
