package com.hml.mall.iface.sys;

import java.util.Set;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.Login;
import com.hml.mall.entity.sys.UserRole;
import com.hml.mall.security.LoginUser;

/**
* <p>
    *  服务类
    * </p>
*
* @author hml
* @since 2021-04-05
*/


public interface ILoginService extends IService<Login> {
 
    Login findByName(String loginNo);

	/**
	 * 查找用户的菜单权限标识集合
	 * @param userName
	 * @return
	 */
	Set<String> findPermissions(String loginNo);

	
	PageResult findPage(PageRequest pageRequest);
	
	Object delete(Login login);
	
	UserRole findUserRole(String loginNo);
	
	LoginUser findLoginInfo(String loginNo);
}
