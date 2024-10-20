package com.hml.mall.mapper.bot;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.bot.DataSource;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2023-12-14
 */
public interface DataSourceMapper extends BaseMapper<DataSource> {

	@Select("select t.* from ( select t1.*,t2.sTime,t2.sresult,t2.issue from ( "
			+ " select distinct wareno,contnum from tb_jy_order where buyorsal = 'S' "
			+ " ) t1 "
			+ " left join tb_data_source t2 on t1.contnum = t2.DataId	"
			+ " ) t ${ew.customSqlSegment}")
	Page<Map<String,Object>> findPage(Page<DataSource> page,@Param(Constants.WRAPPER)Wrapper wrapper);
	
	@Select("select t.* from tb_data_source t where issue < #{issue} and mode =#{mode} order by issue desc limit 0,1")
	DataSource findNearDataSorce(@Param("issue")String issue,@Param("mode")Integer mode);
}
