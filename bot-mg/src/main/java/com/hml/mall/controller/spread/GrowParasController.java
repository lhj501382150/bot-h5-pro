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
import com.hml.mall.entity.spread.GrowParas;
import com.hml.mall.iface.spread.IGrowParasService;


/**
*  前端控制器
* @author hml
* @since 2021-09-06
*/


@RestController
@RequestMapping("/growParas" )
public class GrowParasController {

    @Autowired
    private IGrowParasService  growParasService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growparas:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody GrowParas model) {
    	GrowParas item = growParasService.getById(model.getLeveno());
    	if(item != null) {
    		return HttpResult.error("该级别已存在！");
    	}
        growParasService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growparas:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody GrowParas model) {
        growParasService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growparas:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody GrowParas model) {
        growParasService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growparas:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     GrowParas model = growParasService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growparas:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        GrowParas model = new  GrowParas();
        List<GrowParas> list =  growParasService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('spread:growparas:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = growParasService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
