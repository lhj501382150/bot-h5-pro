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
import com.hml.mall.entity.spread.DrawParas;
import com.hml.mall.iface.spread.IDrawParasService;


/**
*  前端控制器
* @author hml
* @since 2021-09-06
*/


@RestController
@RequestMapping("/drawParas" )
public class DrawParasController {

    @Autowired
    private IDrawParasService  drawParasService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawparas:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody DrawParas model) {
    	DrawParas item = drawParasService.getById(model.getLeveno());
    	if(item!=null) {
    		return HttpResult.error("该等级已存在！ ");
    	}
        drawParasService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawparas:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody DrawParas model) {
        drawParasService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawparas:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody DrawParas model) {
        drawParasService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawparas:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     DrawParas model = drawParasService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawparas:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        DrawParas model = new  DrawParas();
        List<DrawParas> list =  drawParasService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:drawparas:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = drawParasService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
