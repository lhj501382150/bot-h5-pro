package com.hml.mall.controller.sys;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hml.core.http.HttpResult;
import com.hml.mall.entity.sys.UserRole;
import com.hml.mall.iface.sys.IUserRoleService;


/**
*  前端控制器
* @author hml
* @since 2021-04-05
*/


@RestController
@RequestMapping("/userRole" )
public class UserRoleController {

    @Autowired
    private IUserRoleService  userRoleService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @RequestMapping("/save")
    public HttpResult save(UserRole model) {
        userRoleService.save(model);
        return HttpResult.ok();
    }


    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     UserRole model = userRoleService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @RequestMapping("/list")
    public HttpResult list() {
        UserRole model = new  UserRole();
        List<UserRole> list =  userRoleService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @RequestMapping("/pageList")
    public HttpResult pageList(Integer pageNum, Integer pageSize) {

        UserRole model = new  UserRole();
        IPage<UserRole> page = userRoleService.pageList(model, pageNum, pageSize);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
