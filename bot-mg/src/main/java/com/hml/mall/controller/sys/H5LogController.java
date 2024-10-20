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
import com.hml.mall.entity.sys.H5Log;
import com.hml.mall.iface.sys.IH5LogService;


/**
*  前端控制器
* @author hml
* @since 2024-06-25
*/


@RestController
@RequestMapping("/h5Log" )
public class H5LogController {

    @Autowired
    private IH5LogService  h5LogService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:h5log:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody H5Log model) {
        h5LogService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:h5log:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody H5Log model) {
        h5LogService.updateById(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('sys:h5log:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody H5Log model) {
        h5LogService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('sys:h5log:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     H5Log model = h5LogService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('sys:h5log:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        H5Log model = new  H5Log();
        List<H5Log> list =  h5LogService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
//    @PreAuthorize("hasAuthority('sys:h5log:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
    	
    	pageRequest.getParams().put("requestUrl@ne", "/apis/Query/SysPara");
//    	pageRequest.getParams().put("requestUrl@ne", "/apis/Data/AddData");
    	pageRequest.getParams().put("logNo@desc", "1");
        PageResult page = h5LogService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
