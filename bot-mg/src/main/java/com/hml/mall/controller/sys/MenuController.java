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
import com.hml.mall.entity.sys.Menu;
import com.hml.mall.iface.sys.IMenuService;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-05
*/


@RestController
@RequestMapping("/menu" )
public class MenuController {

    @Autowired
    private IMenuService  menuService;

    @PreAuthorize("hasAuthority('sys:menu:add') AND hasAuthority('sys:menu:edit')")
	@PostMapping(value="/save")
	public HttpResult save(@RequestBody Menu record) {
		return HttpResult.ok(menuService.saveOrUpdate(record));
	}

	@PreAuthorize("hasAuthority('sys:menu:delete')")
	@PostMapping(value="/delete")
	public HttpResult delete(@RequestBody Menu record) {
		return HttpResult.ok(menuService.removeById(record));
	}

	@GetMapping(value="/findNavTree")
	public HttpResult findNavTree(@RequestParam String userName) {
		userName = SecurityUtils.getUsername();
		return HttpResult.ok(menuService.findTree(userName, 1));
	}
	
	@GetMapping(value="/findMenuTree")
	public HttpResult findMenuTree() {
		String userName = SecurityUtils.getUsername();
		return HttpResult.ok(menuService.findTree(userName, 0));
	}
}
