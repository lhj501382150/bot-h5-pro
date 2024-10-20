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
import com.hml.mall.entity.spread.Active;
import com.hml.mall.iface.spread.IActiveService;


/**
*  前端控制器
* @author hml
* @since 2022-07-31
*/


@RestController
@RequestMapping("/active" )
public class ActiveController {

    @Autowired
    private IActiveService  activeService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:active:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody Active model) {
        activeService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:active:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody Active model) {
        activeService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:active:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody Active model) {
        activeService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('spread:active:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     Active model = activeService.getById(id);
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
        Active model = new  Active();
        List<Active> list =  activeService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:active:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = activeService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
