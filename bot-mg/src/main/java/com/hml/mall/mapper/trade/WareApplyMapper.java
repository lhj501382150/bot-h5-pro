package com.hml.mall.mapper.trade;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.trade.WareApply;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-05-18
 */
public interface WareApplyMapper extends BaseMapper<WareApply> {
	@Select("select t.* from (select A.*,B.cname from TB_JY_WARE_APPLY A LEFT JOIN tb_jy_wareclass B ON A.CNO = B.CNO) t ${ew.customSqlSegment}")
	Page<WareApply> findPage(Page<WareApply> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
