package com.hml.mall.mapper.trade;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.trade.WareMem;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2022-07-27
 */
public interface WareMemMapper extends BaseMapper<WareMem> {

	@Select("select t.* from ("
			+ " select A.wareno,C.warename "
			+" ,(case when B.num is null then A.num else B.num end) as num "
			+" ,(case when B.salnum is null then A.salnum else B.salnum end) as salnum "
			+" ,(case when B.hstatus is null then 'N' else B.hstatus end) as hstatus "
			+ " from TB_JY_WARE_SC A "
			+ " LEFT JOIN tb_jy_ware_mem B ON A.wareno = B.wareno "
			+ " LEFT JOIN tb_jy_ware C ON A.wareno = C.wareno "
			+ " ) t ${ew.customSqlSegment} order by  wareno ")
	Page<WareMem> findPage(Page<WareMem> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
}
