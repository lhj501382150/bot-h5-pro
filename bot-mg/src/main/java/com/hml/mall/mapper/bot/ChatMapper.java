package com.hml.mall.mapper.bot;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hml.mall.entity.bot.Chat;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2023-12-08
 */
public interface ChatMapper extends BaseMapper<Chat> {

	@Select("select t.* from (	select t1.* ,t2.username,t2.nickname from tb_jy_chat t1  " + 
			"	LEFT JOIN tb_hy_user t2 ON t1.userno = t2.userno " + 
			" ) t ${ew.customSqlSegment}")
	Page<Chat> findPage(Page<Chat> page,@Param(Constants.WRAPPER)Wrapper wrapper);
}
