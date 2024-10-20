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
import com.hml.mall.entity.spread.GrowTask;
import com.hml.mall.iface.spread.IGrowTaskService;


/**
*  前端控制器
* @author hml
* @since 2021-09-06
*/


@RestController
@RequestMapping("/growTask" )
public class GrowTaskController {

    @Autowired
    private IGrowTaskService  growTaskService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growtask:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody GrowTask model) {
        growTaskService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growtask:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody GrowTask model) {
        growTaskService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growtask:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody GrowTask model) {
        growTaskService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growtask:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     GrowTask model = growTaskService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growtask:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        GrowTask model = new  GrowTask();
        List<GrowTask> list =  growTaskService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growtask:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = growTaskService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
