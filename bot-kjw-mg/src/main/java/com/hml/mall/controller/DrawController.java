package com.hml.mall.controller;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.Draw;
import com.hml.mall.entity.DrawType;
import com.hml.mall.service.IDrawService;
import com.hml.mall.service.IDrawTypeService;
import com.hml.utils.DateTimeUtils;


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

    
    @RequestMapping("/getResult")
    public HttpResult getResult(@RequestBody Draw model) {
        Draw item = drawService.getLast(model.getMode());
        if(item != null && !item.getType().equals("C")) {
        	item.setLeftTime(DateTimeUtils.getSeconds(DateTimeUtils.parseDate(item.getDrawTime())));
        }
        return HttpResult.ok(item);
    }

    /**
    * 列表查询（分页）
    *
    * @return
    */
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	pageRequest.getParams().put("data_id@desc", "a");
        PageResult page = drawService.findPage(pageRequest);
        return HttpResult.ok(page);
    }
    
    
    @RequestMapping("/getNewDraw")
    public HttpResult getNewDraw() {
        List<Draw> draws = drawService.getNewDraws();
        for(Draw item:draws) {
        	if(!item.getType().equals("C")) {
        		item.setLeftTime(DateTimeUtils.getSeconds(DateTimeUtils.parseDate(item.getDrawTime())));
        	}
        }
        return HttpResult.ok(draws);
    }
}
