package com.hml.mall.controller.trade;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hml.core.http.HttpResult;
import com.hml.core.page.PageRequest;
import com.hml.core.page.PageResult;
import com.hml.mall.entity.trade.WareMem;
import com.hml.mall.iface.trade.IWareMemService;


/**
*  前端控制器
* @author hml
* @since 2022-07-27
*/


@RestController
@RequestMapping("/wareMem" )
public class WareMemController {

    @Autowired
    private IWareMemService  wareMemService;

    /**
    * 保存
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareMem:add')")
    @RequestMapping("/save")
    public HttpResult save(@RequestBody WareMem model) {
        wareMemService.save(model);
        return HttpResult.ok();
    }
 	/**
    * 修改
    *
    * @param model
    * @return
 	 * @throws Exception 
    */
    @PreAuthorize("hasAuthority('trade:wareMem:edit')")
    @RequestMapping("/edit")
    public HttpResult edit(@RequestBody WareMem model) throws Exception {
        wareMemService.updateStatus(model);
        return HttpResult.ok();
    }
	/**
    * 删除
    *
    * @param model
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareMem:del')")
    @RequestMapping("/delete")
    public HttpResult delete(@RequestBody WareMem model) {
        wareMemService.removeById(model);
        return HttpResult.ok();
    }
    /**
    * 查询详情
    *
    * @param id
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareMem:view')")
    @RequestMapping("/getById")
    public HttpResult getById(Integer id) {
     WareMem model = wareMemService.getById(id);
        // todo 再包装一层
        return HttpResult.ok(model);
    }


    /**
    * 列表查询（非分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareMem:view')")
    @RequestMapping("/list")
    public HttpResult list() {
        WareMem model = new  WareMem();
        List<WareMem> list =  wareMemService.list(model);
        // todo 再包装一层
        return HttpResult.ok(list);
    }


    /**
    * 列表查询（分页）
    *
    * @return
    */
    @PreAuthorize("hasAuthority('trade:wareMem:view')")
    @RequestMapping("/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {

        PageResult page = wareMemService.findPage(pageRequest);
        // todo 再包装一层
        return HttpResult.ok(page);
    }

}
