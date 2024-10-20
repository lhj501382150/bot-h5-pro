package com.hml.mall.controller.sys;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.sys.ImageList;
import com.hml.mall.iface.sys.IImageListService;


/**
*  前端控制器
* @author hml
* @since 2022-01-21
*/


@RestController
@RequestMapping("/imageList" )
public class ImageListController {

    @Autowired
    private IImageListService  imageListService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:img:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody ImageList model) {
        imageListService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:img:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody ImageList model) {
        imageListService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:img:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody ImageList model) {
        imageListService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('sys:img:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     ImageList model = imageListService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:img:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        ImageList model = new  ImageList();
        List<ImageList> list =  imageListService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:img:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = imageListService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
