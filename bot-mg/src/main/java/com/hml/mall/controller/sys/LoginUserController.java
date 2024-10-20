package com.hml.mall.controller.sys;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.mall.constant.SysConstants;
import com.hml.mall.entity.sys.Login;
import com.hml.mall.entity.user.User;
import com.hml.mall.iface.sys.ILoginService;
import com.hml.mall.iface.user.IUserService;
import com.hml.mall.util.PasswordUtils;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-05
*/


@RestController
@RequestMapping("/loginUser" )
public class LoginUserController {

    @Autowired
    private ILoginService  loginService;
    
    @Autowired
    private IUserService userService;
    
	
	@PreAuthorize("hasAuthority('sys:user:add')")
	@PostMapping(value="/save")
	public HttpResult save(@RequestBody Login record) {
		Login user = loginService.getById(record.getLoginno());
		if(user != null) {
			return HttpResult.error("登陆编号已存在");
		}
		if(record.getLoginpwd() != null) {
			String salt = record.getUserno();
			// 新增用户
			if(loginService.findByName(record.getLoginno()) != null) {
				return HttpResult.error("用户名已存在!");
			}
			String password = PasswordUtils.encode(record.getLoginpwd(), salt);
			record.setLoginpwd(password);
		}
		return HttpResult.ok(loginService.save(record));
	}
	@PreAuthorize("hasAuthority('sys:user:edit')")
	@PostMapping(value="/edit")
	public HttpResult edit(@RequestBody Login record) {
		Login user = loginService.getById(record.getLoginno());
		if(user != null) {
			if(SysConstants.ADMIN.equalsIgnoreCase(user.getLoginno())) {
				return HttpResult.error("超级管理员不允许修改!");
			}
		}else {
			return HttpResult.error("用户不存在");
		}
		if(record.getLoginpwd() != null) {
			// 修改用户, 且修改了密码
			String salt = record.getUserno();
			if(!record.getLoginpwd().equals(user.getLoginpwd())) {
				String password = PasswordUtils.encode(record.getLoginpwd(), salt);
				record.setLoginpwd(password);
			}
		}
		return HttpResult.ok(loginService.saveOrUpdate(record));
	}

	@PreAuthorize("hasAuthority('sys:user:del')")
	@PostMapping(value="/delete")
	public HttpResult delete(@RequestBody Login record) {
		Login sysUser = loginService.getById(record.getLoginno());
		if(sysUser != null && SysConstants.ADMIN.equalsIgnoreCase(sysUser.getLoginno())) {
			return HttpResult.error("超级管理员不允许删除!");
		}
		loginService.delete(record);
		return HttpResult.ok();
	}
	
	@GetMapping(value="/findByName")
	public HttpResult findByUserName(@RequestParam String name) {
		Login login = loginService.findByName(name);
		login.setLoginpwd("");
		return HttpResult.ok(login);
	}
	
	@GetMapping(value="/findPermissions")
	public HttpResult findPermissions(@RequestParam String name) {
		return HttpResult.ok(loginService.findPermissions(name));
	}
	
	@GetMapping(value="/findUserRoles")
	public HttpResult findUserRoles(@RequestParam String loginNo) {
		return HttpResult.ok(loginService.findUserRole(loginNo));
	}

 	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
// 		默认只查询管理员
 		pageRequest.getParams().put("type",0);
		return HttpResult.ok(loginService.findPage(pageRequest));
	}
	
 	
	@PostMapping(value="/updatePassword")
	public HttpResult updatePassword(String password,String newPassword) {
		String userName = SecurityUtils.getUsername();
		Login user = loginService.findByName(userName);
		if(user == null) {
			return HttpResult.error("登陆已超时请重新登陆!");
		}
		String oldPwd = PasswordUtils.encode(password, user.getUserno()); 
		if(!oldPwd.equals(user.getLoginpwd())) {
			return HttpResult.error("原始密码不正确");
		}
		
		String newPwd = PasswordUtils.encode(newPassword, user.getUserno()); 
		user.setLoginpwd(newPwd);
		
		User item = userService.getById(user.getLoginno());
		if(item != null) {
			item.setPaypwd(newPwd);
			userService.updateById(item);
		}
		
		return HttpResult.ok(loginService.saveOrUpdate(user));
	}
	
	@PreAuthorize("hasAuthority('firm:member:editPwd')")
	@PostMapping(value="/initPassword")
	public HttpResult initPassword(String userno,String newPassword) {
		Login user = loginService.findByName(userno);
		if(user == null) {
			return HttpResult.error("用户不存在!");
		}
		String newPwd = PasswordUtils.encode(newPassword, user.getUserno()); 
		user.setLoginpwd(newPwd);
		return HttpResult.ok(loginService.saveOrUpdate(user));
	}

}
