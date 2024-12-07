package com.hml.mall.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.DrawType;
import com.hml.mall.service.IDrawService;
import com.hml.mall.service.IDrawTypeService;


/**
*  前端控制器
* @author hml
* @since 2024-12-05
*/


@RestController
@RequestMapping("/draw" )
public class DrawController {

    @Autowired
    private IDrawService  drawService;

    @Autowired
    private IDrawTypeService  drawTypeService;

    @RequestMapping("/findType")
    public HttpResult list() {
        List<DrawType> list = drawTypeService.list();
        return HttpResult.ok(list);
    }

    /**
    * 列表查询（分页）
    *
    * @return
    */
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
        PageResult page = drawService.findPage(pageRequest);
        return HttpResult.ok(page);
    }

}
