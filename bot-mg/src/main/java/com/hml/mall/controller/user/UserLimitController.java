package com.hml.mall.controller.user;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.user.UserLimit;
import com.hml.mall.iface.user.IUserLimitService;


/**
*  前端控制器
* @author hml
* @since 2024-09-19
*/


@RestController
@RequestMapping("/userLimit" )
public class UserLimitController {

    @Autowired
    private IUserLimitService  userLimitService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:userLimit:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody UserLimit model) {
        userLimitService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:userLimit:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody UserLimit model) {
        userLimitService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:userLimit:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody UserLimit model) {
        userLimitService.removeById(model.getUserno());
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('sys:userLimit:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserLimit model = userLimitService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:userLimit:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        UserLimit model = new  UserLimit();
        List<UserLimit> list =  userLimitService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:userLimit:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = userLimitService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
