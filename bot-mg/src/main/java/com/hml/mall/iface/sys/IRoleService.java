package com.hml.mall.iface.sys;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.Menu;
import com.hml.mall.entity.sys.Role;
import com.hml.mall.entity.sys.RoleMenu;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-05
*/


public interface IRoleService extends IService<Role> {

   /**
   * 查询
   *
   * @param model
   * @return
   */
   List<Role> list(Role model);

    /**
    * 分页查询
    *
    * @param model
    * @param pageNum  第几页
    * @param pageSize 每页数量
    * @return
    */
    PageResult findPage(PageRequest pageRequest);
    
    /**
	 * 查询角色菜单集合
	 * @return
	 */
	List<Menu> findRoleMenus(Long roleId);

	/**
	 * 保存角色菜单
	 * @param records
	 * @return
	 */
	int saveRoleMenus(List<RoleMenu> records);
	/**
	 * 删除角色
	 * @param role
	 */
	void deleteRole(Role role);

}
