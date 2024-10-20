package com.hml.mall.mapper.trade;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.trade.WareScHandorder;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-05-13
 */
public interface WareScHandorderMapper extends BaseMapper<WareScHandorder> {

	@Select("select t.* from (select t1.*,t2.warename ,t3.username as susername "
			+ " from tb_jy_ware_sc_handorder t1 "
			+ " left join tb_jy_ware t2 on t1.wareno = t2.wareno "
			+ " left join tb_hy_user t3 on t1.suserno = t3.userno) t ${ew.customSqlSegment} order by hid desc ")
	Page<WareScHandorder> findPage(Page<WareScHandorder> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
