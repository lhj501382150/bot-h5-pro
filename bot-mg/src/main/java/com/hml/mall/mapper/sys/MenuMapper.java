package com.hml.mall.mapper.sys;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hml.mall.entity.sys.Menu;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */
public interface MenuMapper extends BaseMapper<Menu> {

	List<Menu> selectMenuByUser(String userName);
	
	List<Menu> findRoleMenus(Long roleId);
}
