package com.hml.mall.controller.sys;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.LoginLog;
import com.hml.mall.iface.sys.ILoginLogService;
import com.hml.mall.security.LoginUser;
import com.hml.mall.util.SecurityUtils;


/**
*  前端控制器
* @author hml
* @since 2021-04-13
*/


@RestController
@RequestMapping("/loginLog" )
public class LoginLogController {

    @Autowired
    private ILoginLogService  loginLogService;

	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:loginLog:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody LoginLog model) {
        loginLogService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:loginLog:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	LoginUser user = SecurityUtils.getLoginInfo();
    	if(user.getType() > 0){
    		pageRequest.getParams().put("LOGINNO", user.getLoginno());
    	}
    	pageRequest.getParams().put("WATERID@DESC", "desc");
        PageResult page = loginLogService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
