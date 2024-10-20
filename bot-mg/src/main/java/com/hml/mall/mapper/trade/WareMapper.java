package com.hml.mall.mapper.trade;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.trade.Ware;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-17
 */
public interface WareMapper extends BaseMapper<Ware> {
	@Select("select t.* from (select A.*,B.cname,B.sysblkno from TB_JY_WARE A LEFT JOIN tb_jy_wareclass B ON A.CNO = B.CNO) t ${ew.customSqlSegment}")
	Page<Ware> findPage(Page<Ware> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	

	@Select("select t.* from (select t1.*,t2.sysblkno from tb_jy_ware t1 left join tb_jy_wareclass t2 on t1.cno = t2.cno) t ${ew.customSqlSegment}")
	List<Ware> findWare(@Param(Constants.WRAPPER)Wrapper wrapper);
}
