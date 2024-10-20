package com.hml.mall.controller.sys;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.iface.sys.ILogService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-11
*/


@RestController
@RequestMapping("/log" )
public class LogController {

    @Autowired
    private ILogService  logService;

    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:log:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0){
    		pageRequest.getParams().put("OPTUSER", user.getLoginno());
    	}
    	pageRequest.getParams().put("LOG_ID@DESC", "A");
        PageResult page = logService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
