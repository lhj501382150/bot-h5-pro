package com.hml.mall.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

	@Select("SELECT T.* FROM tb_kj_draw T WHERE MODE = #{mode} ORDER BY DATA_ID DESC LIMIT 0,1")
	Draw getLast(@Param("mode")String mode);
}
