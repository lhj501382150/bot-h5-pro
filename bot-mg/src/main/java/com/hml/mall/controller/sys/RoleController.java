package com.hml.mall.controller.sys;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.mall.constant.SysConstants;
import com.hml.mall.entity.sys.Role;
import com.hml.mall.entity.sys.RoleMenu;
import com.hml.mall.iface.sys.IRoleService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-05
*/


@RestController
@RequestMapping("/role" )
public class RoleController {

    @Autowired
    private IRoleService  roleService;

    @PreAuthorize("hasAuthority('sys:role:add')")
	@PostMapping(value="/save")
	public HttpResult save(@RequestBody Role record) {
		Role role = roleService.getById(record.getRoleId());
		if(role != null) {
			return HttpResult.error("角色编号已存在");
		}
		 
		record.setOwner(SecurityUtils.getUsername());
		return HttpResult.ok(roleService.save(record));
	}
    @PreAuthorize("hasAuthority('sys:role:edit')")
    @PostMapping(value="/edit")
    public HttpResult edit(@RequestBody Role record) {
    	Role role = roleService.getById(record.getRoleId());
    	if(role == null) {
    		return HttpResult.error("角色不存在");
    	}else {
    		if(SysConstants.ADMIN.equalsIgnoreCase(role.getRoleName())) {
    			return HttpResult.error("超级管理员不允许修改!");
    		}
    	}
    	 
    	record.setOwner(SecurityUtils.getUsername());
    	return HttpResult.ok(roleService.updateById(record));
    }

	@PreAuthorize("hasAuthority('sys:role:del')")
	@PostMapping(value="/delete")
	public HttpResult delete(@RequestBody Role record) {
		Role role = roleService.getById(record.getRoleId());
		if(role.getRoleName().equals(SysConstants.ADMIN)){
			return HttpResult.error("超级管理员不能删除!");
		}
		roleService.deleteRole(record);
		return HttpResult.ok();
	}

	@PreAuthorize("hasAuthority('sys:role:view')")
	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
		 
		if(!SecurityUtils.getUsername().equals(SysConstants.ADMIN)) {
			pageRequest.getParams().put("ROLE_ID@GE",0);
			pageRequest.setOwner(SecurityUtils.getUsername());
		}
		return HttpResult.ok(roleService.findPage(pageRequest));
	}
	
//	@PreAuthorize("hasAuthority('sys:role:view')")
	@GetMapping(value="/findAll")
	public HttpResult findAll() {
		QueryWrapper<Role> qw = new QueryWrapper<Role>();
		qw.ge("ROLE_ID", 0);
		if(!SecurityUtils.getUsername().equals(SysConstants.ADMIN)) {
			qw.eq("owner", SecurityUtils.getUsername());
		}
		
		List<Role> list = roleService.list(qw);
		return HttpResult.ok(list);
	}
	
	@PreAuthorize("hasAuthority('sys:role:view')")
	@RequestMapping(value="/findRoleMenus")
	public HttpResult findRoleMenus(@RequestParam Long roleId) {
		return HttpResult.ok(roleService.findRoleMenus(roleId));
	}
	
	@PreAuthorize("hasAuthority('sys:role:view')")
	@PostMapping(value="/saveRoleMenus")
	public HttpResult saveRoleMenus(@RequestBody List<RoleMenu> records) {
		if(records !=null && records.size()>0){
			RoleMenu record = records.get(0);
			Role role = roleService.getById(record.getRoleId());
			if(SysConstants.ADMIN.equalsIgnoreCase(role.getRoleName())) {
				// 如果是超级管理员，不允许修改
				return HttpResult.error("超级管理员拥有所有菜单权限，不允许修改！");
			}
		}
		 
		return HttpResult.ok(roleService.saveRoleMenus(records));
	}

}
