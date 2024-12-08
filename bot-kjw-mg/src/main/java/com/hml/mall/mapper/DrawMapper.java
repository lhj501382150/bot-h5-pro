package com.hml.mall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.Draw;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2024-12-05
 */
public interface DrawMapper extends BaseMapper<Draw> {

	@Select("SELECT T.*,t2.MODE_NAME,t2.TIME,t2.TYPE,T2.MODE_URL  FROM tb_kj_draw T "
			+ " left join tb_kj_draw_type t2 on t.mode = t2.mode "
			+ " WHERE T.MODE = #{mode} ORDER BY DATA_ID DESC LIMIT 0,1")
	Draw getLast(@Param("mode")String mode);
	
	@Select("select t.*,t2.MODE_NAME,t2.TIME,t2.TYPE,T2.MODE_URL from "
			+ " ( SELECT t1.*, ROW_NUMBER() OVER (PARTITION BY mode ORDER BY data_id DESC) AS rn  FROM tb_kj_draw t1 ) t "
			+ " left join tb_kj_draw_type t2 on t.mode = t2.mode "
			+ "  where rn = 1 order by t2.seq")
	List<Draw> getNewDraws();
	
	@Select("select t.* from (	"
			+ " SELECT T.*,t2.MODE_NAME,t2.TIME,t2.TYPE,T2.MODE_URL  FROM tb_kj_draw T "
			+ " left join tb_kj_draw_type t2 on t.mode = t2.mode "
			+ "  ) t ${ew.customSqlSegment} ")
	Page<Draw> findPage(Page<Draw> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
