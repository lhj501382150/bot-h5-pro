package com.hml.mall.controller.user;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserChange;
import com.hml.mall.iface.user.IUserChangeService;
import com.hml.mall.util.IPUtils;
import com.hml.mall.util.SecurityUtils;
import com.hml.utils.DateTimeUtils;


/**
*  前端控制器
* @author hml
* @since 2021-06-29
*/


@RestController
@RequestMapping("/userChange" )
public class UserChangeController {

    @Autowired
    private IUserChangeService  userChangeService;

    /**
     * 审核
     *
     * @param model
     * @return
     * @throws Exception 
     */
     @PreAuthorize("hasAuthority('firm:change:audit')")
     @RequestMapping("/audit")
     public HttpResult audit(@RequestBody UserChange model,HttpServletRequest request) throws Exception {
    	 model.setChkip(IPUtils.getIpAddr(request));
    	 model.setChktime(DateTimeUtils.getDateTime());
    	 model.setChkuser(SecurityUtils.getUsername());
    	 model.setCheckstatus("2");
         userChangeService.auditUser(model);
         return HttpResult.ok();
     }
     /**
      * 审核
      *
      * @param model
      * @return
      * @throws Exception 
      */
      @PreAuthorize("hasAuthority('firm:change:reject')")
      @RequestMapping("/reject")
      public HttpResult reject(@RequestBody UserChange model,HttpServletRequest request) throws Exception {
     	 model.setChkip(IPUtils.getIpAddr(request));
     	 model.setChktime(DateTimeUtils.getDateTime());
     	 model.setChkuser(SecurityUtils.getUsername());
     	 model.setCheckstatus("3");
          userChangeService.auditUser(model);
          return HttpResult.ok();
      }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('firm:change:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserChange model = userChangeService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('firm:change:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        UserChange model = new  UserChange();
        List<UserChange> list =  userChangeService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('firm:change:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userChangeService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
