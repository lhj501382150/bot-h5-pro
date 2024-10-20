package com.hml.mall.service.sys;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hml.core.page.MybatisPlusPageHelper;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.constant.SysConstants;
import com.hml.mall.entity.sys.Login;
import com.hml.mall.entity.sys.Menu;
import com.hml.mall.entity.sys.Role;
import com.hml.mall.entity.sys.UserRole;
import com.hml.mall.entity.user.User;
import com.hml.mall.entity.user.UserRelation;
import com.hml.mall.iface.sys.ILoginService;
import com.hml.mall.mapper.sys.LoginMapper;
import com.hml.mall.mapper.sys.MenuMapper;
import com.hml.mall.mapper.sys.RoleMapper;
import com.hml.mall.mapper.sys.UserRoleMapper;
import com.hml.mall.mapper.user.UserMapper;
import com.hml.mall.mapper.user.UserRelationMapper;
import com.hml.mall.security.LoginUser;
import com.hml.utils.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hml
 * @since 2021-04-05
 */

@Service("loginService")
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {


	@Autowired
	private LoginMapper loginMapper;
	
	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private UserRoleMapper userRoleMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserRelationMapper userRelationMapper;
	
	 @Override
	 @Transactional(rollbackFor=Exception.class)
	public boolean saveOrUpdate(Login user) {
		 boolean flag = true;
		 flag = super.saveOrUpdate(user);
//		 处理角色信息
		 UserRole userRole = user.getUserRole();
		 if(flag && user.getUserRole()!=null){
			 QueryWrapper<UserRole> qw = new QueryWrapper<>();
			 qw.eq("loginno",user.getLoginno());
			 userRoleMapper.delete(qw);
			 userRoleMapper.insert(userRole);
		 }
		 return flag;
	}
	
	@Override
	public Login findByName(String username) {
		QueryWrapper<Login> qw = new QueryWrapper<>();
		qw.eq("loginno", username);
		qw.eq("isvalid","Y");
		Login user = loginMapper.selectOne(qw);
		if(user !=null) {
			UserRole userRole = findUserRole(user.getLoginno());
			if(userRole!=null) {
				user.setUserRole(userRole);
				user.setRoleName(getRoleName(userRole));
			}
			if(user.getType() > 0) {
				UserRelation ur = userRelationMapper.selectById(user.getUserno());
				if(ur != null) {
					user.setClevel(ur.getClevel());
				}
			}
		}
		return user;
	}

	@Override
	public Set<String> findPermissions(String loginNo) {
		 Set<String> perms = new HashSet<>();
		 List<Menu> list = null ;
		 if(SysConstants.ADMIN.equals(loginNo)){
			 list = menuMapper.selectList(null);
		 }else{
			 list = menuMapper.selectMenuByUser(loginNo);
			 if(list == null || list.size() <= 0){
				 Login loginUser = loginMapper.selectById(loginNo);
				 User user = userMapper.selectById(loginUser.getUserno());
				 if(user != null) {
					 if(user.getOrgtype()==1) {
						 list = menuMapper.findRoleMenus(SysConstants.ORG_ROLE);
					 }else {
						 list = menuMapper.findRoleMenus(SysConstants.CUSTOM_ROLE);
					 }
				 }
			 }
		 }
		 for(Menu menu:list){
			 String perm = menu.getPerms();
			 if(!StringUtils.isBlank(perm)){
				 perms.add(perm);
			 }
		 }
		return perms;
	}

	 
	@Override
	public PageResult findPage(PageRequest pageRequest) {
		//pageRequest.getParams().put("isvalid", "Y");
		PageResult pr =  MybatisPlusPageHelper.findPage(pageRequest, loginMapper,"findPage");
//		加载用户角色信息
		findUserRoles(pr);
		return pr;
	}
	
	/**
	 * 加载用户角色
	 * @param pageResult
	 */
	private void findUserRoles(PageResult pageResult) {
		List<Login> content = (List<Login>)pageResult.getContent();
		for(Login user:content) {
			UserRole userRole = findUserRole(user.getLoginno());
			if(userRole != null) {
				user.setUserRole(userRole);
				user.setRoleName(getRoleName(userRole));
			}
		}
	}

	private String getRoleName(UserRole userRole) {
		Role role = roleMapper.selectById(userRole.getRoleId());
		if(role == null) {
			return "";
		}
		return role.getRoleName();
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Object delete(Login login) {
		UserRole userRole = login.getUserRole();
		if(userRole != null) {
			QueryWrapper<UserRole> qw = new QueryWrapper<UserRole>();
			qw.eq("LOGINNO", userRole.getLoginno());
			qw.eq("ROLE_ID", userRole.getRoleId());
			userRoleMapper.delete(qw);
		}
		int id = loginMapper.deleteById(login.getLoginno());
		return id;
	}

	@Override
	public UserRole findUserRole(String loginNo) {
		QueryWrapper<UserRole> qw = new QueryWrapper<>();
		qw.eq("LOGINNO", loginNo);
		UserRole userRole = userRoleMapper.selectOne(qw);
		return userRole;
	}
	
	@Override
	public LoginUser findLoginInfo(String loginNo) {
		LoginUser loginUser = new LoginUser();
		loginUser = loginMapper.findLoginInfo(loginNo);
		return loginUser;
	}

}
