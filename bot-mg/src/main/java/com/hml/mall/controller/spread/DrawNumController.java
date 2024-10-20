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
import com.hml.mall.entity.spread.DrawNum;
import com.hml.mall.iface.spread.IDrawNumService;


/**
*  前端控制器
* @author hml
* @since 2021-09-06
*/


@RestController
@RequestMapping("/drawNum" )
public class DrawNumController {

    @Autowired
    private IDrawNumService  drawNumService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawnum:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody DrawNum model) {
    	DrawNum item = drawNumService.getById(model.getUserno());
    	if(item != null) {
    		return HttpResult.error("该客户已存在！");
    	}
        drawNumService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawnum:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody DrawNum model) {
        drawNumService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawnum:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody DrawNum model) {
        drawNumService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawnum:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     DrawNum model = drawNumService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawnum:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        DrawNum model = new  DrawNum();
        List<DrawNum> list =  drawNumService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawnum:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = drawNumService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
