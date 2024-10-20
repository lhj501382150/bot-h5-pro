package com.hml.mall.service.sys;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.Menu;
import com.hml.mall.entity.sys.Role;
import com.hml.mall.entity.sys.RoleMenu;
import com.hml.mall.iface.sys.IRoleService;
import com.hml.mall.mapper.sys.MenuMapper;
import com.hml.mall.mapper.sys.RoleMapper;
import com.hml.mall.mapper.sys.RoleMenuMapper;
import com.hml.mall.util.SecurityUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */

@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

	@Autowired
	private MenuMapper menuMappe;

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	
    @Override
    public List< Role> list(Role model) {
       QueryWrapper<Role> wrapper = new QueryWrapper();
       wrapper.eq("OWNER", SecurityUtils.getUsername());
       return super.list(wrapper);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {

       return MybatisPlusPageHelper.findPage(pageRequest, roleMapper);
    }

	@Override
	public List<Menu> findRoleMenus(Long roleId) {
		 
		List<Menu> menuList = menuMappe.findRoleMenus(roleId);
		return menuList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int saveRoleMenus(List<RoleMenu> records) {
		if(records == null || records.isEmpty()) {
			return 1;
		}
		Integer roleId = records.get(0).getRoleId(); 
		QueryWrapper<RoleMenu> qw = new QueryWrapper<>();
		qw.eq("ROLE_ID", roleId);
		roleMenuMapper.delete(qw);
		if("A".equals(records.get(0).getMenuId())) return 0;
		for(RoleMenu rm : records){
			roleMenuMapper.insert(rm);
		}
		return 0;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteRole(Role role) {
//		删除角色权限信息
		QueryWrapper<RoleMenu> qw = new QueryWrapper<RoleMenu>();
		qw.eq("ROLE_ID", role.getRoleId());
		roleMenuMapper.delete(qw);
//		删除角色
		roleMapper.deleteById(role);
	}

    
}
