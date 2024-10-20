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
import com.hml.mall.entity.spread.DrawOrder;
import com.hml.mall.iface.spread.IDrawOrderService;


/**
*  前端控制器
* @author hml
* @since 2021-09-06
*/


@RestController
@RequestMapping("/drawOrder" )
public class DrawOrderController {

    @Autowired
    private IDrawOrderService  drawOrderService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:draworder:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody DrawOrder model) {
        drawOrderService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:draworder:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody DrawOrder model) {
        drawOrderService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:draworder:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody DrawOrder model) {
        drawOrderService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('spread:draworder:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     DrawOrder model = drawOrderService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:draworder:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        DrawOrder model = new  DrawOrder();
        List<DrawOrder> list =  drawOrderService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:draworder:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = drawOrderService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
