package com.hml.mall.service.sys;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.mall.constant.SysConstants;
import com.hml.mall.entity.sys.Login;
import com.hml.mall.entity.sys.Menu;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.sys.IMenuService;
import com.hml.mall.mapper.sys.LoginMapper;
import com.hml.mall.mapper.sys.MenuMapper;
import com.hml.mall.mapper.user.UserMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */

@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private LoginMapper loginMapper;
	
	@Autowired
	private UserMapper userMapper;

    @Override
    public List< Menu> list(Menu model) {
       QueryWrapper< Menu> wrapper = new QueryWrapper();
       return super.list(wrapper);
    }

    @Override
    public IPage< Menu> pageList( Menu model, Integer pageNum, Integer pageSize) {

       QueryWrapper< Menu> wrapper = new QueryWrapper(model);
       return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
	public List<Menu> findTree(String userName, int menuType) {
		List<Menu> sysMenus = new ArrayList<>();
		List<Menu> menus = findByUser(userName);
		for (Menu menu : menus) {
			if (menu.getParentId() == null || "0".equals(menu.getParentId())) {
				menu.setLevel(0);
				if(!exists(sysMenus, menu)) {
					sysMenus.add(menu);
				}
			}
		}
		sysMenus.sort((o1, o2) -> o1.getOrderNo().compareTo(o2.getOrderNo()));
		findChildren(sysMenus, menus, menuType);
		return sysMenus;
	}
	
	@Override
	public List<Menu> findByUser(String userName) {
		if(userName == null || "".equals(userName) || SysConstants.ADMIN.equalsIgnoreCase(userName)) {
			return menuMapper.selectList(null);
		}else {
			List<Menu> list = null;
			 Login loginUser = loginMapper.selectById(userName);
			 if(loginUser.getType() > 0) {//客户
				 User user = userMapper.selectById(loginUser.getUserno());
				 if(user !=null) {
//					 判断用户单独设置角色 取单独 否则取默认的
					 list = menuMapper.selectMenuByUser(userName);
					 if(list == null || list.size()<=0) {
						 if(user.getOrgtype()==1) {
							 list = menuMapper.findRoleMenus(SysConstants.ORG_ROLE);
						 }else {
							 list = menuMapper.findRoleMenus(SysConstants.CUSTOM_ROLE);
						 }
					 }
				 }
				 return list;
			 }
		}
		return menuMapper.selectMenuByUser(userName);
	}

	
	private boolean exists(List<Menu> menus, Menu sysMenu) {
		boolean exist = false;
		for(Menu menu:menus) {
			if(menu.getMenuId().equals(sysMenu.getMenuId())) {
				exist = true;
			}
		}
		return exist;
	}
	
	private void findChildren(List<Menu> sysMenus, List<Menu> menus, int menuType) {
		for (Menu sysMenu : sysMenus) {
			List<Menu> children = new ArrayList<>();
			for (Menu menu : menus) {
				if(menuType == 1 && menu.getType() == 2) {
					// 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
					continue ;
				}
				if (sysMenu.getMenuId() != null && sysMenu.getMenuId().equals(menu.getParentId())) {
					menu.setParentName(sysMenu.getMenuName());
					menu.setLevel(sysMenu.getLevel() + 1);
					if(!exists(children, menu)) {
						children.add(menu);
					}
				}
			}
			sysMenu.setChildren(children);
			children.sort((o1, o2) -> o1.getOrderNo().compareTo(o2.getOrderNo()));
			findChildren(children, menus, menuType);
		}
	}
}
