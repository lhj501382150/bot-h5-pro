package com.hml.mall.controller.user;


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
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.PasswordUtils;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-05
*/


@RestController
@RequestMapping("/trader" )
public class TraderController {

    @Autowired
    private ILoginService  loginService;
    
    @Autowired
    private IUserService userService;
	
	@PreAuthorize("hasAuthority('firm:trader:add')")
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
		User userOne = userService.getById(record.getUserno());
		if(userOne==null) {
			return HttpResult.error("客户不存在");
		}
		record.setType(userOne.getOrgtype());
		return HttpResult.ok(loginService.save(record));
	}
	@PreAuthorize("hasAuthority('firm:trader:edit')")
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
		User userOne = userService.getById(record.getUserno());
		if(userOne==null) {
			return HttpResult.error("客户不存在");
		}
		record.setType(userOne.getOrgtype());
		System.out.println(record);
		return HttpResult.ok(loginService.saveOrUpdate(record));
	}

	@PreAuthorize("hasAuthority('firm:trader:del')")
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

 	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
// 		默认只查询管理员
 		LoginUser user = SecurityUtils.getLoginInfo();
 		if(user.getType() > 0){
 			pageRequest.getParams().put(user.getQueryNo(), SecurityUtils.getUsername());
 		}
 		pageRequest.getParams().put("type@ge",1);
		return HttpResult.ok(loginService.findPage(pageRequest));
	}
}
