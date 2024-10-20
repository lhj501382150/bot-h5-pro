package com.hml.mall.mapper.trade;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.trade.WareJjHandorder;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-09-30
 */
public interface WareJjHandorderMapper extends BaseMapper<WareJjHandorder> {
	@Select("select t.* from (select t1.*,t2.warename ,t3.username,t4.username as susername "
			+ " from tb_jy_ware_jj_handorder t1 "
			+ " left join tb_jy_ware t2 on t1.wareno = t2.wareno "
			+ " left join tb_hy_user t3 on t1.userno = t3.userno "
			+ " left join tb_hy_user t4 on t1.suserno = t4.userno) t ${ew.customSqlSegment} order by hid desc ")
	Page<WareJjHandorder> findPage(Page<WareJjHandorder> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
