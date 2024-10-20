package com.hml.mall.controller.spread;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.spread.GrowUser;
import com.hml.mall.iface.spread.IGrowUserService;


/**
*  前端控制器
* @author hml
* @since 2021-09-06
*/


@RestController
@RequestMapping("/growUser" )
public class GrowUserController {

    @Autowired
    private IGrowUserService  growUserService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growuser:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody GrowUser model) {
        growUserService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growuser:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody GrowUser model) {
        growUserService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growuser:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody GrowUser model) {
        growUserService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growuser:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     GrowUser model = growUserService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growuser:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        GrowUser model = new  GrowUser();
        List<GrowUser> list =  growUserService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growuser:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = growUserService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
